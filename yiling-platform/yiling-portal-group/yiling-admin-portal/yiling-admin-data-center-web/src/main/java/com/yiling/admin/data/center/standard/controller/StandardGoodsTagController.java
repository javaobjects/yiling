package com.yiling.admin.data.center.standard.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.standard.form.CreateStandardTagsForm;
import com.yiling.admin.data.center.standard.form.QueryStandardTagsForm;
import com.yiling.admin.data.center.standard.form.RemoveStandardTagsForm;
import com.yiling.admin.data.center.standard.form.SaveStandardGoodsTagsForm;
import com.yiling.admin.data.center.standard.form.UpdateStandardTagsForm;
import com.yiling.admin.data.center.standard.vo.StandardGoodsTagOptionVO;
import com.yiling.admin.data.center.standard.vo.StandardGoodsTagVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.standard.api.StandardGoodsTagApi;
import com.yiling.goods.standard.dto.StandardGoodsTagDTO;
import com.yiling.goods.standard.dto.request.CreateStandardTagRequest;
import com.yiling.goods.standard.dto.request.QueryStandardGoodsTagsRequest;
import com.yiling.goods.standard.dto.request.SaveStandardGoodsTagsRequest;
import com.yiling.goods.standard.dto.request.UpdateStandardTagRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 StandardGoodsTagController
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@RestController
@RequestMapping("/standard/tag")
@Api(tags = "标准库商品标签管理")
@Slf4j
public class StandardGoodsTagController extends BaseController {
    @DubboReference
    StandardGoodsTagApi standardGoodsTagApi;

    @ApiOperation(value = "获取标准库标签选择项列表")
    @GetMapping("/options")
    public Result<CollectionObject<StandardGoodsTagOptionVO>> options() {
        List<StandardGoodsTagDTO> list = standardGoodsTagApi.listAll(EnableStatusEnum.ENABLED);
        List<StandardGoodsTagOptionVO> voList = PojoUtils.map(list, StandardGoodsTagOptionVO.class);
        return Result.success(new CollectionObject<>(voList));
    }

    @ApiOperation(value = "获取单个标准库标签列表")
    @GetMapping("/listByEid")
    public Result<CollectionObject<StandardGoodsTagOptionVO>> listByEid(@RequestParam @ApiParam(value = "标准库ID", required = true) Long standardId) {
        // 获取所有可用标签列表
        List<StandardGoodsTagDTO> allEnabledTagList = standardGoodsTagApi.listAll(EnableStatusEnum.ENABLED);
        // 标签列表
        List<StandardGoodsTagOptionVO> voList = PojoUtils.map(allEnabledTagList, StandardGoodsTagOptionVO.class);

        // 获取该企业标签列表
        List<StandardGoodsTagDTO> standardGoodsTagList = standardGoodsTagApi.listByStandardId(standardId);
        if (CollUtil.isEmpty(standardGoodsTagList)) {
            return Result.success(new CollectionObject<>(voList));
        }

        List<Long> standardGoodsTagIdList = standardGoodsTagList.stream().map(StandardGoodsTagDTO::getId).collect(Collectors.toList());
        voList.forEach(e -> {
            if (standardGoodsTagIdList.contains(e.getId())) {
                e.setChecked(true);
            }
        });

        return Result.success(new CollectionObject<>(voList));
    }

    @ApiOperation(value = "保存单个标准库标签信息")
    @PostMapping("/saveStandardGoodsTags")
    @Log(title = "保存单个标准库标签信息",businessType = BusinessTypeEnum.INSERT)
    public Result saveTags(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveStandardGoodsTagsForm form) {
        SaveStandardGoodsTagsRequest request = PojoUtils.map(form, SaveStandardGoodsTagsRequest.class);
        request.setType(1);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = standardGoodsTagApi.saveStandardGoodsTags(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "查询标签列表")
    @PostMapping("/queryTagsListPage")
    public Result<Page<StandardGoodsTagVO>> queryTagsListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryStandardTagsForm form) {
        QueryStandardGoodsTagsRequest request = PojoUtils.map(form, QueryStandardGoodsTagsRequest.class);
        Page<StandardGoodsTagDTO> standardGoodsTagDtoPage = standardGoodsTagApi.queryTagsListPage(request);

        Page<StandardGoodsTagVO> page = PojoUtils.map(standardGoodsTagDtoPage,StandardGoodsTagVO.class);

        page.getRecords().forEach(enterpriseTagVO -> {
            List<Long> standardIdList = standardGoodsTagApi.getStandardIdListByTagId(enterpriseTagVO.getId());
            enterpriseTagVO.setStandardNum(standardIdList.size());
        });

        return Result.success(page);
    }

    @ApiOperation(value = "新增标签")
    @PostMapping("/createTags")
    @Log(title = "新增标签",businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> createTags(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid CreateStandardTagsForm form) {
        CreateStandardTagRequest request = PojoUtils.map(form, CreateStandardTagRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        Boolean result = standardGoodsTagApi.createTags(request);
        return Result.success(result);
    }

    @ApiOperation(value = "修改标签")
    @PostMapping("/updateTags")
    @Log(title = "修改标签",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateTags(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateStandardTagsForm form) {
        UpdateStandardTagRequest request = PojoUtils.map(form, UpdateStandardTagRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        Boolean result = standardGoodsTagApi.updateTags(request);
        return Result.success(result);
    }

    @ApiOperation(value = "批量删除标签")
    @PostMapping("/batchDeleteTags")
    @Log(title = "批量删除标签",businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> batchDeleteTags(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid RemoveStandardTagsForm form) {
        Boolean result = standardGoodsTagApi.batchDeleteTags(form.getTagsIdList(),adminInfo.getCurrentUserId());

        return Result.success(result);
    }
}
