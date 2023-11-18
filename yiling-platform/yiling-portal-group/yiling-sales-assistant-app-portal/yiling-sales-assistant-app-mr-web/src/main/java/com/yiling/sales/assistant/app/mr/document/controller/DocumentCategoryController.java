package com.yiling.sales.assistant.app.mr.document.controller;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.cms.document.api.DocumentCategoryApi;
import com.yiling.cms.document.dto.DocumentCategoryDTO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.sales.assistant.app.mr.document.vo.CategoryVO;

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

    @ApiOperation("栏目列表")
    @GetMapping("queryCategoryList")
    public Result<CollectionObject<CategoryVO>> queryCategoryList(){
        List<DocumentCategoryDTO> categoryDTOS = documentCategoryApi.queryCategoryList(1);
        List<CategoryVO> categoryVOS = PojoUtils.map(categoryDTOS,CategoryVO.class);
        CollectionObject<CategoryVO> collectionObject = new CollectionObject(categoryVOS);
        return Result.success(collectionObject);
    }
}
