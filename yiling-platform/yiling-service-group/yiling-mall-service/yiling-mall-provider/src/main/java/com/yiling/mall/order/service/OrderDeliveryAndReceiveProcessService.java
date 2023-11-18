package com.yiling.mall.order.service;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.integral.dto.request.AddIntegralRequest;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveListInfoRequest;

import cn.hutool.core.date.DateTime;

/**
 * 订单删除,发货，收货接口
 * @author:wei.wang
 * @date:2021/7/16
 */
public interface OrderDeliveryAndReceiveProcessService {

    /**
     * 取消预订单订单
     * @param order
     * @param opUserId
     * @return
     */
    Boolean cancelOrderExpect(Long order,Long opUserId);

    /**
     * 发货
     * @param request
     * @return
     */
    Boolean delivery(SaveOrderDeliveryListInfoRequest request);

    /**
     * 关闭发货
     * @param orderId 订单id
     * @return
     */
    Boolean closeDelivery(Long opUserId,Long orderId);

    /**
     * 页面发货接口
     * @param request
     * @return
     */
    Boolean frontDelivery(SaveOrderDeliveryListInfoRequest request);

    /**
     * 取消订单
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean cancel(Long orderId,Long opUserId);

    /**
     * B2B商家后台取消订单
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean cancelOrderB2BAdmin(Long orderId,Long opUserId);

    /**
     * 收货
     * @param orderId
     * @param opUserId
     * @param buyerEid
     * @return
     */
    Boolean B2BReceive(Long orderId,Long opUserId,Long buyerEid);

    /**
     * 自动收货
     * @param day
     * @return
     */
    Boolean activeB2BReceive(Integer day);

    /**
     * 自动收货根据订单ID
     * @param orderId
     * @return
     */
    Boolean activeB2BReceiveByOrderId(Long orderId);


    /**
     *  获取订单信息(运营后台),要根据
     * @param request
     * @return
     */
    Page<OrderDTO> getBackOrderPage(QueryBackOrderInfoRequest request);


    /**
     * 统计前天收货数量
     * @param orderTypeList 订单类型
     * @return
     */
    Boolean countReceiveOrder(List<Integer> orderTypeList);

    /**
     * 收货
     * @param request
     * @return
     */
    Boolean receive(SaveOrderReceiveListInfoRequest request);

    /**
     * 二级商直接收货
     * @param eidList 以岭直采企业
     * @return
     */
    Boolean secondBusinessAutomaticReceive(List<Long> eidList);

    /**
     * 根据订单编号获取积分对象
     * @param orderNo
     * @param opUserId
     * @return
     */
    AddIntegralRequest getIntegralRecord(String orderNo, Long opUserId);

    /**
     * 账期线下还款
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean confirmPaymentDayRepayment(Long orderId,Long opUserId);
}
