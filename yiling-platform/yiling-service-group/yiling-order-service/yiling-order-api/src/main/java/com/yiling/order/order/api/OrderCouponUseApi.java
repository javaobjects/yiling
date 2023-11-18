package com.yiling.order.order.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.request.QueryOrderCouponUsePageRequest;

/**
 * 订单优惠劵使用记录Api
 * @author:wei.wang
 * @date:2021/10/25
 */
public interface OrderCouponUseApi {

    /**
     * @param orderId 订单ID
     * @param couponType 购物劵类型
     * @return
     */
    List<OrderCouponUseDTO> listOrderCoupon(Long orderId, Integer couponType);

    /**
     *
     * @param orderId 订单ID
     * @param couponType 购物劵类型
     * @param isReturn 退款类型
     * @return
     */
    List<OrderCouponUseDTO> listOrderCouponReturnType(Long orderId, Integer couponType,Integer isReturn);

    /**
     * 根据优惠券活动id查询订单使用记录
     * @param couponActivityId
     * @return
     */
    List<OrderCouponUseDTO> listByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券id查询订单使用记录
     * @param couponIdList
     * @return
     */
    List<OrderCouponUseDTO> listByCouponIdList(List<Long> couponIdList);

    /**
     * 订单使用记录分页
     * @param request
     * @return
     */
    Page<OrderCouponUseDTO> listPage(QueryOrderCouponUsePageRequest request);

    /**
     * 根据优惠券id列表统计使用数量
     * @param couponActivityIdList
     * @return
     */
    List<Map<String, Long>> getCountByCouponActivityId(List<Long> couponActivityIdList);

    /**
     * 根据id退还优惠券
     * @param ids
     * @return
     */
    Boolean returenCouponByIds(List<Long> ids);

    /**
     * @param orderIds 订单id list
     * @return
     */
    List<OrderCouponUseDTO> listByOrderIds(List<Long> orderIds);
}
