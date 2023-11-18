package com.yiling.order.order.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.request.QueryOrderCouponUsePageRequest;
import com.yiling.order.order.entity.OrderCouponUseDO;

/**
 * <p>
 * 订单优惠劵使用记录表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-23
 */
public interface OrderCouponUseService extends BaseService<OrderCouponUseDO> {

    /**
     * @param orderId    订单ID
     * @param couponType 购物劵类型
     * @return
     */
    List<OrderCouponUseDO> selectOrderCouponList(Long orderId, Integer couponType);

    /**
     *
     * @param orderId 订单ID
     * @param couponType 购物劵类型
     * @param isReturn 退款类型
     * @return
     */
    List<OrderCouponUseDO> listOrderCouponReturnType(Long orderId, Integer couponType, Integer isReturn);

    /**
     * @param couponId
     * @return
     */
    List<OrderCouponUseDO> listByCouponId(Long couponId);

    /**
     * 根据优惠券活动id查询订单
     * @param couponActivityId
     * @return
     */
    List<OrderCouponUseDO> listByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券id查询订单使用记录
     * @param couponIdList
     * @return
     */
    List<OrderCouponUseDO> listByCouponIdList(List<Long> couponIdList);

    /**
     * 订单使用记录分页
     * @param request
     * @return
     */
    Page<OrderCouponUseDO> listPage(QueryOrderCouponUsePageRequest request);

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
     * 根据订单id列表查询优惠券使用记录
     * @param orderIds
     * @return
     */
    List<OrderCouponUseDO> listByOrderIds(List<Long> orderIds);
}
