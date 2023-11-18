package com.yiling.admin.cms.document.controller;


import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.cms.document.form.AddCategoryForm;
import com.yiling.admin.cms.document.form.UpdateCategoryForm;
import com.yiling.admin.cms.document.vo.DocumentCategoryVO;
import com.yiling.cms.document.api.DocumentCategoryApi;
import com.yiling.cms.document.dto.DocumentCategoryDTO;
import com.yiling.cms.document.dto.request.AddDocumentCategoryRequest;
import com.yiling.cms.document.dto.request.UpdateDocumentCategoryRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 文献栏目 前端控制器
 * </p>
 *
 * @author gxl
 * @date 2022-06-02
 */
@Api(tags = "文献栏目")
@RestController
@RequestMapping("/document/category")
public class DocumentCategoryController extends BaseController {
    @DubboReference
    private DocumentCategoryApi documentCategoryApi;

    @Log(title = "添加文献栏目",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加栏目")
    @PostMapping("addCategory")
    public Result<Boolean> addCategory(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddCategoryForm addCategoryForm){
        AddDocumentCategoryRequest request = new AddDocumentCategoryRequest();
        PojoUtils.map(addCategoryForm,request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        documentCategoryApi.addCategory(request);
        return Result.success(true);
    }
    @Log(title = "编辑和禁用启用",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("编辑和禁用启用")
    @PostMapping("updateCategory")
    public  Result<Boolean> updateCategory(@CurrentUser CurrentAdminInfo currentAdminInfo,@RequestBody @Valid UpdateCategoryForm updateCategoryForm){
        UpdateDocumentCategoryRequest request = new UpdateDocumentCategoryRequest();
        PojoUtils.map(updateCategoryForm,request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        documentCategoryApi.updateCategory(request);
        return Result.success(true);
    }

    @ApiOperation("栏目列表")
    @GetMapping("queryCategoryList")
    public Result<CollectionObject<List<DocumentCategoryVO>>> queryCategoryList(@RequestParam(required = false) Integer status){
        List<DocumentCategoryDTO> categoryDTOS = documentCategoryApi.queryCategoryList(status);
        List<DocumentCategoryVO> documentCategoryVOS = PojoUtils.map(categoryDTOS, DocumentCategoryVO.class);
        CollectionObject<List<DocumentCategoryVO>> collectionObject = new CollectionObject(documentCategoryVOS);
        return Result.success(collectionObject);
    }
}
