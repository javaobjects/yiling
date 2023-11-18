package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.api.FlowEnterpriseSupplierMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowEnterpriseSupplierMappingPageRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseSupplierMappingExportServiceImpl
 * @描述
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
@Slf4j
@Service("flowEnterpriseSupplierMappingExportService")
public class FlowEnterpriseSupplierMappingExportServiceImpl implements BaseExportQueryDataService<QueryFlowEnterpriseSupplierMappingPageRequest> {
    @DubboReference
    private FlowEnterpriseSupplierMappingApi flowEnterpriseSupplierMappingApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("flowSupplierName", "原始供应商名称");
        FIELD.put("crmOrgId", "标准机构编码");
        FIELD.put("orgName", "标准机构名称");
        FIELD.put("crmEnterpriseId", "经销商编码");
        FIELD.put("enterpriseName", "经销商名称");
        FIELD.put("province", "省份");
    }

    @Override
    public QueryExportDataDTO queryData(QueryFlowEnterpriseSupplierMappingPageRequest request) {
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        Page<FlowEnterpriseSupplierMappingDTO> page = flowEnterpriseSupplierMappingApi.pageList(request);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            page.getRecords().forEach(supplierMappingDTO->{
                Map<String, Object> map = BeanUtil.beanToMap(supplierMappingDTO);
                if(null == supplierMappingDTO.getCrmOrgId() || supplierMappingDTO.getCrmOrgId()==0){
                    map.put("crmOrgId","");
                }
                data.add(map);
            });
            ExportDataDTO exportDataDTO = new ExportDataDTO();
            String dateFormat = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            if(request.getCrmOrgId()!=null && request.getCrmOrgId()==0){
                exportDataDTO.setSheetName("供应商未对照关系_"+dateFormat);
            }else {
                exportDataDTO.setSheetName("供应商对照关系_"+dateFormat);
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
    public QueryFlowEnterpriseSupplierMappingPageRequest getParam(Map<String, Object> map) {
        QueryFlowEnterpriseSupplierMappingPageRequest request = PojoUtils.map(map, QueryFlowEnterpriseSupplierMappingPageRequest.class);
        request.setCurrent(1);
        request.setSize(10000);
        if(request.getCrmOrgId()!=null && request.getCrmOrgId()==0){
            request.setOrderByUploadTime(true);
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
        request.setUserDatascopeBO(userDatascopeBO);
        return request;
    }
}
