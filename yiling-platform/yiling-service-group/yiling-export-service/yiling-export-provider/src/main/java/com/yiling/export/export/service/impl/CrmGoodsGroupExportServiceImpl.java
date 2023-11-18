package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.api.CrmDepartProductGroupApi;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupRelationDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsGroupPageRequest;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsGroupExportServiceImpl
 * @描述
 * @创建时间 2023/4/14
 * @修改人 shichen
 * @修改时间 2023/4/14
 **/
@Slf4j
@Service("crmGoodsGroupExportService")
public class CrmGoodsGroupExportServiceImpl implements BaseExportQueryDataService<QueryCrmGoodsGroupPageRequest> {
    @DubboReference
    private CrmGoodsGroupApi crmGoodsGroupApi;

    @DubboReference
    private CrmDepartProductGroupApi crmDepartProductGroupApi;

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private EsbOrganizationApi esbOrganizationApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("id", "产品组ID");
        FIELD.put("code", "产品组编码");
        FIELD.put("name", "产品组名称");
        FIELD.put("status", "状态");
        FIELD.put("departments", "业务部门");
        FIELD.put("goodsCode", "商品编码");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsStatus", "产品启用状态");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCrmGoodsGroupPageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();

        if(StringUtils.isNotBlank(request.getDepartment())){
            List<EsbOrganizationDTO> orgList = esbOrganizationApi.findByOrgName(request.getDepartment(),null);
            if(CollectionUtil.isEmpty(orgList)){
                return result;
            }
            List<Long> departIds = orgList.stream().map(EsbOrganizationDTO::getOrgId).collect(Collectors.toList());
            request.setDepartIds(departIds);
        }
        Page<CrmGoodsGroupDTO> page = crmGoodsGroupApi.queryGroupPage(request);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            List<Long> groupIds = page.getRecords().stream().map(CrmGoodsGroupDTO::getId).collect(Collectors.toList());
            //获取组和商品关联
            Map<Long, List<CrmGoodsGroupRelationDTO>> goodsRelationMap = crmGoodsGroupApi.findGoodsRelationByGroupIds(groupIds);
            //获取组和部门关联
            List<CrmDepartmentProductRelationDTO> departRelationList = crmDepartProductGroupApi.getByGroupIds(groupIds);
            Map<Long, List<CrmDepartmentProductRelationDTO>> departRelationMap = departRelationList.stream().collect(Collectors.groupingBy(CrmDepartmentProductRelationDTO::getProductGroupId));
            //获取商品信息
            List<Long> goodsIds = goodsRelationMap.values().stream().flatMap(Collection::stream).map(CrmGoodsGroupRelationDTO::getGoodsId).distinct().collect(Collectors.toList());
            List<CrmGoodsInfoDTO> goodsList = crmGoodsInfoApi.findByIds(goodsIds);
            Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getId, CrmGoodsInfoDTO::getGoodsName));

            page.getRecords().forEach(group->{
                List<CrmDepartmentProductRelationDTO> departList = departRelationMap.get(group.getId());
                List<String> departStrList= ListUtil.toList();
                if(CollectionUtil.isNotEmpty(departList)){
                    departList.forEach(depart->{
                        departStrList.add(depart.getDepartment()+"(编码:"+depart.getDepartmentId()+")");
                    });
                }
                String departments = StringUtils.join(departStrList,",");
                String status = "";
                if(group.getStatus()==0){
                    status="有效";
                }else if(group.getStatus()==1){
                    status="无效";
                }

                List<CrmGoodsGroupRelationDTO> goodsRelationList = goodsRelationMap.get(group.getId());
                if(CollectionUtil.isNotEmpty(goodsRelationList)){
                    String finalStatus = status;
                    goodsRelationList.forEach(goodsRelation->{
                        Map<String, Object> map = BeanUtil.beanToMap(group);
                        map.put("status", finalStatus);
                        map.put("departments", departments);
                        map.put("goodsCode", goodsRelation.getGoodsCode());
                        map.put("goodsName", goodsMap.getOrDefault(goodsRelation.getGoodsId(), ""));
                        if(goodsRelation.getStatus() == 0){
                            map.put("goodsStatus", "启用");
                        }else if(goodsRelation.getStatus() == 1){
                            map.put("goodsStatus", "停用");
                        }
                        data.add(map);
                    });
                }else {
                    Map<String, Object> map = BeanUtil.beanToMap(group);
                    map.put("status", status);
                    map.put("departments", departments);
                    data.add(map);
                }
            });
            ExportDataDTO exportDataDTO = new ExportDataDTO();
            exportDataDTO.setSheetName("产品组信息");
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
    public QueryCrmGoodsGroupPageRequest getParam(Map<String, Object> map) {
        QueryCrmGoodsGroupPageRequest request = PojoUtils.map(map, QueryCrmGoodsGroupPageRequest.class);
        request.setCurrent(1);
        request.setSize(10000);
        return request;
    }
}
