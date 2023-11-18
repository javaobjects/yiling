package com.yiling.export.export.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.QueryDataScopeRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockWarnApi;
import com.yiling.dataflow.statistics.dto.StockWarnDTO;
import com.yiling.dataflow.statistics.dto.request.StockWarnPageRequest;
import com.yiling.dataflow.statistics.enums.FlowAnalyseStockStatusEnum;
import com.yiling.export.export.bo.ExportStockWarnPageListBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("stockWarningPageListExportService")
@Slf4j
public class StockWarningPageListExportServiceImpl  implements BaseExportQueryDataService<StockWarnPageRequest> {
    @DubboReference
    private CrmSupplierApi crmSupplierApi;
    @DubboReference(timeout = 60000)
    private FlowAnalyseStockWarnApi flowAnalyseStockWarnApi;
    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference(timeout = 60000)
    private CrmEnterpriseApi crmEnterpriseApi;
    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("ename", "经销商");
        FIELD.put("enameLevel","渠道等级");
        FIELD.put("goodsName", "商品");
        FIELD.put("near3AverageDailySales", "近3天日均销量");
        FIELD.put("near3AbleSaleDaysStr", "库存可销售天数\r\n(近3天日均销量)");
        FIELD.put("near30AverageDailySales", "近30天日均销量");
        FIELD.put("near30AbleSaleDaysStr", "库存可销售天数\r\n(近30天日均销量)");
        FIELD.put("stockQuantity", "库存数量");
        FIELD.put("statusStr", "库存状态");
        FIELD.put("replenishQuantityMax", "建议补货数量上限");
        FIELD.put("replenishQuantityMin", "建议补货数量下限");
    }
    @Override
    public QueryExportDataDTO queryData(StockWarnPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(request.getSjmsUserDatascopeBO().getOrgDatascope())){
            return setResultParam(result,data);
        }
        Page<StockWarnDTO> page;
        int current=1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = flowAnalyseStockWarnApi.getStockWarnPage(request);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            for (StockWarnDTO e : page.getRecords()) {
                ExportStockWarnPageListBO bo=new ExportStockWarnPageListBO();
                PojoUtils.map(e,bo);
                bo.setNear3AbleSaleDaysStr((bo.getNear3AbleSaleDays()==null||bo.getNear3AbleSaleDays().compareTo(BigDecimal.ZERO)==0)?"0": bo.getNear3AbleSaleDays().setScale(1,BigDecimal.ROUND_DOWN).toString());
                bo.setNear30AbleSaleDaysStr((bo.getNear30AbleSaleDays()==null||bo.getNear30AbleSaleDays().compareTo(BigDecimal.ZERO)==0)?"0": bo.getNear30AbleSaleDays().setScale(1,BigDecimal.ROUND_DOWN).toString());
                bo.setStatusStr(FlowAnalyseStockStatusEnum.getDescByCode(bo.getStatus()));
                Map<String, Object> dataPojo = BeanUtil.beanToMap(bo);
                data.add(dataPojo);
            }
            current=current+1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));
        return setResultParam(result,data);
    }

    @Override
    public StockWarnPageRequest getParam(Map<String, Object> map) {
        //参数eid的转换为eidList
        StockWarnPageRequest request= PojoUtils.map(map, StockWarnPageRequest.class);
        String empId=map.getOrDefault("currentUserCode","").toString();
        // 获取权限
        SjmsUserDatascopeBO sjmsUserDatascopeBO = sjmsUserDatascopeApi.getByEmpId(empId);
        log.info("补货预测获取权限:empId={},sjms={}",empId,sjmsUserDatascopeBO);
        //代表没权限返回错误
        request.setSjmsUserDatascopeBO(sjmsUserDatascopeBO);
        List<Long> eidList= Lists.newArrayList();
        List<Long> listByLevelAndGroupEidList= ListUtil.empty();
        if (Objects.nonNull(request.getSupplierLevel()) ||Objects.nonNull(request.getBusinessSystem())){
            listByLevelAndGroupEidList = crmSupplierApi.listByLevelAndGroup(request.getSupplierLevel(), request.getBusinessSystem());
            listByLevelAndGroupEidList.add(-1L);
        }
        log.info("listByLevelAndGroupEidList->{},eid->{}",listByLevelAndGroupEidList,request.getCrmEnterpriseId());
        if(Objects.nonNull(request.getCrmEnterpriseId())&& request.getCrmEnterpriseId().intValue()>0&&CollUtil.isEmpty(listByLevelAndGroupEidList)||(CollUtil.isNotEmpty(listByLevelAndGroupEidList)&&listByLevelAndGroupEidList.contains(request.getCrmEnterpriseId()))){
            eidList.add(request.getCrmEnterpriseId());
        }
        if(Objects.nonNull(request.getCrmEnterpriseId())&& request.getCrmEnterpriseId().intValue()>0&&(CollUtil.isNotEmpty(listByLevelAndGroupEidList)&&!listByLevelAndGroupEidList.contains(request.getCrmEnterpriseId()))){
            eidList.add(-1L);
        }
        if(Objects.isNull(request.getCrmEnterpriseId())&&CollUtil.isNotEmpty(listByLevelAndGroupEidList)){
            eidList.addAll(listByLevelAndGroupEidList);
        }
        //permit  当不等于空的时候有权限控制
        // 2中逻辑控制 1中时获取权限2个list的交集，1中时返回权限列表的list;
        if(OrgDatascopeEnum.PORTION==OrgDatascopeEnum.getFromCode(sjmsUserDatascopeBO.getOrgDatascope())){
            QueryDataScopeRequest request1=new QueryDataScopeRequest();
            request1.setSjmsUserDatascopeBO(sjmsUserDatascopeBO);
            List<CrmEnterpriseDTO>  permitCrmEnterList= crmEnterpriseApi.getCrmEnterpriseListByDataScope(request1);
            List<Long> eIdsPermitList= Optional.ofNullable(permitCrmEnterList.stream().filter(m->m.getEid()>0).map(CrmEnterpriseDTO::getEid).collect(Collectors.toList())).orElse(Lists.newArrayList());
            //什么时间获取交集
            //当 查询条件大于>的时候获取交集
            if(!eidList.isEmpty()){
                List<Long> intersection=eIdsPermitList.stream().filter(item->eidList.contains(item)).collect(Collectors.toList());
                if(Objects.nonNull(request.getBusinessSystem())||Objects.nonNull(request.getSupplierLevel())){
                    intersection.add(-1L);
                }
                if(intersection.isEmpty()){
                    intersection.add(-1L);
                }
                return  request.setCrmIdList(intersection);
            }
            return request.setCrmIdList(eIdsPermitList);
        }

        return  request.setCrmIdList(eidList);
    }
    private QueryExportDataDTO setResultParam(QueryExportDataDTO result,List<Map<String, Object>> data){
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("库存预警列表");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }
}
