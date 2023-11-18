package com.yiling.export.export.service.impl;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.B2bSettDetailExcelModel;
import com.yiling.export.export.model.B2bSettExcelModel;
import com.yiling.export.export.model.ExportOrderReturnEnterpriseBuyerCenterModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.settlement.b2b.api.SettlementApi;
import com.yiling.settlement.b2b.api.SettlementDetailApi;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.SettlementDetailDTO;
import com.yiling.settlement.b2b.dto.request.ExportSettlementSimpleInfoPageListRequest;
import com.yiling.settlement.b2b.dto.request.QuerySettlementPageListRequest;
import com.yiling.settlement.b2b.enums.SettlementStatusEnum;
import com.yiling.settlement.b2b.enums.SettlementTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 采购关系导出
 *
 * @author dexi.yao
 * @date: 2023/3/17
 */
@Slf4j
@Service("b2bSettlementSimpleInfoExportServiceImpl")
public class B2bSettlementSimpleInfoExportServiceImpl implements BaseExportQueryDataService<ExportSettlementSimpleInfoPageListRequest> {

    @DubboReference
    DictApi dictApi;
    @DubboReference
    SettlementApi settlementApi;
    @DubboReference
    SettlementDetailApi settlementDetailApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private ThreadLocal<List<DictBO>> dictThreadLocal = new ThreadLocal<>();

    /**
     * 获取数据字典
     *
     * @return
     */
    private List<DictBO> getDictDataList() {

        List<DictBO> dictBOList = dictThreadLocal.get();

        if (dictBOList == null) {
            List<DictBO> dictApiEnabledList = dictApi.getEnabledList();
            dictThreadLocal.set(dictApiEnabledList);
            dictBOList = dictApiEnabledList;
        }

        return dictBOList;
    }


    /**
     * 获取数据字典值
     *
     * @param key
     * @param data
     * @return
     */
    private String getDictDataValue(String key, Integer data) {

        if (data == null || data == 0) {

            return "--";
        }

        List<DictBO> dataList = this.getDictDataList();
        if (CollectionUtil.isEmpty(dataList)) {
            return "--";
        }

        Map<String, DictBO> dictBOMap = dataList.stream().collect(Collectors.toMap(DictBO::getName, Function.identity()));
        DictBO dictBO = dictBOMap.get(key);
        if (dictBO == null) {
            return "--";
        }

        List<DictBO.DictData> dictBODataList = dictBO.getDataList();
        if (CollectionUtil.isEmpty(dictBODataList)) {

            return "--";
        }

        Map<String, DictBO.DictData> dictDataMap = dictBODataList.stream().collect(Collectors.toMap(DictBO.DictData::getValue, Function.identity(), (k1, k2) -> k1));

        return Optional.ofNullable(dictDataMap.get(data + "")).map(t -> t.getLabel()).orElse("--");
    }

    @Override
    public ExportSettlementSimpleInfoPageListRequest getParam(Map<String, Object> map) {
        ExportSettlementSimpleInfoPageListRequest request = PojoUtils.map(map, ExportSettlementSimpleInfoPageListRequest.class);
        return request;
    }


    public List<B2bSettExcelModel> querySett(QuerySettlementPageListRequest request) {

        Page<SettlementDTO> page = settlementApi.querySettlementPageList(request);
        ;
        List<SettlementDTO> settList = page.getRecords();

        if (CollUtil.isEmpty(settList)) {
            return Collections.emptyList();
        }

        //查询企业信息
        List<Long> eidList = settList.stream().map(SettlementDTO::getEid).collect(Collectors.toList());
        eidList = eidList.stream().distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);

        Map<Long, String> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));

        List<B2bSettExcelModel> result = settList.stream().map(t -> {

            B2bSettExcelModel var = PojoUtils.map(t, B2bSettExcelModel.class);

            var.setEname(enterpriseMap.getOrDefault(t.getEid(), ""));
            var.setTypeStr(SettlementTypeEnum.getByCode(t.getType()).getName());
            var.setStatusStr(SettlementStatusEnum.getByCode(t.getStatus()).getName());

            return var;

        }).collect(Collectors.toList());

        return result;
    }

    public List<B2bSettDetailExcelModel> querySettDetail(List<Long> settIds) {

        List<SettlementDetailDTO> settlementDetailDTOS = settlementDetailApi.querySettlementDetailBySettlementId(settIds);

        List<SettlementDTO> settlementDTOList = settlementApi.getByIdList(settIds);
        Map<Long, SettlementDTO> settlementDTOMap = settlementDTOList.stream().collect(Collectors.toMap(SettlementDTO::getId, e -> e));

        //查询企业信息
        List<Long> eidIdList = settlementDetailDTOS.stream().map(SettlementDetailDTO::getBuyerEid).collect(Collectors.toList());
        eidIdList.addAll(settlementDetailDTOS.stream().map(SettlementDetailDTO::getSellerEid).collect(Collectors.toList()));
        eidIdList = eidIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, String> entDTOMap = enterpriseApi.listByIds(eidIdList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));

        //查询订单信息
        List<Long> orderIdList = settlementDetailDTOS.stream().map(SettlementDetailDTO::getOrderId).collect(Collectors.toList());
        Map<Long, OrderDTO> orderDTOMap = orderApi.listByIds(orderIdList).stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));


        List<B2bSettDetailExcelModel> result = settlementDetailDTOS.stream().map(t -> {

            B2bSettDetailExcelModel var = PojoUtils.map(t, B2bSettDetailExcelModel.class);

            SettlementDTO settlementDTO = settlementDTOMap.get(t.getSettlementId());

            var.setCode(settlementDTO.getCode());
            var.setBuyerName(entDTOMap.getOrDefault(t.getBuyerEid(), ""));
            var.setSellerName(entDTOMap.getOrDefault(t.getSellerEid(), ""));
            //设置退款金额
            //设置结算单类型
            var.setTypeStr(SettlementTypeEnum.getByCode(settlementDTO.getType()).getName());
            OrderDTO orderDTO = orderDTOMap.get(t.getOrderId());
            if (ObjectUtil.isNotNull(orderDTO)) {
                var.setOrderCreateTime(orderDTO.getCreateTime());
                var.setPaymentTime(orderDTO.getPaymentTime());
                var.setCustomerErpCode(orderDTO.getCustomerErpCode());
            }

            return var;

        }).collect(Collectors.toList());
        return result;
    }

    @Override
    @SneakyThrows
    public byte[] getExportByte(ExportSettlementSimpleInfoPageListRequest request, String file) {
        if (request == null) {
            return null;
        }
        String tmpDirPath = FileUtil.getTmpDirPath() + File.separator + "B2bSettlementSimpleInfoExport";
        File tmpExcelDir = FileUtil.newFile(tmpDirPath + File.separator + "excel");

        if (!tmpExcelDir.isDirectory()) {
            tmpExcelDir.mkdirs();
        }
        file = file.substring(0, file.lastIndexOf(".xlsx"));
        String fileName = file + Constants.SEPARATOR_UNDERLINE + "结算单" + DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT) + ".xlsx";

        QuerySettlementPageListRequest mainQueryRequest = PojoUtils.map(request, QuerySettlementPageListRequest.class);
        //根据供应商名字查eid
        if (StrUtil.isNotBlank(request.getEntName())) {
            List<Long> eidList = ListUtil.toList();
            int current = 1;
            Page<EnterpriseDTO> entPage;
            QueryEnterprisePageListRequest queryRequest = new QueryEnterprisePageListRequest();
            queryRequest.setSize(100);
            queryRequest.setName(request.getEntName());
            //分页查询申请单列表
            do {
                queryRequest.setCurrent(current);
                //分页查询符合结算条件的订单
                entPage = enterpriseApi.pageList(queryRequest);
                if (CollUtil.isNotEmpty(entPage.getRecords())) {
                    List<Long> eids = entPage.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                    eidList.addAll(eids);
                }
                current = current + 1;
            } while (entPage != null && CollUtil.isNotEmpty(entPage.getRecords()));
            mainQueryRequest.setEidList(eidList);
        }

        int current = 1;
        int settSheetNum = 1;
        int settDetailSheetNum = 1;
        int size = 200;
        List<Long> settIdList = ListUtil.toList();
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();
        try {
            //结算单
            settSheet:
            do {
                WriteSheet writeSheet = EasyExcel.writerSheet("结算单" + settSheetNum).head(B2bSettExcelModel.class).build();
                while (true) {
                    mainQueryRequest.setCurrent(current);
                    mainQueryRequest.setSize(size);
                    List<B2bSettExcelModel> list = querySett(mainQueryRequest);
                    // 写文件
                    excelWriter.write(list, writeSheet);

                    if (CollUtil.isEmpty(list)) {
                        break settSheet;
                    }
                    settIdList.addAll(list.stream().map(B2bSettExcelModel::getId).collect(Collectors.toList()));

                    if (current % 5000 == 0) {   // 10w数据做文件切割
                        settSheetNum++;
                        current++;
                        break;
                    }
                    current++;
                }

            } while (true);

            //结算单明细
            List<Long> tempSettIdList;
            int querySettDetailCurrent = 0;
            settDetailSheet:
            do {
                WriteSheet writeSheet = EasyExcel.writerSheet("结算单明细" + settDetailSheetNum).head(B2bSettDetailExcelModel.class).build();

                while (true) {
                    tempSettIdList = CollUtil.page(querySettDetailCurrent, 2, settIdList);
                    if (CollUtil.isEmpty(tempSettIdList)) {
                        break settDetailSheet;
                    }
                    List<B2bSettDetailExcelModel> list = querySettDetail(tempSettIdList);
                    // 写文件
                    excelWriter.write(list, writeSheet);

                    if (CollUtil.isEmpty(list)) {
                        break settDetailSheet;
                    }

                    if (querySettDetailCurrent+1 % 50000 == 0) {   // 10w数据做文件切割
                        settDetailSheetNum++;
                        querySettDetailCurrent++;
                        break;
                    }
                    querySettDetailCurrent++;
                }

            } while (true);
        } catch (Exception e) {
            log.error("导出结算单报错，异常原因={}", e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        return getFileByte(tmpDirPath, fileName);
    }


    @Override
    public QueryExportDataDTO queryData(ExportSettlementSimpleInfoPageListRequest request) {
        return null;
    }

    @Override
    public boolean isReturnData() {

        return false;
    }

    private byte[] getFileByte(String dir, String fileName) {
        //  压缩文件
        try {
            File zipFile = FileUtil.newFile(fileName);
            if (null != zipFile) {
                return FileUtil.readBytes(zipFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(dir);
        }
        return new byte[0];
    }
}
