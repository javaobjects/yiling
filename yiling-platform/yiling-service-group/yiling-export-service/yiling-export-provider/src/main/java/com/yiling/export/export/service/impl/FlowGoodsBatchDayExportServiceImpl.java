package com.yiling.export.export.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.yiling.dataflow.crm.enums.CrmSupplierLevelEnum;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.FlowGoodsBatchDetailExcelModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.search.flow.api.EsFlowGoodsBatchDetailApi;
import com.yiling.search.flow.dto.EsFlowGoodsBatchDetailDTO;
import com.yiling.search.flow.dto.EsFlowSaleDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/30
 */
@Slf4j
@Service("flowGoodsBatchDayExportService")
public class FlowGoodsBatchDayExportServiceImpl implements BaseExportQueryDataService<EsFlowGoodsBatchDetailScrollRequest> {

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @DubboReference
    private ErpClientApi erpClientApi;

    @DubboReference
    private EsFlowGoodsBatchDetailApi esFlowGoodsBatchDetailApi;

    @Override
    public QueryExportDataDTO queryData(EsFlowGoodsBatchDetailScrollRequest esFlowGoodsBatchDetailSearchRequest) {
        return null;
    }

    @Override
    public EsFlowGoodsBatchDetailScrollRequest getParam(Map<String, Object> map) {
        if (map.get("startTime") == null || map.get("endTime") == null) {
            throw new BusinessException(ResultCode.PARAM_MISS, "必须选择开始时间和结束时间");
        }

        //  获取opUserId
        if (map.get("currentUserCode") == null) {
            throw new BusinessException(ResultCode.FAILED);
        }
        String currentUserCode = String.valueOf(map.get("currentUserCode"));

        EsFlowGoodsBatchDetailScrollRequest request = PojoUtils.map(map, EsFlowGoodsBatchDetailScrollRequest.class);
        Date startTime = DateUtil.beginOfDay(DateUtil.parse((String) map.get("startTime")));
        Date endTime = DateUtil.endOfDay(DateUtil.parse((String) map.get("endTime")));
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setDelFlag(0);
        if (ObjectUtil.isNotNull(map.get("goodsSpec"))) {
            request.setPoSpecifications((String.valueOf(map.get("goodsSpec"))));
        }

        //  查询权限
        SjmsUserDatascopeBO datascopeBO = sjmsUserDatascopeApi.getByEmpId(currentUserCode);
        if (datascopeBO == null || OrgDatascopeEnum.NONE.getCode().equals(datascopeBO.getOrgDatascope())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        List<String> provinceCodeList = new ArrayList<>();
        List<Long> eidList = new ArrayList<>();
        if (OrgDatascopeEnum.PORTION.getCode().equals(datascopeBO.getOrgDatascope())) {
            provinceCodeList = datascopeBO.getOrgPartDatascopeBO().getProvinceCodes();  // 省区
            List<Long> crmEidList = datascopeBO.getOrgPartDatascopeBO().getCrmEids();   // 机构id
            if (CollUtil.isNotEmpty(crmEidList)) {
                List<ErpClientDTO> erpClientList = erpClientApi.getWithDatascopeByCrmEnterpriseIdList(crmEidList);
                List<Long> eids = erpClientList.stream().filter(o -> ObjectUtil.isNotNull(o.getRkSuId()) && o.getRkSuId().intValue() > 0).map(ErpClientDTO::getRkSuId).distinct().collect(Collectors.toList());
                if (CollUtil.isEmpty(provinceCodeList) && CollUtil.isEmpty(eids)) {
                    // 如果省区列表为空，并且映射后的eid为空，则判定无权限
                    throw new BusinessException(ResultCode.FORBIDDEN);
                }
                eidList.addAll(eids);
            }
        }

        // 若有经销商查询条件，将crmid转化为eid
        Long eid = null;
        if (request.getCrmEnterpriseId() != null) {
            ErpClientDTO erpClientDTO = erpClientApi.getByCrmEnterpriseId(request.getCrmEnterpriseId());
            if (erpClientDTO == null || erpClientDTO.getRkSuId() <= 0) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
            eid = erpClientDTO.getRkSuId();
        }

        request.setEid(eid);
        request.setEids(eidList);
        request.setProvinceCodes(provinceCodeList);
        return request;
    }

    @Override
    public String getFileSuffix() {
        return "zip";
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(EsFlowGoodsBatchDetailScrollRequest request, String fileName) {
        if (request == null) {
            return null;
        }

        fileName = fileName.substring(0, fileName.lastIndexOf(".zip"));

        String tmpDir = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis();
        String tmpExcelDir = tmpDir + File.separator + fileName;
        File tmpExcelFileDir = new File(tmpExcelDir);
        if (!tmpExcelFileDir.exists()) {
            tmpExcelFileDir.mkdirs();
        }

        int fileIdx = 0;
        int current = 1;
        int size = 2000;

        request.setSize(2000);
        EsScrollDTO<EsFlowGoodsBatchDetailDTO> esScrollDTO = esFlowGoodsBatchDetailApi.scrollFlowGoodsBatchDetailFirst(request);
        outer: do {
            String fileFullName = tmpExcelDir + File.separator + fileName + "-" + fileIdx + ".xlsx";
            ExcelWriter excelWriter = EasyExcel.write(fileFullName, FlowGoodsBatchDetailExcelModel.class).build();
            try {
                WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();
                while (true) {

                    List<FlowGoodsBatchDetailExcelModel> list = new ArrayList<>();
                    for (EsFlowGoodsBatchDetailDTO esFlowGoodsBatchDetailDTO : esScrollDTO.getData()) {
                        FlowGoodsBatchDetailExcelModel flowGoodsBatchDetailExcelModel = PojoUtils.map(esFlowGoodsBatchDetailDTO, FlowGoodsBatchDetailExcelModel.class);
                        if (esFlowGoodsBatchDetailDTO.getSupplierLevel() != null) {
                            String supplierLevelDesc = Arrays.stream(CrmSupplierLevelEnum.values())
                                    .filter(c -> c.getCode().equals(esFlowGoodsBatchDetailDTO.getSupplierLevel()))
                                    .findAny().map(CrmSupplierLevelEnum::getName).orElse("");
                            flowGoodsBatchDetailExcelModel.setSupplierLevel(supplierLevelDesc);
                        }
                        list.add(flowGoodsBatchDetailExcelModel);
                    }

                    // 写文件
                    excelWriter.write(list, writeSheet);

                    if (list.size() < size) {
                        break outer;
                    }

                    QueryScrollRequest continueRequest = new QueryScrollRequest();
                    continueRequest.setScrollId(esScrollDTO.getScrollId());
                    continueRequest.setSize(size);
                    esScrollDTO = esFlowGoodsBatchDetailApi.scrollFlowGoodsBatchDetailContinue(continueRequest);

                    if (esScrollDTO == null || StringUtils.isEmpty(esScrollDTO.getScrollId())) {
                        break outer;
                    }

                    if (current % 500 == 0) {   // 100w数据做文件切割
                        current++;
                        break;
                    }
                    current++;
                }

            } catch (Exception e) {
                log.error("库存日流向数据导出失败:" + e);
                throw new ServiceException(ResultCode.FAILED, "库存日流向数据导出失败！");
            } finally {
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
            fileIdx++;
        } while (StringUtils.isNotEmpty(esScrollDTO.getScrollId()));

        //  压缩文件
        try {
            File zipFile = ZipUtil.zip(tmpExcelDir);
            if (null != zipFile) {
                return FileUtil.readBytes(zipFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(tmpDir);
        }

        return null;
    }

}
