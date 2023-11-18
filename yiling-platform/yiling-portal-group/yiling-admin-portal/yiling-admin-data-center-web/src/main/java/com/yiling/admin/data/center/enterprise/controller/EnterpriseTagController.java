package com.yiling.admin.data.center.enterprise.controller;

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
import com.yiling.admin.data.center.enterprise.form.CreateTagsForm;
import com.yiling.admin.data.center.enterprise.form.QueryTagsForm;
import com.yiling.admin.data.center.enterprise.form.RemoveTagsForm;
import com.yiling.admin.data.center.enterprise.form.SaveEnterpriseTagsForm;
import com.yiling.admin.data.center.enterprise.form.UpdateTagsForm;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseTagOptionVO;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseTagVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.CreateTagsRequest;
import com.yiling.user.enterprise.dto.request.QueryTagsRequest;
import com.yiling.user.enterprise.dto.request.SaveEnterpriseTagsRequest;
import com.yiling.user.enterprise.dto.request.UpdateTagsRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业标签模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/10/14
 */
@RestController
@RequestMapping("/enterprise/tag")
@Api(tags = "企业标签模块接口")
@Slf4j
public class EnterpriseTagController extends BaseController {

    @DubboReference
    EnterpriseTagApi enterpriseTagApi;

    @ApiOperation(value = "获取企业标签选择项列表")
    @GetMapping("/options")
    public Result<CollectionObject<EnterpriseTagOptionVO>> options() {
        List<EnterpriseTagDTO> list = enterpriseTagApi.listAll(EnableStatusEnum.ENABLED);
        List<EnterpriseTagOptionVO> voList = PojoUtils.map(list, EnterpriseTagOptionVO.class);
        return Result.success(new CollectionObject<>(voList));
    }

    @ApiOperation(value = "获取单个企业标签列表")
    @GetMapping("/listByEid")
    public Result<CollectionObject<EnterpriseTagOptionVO>> listByEid(@RequestParam @ApiParam(value = "企业ID", required = true) Long eid) {
        // 获取所有可用标签列表
        List<EnterpriseTagDTO> allEnabledTagList = enterpriseTagApi.listAll(EnableStatusEnum.ENABLED);
        // 标签列表
        List<EnterpriseTagOptionVO> voList = PojoUtils.map(allEnabledTagList, EnterpriseTagOptionVO.class);

        // 获取该企业标签列表
        List<EnterpriseTagDTO> enterpriseTagList = enterpriseTagApi.listByEid(eid);
        if (CollUtil.isEmpty(enterpriseTagList)) {
            return Result.success(new CollectionObject<>(voList));
        }

        List<Long> enterpriseTagIdList = enterpriseTagList.stream().map(EnterpriseTagDTO::getId).collect(Collectors.toList());
        voList.forEach(e -> {
            if (enterpriseTagIdList.contains(e.getId())) {
                e.setChecked(true);
            }
        });

        return Result.success(new CollectionObject<>(voList));
    }

    @ApiOperation(value = "保存单个企业标签信息")
    @PostMapping("/saveEnterpriseTags")
    @Log(title = "保存单个企业标签信息",businessType = BusinessTypeEnum.INSERT)
    public Result saveTags(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveEnterpriseTagsForm form) {
        SaveEnterpriseTagsRequest request = PojoUtils.map(form, SaveEnterpriseTagsRequest.class);
        request.setType(1);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = enterpriseTagApi.saveEnterpriseTags(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "查询标签列表")
    @PostMapping("/queryTagsListPage")
    public Result<Page<EnterpriseTagVO>> queryTagsListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryTagsForm form) {
        QueryTagsRequest request = PojoUtils.map(form,QueryTagsRequest.class);
        Page<EnterpriseTagDTO> enterpriseTagDtoPage = enterpriseTagApi.queryTagsListPage(request);

        Page<EnterpriseTagVO> page = PojoUtils.map(enterpriseTagDtoPage,EnterpriseTagVO.class);

        page.getRecords().forEach(enterpriseTagVO -> {
            List<Long> eidList = enterpriseTagApi.getEidListByTagId(enterpriseTagVO.getId());
            enterpriseTagVO.setEnterpriseNum(eidList.size());
        });

        return Result.success(page);
    }

    @ApiOperation(value = "新增标签")
    @PostMapping("/createTags")
    @Log(title = "新增标签",businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> createTags(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid CreateTagsForm form) {
        CreateTagsRequest request = PojoUtils.map(form,CreateTagsRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        Boolean result = enterpriseTagApi.createTags(request);
        return Result.success(result);
    }

    @ApiOperation(value = "修改标签")
    @PostMapping("/updateTags")
    @Log(title = "修改标签",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateTags(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateTagsForm form) {
        UpdateTagsRequest request = PojoUtils.map(form,UpdateTagsRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        Boolean result = enterpriseTagApi.updateTags(request);
        return Result.success(result);
    }

    @ApiOperation(value = "批量删除标签")
    @PostMapping("/batchDeleteTags")
    @Log(title = "批量删除标签",businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> batchDeleteTags(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid RemoveTagsForm form) {
        Boolean result = enterpriseTagApi.batchDeleteTags(form.getTagsIdList(),adminInfo.getCurrentUserId());

        return Result.success(result);
    }

}
