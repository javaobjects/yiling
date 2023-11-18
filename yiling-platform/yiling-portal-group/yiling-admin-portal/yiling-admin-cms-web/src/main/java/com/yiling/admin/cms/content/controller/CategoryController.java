package com.yiling.admin.cms.content.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import com.beust.jcommander.internal.Lists;
import com.yiling.admin.cms.content.config.CmsBusinessLineConfig;
import com.yiling.admin.cms.content.config.ModulesConfig;
import com.yiling.admin.cms.content.vo.*;
import com.yiling.cms.content.dto.CategoryDisplayLineDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.cms.content.form.AddCategoryForm;
import com.yiling.admin.cms.content.form.UpdateCategoryForm;
import com.yiling.cms.content.api.CategoryApi;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.cms.content.dto.request.AddCategoryRequest;
import com.yiling.cms.content.dto.request.UpdateCategoryRequest;
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
 * 栏目 前端控制器
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Api(tags = "栏目")
@RestController
@RequestMapping("/cms/category")
public class CategoryController extends BaseController {

    @DubboReference
    CategoryApi categoryApi;

    @Autowired
    CmsBusinessLineConfig businessLineConfig;

    @Log(title = "添加栏目", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加栏目")
    @PostMapping("addCategory")
    public Result<Boolean> addCategory(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddCategoryForm addCategoryForm) {
        AddCategoryRequest request = new AddCategoryRequest();
        PojoUtils.map(addCategoryForm, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        categoryApi.addCategory(request);
        return Result.success(true);
    }

    @Log(title = "编辑和禁用启用", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("编辑和禁用启用")
    @PostMapping("updateCategory")
    public Result<Boolean> updateCategory(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid UpdateCategoryForm updateCategoryForm) {
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        PojoUtils.map(updateCategoryForm, request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        categoryApi.updateCategory(request);
        return Result.success(true);
    }

    @ApiOperation("栏目列表")
    @GetMapping("queryCategoryList")
    public Result<CollectionObject<List<CategoryVO>>> queryCategoryList(@RequestParam(required = false) Integer status) {
        List<CategoryDTO> categoryDTOS = categoryApi.queryCategoryList(status);
        List<CategoryVO> categoryVOS = PojoUtils.map(categoryDTOS, CategoryVO.class);
        CollectionObject<List<CategoryVO>> collectionObject = new CollectionObject(categoryVOS);
        return Result.success(collectionObject);
    }

    @ApiOperation("单个栏目")
    @GetMapping("getCategoryById")
    public Result<CategoryVO> getCategoryById(@RequestParam Long id) {
        CategoryDTO category = categoryApi.getCategoryById(id);
        CategoryVO categoryVO = new CategoryVO();
        PojoUtils.map(category, categoryVO);
        return Result.success(categoryVO);
    }

    @ApiOperation("获取指定业务线下的板块栏目")
    @GetMapping("getModulesByLineId")
    public Result<List<ModuleVO>> getModulesByLineId(@RequestParam Long lineId) {
        List<CategoryDTO> categoryDTOS = categoryApi.queryAppCategoryList(lineId);
        if (CollUtil.isEmpty(categoryDTOS)) {
            return Result.success(Lists.newArrayList());
        }
        Map<Long, List<CategoryDTO>> moduleCategoryMap = categoryDTOS.stream().collect(Collectors.groupingBy(CategoryDTO::getModuleId));
        List<ModuleVO> moduleList = Lists.newArrayList();
        moduleCategoryMap.forEach((k, v) -> {
            ModuleVO moduleVO = new ModuleVO();
            moduleVO.setModuleId(k);
            List<ModuleCategoryVO> moduleCategoryList = Lists.newArrayList();
            v.forEach(category -> {
                ModuleCategoryVO categoryVO = new ModuleCategoryVO();
                categoryVO.setCategoryId(category.getId());
                categoryVO.setCategoryName(category.getCategoryName());
                moduleCategoryList.add(categoryVO);
            });
            moduleVO.setModuleCategoryList(moduleCategoryList);
            moduleList.add(moduleVO);
        });

        return Result.success(moduleList);

        // List<CategoryDisplayLineDTO> categoryList = categoryApi.getCategoryByLineId(lineId);
        // if (CollUtil.isEmpty(categoryList)) {
        //     return Result.success(Lists.newArrayList());
        // }
        // Map<Long, CategoryDisplayLineDTO> moduleMap = categoryList.stream().collect(Collectors.toMap(CategoryDisplayLineDTO::getModuleId, o -> o, (k1, k2) -> k1));
        // Map<Long, List<CategoryDisplayLineDTO>> moduleCategoryMap = categoryList.stream().collect(Collectors.groupingBy(CategoryDisplayLineDTO::getModuleId));
        // List<ModuleVO> moduleList = Lists.newArrayList();
        // moduleCategoryMap.forEach((k, v) -> {
        //
        //     ModuleVO moduleVO = new ModuleVO();
        //     moduleVO.setModuleId(k);
        //     moduleVO.setModuleName(moduleMap.get(k).getModuleName());
        //
        //     List<ModuleCategoryVO> moduleCategoryList = Lists.newArrayList();
        //     v.forEach(category -> {
        //         ModuleCategoryVO categoryVO = new ModuleCategoryVO();
        //         categoryVO.setCategoryId(category.getCategoryId());
        //         categoryVO.setCategoryName(category.getCategoryName());
        //         moduleCategoryList.add(categoryVO);
        //     });
        //     moduleVO.setModuleCategoryList(moduleCategoryList);
        //     moduleList.add(moduleVO);
        // });
        //
        // return Result.success(moduleList);
    }

}
