package com.yiling.admin.data.center.standard.controller;


import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.data.center.standard.form.SaveCategoryInfoForm;
import com.yiling.admin.data.center.standard.form.UpdateCategoryNameForm;
import com.yiling.admin.data.center.standard.vo.StandardGoodsCategoryInfoAllVO;
import com.yiling.admin.data.center.standard.vo.StandardGoodsCategoryVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.standard.api.StandardGoodsCategoryApi;
import com.yiling.goods.standard.dto.StandardGoodsCategoryDTO;
import com.yiling.goods.standard.dto.StandardGoodsCategoryInfoAllDTO;
import com.yiling.goods.standard.dto.request.SaveCategoryInfoRequest;
import com.yiling.goods.standard.dto.request.UpdateCategoryNameRequest;
import com.yiling.goods.standard.enums.StandardResultCode;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 质量监管 Controller
 *
 * @author: wei.wang
 * @date: 2021/5/12
 */
@RestController
@RequestMapping("/standard/goods/category")
@Api(tags = "药品分类管理")
@Slf4j
public class StandardGoodsCategoryController extends BaseController {

    @DubboReference
    StandardGoodsCategoryApi standardGoodsCateApi;

    /**
     * 获取一级分类
     *
     * @return
     */
    @ApiOperation(value = "获取一级分类")
    @PostMapping("/first")
    public Result<CollectionObject<List<StandardGoodsCategoryVO>>> getFirstCate() {
        List<StandardGoodsCategoryDTO> firstCateInfo = standardGoodsCateApi.getFirstCateInfo();
        List<StandardGoodsCategoryVO> list = PojoUtils.map(firstCateInfo, StandardGoodsCategoryVO.class);
        CollectionObject<List<StandardGoodsCategoryVO>> result = new CollectionObject(list);
        return Result.success(result);
    }


    /**
     * 获取所有二级分类
     *
     * @return
     */
    @ApiOperation(value = "获取二级分类")
    @PostMapping("/second/all")
    public Result<CollectionObject<List<StandardGoodsCategoryVO>>> getSecondCate() {
        List<StandardGoodsCategoryDTO> firstCateInfo = standardGoodsCateApi.getSecondCateInfo();
        List<StandardGoodsCategoryVO> voList = PojoUtils.map(firstCateInfo, StandardGoodsCategoryVO.class);
        CollectionObject<List<StandardGoodsCategoryVO>> result = new CollectionObject(voList);
        return Result.success(result);
    }

    /**
     * 获取所有二级分类
     *
     * @return
     */
    @ApiOperation(value = "根据父分类获取子分类")
    @GetMapping("/children")
    public Result<CollectionObject<List<StandardGoodsCategoryVO>>> getchildrenCate(@RequestParam(value = "parentId", required = true) Long parentId) {
        List<StandardGoodsCategoryDTO> firstCateInfo = standardGoodsCateApi.getSecondCateOne(parentId);
        List<StandardGoodsCategoryVO> voList = PojoUtils.map(firstCateInfo, StandardGoodsCategoryVO.class);
        CollectionObject<List<StandardGoodsCategoryVO>> result = new CollectionObject(voList);
        return Result.success(result);
    }


    /**
     * 获取药品分类列表
     *
     * @return
     */
    @ApiOperation(value = "获取药品分类列表")
    @PostMapping("/getAll")
    public Result<CollectionObject<List<StandardGoodsCategoryInfoAllVO>>> getAllCateInfo() {
        List<StandardGoodsCategoryInfoAllDTO> allCateInfo = standardGoodsCateApi.getAllCateInfo();
        List<StandardGoodsCategoryInfoAllVO> voList = PojoUtils.map(allCateInfo, StandardGoodsCategoryInfoAllVO.class);
        CollectionObject<List<StandardGoodsCategoryInfoAllVO>> result = new CollectionObject(voList);
        return Result.success(result);

    }

    /**
     * 获取药品分类列表
     *
     * @return
     */
    @ApiOperation(value = "编辑名称")
    @PostMapping("/update/name")
    public Result<BoolObject> updateCateName(@CurrentUser CurrentAdminInfo staffInfo,
                                             @Valid @RequestBody UpdateCategoryNameForm form) {
        UpdateCategoryNameRequest request = PojoUtils.map(form, UpdateCategoryNameRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        StandardGoodsCategoryDTO one = standardGoodsCateApi.getOneById(request.getId());
        if(one == null){
            throw new BusinessException(StandardResultCode.STANDARD_DATA_CATEGORY_UPDATE);

        }
        StandardGoodsCategoryDTO categoryName;
        if(0 == one.getParentId()){
             categoryName = standardGoodsCateApi.getCategoryByName(1, request.getName());
        }else{
             categoryName = standardGoodsCateApi.getCategoryByName(2, request.getName());
        }
        if(categoryName != null){
            throw new BusinessException(StandardResultCode.STANDARD_CATEGORY_NAME_REPEAT);
        }

        Boolean result = standardGoodsCateApi.updateCateName(request);
        BoolObject vo = new BoolObject(result);
        return Result.success(vo);
    }

    /**
     * 获取药品分类列表
     *
     * @return
     */
    @ApiOperation(value = "归类")
    @GetMapping("/update/type")
    public Result<BoolObject> updateCateName(@RequestParam(value = "parentId", required = true)Long parentId,
                                             @RequestParam(value = "id", required = true)Long id,
                                             @CurrentUser CurrentAdminInfo staffInfo) {

        StandardGoodsCategoryDTO one = standardGoodsCateApi.getOneById(id);
        if(one == null){
            throw new BusinessException(StandardResultCode.STANDARD_DATA_CATEGORY_UPDATE);
        }

        StandardGoodsCategoryDTO categoryName;
        if(0 == parentId){
            categoryName = standardGoodsCateApi.getCategoryByName(1, one.getName());
        }else{
            categoryName = standardGoodsCateApi.getCategoryByName(2, one.getName());
            if(categoryName != null && categoryName.getId().equals(id)){
                categoryName = null;
            }
        }
        if(categoryName != null){
            throw new BusinessException(StandardResultCode.STANDARD_CATEGORY_NAME_REPEAT);
        }

        Boolean result = standardGoodsCateApi.updateCateParentId(parentId,id,staffInfo.getCurrentUserId());
        BoolObject vo = new BoolObject(result);
        return Result.success(vo);
    }


    /**
     * 新增分类
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "新增分类")
    @PostMapping("/save")
    public Result<BoolObject> saveCateInfo(@CurrentUser CurrentAdminInfo staffInfo,
                                           @Valid @RequestBody SaveCategoryInfoForm form) {
        SaveCategoryInfoRequest request = PojoUtils.map(form, SaveCategoryInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        StandardGoodsCategoryDTO categoryName;
        if(0 == request.getParentId()){
            categoryName = standardGoodsCateApi.getCategoryByName(1, request.getName());
        }else{
            categoryName = standardGoodsCateApi.getCategoryByName(2, request.getName());
        }
        if(categoryName != null){
            throw new BusinessException(StandardResultCode.STANDARD_CATEGORY_NAME_REPEAT);

        }
        Boolean result = standardGoodsCateApi.saveCateInfo(request);
        BoolObject vo = new BoolObject(result);
        return Result.success(vo);
    }


}



