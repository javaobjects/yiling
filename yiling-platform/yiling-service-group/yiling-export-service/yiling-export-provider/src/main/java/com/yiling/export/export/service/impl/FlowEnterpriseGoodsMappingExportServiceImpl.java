package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
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
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.search.flow.api.EsFlowEnterpriseGoodsMappingApi;
import com.yiling.search.flow.dto.EsFlowEnterpriseGoodsMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseGoodsMappingSearchRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseGoodsMappingExportServiceImpl
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@Slf4j
@Service("flowEnterpriseGoodsMappingExportService")
public class FlowEnterpriseGoodsMappingExportServiceImpl implements BaseExportQueryDataService<EsFlowEnterpriseGoodsMappingSearchRequest> {
    @DubboReference
    private EsFlowEnterpriseGoodsMappingApi esFlowEnterpriseGoodsMappingApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @DubboReference
    ErpClientApi erpClientApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("flowGoodsName", "*原始商品名称");
        FIELD.put("flowSpecification", "*原始商品规格");
        FIELD.put("flowGoodsInSn", "原始商品内码");
        FIELD.put("flowManufacturer", "原始商品厂家");
        FIELD.put("flowUnit", "原始商品单位");
        FIELD.put("convertUnit", "*转换单位");
        FIELD.put("convertNumber", "*转换系数");
        FIELD.put("crmGoodsCode", "*标准产品编码");
        FIELD.put("goodsName", "*标准产品名称");
        FIELD.put("crmEnterpriseId", "*经销商编码");
        FIELD.put("enterpriseName", "*经销商名称");
        FIELD.put("implementer", "实施负责人");
    }

    @Override
    public QueryExportDataDTO queryData(EsFlowEnterpriseGoodsMappingSearchRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        EsAggregationDTO<EsFlowEnterpriseGoodsMappingDTO> esAggregationDTO = esFlowEnterpriseGoodsMappingApi.searchFlowEnterpriseGoodsMapping(request);
        if(CollectionUtil.isNotEmpty(esAggregationDTO.getData())){
            //获取实施负责人
            List<Long> crmEnterpriseIds = esAggregationDTO.getData().stream().map(EsFlowEnterpriseGoodsMappingDTO::getCrmEnterpriseId).distinct().collect(Collectors.toList());
            Map<Long, String> implementerMap = erpClientApi.getInstallEmployeeByCrmEnterpriseIdList(crmEnterpriseIds);
            esAggregationDTO.getData().forEach(esDTO->{
                Map<String, Object> map = BeanUtil.beanToMap(esDTO);
                if(esDTO.getConvertUnit().equals(1)){
                    map.put("convertUnit","乘");
                }else if(esDTO.getConvertUnit().equals(2)){
                    map.put("convertUnit","除");
                }else {
                    map.put("convertUnit","");
                }
                if(null == esDTO.getCrmGoodsCode() || esDTO.getCrmGoodsCode()==0){
                    map.put("crmGoodsCode","");
                }
                if(null == esDTO.getConvertNumber() || BigDecimal.ZERO.compareTo(esDTO.getConvertNumber())>=0){
                    map.put("convertNumber","");
                } else {
                    map.put("convertNumber",esDTO.getConvertNumber().stripTrailingZeros().toPlainString());
                }
                map.put("implementer",implementerMap.getOrDefault(esDTO.getCrmEnterpriseId(),""));
                data.add(map);
            });
            ExportDataDTO exportDataDTO = new ExportDataDTO();
            String dateFormat = DateUtil.format(new Date(), "yyyyMMddHHmmss");
            if(request.getCrmGoodsCode()!=null && request.getCrmGoodsCode()==0){
                exportDataDTO.setSheetName("产品未对照关系_"+dateFormat);
            }else {
                exportDataDTO.setSheetName("产品对照关系_"+dateFormat);
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
    public EsFlowEnterpriseGoodsMappingSearchRequest getParam(Map<String, Object> map) {
        EsFlowEnterpriseGoodsMappingSearchRequest request = PojoUtils.map(map, EsFlowEnterpriseGoodsMappingSearchRequest.class);
        request.setCurrent(1);
        request.setSize(10000);
        if(request.getCrmGoodsCode()!=null && request.getCrmGoodsCode()==0){
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
        log.info("产品对照导出权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
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
