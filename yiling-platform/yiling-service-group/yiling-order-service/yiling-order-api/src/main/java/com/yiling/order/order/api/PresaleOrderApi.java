package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.UpdatePresaleOrderRequest;

/** 预售订单
 * @author zhigang.guo
 * @date: 2022/10/10
 */
public interface PresaleOrderApi {

    /**
     * 获取预售订单扩展信息
     * @param orderId 订单Id
     * @return 预售订单扩展信息
     */
    PresaleOrderDTO getOrderInfo(Long orderId);

    /**
     * 批量查询预售订单信息
     * @param orderIdList
     * @return
     */
    List<PresaleOrderDTO> listByOrderIds(List<Long> orderIdList);

    /**
     * 通过订单号批量查询预售订单信息
     * @param orderNoList
     * @return
     */
    List<PresaleOrderDTO> listByOrderNos(List<String> orderNoList);


    /**
     * 修改订单是否已支付定金和尾款
     * @param request
     * @return
     */
    boolean updatePresalOrderByOrderId(UpdatePresaleOrderRequest request);


    /**
     * 查询超时未支付尾款订单
     * @return
     */
    List<Long> selectTimeOutNotPayBalanceOrder();


    /**
     * 获取需要发送尾款短信提醒的订单
     * @param hour
     * @return
     */
    List<String> selectNeedSendBalanceSmsOrders(Integer hour);


    /**
     * 支付订单尾款
     * @return
     */
    List<String> selectNeedPayBalanceSmsOrders();








}
