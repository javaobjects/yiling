package com.yiling.mall.category.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.category.dto.CategoryDTO;
import com.yiling.mall.category.dto.HomeCategoryDTO;
import com.yiling.mall.category.dto.request.QueryCategoryPageListRequest;
import com.yiling.mall.category.dto.request.SaveCategoryRequest;
import com.yiling.mall.category.dto.request.UpdateCategoryRequest;

/**
 * 商品分类 Api
 *
 * @author: yuecheng.chen
 * @date: 2021/6/16
 */
public interface CategoryApi {
    /**
     * 查询分类分页列表
     *
     * @param request
     * @return
     */
    Page<CategoryDTO> pageList(QueryCategoryPageListRequest request);

    /**
     * 获取分类明细
     * @param categoryId  分类Id
     * @return
     */
    CategoryDTO get(Long categoryId);

    /**
     * 创建分类
     *
     * @param request
     * @return
     */
    Boolean createCategory(SaveCategoryRequest request);

    /**
     * 修改分类
     *
     * @param request
     * @return
     */
    Boolean updateCategory(UpdateCategoryRequest request);

    /**
     * pop首页-获取商品分类栏
     * @param num       展示个数（最多8栏）
     * @return
     */
    List<HomeCategoryDTO> getCategoryList(Integer num);
}
