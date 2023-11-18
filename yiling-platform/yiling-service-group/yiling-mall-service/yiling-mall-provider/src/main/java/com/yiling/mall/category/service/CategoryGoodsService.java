package com.yiling.mall.category.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.category.dto.request.QueryCategoryGoodsPageListRequest;
import com.yiling.mall.category.entity.CategoryGoodsDO;

/**
 * <p>
 * 分类商品表 服务类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
public interface CategoryGoodsService extends BaseService<CategoryGoodsDO> {
    /**
     * 获取分类商品明细信息
     * @param request
     * @return
     */
    Page<CategoryGoodsDO> pageList(QueryCategoryGoodsPageListRequest request);

    /**
     * 统计分类列表下的商品个数
     * @param categoryIds   商品分类ID集合
     * @return key:categoryId value:商品个数
     */
    Map<Long, Long> countCategoryGoods(List<Long> categoryIds);

    /**
     * 根据分类ID。查询分类商品集合
     * @param categoryId
     * @return
     */
    List<CategoryGoodsDO> queryCategoryGoodsByCategoryId(Long categoryId);
}
