package com.yiling.dataflow.crm.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsCategoryDO;
import com.yiling.dataflow.crm.service.CrmGoodsCategoryService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryApiImpl
 * @描述
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
@Slf4j
@DubboService
public class CrmGoodsCategoryApiImpl implements CrmGoodsCategoryApi {
    @Autowired
    private CrmGoodsCategoryService crmGoodsCategoryService;

    @Override
    public Long saveOrUpdateCategory(SaveOrUpdateCrmGoodsCategoryRequest request) {
        return crmGoodsCategoryService.saveOrUpdateCategory(request);
    }

    @Override
    public CrmGoodsCategoryDTO findByCodeOrName(String code, String name) {
        return crmGoodsCategoryService.findByCodeOrName(code,name);
    }

    @Override
    public List<CrmGoodsCategoryDTO> getFinalStageCategory(String category) {
        return crmGoodsCategoryService.getFinalStageCategory(category);
    }

    @Override
    public List<Integer> getAllLevel() {
        return crmGoodsCategoryService.getAllLevel();
    }

    @Override
    public List<CrmGoodsCategoryDTO> queryCategoryList(QueryCrmGoodsCategoryRequest request) {
        return crmGoodsCategoryService.queryCategoryList(request);
    }

    @Override
    public Map<Long, Long> getGoodsCountByCategory(List<Long> categoryIds) {
        return crmGoodsCategoryService.getGoodsCountByCategory(categoryIds);
    }

    @Override
    public List<CrmGoodsCategoryDTO> findByIds(List<Long> ids) {
        if(CollectionUtil.isEmpty(ids)){
            return ListUtil.empty();
        }
        return PojoUtils.map(crmGoodsCategoryService.listByIds(ids),CrmGoodsCategoryDTO.class);
    }

    @Override
    public CrmGoodsCategoryDTO findById(Long id) {
        CrmGoodsCategoryDO categoryDO = crmGoodsCategoryService.getById(id);
        return PojoUtils.map(categoryDO,CrmGoodsCategoryDTO.class);
    }

    @Override
    public List<CrmGoodsCategoryDTO> findByParentId(Long parentId) {
        return crmGoodsCategoryService.findByParentId(parentId);
    }

    @Override
    public List<CrmGoodsCategoryDTO> findBakByParentId(Long parentId, String tableSuffix) {
        return crmGoodsCategoryService.findBakByParentId(parentId,tableSuffix);
    }

    @Override
    public Long findFirstCategoryByFinal(Long categoryId, String tableSuffix) {
        return crmGoodsCategoryService.findFirstCategoryByFinal(categoryId,tableSuffix);
    }

    @Override
    public void deleteCategoryById(Long id, Long opUserId) {
        CrmGoodsCategoryDO deleteDO = new CrmGoodsCategoryDO();
        deleteDO.setId(id);
        deleteDO.setOpUserId(opUserId);
        crmGoodsCategoryService.deleteByIdWithFill(deleteDO);
    }
}
