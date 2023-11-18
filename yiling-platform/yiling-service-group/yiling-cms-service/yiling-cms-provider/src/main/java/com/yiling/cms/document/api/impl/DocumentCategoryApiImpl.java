package com.yiling.cms.document.api.impl;

import java.util.List;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.cms.document.api.DocumentCategoryApi;
import com.yiling.cms.document.dto.DocumentCategoryDTO;
import com.yiling.cms.document.dto.request.AddDocumentCategoryRequest;
import com.yiling.cms.document.dto.request.UpdateDocumentCategoryRequest;
import com.yiling.cms.document.entity.DocumentCategoryDO;
import com.yiling.cms.document.service.DocumentCategoryService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

/**
 * 文献栏目
 * @author: gxl
 * @date: 2022/6/2
 */
@DubboService
public class DocumentCategoryApiImpl implements DocumentCategoryApi {
    @Autowired
    private  DocumentCategoryService documentCategoryService;

    @Override
    public void addCategory(AddDocumentCategoryRequest addCategoryRequest) {
        LambdaQueryWrapper<DocumentCategoryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DocumentCategoryDO::getCategoryName,addCategoryRequest.getCategoryName()).last("limit 1");
        DocumentCategoryDO one = documentCategoryService.getOne(wrapper);
        if(Objects.nonNull(one)){
            throw new BusinessException(ResultCode.FAILED,"此栏目已存在");
        }
        DocumentCategoryDO documentCategoryDO = new DocumentCategoryDO();
        PojoUtils.map(addCategoryRequest,documentCategoryDO);
        documentCategoryService.save(documentCategoryDO);
    }

    @Override
    public void updateCategory(UpdateDocumentCategoryRequest updateCategoryRequest) {
        documentCategoryService.updateCategory(updateCategoryRequest);
    }

    @Override
    public List<DocumentCategoryDTO> queryCategoryList(Integer status) {
        return documentCategoryService.queryCategoryList(status);
    }


}