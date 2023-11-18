package com.yiling.order.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.B2BOrderQuantityDTO;
import com.yiling.order.order.dto.OrderB2BPaymentDTO;
import com.yiling.order.order.dto.request.B2bOrderConfirmRequest;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.OrderB2BPaymentRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.enums.OrderPlatformTypeEnum;

/**
 * B2B订单Service
 * @author:wei.wang
 * @date:2021/10/19
 */
public interface OrderB2BService {
    /**
     * 全部订单查询接口
     * @param request
     * @return
     */
    Page<OrderDO> getB2BAppOrder(OrderB2BPageRequest request);

    /**
     * 根据id获取订单信息
     * @param orderId
     * @return
     */
    OrderDO getB2BOrderOne(Long orderId);

    /**
     * 取消订单
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean B2BCancel(Long orderId, Long opUserId);

    /**
     * 收货
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean B2BReceive(Long orderId,Long opUserId);

    /**
     * 超过7天自动收货
     * @param day 天数
     * @return
     */
    void activeB2BReceive(Integer day);

    /**
     * 自动收货根据订单ID
     * @param orderId
     * @return
     */
    Boolean activeB2BReceiveByOrderId(Long orderId);



    /**
     * 获取订单账期列表
     * @param request
     * @return
     */
    Page<OrderB2BPaymentDTO> getOrderB2BPaymentList(OrderB2BPaymentRequest request);

    /**
     * 统计订单数量
     * @param eid
     * @return
     */
    B2BOrderQuantityDTO countB2BOrderQuantity(Long eid, OrderPlatformTypeEnum orderPlatformTypeEnum);

    /**
     * 查询B2B在线支付未取消的订单
     * @param minute
     * @return
     */
    List<Long> selectOnlineNotPayOrder(Integer minute);

    /**
     * B2B订单确认
     * @param confirmRequests
     * @return
     */
    Boolean b2bOrderConfirm(List<B2bOrderConfirmRequest> confirmRequests);

}
