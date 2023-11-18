package com.yiling.mall.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.mall.order.dto.request.OrderCouponUseReturnRequest;
import com.yiling.mall.order.service.OrderCouponUseService;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.enums.OrderErrorCode;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/11/19
 */
@Slf4j
@Service
public class OrderCouponUseServiceImpl implements OrderCouponUseService {

    @DubboReference
    private OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    private CouponApi         couponApi;

    @Override
    public Boolean orderReturnCoupon(OrderCouponUseReturnRequest request) {
        log.info("orderReturnCoupon, request -> {}", JSON.toJSONString(request));
        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getOpUserId()) || CollUtil.isEmpty(request.getCouponIdList())){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        List<Long> couponIdList = request.getCouponIdList().stream().distinct().collect(Collectors.toList());
        // 查询订单使用优惠券记录
        List<OrderCouponUseDTO> orderCouponUseList = orderCouponUseApi.listByCouponIdList(couponIdList);
        if(CollUtil.isEmpty(orderCouponUseList)){
            log.error("orderReturnCoupon, 此订单未查询到订单优惠劵使用记录, couponId：{}", couponIdList.toString());
            throw new BusinessException(OrderErrorCode.ORDER_COUPON_USE_NULL_ERROR);
        }
        // 是否已归还
        List<OrderCouponUseDTO> notReturnList = orderCouponUseList.stream()
                .filter(o -> ObjectUtil.equal(com.yiling.marketing.common.enums.OrderCouponUseReturnTypeEnum.NOT_RETURN.getCode(), o.getIsReturn())).collect(Collectors.toList());
        if(CollUtil.isEmpty(notReturnList) ){
            List<Long> returnedIds = new ArrayList<>();
            if(CollUtil.isNotEmpty(notReturnList)){
                List<Long> notReturnIds = notReturnList.stream().map(OrderCouponUseDTO::getId).collect(Collectors.toList());
                returnedIds = orderCouponUseList.stream().filter(o -> !notReturnIds.contains(o.getId())).map(OrderCouponUseDTO::getId).collect(Collectors.toList());
            }
            log.error("orderReturnCoupon, 订单取消优惠券已退还至账户，此订单优惠劵使用已退还，orderCouponUseId：{}", returnedIds.toString());
            throw new BusinessException(OrderErrorCode.ORDER_COUPON_USE_RETURNED_ERROR);
        }
        // 订单优惠劵使用记录归还
        orderCouponUseApi.returenCouponByIds(couponIdList);

        // 优惠券归还
        List<CouponDTO> couponList = couponApi.getByIdList(couponIdList);
        if(CollUtil.isEmpty(couponList)){
            throw new BusinessException(CouponErrorCode.COUPON_NULL_ERROR);
        }
        for (CouponDTO coupon : couponList) {
            if(ObjectUtil.equal(CouponUsedStatusEnum.NOT_USED.getCode(), coupon.getUsedStatus())){
                log.error("orderReturnCoupon, 此优惠券未使用，优惠券id：[{}]", coupon.getId());
                throw new BusinessException(OrderErrorCode.ORDER_COUPON_USE_RETURNED_ERROR);
            }
        }
        // 无论优惠券是否过期、废弃，直接退还，更新为未使用
        return couponApi.returenCouponByIds(couponIdList);
    }
}
