package com.yiling.export.export.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.yiling.dataflow.agency.enums.CrmSupplierLevelEnum;
import com.yiling.dataflow.flowcollect.api.FlowCollectHeartStatisticsApi;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.enums.ClientFlowModeEnum;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Service("flowCollectHeartStatisticExportService")
public class FlowCollectHeartStatisticExportServiceImpl implements BaseExportQueryDataService<QueryDayCollectStatisticsRequest> {
   // private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference(timeout = 60*1000)
    private FlowCollectHeartStatisticsApi flowCollectHeartStatisticsApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Override
    public QueryExportDataDTO queryData(QueryDayCollectStatisticsRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();

        if (null == request.getUserDatascopeBO() || OrgDatascopeEnum.NONE.getCode().equals(request.getUserDatascopeBO().getOrgDatascope())) {
            return setResultParam(result,data);
        }
        if (OrgDatascopeEnum.PORTION.getCode().equals(request.getUserDatascopeBO() .getOrgDatascope()) && CollUtil.isEmpty(request.getUserDatascopeBO() .getOrgPartDatascopeBO().getCrmEids()) && CollUtil.isEmpty(request.getUserDatascopeBO() .getOrgPartDatascopeBO().getProvinceCodes())) {

             return setResultParam(result,data);
        }
        //操作数据
        buildExportData(request, data);
        //设置结果
        return setResultParam(result, data);
    }
    void buildExportData(QueryDayCollectStatisticsRequest request, List<Map<String, Object>> data) {
        Page<FlowDayHeartStatisticsBO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            //数据处理部分
            //  查询导出的数据填入data
            //供应商角色
            page = flowCollectHeartStatisticsApi.page(request);
            List<FlowDayHeartStatisticsBO> records = page.getRecords();
            if (CollUtil.isEmpty(records)) {
                continue;
            }
            buildEsbNameInfo(page.getRecords());
            buildFlowDayHeartStatisticsDetailVO(page.getRecords());
            //组装数据
            page.getRecords().forEach(item -> {
                Map<String,Object> stringObjectMap=new HashMap<>();
                stringObjectMap.put("crmEnterpriseId", item.getCrmEnterpriseId());//经销商编码
                stringObjectMap.put("ename", item.getEname());//经销商名称
                stringObjectMap.put("commerceJobNumber", item.getCommerceJobNumber());//商务负责人工号
                stringObjectMap.put("commerceLiablePerson", item.getCommerceLiablePerson());//商务负责人工号
                stringObjectMap.put("flowJobNumber", item.getFlowJobNumber());//流向打取人工号
                stringObjectMap.put("flowLiablePerson", item.getFlowLiablePerson());//流向打取人
                stringObjectMap.put("supplierLevelEnum", CrmSupplierLevelEnum.getNameByCode(item.getSupplierLevel()));//经销商级别
                stringObjectMap.put("flowMode", ClientFlowModeEnum.getFromCode(item.getFlowMode())!=null?ClientFlowModeEnum.getFromCode(item.getFlowMode()).getDesc():null);//流向收集方式 :ClientFlowModeEnum
                stringObjectMap.put("depthTime", item.getDepthTime());//对接时间
                stringObjectMap.put("installEmployee",item.getInstallEmployee());//实施负责人
                stringObjectMap.put("flowTypeEnum", FlowTypeEnum.getByCode(item.getFlowType())!=null?FlowTypeEnum.getByCode(item.getFlowType()).getDesc():null);//日流向类型
                //转化基本信息
                //动态列转化
                if(CollUtil.isNotEmpty(item.getDateHeadersInfoMap())){
                    dynamicDateTable(item,stringObjectMap);
                }
                data.add(stringObjectMap);
            });
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
    }

    /**
     * 动态字段
     * @param item
     * @param stringObjectMap
     */
    private void dynamicDateTable(FlowDayHeartStatisticsBO item, Map<String, Object> stringObjectMap) {
        for (Map.Entry<String,  FlowDayHeartStatisticsDetailBO> entry : item.getDateHeadersInfoMap().entrySet()) {
            String mapKey = entry.getKey();
            FlowDayHeartStatisticsDetailBO mapValue = entry.getValue();
            // 1-未上传 2-已上传
            stringObjectMap.put(mapKey,Integer.valueOf(2).equals(mapValue.getFlowStatus())?"已上传":"未上传");
        }
    }

    @Override
    public QueryDayCollectStatisticsRequest getParam(Map<String, Object> map) {
        QueryDayCollectStatisticsRequest request=  PojoUtils.map(map, QueryDayCollectStatisticsRequest.class);
        String empId=map.getOrDefault("currentUserCode","").toString();
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(empId);
        request.setUserDatascopeBO(byEmpId);
        return request;
    }
    private QueryExportDataDTO setResultParam(QueryExportDataDTO result, List<Map<String, Object>> data) {
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("日流向心跳统计");
        // 页签字段
        exportDataDTO.setFieldMap(buildField());
        // 页签数据
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }
    private void buildFlowDayHeartStatisticsDetailVO(List<FlowDayHeartStatisticsBO> records) {
        if (CollUtil.isEmpty(records)) {
            return;
        }
        //根据ID查询明细中数据
        List<Long> flowIds = records.stream().map(FlowDayHeartStatisticsBO::getId).collect(Collectors.toList());
        List<FlowDayHeartStatisticsDetailBO> flowDetails = flowCollectHeartStatisticsApi.listDetailsByFchsIds(flowIds);
        //根据fcsID分组
        Map<Long, List<FlowDayHeartStatisticsDetailBO>> fcsDetailsMap = flowDetails.stream().collect(Collectors.groupingBy(FlowDayHeartStatisticsDetailBO::getFchId));
        records.stream().forEach(e -> {
            if(CollUtil.isNotEmpty(fcsDetailsMap.get(e.getId()))){
                e.setDateHeadersInfoMap(fcsDetailsMap.get(e.getId()).stream().collect(Collectors.toMap(FlowDayHeartStatisticsDetailBO::getDataTime, m -> m, (v1, v2) -> v1)));
            }
        });
    }
    private void buildEsbNameInfo(List<FlowDayHeartStatisticsBO> records) {
        if (CollUtil.isEmpty(records)) {
            return;
        }
        //流向打取人工号获取批量
        List<String> flowJobEmpIds = Optional.ofNullable(records.stream().filter(m -> StringUtils.isNotBlank(m.getFlowJobNumber())).map(FlowDayHeartStatisticsBO::getFlowJobNumber).collect(Collectors.toList())).orElse(Lists.newArrayList());
        List<String> commerceJobNumbers = Optional.ofNullable(records.stream().filter(m -> StringUtils.isNotBlank(m.getCommerceJobNumber())).map(FlowDayHeartStatisticsBO::getCommerceJobNumber).collect(Collectors.toList())).orElse(Lists.newArrayList());
        flowJobEmpIds.addAll(commerceJobNumbers);
        //流向打取人人员列表
        List<EsbEmployeeDTO> flowEsbEmployeeDTOS = CollectionUtil.isEmpty(flowJobEmpIds) ? Lists.newArrayList() : esbEmployeeApi.listByEmpIds(flowJobEmpIds);
        //流向打取人人员列表Map
        Map<String, EsbEmployeeDTO> flowEsbEmployeeDTOSMap = flowEsbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId, m -> m, (v1, v2) -> v1));
        records.stream().forEach(m -> {
            //流向打取人和商业负责人
            EsbEmployeeDTO flowEmp = flowEsbEmployeeDTOSMap.get(m.getFlowJobNumber());
            EsbEmployeeDTO commerceEmp = flowEsbEmployeeDTOSMap.get(m.getCommerceJobNumber());
            m.setFlowLiablePerson(Objects.nonNull(flowEmp) ? flowEmp.getEmpName() : null);
            m.setCommerceLiablePerson(Objects.nonNull(commerceEmp) ? commerceEmp.getEmpName() : null);
        });
    }
//    public static List<String> buildDateHeader() {
//        List<String> dateHeader = new ArrayList<>();
//        //默认30内，不包括当天
//        for (int i = 30; i > 0; i--) {
//            dateHeader.add(DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd"));
//        }
//        return dateHeader;
//    }
    private LinkedHashMap<String, String> buildField(){
        LinkedHashMap<String, String> field = new LinkedHashMap<>();
        field.put("crmEnterpriseId", "经销商编码");//经销商编码
        field.put("ename", "经销商名称");//经销商名称
        field.put("flowTypeEnum", "日流向类型");//日流向类型
        for (int i = 1; i < 31; i++) {
            String dateFormat=DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd");
            //格式为2xxx-mm-dd
            field.put(dateFormat,dateFormat);
        }
        field.put("commerceJobNumber", "商务负责人工号");//商务负责人工号
        field.put("commerceLiablePerson", "商务负责人");//商务负责人工号
        field.put("flowJobNumber", "流向打取人工号");//流向打取人工号
        field.put("flowLiablePerson", "流向打取人");//流向打取人
        field.put("supplierLevelEnum", "经销商级别");//经销商级别
        field.put("flowMode", "流向收集方式");//流向收集方式 :ClientFlowModeEnum
        field.put("depthTime", "对接时间");//对接时间
        field.put("installEmployee", "实施负责人");//实施负责人

        return field;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            String dateFormat=DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd");
            //格式为2xxx-mm-dd
            System.out.println(dateFormat);
        }
    }
}
