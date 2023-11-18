package com.yiling.mall.category.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.category.dto.request.QueryCategoryPageListRequest;
import com.yiling.mall.category.dto.request.SaveCategoryRequest;
import com.yiling.mall.category.dto.request.UpdateCategoryRequest;
import com.yiling.mall.category.entity.CategoryDO;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
public interface CategoryService extends BaseService<CategoryDO> {
    /**
     * 查询分类分页列表
     *
     * @param request
     * @return
     */
    Page<CategoryDO> pageList(QueryCategoryPageListRequest request);

    /**
     * 获取分类信息
     * @param categoryId  categoryId
     * @return
     */
    CategoryDO get(Long categoryId);

    /**
     * 创建分类
     *
     * @param request
     * @return
     */
    Boolean addCategory(SaveCategoryRequest request);

    /**
     * 修改分类
     *
     * @param request
     * @return
     */
    Boolean updateCategory(UpdateCategoryRequest request);

    /**
     * 查询分类栏列表
     * @param eid   eid
     * @param num   展示个数
     * @return
     */
    List<CategoryDO> queryCategoryList(Long eid, Integer num);
}
