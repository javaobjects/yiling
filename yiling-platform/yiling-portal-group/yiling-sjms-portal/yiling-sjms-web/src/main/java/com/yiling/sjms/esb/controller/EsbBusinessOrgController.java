package com.yiling.sjms.esb.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.sjms.esb.form.DeleteBusinessOrganizationForm;
import com.yiling.sjms.esb.form.QueryEsbBzOrgForm;
import com.yiling.sjms.esb.form.SaveBusinessOrganizationForm;
import com.yiling.sjms.esb.form.UpdateTargetStatusForm;
import com.yiling.sjms.esb.vo.EsbBusinessOrgTreeVO;
import com.yiling.sjms.esb.vo.EsbOrgInfoTreeVO;
import com.yiling.sjms.esb.vo.SimpleEsbBzOrgVO;
import com.yiling.user.esb.api.EsbBusinessOrganizationApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.bo.EsbBusinessOrgTreeBO;
import com.yiling.user.esb.bo.EsbOrgInfoTreeBO;
import com.yiling.user.esb.bo.SimpleEsbBzOrgBO;
import com.yiling.user.esb.dto.request.DeleteBusinessOrganizationRequest;
import com.yiling.user.esb.dto.request.SaveBusinessOrganizationRequest;
import com.yiling.user.esb.dto.request.UpdateTargetStatusRequest;
import com.yiling.user.esb.enums.EsbBusinessOrganizationTagTypeEnum;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * ESB业务架构接口
 *
 * @author: lun.yu
 * @date: 2023-04-13
 */
@Slf4j
@RestController
@RequestMapping("/esb/bzorg")
@Api(tags = "ESB业务架构接口")
public class EsbBusinessOrgController extends BaseController {

    @DubboReference(timeout = 1000 * 10)
    EsbBusinessOrganizationApi esbBusinessOrganizationApi;
    @DubboReference
    EsbOrganizationApi esbOrganizationApi;

    @ApiOperation(value = "获取部门架构树")
    @GetMapping("/queryOrgTree")
    public Result<CollectionObject<EsbOrgInfoTreeVO>> queryOrgTree(@CurrentUser CurrentSjmsUserInfo userInfo) {
        List<EsbOrgInfoTreeBO> orgInfoTreeBOList = esbOrganizationApi.listTree();
        List<EsbOrgInfoTreeVO> esbOrgTreeVOList = PojoUtils.map(orgInfoTreeBOList, EsbOrgInfoTreeVO.class);
        return Result.success(new CollectionObject<>(esbOrgTreeVOList));
    }

    @ApiOperation(value = "生成业务架构")
    @PostMapping("/saveBusinessOrg")
    @Log(title = "生成业务架构", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> saveBusinessOrg(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveBusinessOrganizationForm form) {
        SaveBusinessOrganizationRequest request = PojoUtils.map(form, SaveBusinessOrganizationRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        boolean result = esbBusinessOrganizationApi.saveBusinessOrg(request);
        return Result.success(result);
    }

    @ApiOperation(value = "删除打标")
    @PostMapping("/deleteTag")
    @Log(title = "删除打标", businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> deleteTag(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid DeleteBusinessOrganizationForm form) {
        DeleteBusinessOrganizationRequest request = PojoUtils.map(form, DeleteBusinessOrganizationRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        boolean result = esbBusinessOrganizationApi.deleteTag(request);
        return Result.success(result);
    }

    @ApiOperation(value = "设置是否可以指标上传")
    @PostMapping("/setTargetStatus")
    @Log(title = "设置是否可以指标上传", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> setTargetStatus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateTargetStatusForm form) {
        UpdateTargetStatusRequest request = PojoUtils.map(form, UpdateTargetStatusRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        boolean result = esbBusinessOrganizationApi.setTargetStatus(request);
        return Result.success(result);
    }

    @ApiOperation(value = "获取业务架构树")
    @GetMapping("/queryBzOrgTree")
    public Result<CollectionObject<EsbBusinessOrgTreeVO>> queryBzOrgTree(@CurrentUser CurrentSjmsUserInfo userInfo) {
        List<EsbBusinessOrgTreeBO> esbBusinessOrgTreeBOList = esbBusinessOrganizationApi.queryBzOrgTree();
        List<EsbBusinessOrgTreeVO> esbBusinessOrgTreeVOList = PojoUtils.map(esbBusinessOrgTreeBOList, EsbBusinessOrgTreeVO.class);
        return Result.success(new CollectionObject<>(esbBusinessOrgTreeVOList));
    }


    @ApiOperation(value = "获取ESB业务架构指定层级的所有数据")
    @GetMapping("/getBzOrgListByTagType")
    public Result<CollectionObject<SimpleEsbBzOrgVO>> getBzOrgListByTagType(@CurrentUser CurrentSjmsUserInfo userInfo, @ApiParam("获取数据层级：1-事业部 2-业务省区 3-区办") @RequestParam("tagType") Integer tagType) {
        List<SimpleEsbBzOrgBO> simpleEsbBzOrgBOList = esbBusinessOrganizationApi.getBzOrgListByTagType(EsbBusinessOrganizationTagTypeEnum.getByCode(tagType));
        List<SimpleEsbBzOrgVO> simpleEsbBzOrgVOList = PojoUtils.map(simpleEsbBzOrgBOList, SimpleEsbBzOrgVO.class);
        return Result.success(new CollectionObject<>(simpleEsbBzOrgVOList));
    }

    @ApiOperation(value = "根据部门ID获取事业部下的指定层级业务架构")
    @PostMapping("/getBzOrgListByOrgId")
    public Result<CollectionObject<SimpleEsbBzOrgVO>> getBzOrgListByOrgId(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryEsbBzOrgForm form) {
        List<SimpleEsbBzOrgBO> simpleEsbBzOrgBOList = esbBusinessOrganizationApi.getBzOrgListByOrgId(form.getOrgId(), EsbBusinessOrganizationTagTypeEnum.getByCode(form.getTagType()));
        List<SimpleEsbBzOrgVO> simpleEsbBzOrgVOList = PojoUtils.map(simpleEsbBzOrgBOList, SimpleEsbBzOrgVO.class);
        return Result.success(new CollectionObject<>(simpleEsbBzOrgVOList));
    }

}
