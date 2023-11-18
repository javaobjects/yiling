package com.yiling.mall.payment.service.strategy;

import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.util.function.Function;
import com.google.common.collect.Maps;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.mall.payment.service.MsgStrategyService;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.user.common.enums.MemberOrderStatusEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberOrderApi;
import com.yiling.user.member.dto.request.OpenMemberRequest;

import cn.hutool.core.comparator.CompareUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/** 会员支付回调
 * @author zhigang.guo
 * @date: 2022/9/15
 */
@Component
@Slf4j
public class MemberPayMsgStrategy implements MsgStrategyService<PayOrderDTO,TradeTypeEnum> {

    @DubboReference
    MemberApi memberApi;
    @DubboReference
    MemberOrderApi memberOrderApi;

    @DubboReference
    CouponApi couponApi;

    /**
     * 会员支付具体处理逻辑函数
     */
    private final Map<AppOrderStatusEnum, Function<PayOrderDTO,MqAction>> processMessageFunctions = Maps.newHashMapWithExpectedSize(4);


    @PostConstruct
    private void init() {

        processMessageFunctions.put(AppOrderStatusEnum.SUCCESS,this::successMessageFunction);
        processMessageFunctions.put(AppOrderStatusEnum.CLOSE,this::failureMessageFunction);
        // 回调支付中,直接返回消息已确认，无需处理
        processMessageFunctions.put(AppOrderStatusEnum.PAY_ING,(t)-> MqAction.CommitMessage);
        processMessageFunctions.put(AppOrderStatusEnum.WAIT_PAY,this::failureMessageFunction);
    }



    @Override
    public TradeTypeEnum getMsgTradeType() {

        return TradeTypeEnum.MEMBER;
    }


    /**
     * 会员支付回调成功，开通会员
     * @param payOrderDTO
     * @return
     */
    private MqAction successMessageFunction(PayOrderDTO payOrderDTO) {

        OpenMemberRequest request = new OpenMemberRequest();
        request.setOrderNo(payOrderDTO.getAppOrderNo());
        request.setTradeNo(payOrderDTO.getThirdTradeNo());
        request.setStatus(MemberOrderStatusEnum.PAY_SUCCESS.getCode());
        request.setOpUserId(payOrderDTO.getUserId());
        request.setPayWay(payOrderDTO.getPayWay());
        request.setPayChannel(payOrderDTO.getPaySource());
        Boolean flag =  memberApi.openMember(request);

        if (flag) {

            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;
    }


    /**
     * 会员支付，支付失败，调整购买记录状态，如果有使用会员优惠劵，回退优惠劵
     * @param payOrderDTO
     * @return
     */
    private MqAction failureMessageFunction(PayOrderDTO payOrderDTO) {

        // 订单是否使用了会员券,并且还没有回退
        Long couponId = memberOrderApi.getMemberOrderCouponId(payOrderDTO.getAppOrderNo());

        // 表示使用会员优惠券使用记录
        if (couponId != null && CompareUtil.compare(couponId,0l) > 0) {

            couponApi.returenCouponByIds(Collections.singletonList(couponId));
        }

        OpenMemberRequest request = new OpenMemberRequest();
        request.setOrderNo(payOrderDTO.getAppOrderNo());
        request.setTradeNo(payOrderDTO.getThirdTradeNo());
        request.setStatus(MemberOrderStatusEnum.PAY_FAIL.getCode());
        request.setOpUserId(payOrderDTO.getUserId());
        request.setPayWay(payOrderDTO.getPayWay());
        request.setPayChannel(payOrderDTO.getPaySource());
        Boolean flag =  memberApi.openMember(request);

        if (flag) {

            return MqAction.CommitMessage;
        }

        // 回滚事务
        throw new RuntimeException("会员支付,回调失败!");
    }

    @Override
    @GlobalTransactional
    public MqAction processMessage(PayOrderDTO payOrderDTO) {

        if (log.isDebugEnabled()) {

            log.debug("会员订单支付回调:{}", payOrderDTO);
        }

        return processMessageFunctions.get(AppOrderStatusEnum.getByCode(payOrderDTO.getAppOrderStatus())).apply(payOrderDTO);
    }
}
