package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryExportServiceImpl
 * @描述
 * @创建时间 2023/4/14
 * @修改人 shichen
 * @修改时间 2023/4/14
 **/
@Slf4j
@Service("crmGoodsCategoryExportService")
public class CrmGoodsCategoryExportServiceImpl implements BaseExportQueryDataService<QueryCrmGoodsCategoryRequest> {

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("id", "品类id");
        FIELD.put("code", "品类编码");
        FIELD.put("name", "品类名称");
        FIELD.put("categoryLevel", "分类层级");
        FIELD.put("parentName", "上级分类");
        FIELD.put("finalStageFlag", "是否末级");
        FIELD.put("goodsCount", "关联产品数");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCrmGoodsCategoryRequest request) {
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        List<CrmGoodsCategoryDTO> categoryList = crmGoodsCategoryApi.queryCategoryList(request);
        if(CollectionUtil.isNotEmpty(categoryList)){
            //获取上级品类
            List<Long> parentId = categoryList.stream().filter(category -> category.getParentId() > 0).map(CrmGoodsCategoryDTO::getParentId).distinct().collect(Collectors.toList());
            List<CrmGoodsCategoryDTO> parentList = crmGoodsCategoryApi.findByIds(parentId);
            Map<Long, String> parentMap = parentList.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, CrmGoodsCategoryDTO::getName));
            //获取品类商品数量
            List<Long> categoryIds = categoryList.stream().map(CrmGoodsCategoryDTO::getId).collect(Collectors.toList());
            Map<Long, Long> goodsCountMap = crmGoodsCategoryApi.getGoodsCountByCategory(categoryIds);
            categoryList.forEach(category->{
                Map<String, Object> map = BeanUtil.beanToMap(category);
                map.put("parentName",parentMap.getOrDefault(category.getParentId(),""));
                map.put("finalStageFlag",category.getFinalStageFlag() == 1 ? "是":"否");
                map.put("goodsCount",goodsCountMap.getOrDefault(category.getId(),0L));
                data.add(map);
            });

            ExportDataDTO exportDataDTO = new ExportDataDTO();
            exportDataDTO.setSheetName("产品品类信息");
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
    public QueryCrmGoodsCategoryRequest getParam(Map<String, Object> map) {
        QueryCrmGoodsCategoryRequest request = PojoUtils.map(map, QueryCrmGoodsCategoryRequest.class);
        return request;
    }
}
