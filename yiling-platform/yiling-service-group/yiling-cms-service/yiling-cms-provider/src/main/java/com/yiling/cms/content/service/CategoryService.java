package com.yiling.cms.content.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.cms.content.dto.request.AddCategoryRequest;
import com.yiling.cms.content.dto.request.QueryCategoryPageRequest;
import com.yiling.cms.content.dto.request.UpdateCategoryRequest;
import com.yiling.cms.content.entity.CategoryDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 栏目 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
public interface CategoryService extends BaseService<CategoryDO> {

    /**
     * 添加栏目
     *
     * @param addCategoryRequest
     */
    void addCategory(AddCategoryRequest addCategoryRequest);

    /**
     * 编辑
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
     * 栏目
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
     * 根据业务线id、模块id获取分类
     *
     * @param lineId
     * @param moduleId
     * @return
     */
    List<CategoryDTO> getCategoryByLineIdAndModuleId(Long lineId, Long moduleId);

    List<CategoryDTO> getCategorySaAndB2B(Long lineId, Long moduleId);

    /**
     * 分页查询栏目供以岭互联网医院使用
     *
     * @param request
     * @return
     */
    Page<CategoryDTO> queryCategoryHospitalList(QueryCategoryPageRequest request);
}
