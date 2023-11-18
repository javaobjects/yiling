package com.yiling.order.order.api.impl;


import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.request.QueryOrderCouponUsePageRequest;
import com.yiling.order.order.service.OrderCouponUseService;

/**
 * 订单优惠劵使用记录Api
 * @author:wei.wang
 * @date:2021/10/25
 */
@DubboService
public class OrderCouponUseApiImpl implements OrderCouponUseApi {

    @Autowired
    private OrderCouponUseService orderCouponUseService;

    /**
     * @param orderId    订单ID
     * @param couponType 购物劵类型
     * @return
     */
    @Override
    public List<OrderCouponUseDTO> listOrderCoupon(Long orderId, Integer couponType) {

        return PojoUtils.map(orderCouponUseService.selectOrderCouponList(orderId,couponType),OrderCouponUseDTO.class);
    }

    /**
     * @param orderId    订单ID
     * @param couponType 购物劵类型
     * @param isReturn   退款类型
     * @return
     */
    @Override
    public List<OrderCouponUseDTO> listOrderCouponReturnType(Long orderId, Integer couponType, Integer isReturn) {
        return PojoUtils.map(orderCouponUseService.listOrderCouponReturnType(orderId,couponType,isReturn),OrderCouponUseDTO.class);
    }

    @Override
    public List<OrderCouponUseDTO> listByCouponActivityId(Long couponActivityId) {
        return PojoUtils.map(orderCouponUseService.listByCouponActivityId(couponActivityId),OrderCouponUseDTO.class);
    }

    @Override
    public List<OrderCouponUseDTO> listByCouponIdList(List<Long> couponIdList) {
        return PojoUtils.map(orderCouponUseService.listByCouponIdList(couponIdList),OrderCouponUseDTO.class);
    }

    @Override
    public Page<OrderCouponUseDTO> listPage(QueryOrderCouponUsePageRequest request) {
        return PojoUtils.map(orderCouponUseService.listPage(request),OrderCouponUseDTO.class);
    }

    @Override
    public List<Map<String, Long>> getCountByCouponActivityId(List<Long> couponActivityIdList) {
        return orderCouponUseService.getCountByCouponActivityId(couponActivityIdList);
    }

    @Override
    public Boolean returenCouponByIds(List<Long> ids) {
        return orderCouponUseService.returenCouponByIds(ids);
    }

    @Override
    public List<OrderCouponUseDTO> listByOrderIds(List<Long> orderIds) {
        return PojoUtils.map(orderCouponUseService.listByOrderIds(orderIds),OrderCouponUseDTO.class);
    }
}
