package com.yiling.admin.b2b.lotteryactivity.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.lotteryactivity.form.AddCustomerPageForm;
import com.yiling.admin.b2b.lotteryactivity.form.AddOrDeleteMemberForm;
import com.yiling.admin.b2b.lotteryactivity.form.DeleteCustomerPageForm;
import com.yiling.admin.b2b.lotteryactivity.form.QueryCustomerPageForm;
import com.yiling.admin.b2b.lotteryactivity.form.QueryHadAddCustomerPageForm;
import com.yiling.admin.b2b.lotteryactivity.form.QueryMemberPageForm;
import com.yiling.admin.b2b.lotteryactivity.vo.EnterpriseSimpleVO;
import com.yiling.admin.b2b.lotteryactivity.vo.MemberSimpleVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityGiveScopeApi;
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.bo.MemberSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.AddOrDeleteMemberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.DeleteCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryMemberPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽奖活动客户范围相关 Controller
 *
 * @author: lun.yu
 * @date: 2022-09-27
 */
@Slf4j
@RestController
@RequestMapping("/lotteryCustomerScope")
@Api(tags = "抽奖活动客户范围相关接口")
public class LotteryCustomerScopeController extends BaseController {

    @DubboReference
    LotteryActivityGiveScopeApi lotteryActivityGiveScopeApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    MemberApi memberApi;

    @ApiOperation(value = "选择客户分页列表")
    @PostMapping("/queryCustomerPage")
    public Result<Page<EnterpriseSimpleVO>> queryCustomerPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCustomerPageForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        request.setStatus(EnableStatusEnum.ENABLED.getCode());
        request.setNotInTypeList(ListUtil.toList(EnterpriseTypeEnum.INDUSTRY.getCode(), EnterpriseTypeEnum.BUSINESS.getCode()));
        Page<EnterpriseDTO> dtoPage = enterpriseApi.pageList(request);

        return Result.success(PojoUtils.map(dtoPage, EnterpriseSimpleVO.class));
    }

    @ApiOperation(value = "已添加客户分页列表")
    @PostMapping("/queryHadAddCustomerPage")
    public Result<Page<EnterpriseSimpleVO>> queryHadAddCustomerPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryHadAddCustomerPageForm form) {
        QueryCustomerPageRequest request = PojoUtils.map(form, QueryCustomerPageRequest.class);
        Page<EnterpriseSimpleBO> simpleBOPage = lotteryActivityGiveScopeApi.queryHadAddCustomerPage(request);

        return Result.success(PojoUtils.map(simpleBOPage, EnterpriseSimpleVO.class));
    }

    @ApiOperation(value = "选择方案会员分页列表")
    @PostMapping("/queryMemberPage")
    public Result<Page<MemberSimpleVO>> queryMemberPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryPageListForm form) {
        QueryMemberRequest request = PojoUtils.map(form, QueryMemberRequest.class);
        Page<MemberDTO> memberDTOPage = memberApi.queryListPage(request);

        return Result.success(PojoUtils.map(memberDTOPage, MemberSimpleVO.class));
    }

    @ApiOperation(value = "已添加会员分页列表")
    @PostMapping("/queryHadAddMemberPage")
    public Result<Page<MemberSimpleVO>> queryHadAddMemberPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryMemberPageForm form) {
        QueryMemberPageRequest request = PojoUtils.map(form, QueryMemberPageRequest.class);
        Page<MemberSimpleBO> simpleBOPage = lotteryActivityGiveScopeApi.queryHadAddMemberPage(request);

        return Result.success(PojoUtils.map(simpleBOPage, MemberSimpleVO.class));
    }

    @ApiOperation(value = "选择推广方分页列表")
    @PostMapping("/queryPromoterPage")
    public Result<Page<EnterpriseSimpleVO>> queryPromoterPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCustomerPageForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        request.setStatus(EnableStatusEnum.ENABLED.getCode());
        request.setInTypeList(ListUtil.toList(EnterpriseTypeEnum.BUSINESS.getCode()));
        Page<EnterpriseDTO> dtoPage = enterpriseApi.pageList(request);

        return Result.success(PojoUtils.map(dtoPage, EnterpriseSimpleVO.class));
    }

    @ApiOperation(value = "已添加推广方分页列表")
    @PostMapping("/queryHadAddPromoterPage")
    public Result<Page<MemberSimpleVO>> queryHadAddPromoterPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryHadAddCustomerPageForm form) {
        QueryCustomerPageRequest request = PojoUtils.map(form, QueryCustomerPageRequest.class);
        Page<EnterpriseSimpleBO> simpleBOPage = lotteryActivityGiveScopeApi.queryHadAddPromoterPage(request);

        return Result.success(PojoUtils.map(simpleBOPage, MemberSimpleVO.class));
    }

    @ApiOperation(value = "添加指定客户")
    @PostMapping("/addCustomer")
    public Result<Void> addCustomer(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AddCustomerPageForm form) {
        AddCustomerPageRequest request = PojoUtils.map(form, AddCustomerPageRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityGiveScopeApi.addCustomer(request);
        return Result.success();
    }

    @ApiOperation(value = "删除指定客户")
    @PostMapping("/deleteCustomer")
    public Result<Void> deleteCustomer(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteCustomerPageForm form) {
        DeleteCustomerPageRequest request = PojoUtils.map(form, DeleteCustomerPageRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityGiveScopeApi.deleteCustomer(request);
        return Result.success();
    }

    @ApiOperation(value = "添加会员")
    @PostMapping("/addMember")
    public Result<Void> addMember(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AddOrDeleteMemberForm form) {
        AddOrDeleteMemberRequest request = PojoUtils.map(form, AddOrDeleteMemberRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityGiveScopeApi.addMember(request);
        return Result.success();
    }

    @ApiOperation(value = "删除会员")
    @PostMapping("/deleteMember")
    public Result<Void> deleteMember(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AddOrDeleteMemberForm form) {
        AddOrDeleteMemberRequest request = PojoUtils.map(form, AddOrDeleteMemberRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityGiveScopeApi.deleteMember(request);
        return Result.success();
    }

    @ApiOperation(value = "添加推广方")
    @PostMapping("/addPromoter")
    public Result<Void> addPromoter(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AddCustomerPageForm form) {
        AddCustomerPageRequest request = PojoUtils.map(form, AddCustomerPageRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityGiveScopeApi.addPromoter(request);
        return Result.success();
    }

    @ApiOperation(value = "删除推广方")
    @PostMapping("/deletePromoter")
    public Result<Void> deletePromoter(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteCustomerPageForm form) {
        DeleteCustomerPageRequest request = PojoUtils.map(form, DeleteCustomerPageRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityGiveScopeApi.deletePromoter(request);
        return Result.success();
    }

}
