package com.yiling.cms.document.service;

import java.util.List;

import com.yiling.cms.document.dto.DocumentCategoryDTO;
import com.yiling.cms.document.dto.request.UpdateDocumentCategoryRequest;
import com.yiling.cms.document.entity.DocumentCategoryDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 文献栏目 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-06-02
 */
public interface DocumentCategoryService extends BaseService<DocumentCategoryDO> {

    /**
     * 编辑
     *
     * @param updateCategoryRequest
     */
    void updateCategory(UpdateDocumentCategoryRequest updateCategoryRequest);

    /**
     * 栏目列表
     *
     * @return
     */
    List<DocumentCategoryDTO> queryCategoryList(Integer status);


}
