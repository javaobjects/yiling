package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.api.CrmGoodsTagApi;
import com.yiling.dataflow.crm.bo.CrmGoodsTagRelationBO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.enums.CrmGoodsTagTypeEnum;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsInfoExportServiceImpl
 * @描述
 * @创建时间 2023/4/13
 * @修改人 shichen
 * @修改时间 2023/4/13
 **/
@Slf4j
@Service("crmGoodsInfoExportService")
public class CrmGoodsInfoExportServiceImpl implements BaseExportQueryDataService<QueryCrmGoodsInfoPageRequest> {

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @DubboReference
    private CrmGoodsTagApi crmGoodsTagApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("id", "产品ID");
        FIELD.put("goodsCode", "产品编码");
        FIELD.put("goodsGroup", "产品分类");
        FIELD.put("goodsName", "产品名称");
        FIELD.put("goodsSpec", "规格");
        FIELD.put("goodsNameAbbr", "缩写");
        FIELD.put("packing", "小包装");
        FIELD.put("unit", "计量单位");
        FIELD.put("dosageForm", "剂型");
        FIELD.put("conversionFactor", "换算因子");
        FIELD.put("isPrescription", "是否处方药");
        FIELD.put("category", "品种类型");
        FIELD.put("status", "状态");
        FIELD.put("supplyPrice", "供货价");
        FIELD.put("exFactoryPrice", "出厂价");
        FIELD.put("purchasePrice", "采购价");
        FIELD.put("salesPrice", "销售价");
        FIELD.put("assessmentPrice", "考核价");
        FIELD.put("startTime", "开始时间");
        FIELD.put("endTime", "结束时间");
        FIELD.put("isGroupPurchase", "是否团购产品");
        FIELD.put("notLockTags", "非锁标签");
        FIELD.put("groupPurchaseTags", "团购标签");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCrmGoodsInfoPageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        Page<CrmGoodsInfoDTO> page = crmGoodsInfoApi.getPage(request);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            List<Long> categoryIds = page.getRecords().stream().filter(goods -> goods.getCategoryId() > 0).map(CrmGoodsInfoDTO::getCategoryId).distinct().collect(Collectors.toList());
            List<CrmGoodsCategoryDTO> categoryList = crmGoodsCategoryApi.findByIds(categoryIds);
            Map<Long, String> categoryMap = categoryList.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, CrmGoodsCategoryDTO::getName));

            List<Long> goodsIds = page.getRecords().stream().map(CrmGoodsInfoDTO::getId).collect(Collectors.toList());
            Map<Long, List<CrmGoodsTagRelationBO>> goodsTagMap = crmGoodsTagApi.findTagByGoodsIds(goodsIds);

            page.getRecords().forEach(goodsInfo->{
                Map<String, Object> map = BeanUtil.beanToMap(goodsInfo);
                if(goodsInfo.getStatus()==0){
                    map.put("status","有效");
                }else if(goodsInfo.getStatus()==1){
                    map.put("status","无效");
                }
                if(goodsInfo.getIsGroupPurchase()==0){
                    map.put("isGroupPurchase","未开启团购");
                }else if(goodsInfo.getIsGroupPurchase()==1){
                    map.put("isGroupPurchase","开启团购");
                }
                if(goodsInfo.getIsPrescription()==1){
                    map.put("isPrescription","否");
                }else if(goodsInfo.getIsPrescription()==2){
                    map.put("isPrescription","是");
                }else {
                    map.put("isGroupPurchase","");
                }
                map.put("supplyPrice",goodsInfo.getSupplyPrice().stripTrailingZeros().toPlainString());
                map.put("exFactoryPrice",goodsInfo.getExFactoryPrice().stripTrailingZeros().toPlainString());
                map.put("purchasePrice",goodsInfo.getPurchasePrice().stripTrailingZeros().toPlainString());
                map.put("salesPrice",goodsInfo.getSalesPrice().stripTrailingZeros().toPlainString());
                map.put("assessmentPrice",goodsInfo.getAssessmentPrice().stripTrailingZeros().toPlainString());
                map.put("category",categoryMap.getOrDefault(goodsInfo.getCategoryId(),""));
                List<CrmGoodsTagRelationBO> relationBOS = goodsTagMap.get(goodsInfo.getId());
                if(CollectionUtil.isNotEmpty(relationBOS)){
                    List<String> notLockTagList = relationBOS.stream().filter(e->CrmGoodsTagTypeEnum.NOT_LOCK.getType().equals(e.getTagType())).map(CrmGoodsTagRelationBO::getTagName).collect(Collectors.toList());
                    map.put("notLockTags", StringUtils.join(notLockTagList, ";"));
                    List<String> groupPurchaseTagList = relationBOS.stream().filter(e->CrmGoodsTagTypeEnum.GROUP_PURCHASE.getType().equals(e.getTagType())).map(CrmGoodsTagRelationBO::getTagName).collect(Collectors.toList());
                    map.put("groupPurchaseTags", StringUtils.join(groupPurchaseTagList, ";"));
                }
                data.add(map);
            });
            ExportDataDTO exportDataDTO = new ExportDataDTO();
            exportDataDTO.setSheetName("标准商品信息");
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
    public QueryCrmGoodsInfoPageRequest getParam(Map<String, Object> map) {
        QueryCrmGoodsInfoPageRequest request = PojoUtils.map(map, QueryCrmGoodsInfoPageRequest.class);
        request.setCurrent(1);
        request.setSize(10000);
        return request;
    }
}
