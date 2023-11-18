package com.yiling.sales.assistant.app.content.controller;


import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.cms.content.api.CategoryApi;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.app.content.vo.OpenCategoryVO;
import com.yiling.user.system.bo.CurrentStaffInfo;

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
        List<CategoryDTO> categoryDTOS = categoryApi.getCategorySaAndB2B(lineId, moduleId);
        List<OpenCategoryVO> result = PojoUtils.map(categoryDTOS, OpenCategoryVO.class);
        if (CollectionUtils.isNotEmpty(result)) {
            OpenCategoryVO categoryVO = new OpenCategoryVO();
            categoryVO.setCategoryName("全部").setId(null);
            result.add(0, categoryVO);
        }
        return Result.success(result);
    }
}
