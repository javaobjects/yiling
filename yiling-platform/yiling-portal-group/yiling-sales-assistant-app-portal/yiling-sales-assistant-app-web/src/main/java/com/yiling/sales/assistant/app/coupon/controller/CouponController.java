package com.yiling.sales.assistant.app.coupon.controller;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanAndOwnDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.sales.assistant.app.coupon.form.GetCouponsForm;
import com.yiling.sales.assistant.app.coupon.form.QueryShopCouponsListForm;
import com.yiling.sales.assistant.app.coupon.vo.CouponActivityCanAndOwnVO;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 优惠券接口
 *
 * @author: houjie.sun
 * @date: 2022/4/6
 */
@RestController
@RequestMapping("/coupon")
@Api(tags = "优惠券接口")
@Slf4j
public class CouponController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    CouponActivityApi couponActivityApi;

    @UserAccessAuthentication
    @ApiOperation(value = "商品详情可领取、已领取优惠券活动列表列表", httpMethod = "POST")
    @PostMapping(path = "/getCouponActivityCanAndOwnList")
    public Result<CouponActivityCanAndOwnVO> getCouponActivityCanAndOwnList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryShopCouponsListForm form) {
        QueryCouponActivityCanReceiveRequest request = new QueryCouponActivityCanReceiveRequest();
        request.setCurrentEid(form.getCustomerEid());
        request.setEid(form.getShopEid());
        request.setPlatformType(CouponPlatformTypeEnum.SALES_ASSIST.getCode());
        CouponActivityCanAndOwnDTO canAndOwnList = couponActivityApi.getCanAndOwnListByEid(request);
        CouponActivityCanAndOwnVO vo = PojoUtils.map(canAndOwnList, CouponActivityCanAndOwnVO.class);
        return Result.success(vo);
    }

    @Log(title = "领取优惠券", businessType = BusinessTypeEnum.INSERT)
    @UserAccessAuthentication
    @ApiOperation(value = "立即领取", httpMethod = "POST")
    @PostMapping(path = "/get")
    public Result<Boolean> get(@CurrentUser CurrentStaffInfo staffInfo, @NotNull @RequestBody GetCouponsForm form) {
        Long currentEid = form.getCustomerEid();
        Long currentUserId = staffInfo.getCurrentUserId();
        CouponActivityReceiveRequest request = new CouponActivityReceiveRequest();
        request.setCouponActivityId(form.getCouponActivityId());
        request.setUserId(currentUserId);
        request.setEid(currentEid);
        request.setPlatformType(CouponPlatformTypeEnum.SALES_ASSIST.getCode());
        // 企业信息
        EnterpriseDTO enterprise = Optional.ofNullable(enterpriseApi.getById(currentEid)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        request.setEtype(enterprise.getType());
        // 用户信息
        UserDTO user = Optional.ofNullable(userApi.getById(staffInfo.getCurrentUserId())).orElse(new UserDTO());
        request.setUserName(user.getUsername());
        // 会员信息
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(currentEid)).orElse(new CurrentMemberForMarketingDTO());
        request.setCurrentMember(member.getCurrentMember());
        return Result.success(couponActivityApi.receiveByCouponActivityId(request));
    }

    @Log(title = "领取优惠券-一键领券", businessType = BusinessTypeEnum.INSERT)
    @UserAccessAuthentication
    @ApiOperation(value = "一键领券", httpMethod = "POST")
    @PostMapping(path = "/oneKeyReceive")
    public Result<Boolean> oneKeyReceive(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryShopCouponsListForm form) {
        log.info("oneKeyReceive param is"+JSONUtil.toJsonStr(form));
        QueryCouponActivityCanReceiveRequest request = new QueryCouponActivityCanReceiveRequest();
        request.setCurrentEid(form.getCustomerEid());
        request.setShopEid(form.getShopEid());
        request.setPlatformType(CouponPlatformTypeEnum.SALES_ASSIST.getCode());
        Long currentEid = form.getCustomerEid();
        Long currentUserId = staffInfo.getCurrentUserId();
        request.setUserId(currentUserId);
        request.setEid(currentEid);
        request.setPlatformType(CouponPlatformTypeEnum.SALES_ASSIST.getCode());
        // 企业信息
        EnterpriseDTO enterprise = Optional.ofNullable(enterpriseApi.getById(currentEid)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        request.setEtype(enterprise.getType());
        request.setEname(enterprise.getName());
        // 用户信息
        UserDTO user = Optional.ofNullable(userApi.getById(staffInfo.getCurrentUserId())).orElse(new UserDTO());
        request.setUserName(user.getUsername());
        // 会员信息
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(currentEid)).orElse(new CurrentMemberForMarketingDTO());
        request.setCurrentMember(member.getCurrentMember());
        return Result.success(couponActivityApi.oneKeyReceive(request));
    }

}
