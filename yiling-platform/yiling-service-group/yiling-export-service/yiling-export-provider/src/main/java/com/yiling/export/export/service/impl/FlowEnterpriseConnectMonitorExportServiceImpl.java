package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.crm.enums.CrmSupplierLevelEnum;
import com.yiling.dataflow.flow.api.FlowEnterpriseConnectMonitorApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseConnectMonitorDTO;
import com.yiling.dataflow.flow.dto.request.QueryEnterpriseConnectMonitorPageRequest;
import com.yiling.dataflow.flow.enums.ConnectStatusEnum;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.enums.ClientFlowModeEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectMonitorExportServiceImpl
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27f
 **/
@Slf4j
@Service("flowEnterpriseConnectMonitorExportService")
public class FlowEnterpriseConnectMonitorExportServiceImpl implements BaseExportQueryDataService<QueryEnterpriseConnectMonitorPageRequest> {

    @DubboReference
    private FlowEnterpriseConnectMonitorApi flowEnterpriseConnectMonitorApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("crmEnterpriseId", "经销商编码");
        FIELD.put("crmEnterpriseName", "经销商名称");
        FIELD.put("supplierLevel", "经销商级别");
        FIELD.put("installEmployee", "实施负责人");
        FIELD.put("flowMode", "流向收集方式");
        FIELD.put("dockingTime", "对接时间");
        FIELD.put("flowCollectionTime", "上次收集流向时间");
        FIELD.put("returnFlowDayCount", "过去30天回传流向的天数");
        FIELD.put("connectStatus", "直连状态");
        FIELD.put("explanation", "说明");
    }

    @Override
    public QueryExportDataDTO queryData(QueryEnterpriseConnectMonitorPageRequest request) {
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<FlowEnterpriseConnectMonitorDTO> monitorDTOList = flowEnterpriseConnectMonitorApi.listByQuery(request);
        if(CollectionUtil.isNotEmpty(monitorDTOList)){
            List<Map<String, Object>> data = new ArrayList<>();
            monitorDTOList.forEach(monitor->{
                Map<String, Object> dataPojo = BeanUtil.beanToMap(monitor);
                ClientFlowModeEnum flowMode = ClientFlowModeEnum.getFromCode(monitor.getFlowMode());
                if(null != flowMode){
                    dataPojo.put("flowMode", flowMode.getDesc());
                }else {
                    dataPojo.put("flowMode", "");
                }
                CrmSupplierLevelEnum supplierLevel = CrmSupplierLevelEnum.getByCode(monitor.getSupplierLevel());
                if(null != supplierLevel){
                    dataPojo.put("supplierLevel", supplierLevel.getName());
                }else {
                    dataPojo.put("supplierLevel", "");
                }
                ConnectStatusEnum connectStatus = ConnectStatusEnum.getByCode(monitor.getConnectStatus());
                if(null != connectStatus){
                    dataPojo.put("connectStatus", connectStatus.getMessage());
                }else {
                    dataPojo.put("connectStatus", "");
                }
                data.add(dataPojo);
            });

            ExportDataDTO exportDataDTO = new ExportDataDTO();
            exportDataDTO.setSheetName("直连接口监控导出");
            // 页签字段
            exportDataDTO.setFieldMap(FIELD);
            // 页签数据
            exportDataDTO.setData(data);

            List<ExportDataDTO> sheets = new ArrayList<>();
            sheets.add(exportDataDTO);
            result.setSheets(sheets);
        }
        return result;
    }

    @Override
    public QueryEnterpriseConnectMonitorPageRequest getParam(Map<String, Object> map) {
        QueryEnterpriseConnectMonitorPageRequest request = PojoUtils.map(map, QueryEnterpriseConnectMonitorPageRequest.class);
        if(null != request.getStartDockingTime()){
            request.setStartDockingTime(DateUtil.beginOfDay(request.getStartDockingTime()));
        }
        if(null != request.getEndDockingTime()){
            request.setEndDockingTime(DateUtil.endOfDay(request.getEndDockingTime()));
        }
        if(null != request.getStartCollectionTime()){
            request.setStartCollectionTime(DateUtil.beginOfDay(request.getStartCollectionTime()));
        }
        if(null != request.getEndCollectionTime()){
            request.setEndCollectionTime(DateUtil.endOfDay(request.getEndCollectionTime()));
        }
        String empId=map.getOrDefault("currentUserCode","").toString();
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(empId);
        log.info("直连接口监控导出权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        if(null == userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        request.setUserDatascopeBO(userDatascopeBO);
        return request;
    }
}
