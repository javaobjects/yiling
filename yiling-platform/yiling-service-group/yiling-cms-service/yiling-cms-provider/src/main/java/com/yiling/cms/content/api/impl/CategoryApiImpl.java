package com.yiling.cms.content.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.CategoryApi;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.cms.content.dto.CategoryDisplayLineDTO;
import com.yiling.cms.content.dto.request.AddCategoryRequest;
import com.yiling.cms.content.dto.request.QueryCategoryPageRequest;
import com.yiling.cms.content.dto.request.UpdateCategoryRequest;
import com.yiling.cms.content.service.CategoryDisplayLineService;
import com.yiling.cms.content.service.CategoryService;

/**
 * 栏目
 *
 * @author: gxl
 * @date: 2022/3/23
 */
@DubboService
public class CategoryApiImpl implements CategoryApi {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryDisplayLineService displayLineService;

    @Override
    public void addCategory(AddCategoryRequest addCategoryRequest) {
        categoryService.addCategory(addCategoryRequest);
    }

    @Override
    public void updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        categoryService.updateCategory(updateCategoryRequest);
    }

    @Override
    public List<CategoryDTO> queryCategoryList(Integer status) {
        return categoryService.queryCategoryList(status);
    }

    @Override
    public List<CategoryDisplayLineDTO> queryCategoryList() {
        return displayLineService.getCategoryList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return categoryService.getCategoryById(id);
    }

    @Override
    public List<CategoryDTO> queryAppCategoryList(Long lineId) {
        return categoryService.queryAppCategoryList(lineId);
    }

    @Override
    public List<CategoryDTO> getCategoryByLineIdAndModuleId(Long lineId, Long moduleId) {
        return categoryService.getCategoryByLineIdAndModuleId(lineId, moduleId);
    }

    @Override
    public List<CategoryDTO> getCategorySaAndB2B(Long lineId, Long moduleId) {
        return categoryService.getCategorySaAndB2B(lineId, moduleId);
    }

    @Override
    public List<CategoryDisplayLineDTO> getCategoryByLineId(Long lineId) {
        return displayLineService.getCategoryByLineId(lineId);
    }

    @Override
    public Page<CategoryDTO> queryCategoryHospitalList(QueryCategoryPageRequest request) {
        return categoryService.queryCategoryHospitalList(request);
    }
}