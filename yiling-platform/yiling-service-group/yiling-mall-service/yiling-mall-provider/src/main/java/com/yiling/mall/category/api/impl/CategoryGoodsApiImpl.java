package com.yiling.mall.category.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.category.api.CategoryGoodsApi;
import com.yiling.mall.category.dto.CategoryGoodsDTO;
import com.yiling.mall.category.dto.HomeCategoryGoodsDTO;
import com.yiling.mall.category.dto.request.QueryCategoryGoodsPageListRequest;
import com.yiling.mall.category.service.CategoryGoodsService;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/15 0015
 */
@DubboService
public class CategoryGoodsApiImpl implements CategoryGoodsApi {

    @Autowired
    private CategoryGoodsService categoryGoodsService;

    @Override
    public Page<CategoryGoodsDTO> pageList(QueryCategoryGoodsPageListRequest request) {
        return PojoUtils.map(categoryGoodsService.pageList(request), CategoryGoodsDTO.class);
    }

    @Override
    public Map<Long, Long> countCategoryGoods(List<Long> categoryIds) {
        return categoryGoodsService.countCategoryGoods(categoryIds);
    }

    @Override
    public List<HomeCategoryGoodsDTO> getCategoryGoodsByCategoryId(Long categoryId) {
        return PojoUtils.map(categoryGoodsService.queryCategoryGoodsByCategoryId(categoryId), HomeCategoryGoodsDTO.class);
    }
}
