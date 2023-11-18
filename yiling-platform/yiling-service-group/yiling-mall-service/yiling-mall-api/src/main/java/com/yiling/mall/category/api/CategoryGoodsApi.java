package com.yiling.mall.category.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.category.dto.CategoryGoodsDTO;
import com.yiling.mall.category.dto.HomeCategoryGoodsDTO;
import com.yiling.mall.category.dto.request.QueryCategoryGoodsPageListRequest;

/**
 * 分类商品明细 Api
 *
 * @author: yuecheng.chen
 * @date: 2021/6/16
 */
public interface CategoryGoodsApi {

    /**
     * 获取分类商品明细分页
     * @param request
     * @return
     */
    Page<CategoryGoodsDTO> pageList(QueryCategoryGoodsPageListRequest request);

    /**
     * 统计分类列表下的商品个数
     * @param categoryIds   商品分类ID集合
     * @return key:categoryId value:商品个数
     */
    Map<Long, Long> countCategoryGoods(List<Long> categoryIds);

    /**
     * 根据分类栏ID获取商品集合
     * @param categoryId    分类栏ID
     * @return
     */
    List<HomeCategoryGoodsDTO> getCategoryGoodsByCategoryId(Long categoryId);
}
