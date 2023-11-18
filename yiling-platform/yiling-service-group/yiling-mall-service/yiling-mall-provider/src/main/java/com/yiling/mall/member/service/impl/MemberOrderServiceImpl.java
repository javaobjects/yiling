package com.yiling.mall.member.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.member.dao.MemberOrderMapper;
import com.yiling.mall.member.entity.MemberBuyStageDO;
import com.yiling.mall.member.entity.MemberDO;
import com.yiling.mall.member.entity.MemberOrderCouponDO;
import com.yiling.mall.member.entity.MemberOrderDO;
import com.yiling.mall.member.service.MemberBuyStageService;
import com.yiling.mall.member.service.MemberOrderCouponService;
import com.yiling.mall.member.service.MemberOrderService;
import com.yiling.mall.member.service.MemberService;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.coupon.dto.request.UseMemberCouponRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.request.QueryActivityDetailRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.MemberOrderStatusEnum;
import com.yiling.user.member.dto.request.MemberOrderRequest;
import com.yiling.user.member.enums.MemberOrderCouponUseStatusEnum;

import cn.hutool.core.collection.CollUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 会员订单表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-20
 */
@Slf4j
@Service
public class MemberOrderServiceImpl extends BaseServiceImpl<MemberOrderMapper, MemberOrderDO> implements MemberOrderService {

    @Autowired
    MemberBuyStageService memberBuyStageService;
    @Autowired
    MemberOrderCouponService memberOrderCouponService;
    @Autowired
    MemberService memberService;

    @DubboReference
    CouponActivityApi couponActivityApi;

    @Override
    @GlobalTransactional
    public MemberOrderDO createMemberOrder(MemberOrderRequest request) {
        MemberOrderDO memberOrderDO = PojoUtils.map(request, MemberOrderDO.class);

        MemberBuyStageDO memberBuyStageDO = Optional.ofNullable(memberBuyStageService.getById(request.getBuyStageId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.BUY_STAGE_NOT_EXIST));

        MemberDO memberDO = Optional.ofNullable(memberService.getById(memberBuyStageDO.getMemberId())).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_NOT_EXIST));
        if (memberDO.getStopGet() == 1) {
            throw new BusinessException(UserErrorCode.MEMBER_STATUS_ERROR);
        }
        // 校验出现零或负金额订单，控制无法支付
        if (request.getPayAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(UserErrorCode.MEMBER_ORDER_PAY_AMOUNT_ERROR);
        }
        // 校验一张优惠券不能重复使用
        if (Objects.nonNull(request.getCouponId()) && request.getCouponId() != 0) {
            List<MemberOrderCouponDO> memberOrderCouponDOList = memberOrderCouponService.getByCouponId(request.getCouponId());
            List<MemberOrderCouponDO> list = memberOrderCouponDOList.stream().filter(memberOrderCouponDO -> memberOrderCouponDO.getUseStatus().equals(MemberOrderCouponUseStatusEnum.USED.getCode())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(list)) {
                throw new BusinessException(UserErrorCode.MEMBER_ORDER_COUPON_USED);
            }
        }

        // 生成会员订单
        memberOrderDO.setMemberId(memberBuyStageDO.getMemberId());
        memberOrderDO.setMemberName(memberDO.getName());
        memberOrderDO.setStatus(MemberOrderStatusEnum.WAITING_PAY.getCode());
        log.info("创建会员订单完成 企业ID={} 对应的会员订单信息={}", request.getEid() , JSONObject.toJSONString(memberOrderDO));
        this.save(memberOrderDO);

        if (Objects.nonNull(request.getCouponId()) && request.getCouponId() != 0) {
            // 获取优惠券活动ID进行保存
            QueryActivityDetailRequest detailRequest = new QueryActivityDetailRequest();
            detailRequest.setCouponId(request.getCouponId());
            detailRequest.setEid(request.getEid());
            detailRequest.setMemberId(request.getBuyStageId());
            Long couponActivityId = Optional.ofNullable(couponActivityApi.getActivityIdByCouponId(detailRequest)).orElseThrow(() -> new BusinessException(CouponErrorCode.COUPON_NULL_ERROR));

            // 生成会员使用优惠券信息
            MemberOrderCouponDO memberOrderCouponDO = PojoUtils.map(request, MemberOrderCouponDO.class);
            memberOrderCouponDO.setMemberId(memberBuyStageDO.getMemberId());
            memberOrderCouponDO.setMemberName(memberDO.getName());
            memberOrderCouponDO.setBuyStageId(memberBuyStageDO.getId());
            memberOrderCouponDO.setUseStatus(MemberOrderCouponUseStatusEnum.USED.getCode());
            memberOrderCouponDO.setCouponActivityId(couponActivityId);
            memberOrderCouponService.save(memberOrderCouponDO);

            // 调用优惠券模块，使用优惠券
            UseMemberCouponRequest couponRequest = new UseMemberCouponRequest();
            couponRequest.setId(request.getCouponId());
            couponRequest.setMemberId(memberDO.getId());
            couponRequest.setCurrentEid(memberOrderDO.getEid());
            couponRequest.setCurrentUserId(request.getOpUserId());
            couponActivityApi.useMemberCoupon(couponRequest);
            log.info("生成会员订单 调用优惠券模块使用优惠券 调用入参数据={}", JSONObject.toJSONString(couponRequest));
        }

        return memberOrderDO;

    }

}
