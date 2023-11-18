package com.yiling.mall.category.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.category.api.CategoryApi;
import com.yiling.mall.category.dto.CategoryDTO;
import com.yiling.mall.category.dto.HomeCategoryDTO;
import com.yiling.mall.category.dto.request.QueryCategoryPageListRequest;
import com.yiling.mall.category.dto.request.SaveCategoryRequest;
import com.yiling.mall.category.dto.request.UpdateCategoryRequest;
import com.yiling.mall.category.entity.CategoryDO;
import com.yiling.mall.category.service.CategoryService;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/15 0015
 */
@DubboService
public class CategoryApiImpl implements CategoryApi {

    @Autowired
    private CategoryService categoryService;

    @Override
    public Page<CategoryDTO> pageList(QueryCategoryPageListRequest request) {
        Page<CategoryDO> page = categoryService.pageList(request);
        List<CategoryDTO> records = page.getRecords().stream().map(e -> PojoUtils.map(e, CategoryDTO.class)).collect(Collectors.toList());

        Page<CategoryDTO> pageResult = PojoUtils.map(page, CategoryDTO.class);
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public CategoryDTO get(Long categoryId) {
        return PojoUtils.map(categoryService.get(categoryId), CategoryDTO.class);
    }

    @Override
    public Boolean createCategory(SaveCategoryRequest request) {
        return categoryService.addCategory(request);
    }

    @Override
    public Boolean updateCategory(UpdateCategoryRequest request) {
        return categoryService.updateCategory(request);
    }

    @Override
    public List<HomeCategoryDTO> getCategoryList(Integer num) {
        return PojoUtils.map(categoryService.queryCategoryList(Constants.YILING_EID, num), HomeCategoryDTO.class);
    }

}
