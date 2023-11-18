package com.yiling.export.export.service.impl;

import java.io.File;
import java.util.ArrayList;
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
import com.yiling.export.export.model.PopProcRelationExcelModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.procrelation.api.ProcurementRelationApi;
import com.yiling.user.procrelation.dto.ProcurementRelationDTO;
import com.yiling.user.procrelation.dto.request.QueryProcRelationPageRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 采购关系导出
 *
 * @author dexi.yao
 * @date: 2023/3/17
 */
@Slf4j
@Service("popProcRelationExportService")
public class PopProcRelationExportServiceImpl implements BaseExportQueryDataService<QueryProcRelationPageRequest> {

    @DubboReference
    ProcurementRelationApi procurementRelationApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    DictApi dictApi;

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
    public QueryProcRelationPageRequest getParam(Map<String, Object> map) {
        Long channelId = Long.parseLong(map.getOrDefault("channelId", 0L).toString());
        Long channelPartnerEid = Long.parseLong(map.getOrDefault("channelPartnerEid", 0L).toString());
        List<Long> channelEid = ListUtil.toList();
        QueryProcRelationPageRequest request = PojoUtils.map(map, QueryProcRelationPageRequest.class);
        //如果传了渠道商的渠道类型
        if (channelId != 0) {
            channelEid = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.getByCode(channelId));
        }
        //如果传了渠道商id
        if (channelPartnerEid != 0) {
            if (CollUtil.isNotEmpty(channelEid)&& !channelEid.contains(channelPartnerEid)) {
                return null;
            } else {
                request.setChannelPartnerEid(ListUtil.toList(channelPartnerEid));
            }
        } else {
            request.setChannelPartnerEid(channelEid);
        }
        return request;
    }


    public List<PopProcRelationExcelModel> queryPageData(QueryProcRelationPageRequest request) {

        Page<ProcurementRelationDTO> page = procurementRelationApi.queryProcRelationPage(request);
        List<ProcurementRelationDTO> relationDTOList = page.getRecords();

        if (CollUtil.isEmpty(relationDTOList)) {
            return Collections.emptyList();
        }

        //查询企业信息
        List<Long> eidList = relationDTOList.stream().map(ProcurementRelationDTO::getDeliveryEid).collect(Collectors.toList());
        eidList.addAll(relationDTOList.stream().map(ProcurementRelationDTO::getChannelPartnerEid).collect(Collectors.toList()));
        eidList = eidList.stream().distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);

        Map<Long, String> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        Map<Long, Long> enterpriseChannelMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getChannelId));
        List<Long> userIds = relationDTOList.stream().map(ProcurementRelationDTO::getCreateUser).collect(Collectors.toList());
        userIds.addAll(relationDTOList.stream().map(ProcurementRelationDTO::getUpdateUser).collect(Collectors.toList()));
        userIds = userIds.stream().distinct().collect(Collectors.toList());
        List<UserDTO> list = userApi.listByIds(new ArrayList<>(userIds));
        Map<Long, String> userMap = list.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        List<PopProcRelationExcelModel> result = relationDTOList.stream().map(t -> {

            PopProcRelationExcelModel z = PojoUtils.map(t, PopProcRelationExcelModel.class);

            z.setDeliveryName(enterpriseMap.getOrDefault(z.getDeliveryEid(), ""));
            z.setChannelPartnerName(enterpriseMap.getOrDefault(z.getChannelPartnerEid(), ""));
            z.setCreateUserStr(userMap.getOrDefault(z.getCreateUser(), ""));
            z.setUpdateUserStr(userMap.getOrDefault(z.getUpdateUser(), ""));

            z.setChannelPartnerChannelStr(getDictDataValue("channel_type", enterpriseChannelMap.getOrDefault(t.getChannelPartnerEid(), 0L).intValue()));
            z.setDeliveryTypeStr(getDictDataValue("pop_proc_rela_deli_type", t.getDeliveryType()));
            z.setProcRelationStatusStr(getDictDataValue("pop_proc_rela_status", t.getProcRelationStatus()));

            return z;

        }).collect(Collectors.toList());

        return result;
    }

    @Override
    @SneakyThrows
    public byte[] getExportByte(QueryProcRelationPageRequest request, String file) {
        if (request == null) {
            return null;
        }
        String tmpDirPath = FileUtil.getTmpDirPath() + File.separator + "PopProcRelationExportTaskExport";
        File tmpExcelDir = FileUtil.newFile(tmpDirPath + File.separator + "excel");

        if (!tmpExcelDir.isDirectory()) {
            tmpExcelDir.mkdirs();
        }
        file = file.substring(0, file.lastIndexOf(".xlsx"));
        String fileName = file + Constants.SEPARATOR_UNDERLINE + "建采管理" + DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT) + ".xlsx";


        int current = 1;
        int sheetNum = 1;
        int size = 2000;
        ExcelWriter excelWriter = EasyExcel.write(fileName, PopProcRelationExcelModel.class).build();
        try {
            sheet:
            do {
                WriteSheet writeSheet = EasyExcel.writerSheet("sheet" + sheetNum).build();
                while (true) {
                    request.setCurrent(current);
                    request.setSize(size);
                    List<PopProcRelationExcelModel> list = queryPageData(request);

                    if (CollUtil.isEmpty(list)) {
                        break sheet;
                    }
                    // 写文件
                    excelWriter.write(list, writeSheet);

                    if (current % 500 == 0) {   // 100w数据做文件切割
                        sheetNum++;
                        current++;
                        break;
                    }
                    current++;
                }

            } while (true);
        } catch (Exception e) {
            log.error("导出pop采购关系报错，异常原因={}", e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        return getFileByte(tmpDirPath, fileName);
    }


    @Override
    public QueryExportDataDTO queryData(QueryProcRelationPageRequest request) {
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
