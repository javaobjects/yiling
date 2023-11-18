package com.yiling.cms.document.api;

import java.util.List;

import com.yiling.cms.document.dto.DocumentCategoryDTO;
import com.yiling.cms.document.dto.request.AddDocumentCategoryRequest;
import com.yiling.cms.document.dto.request.UpdateDocumentCategoryRequest;

/**
 * 文献栏目
 * @author: gxl
 * @date: 2022/6/2
 */
public interface DocumentCategoryApi {
    /**
     * 添加栏目
     * @param addCategoryRequest
     */
    void addCategory(AddDocumentCategoryRequest addCategoryRequest);

    /**
     * 编辑栏目
     * @param updateCategoryRequest
     */
    void updateCategory(UpdateDocumentCategoryRequest updateCategoryRequest);

    /**
     *
     * 栏目列表
     * @return
     */
    List<DocumentCategoryDTO> queryCategoryList(Integer status);

}
