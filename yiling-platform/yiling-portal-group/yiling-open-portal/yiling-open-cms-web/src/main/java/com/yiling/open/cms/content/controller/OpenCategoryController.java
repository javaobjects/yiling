package com.yiling.open.cms.content.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.CategoryApi;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.cms.content.dto.request.AddCategoryRequest;
import com.yiling.cms.content.dto.request.QueryCategoryPageRequest;
import com.yiling.cms.content.dto.request.UpdateCategoryRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.cms.content.form.AddCategoryForm;
import com.yiling.open.cms.content.form.QueryCategoryPageForm;
import com.yiling.open.cms.content.form.UpdateCategoryForm;
import com.yiling.open.cms.content.vo.AllCategoryListVO;
import com.yiling.open.cms.content.vo.CategoryListVO;
import com.yiling.open.cms.content.vo.OpenCategoryVO;
import com.yiling.open.cms.document.vo.CategoryVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * Open栏目 前端控制器
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-11
 */
@Api(tags = "栏目")
@RestController
@RequestMapping("/cms/category")
public class OpenCategoryController extends BaseController {

    @DubboReference
    private CategoryApi categoryApi;

    @ApiOperation("栏目列表")
    @GetMapping("queryCategoryList")
    @Log(title = "栏目列表", businessType = BusinessTypeEnum.OTHER)
    public Result<List<OpenCategoryVO>> queryCategoryList(Long lineId, Long moduleId) {
        List<CategoryDTO> categoryDTOS = categoryApi.getCategoryByLineIdAndModuleId(lineId, moduleId);
        List<OpenCategoryVO> result = PojoUtils.map(categoryDTOS, OpenCategoryVO.class);
        if (CollectionUtils.isNotEmpty(result)) {
            OpenCategoryVO categoryVO = new OpenCategoryVO();
            categoryVO.setCategoryName("全部").setId(null);
            result.add(0, categoryVO);
        }
        return Result.success(result);
    }

    @Log(title = "添加栏目", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加栏目")
    @PostMapping("addCategory")
    public Result<Boolean> addCategory(@RequestBody @Valid AddCategoryForm addCategoryForm) {
        AddCategoryRequest request = new AddCategoryRequest();
        PojoUtils.map(addCategoryForm, request);
        categoryApi.addCategory(request);
        return Result.success(true);
    }

    @Log(title = "编辑和禁用启用", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("编辑和禁用启用")
    @PostMapping("updateCategory")
    public Result<Boolean> updateCategory(@RequestBody @Valid UpdateCategoryForm updateCategoryForm) {
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        PojoUtils.map(updateCategoryForm, request);
        categoryApi.updateCategory(request);
        return Result.success(true);
    }

    @ApiOperation("单个栏目")
    @GetMapping("getCategoryById")
    public Result<CategoryVO> getCategoryById(@RequestParam Long id) {
        CategoryDTO category = categoryApi.getCategoryById(id);
        CategoryVO categoryVO = new CategoryVO();
        PojoUtils.map(category, categoryVO);
        return Result.success(categoryVO);
    }

    @ApiOperation("栏目列表")
    @PostMapping("queryCategoryHospitalList")
    public Result<Page<CategoryListVO>> queryCategoryHospitalList(@Valid @RequestBody QueryCategoryPageForm form) {
        QueryCategoryPageRequest request = PojoUtils.map(form, QueryCategoryPageRequest.class);
        Page<CategoryDTO> categoryDTOS = categoryApi.queryCategoryHospitalList(request);
        if (categoryDTOS.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        return Result.success(PojoUtils.map(categoryDTOS, CategoryListVO.class));
    }


    @ApiOperation("获取业务线下的所有栏目")
    @GetMapping("getCategoryByLineId")
    public Result<List<AllCategoryListVO>> getCategoryByLineId(@RequestParam("lineId") Long lineId) {
        List<CategoryDTO> categoryDTOS = categoryApi.queryAppCategoryList(lineId);
        Map<Long, List<CategoryDTO>> listMap = categoryDTOS.stream().collect(Collectors.groupingBy(CategoryDTO::getModuleId));
        List<AllCategoryListVO> list = new ArrayList<>();
        for (Long aLong : listMap.keySet()) {
            AllCategoryListVO vo = new AllCategoryListVO();
            vo.setModuleId(aLong);
            vo.setCategoryList(PojoUtils.map(listMap.get(aLong), CategoryListVO.class));
            list.add(vo);
        }
        return Result.success(list);
    }
}
