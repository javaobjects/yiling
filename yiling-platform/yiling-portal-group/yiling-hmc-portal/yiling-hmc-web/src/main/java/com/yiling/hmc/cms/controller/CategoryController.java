package com.yiling.hmc.cms.controller;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.yiling.cms.content.api.CategoryApi;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.hmc.cms.vo.CategoryVO;

import cn.hutool.core.collection.CollUtil;
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
    private CategoryApi categoryApi;

    @ApiOperation("栏目列表")
    @GetMapping("queryCategoryList")
    public Result<CollectionObject<List<CategoryVO>>> queryCategoryList(){
        List<CategoryDTO> categoryDTOS = categoryApi.getCategoryByLineIdAndModuleId(1L, 1L);
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setCategoryName("全部").setId(null);

        if(CollUtil.isEmpty(categoryDTOS)){
            List<CategoryVO> first = Lists.newArrayList();
            first.add(categoryVO);
            return Result.success( new CollectionObject(first));
        }
        List<CategoryVO> categoryVOS = PojoUtils.map(categoryDTOS,CategoryVO.class);
        CollectionObject<List<CategoryVO>> collectionObject = new CollectionObject(categoryVOS);
        categoryVOS.add(0,categoryVO);
        return Result.success(collectionObject);
    }

}
