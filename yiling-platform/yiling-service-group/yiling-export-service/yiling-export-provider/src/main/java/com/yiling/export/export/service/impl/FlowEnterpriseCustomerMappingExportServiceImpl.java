package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.search.flow.api.EsFlowEnterpriseCustomerMappingApi;
import com.yiling.search.flow.dto.EsFlowEnterpriseCustomerMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseCustomerMappingSearchRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseCustomerMappingExportServiceImpl
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Slf4j
@Service("flowEnterpriseCustomerMappingExportService")
public class FlowEnterpriseCustomerMappingExportServiceImpl implements BaseExportQueryDataService<EsFlowEnterpriseCustomerMappingSearchRequest> {

    @DubboReference
    private EsFlowEnterpriseCustomerMappingApi esFlowEnterpriseCustomerMappingApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("flowCustomerName", "原始客户名称");
        FIELD.put("crmOrgId", "标准机构编码");
        FIELD.put("orgName", "标准机构名称");
        FIELD.put("crmEnterpriseId", "经销商编码");
        FIELD.put("enterpriseName", "经销商名称");
        FIELD.put("province", "省份");
    }

    @Override
    public QueryExportDataDTO queryData(EsFlowEnterpriseCustomerMappingSearchRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> esAggregationDTO = esFlowEnterpriseCustomerMappingApi.searchFlowEnterpriseCustomerMapping(request);
        if(CollectionUtil.isNotEmpty(esAggregationDTO.getData())){
            esAggregationDTO.getData().forEach(esDTO->{
                Map<String, Object> map = BeanUtil.beanToMap(esDTO);
                if(null == esDTO.getCrmOrgId() || esDTO.getCrmOrgId()==0){
                    map.put("crmOrgId","");
                }
                data.add(map);
            });
            ExportDataDTO exportDataDTO = new ExportDataDTO();
            String dateFormat = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            if(request.getCrmOrgId()!=null && request.getCrmOrgId()==0){
                exportDataDTO.setSheetName("客户未对照关系_"+dateFormat);
            }else {
                exportDataDTO.setSheetName("客户对照关系_"+dateFormat);
            }
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
    public EsFlowEnterpriseCustomerMappingSearchRequest getParam(Map<String, Object> map) {
        EsFlowEnterpriseCustomerMappingSearchRequest request = PojoUtils.map(map, EsFlowEnterpriseCustomerMappingSearchRequest.class);
        request.setCurrent(1);
        request.setSize(10000);
        if(request.getCrmOrgId()!=null && request.getCrmOrgId()==0){
            request.setSortField("last_upload_time");
        }else {
            request.setSortField("update_time");
        }
        if(null!=request.getStartUpdateTime()){
            request.setStartUpdateTime(DateUtil.beginOfDay(request.getStartUpdateTime()));
        }
        if(null!=request.getEndUpdateTime()){
            request.setEndUpdateTime(DateUtil.endOfDay(request.getEndUpdateTime()));
        }
        if(null!=request.getStartLastUploadTime()){
            request.setStartLastUploadTime(DateUtil.beginOfDay(request.getStartLastUploadTime()));
        }
        if(null!=request.getEndLastUploadTime()){
            request.setEndLastUploadTime(DateUtil.endOfDay(request.getEndLastUploadTime()));
        }
        String empId=map.getOrDefault("currentUserCode","").toString();
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(empId);
        log.info("客户对照导出权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        if(null == userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        request.setOrgDatascope(userDatascopeBO.getOrgDatascope());
        if(null!=userDatascopeBO.getOrgPartDatascopeBO()){
            request.setCrmEnterpriseIds(userDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
            request.setProvinceCodes(userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
        }
        return request;
    }
}
