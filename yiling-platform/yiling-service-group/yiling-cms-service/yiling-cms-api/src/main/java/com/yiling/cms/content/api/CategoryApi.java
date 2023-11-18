package com.yiling.cms.content.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.cms.content.dto.CategoryDisplayLineDTO;
import com.yiling.cms.content.dto.request.AddCategoryRequest;
import com.yiling.cms.content.dto.request.QueryCategoryPageRequest;
import com.yiling.cms.content.dto.request.UpdateCategoryRequest;

/**
 * 栏目
 *
 * @author: gxl
 * @date: 2022/3/23
 */
public interface CategoryApi {

    /**
     * 添加栏目
     *
     * @param addCategoryRequest
     */
    void addCategory(AddCategoryRequest addCategoryRequest);

    /**
     * 编辑栏目
     *
     * @param updateCategoryRequest
     */
    void updateCategory(UpdateCategoryRequest updateCategoryRequest);

    /**
     * 栏目列表
     *
     * @return
     */
    List<CategoryDTO> queryCategoryList(Integer status);

    /**
     * 栏目列表
     *
     * @return
     */
    List<CategoryDisplayLineDTO> queryCategoryList();

    /**
     * 查询单个栏目
     *
     * @param id
     * @return
     */
    CategoryDTO getCategoryById(Long id);

    /**
     * app端栏目列表
     *
     * @return
     */
    List<CategoryDTO> queryAppCategoryList(Long lineId);

    /**
     * 获取业务线下模块下的栏目
     *
     * @param lineId
     * @param moduleId
     * @return
     */
    List<CategoryDTO> getCategoryByLineIdAndModuleId(Long lineId, Long moduleId);

    List<CategoryDTO> getCategorySaAndB2B(Long lineId, Long moduleId);

    /**
     * 获取业务线下模块下的栏目
     *
     * @param lineId
     * @return
     */
    List<CategoryDisplayLineDTO> getCategoryByLineId(Long lineId);

    /**
     * 分页查询栏目供以岭互联网医院使用
     *
     * @param request
     * @return
     */
    Page<CategoryDTO> queryCategoryHospitalList(QueryCategoryPageRequest request);
}