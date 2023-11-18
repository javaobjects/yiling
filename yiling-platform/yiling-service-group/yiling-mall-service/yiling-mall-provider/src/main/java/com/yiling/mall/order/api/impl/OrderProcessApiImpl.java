package com.yiling.mall.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.framework.common.pojo.Result;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.mall.order.dto.request.PresaleOrderSubmitRequest;
import com.yiling.mall.order.dto.request.RefundReTryRequest;
import com.yiling.mall.order.service.OrderDeliveryAndReceiveProcessService;
import com.yiling.mall.order.service.OrderModifyAuditService;
import com.yiling.mall.order.service.OrderRefundService;
import com.yiling.mall.order.service.OrderSubmitFactory;
import com.yiling.mall.order.service.PresaleOrderService;
import com.yiling.mall.order.service.ReturnOrderService;
import com.yiling.marketing.integral.dto.request.AddIntegralRequest;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.B2BOrderReturnApplyRequest;
import com.yiling.order.order.dto.request.ModifyOrderNotAuditRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnVerifyRequest;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveListInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderNotAuditRequest;
import com.yiling.order.order.enums.PreSalOrderReminderTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@DubboService
@Slf4j
public class OrderProcessApiImpl implements OrderProcessApi {

    @Autowired
    OrderSubmitFactory                    orderSubmitFactory;
    @Autowired
    OrderDeliveryAndReceiveProcessService orderDeliveryAndReceiveProcessService;
    @Autowired
    ReturnOrderService                    returnOrderService;
    @Autowired
    OrderModifyAuditService               orderModifyAuditService;
    @Autowired
    OrderRefundService                    orderRefundService;
    @DubboReference
    MqMessageSendApi                      mqMessageSendApi;
    @Autowired
    private PresaleOrderService          presaleOrderService;


    @Override
    public OrderSubmitBO preSaleOrderSubmit(PresaleOrderSubmitRequest request) {

        return presaleOrderService.preSaleOrderSubmit(request);
    }

    @Override
    public OrderSubmitBO submit(OrderSubmitRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("order submit request param:{}",request);
        }
        OrderSubmitBO orderSubmitBo =  orderSubmitFactory.getInstance(request.getOrderTypeEnum(), request.getOrderSourceEnum()).submit(request);

        orderSubmitBo.getMqMessageBOList().forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));

        return orderSubmitBo;
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    public Boolean cancelOrderExpect(Long orderId, Long opUserId) {
        return orderDeliveryAndReceiveProcessService.cancelOrderExpect(orderId, opUserId);
    }

    /**
     * 发货
     *
     * @param request
     * @return
     */
    @Override
    public Boolean delivery(SaveOrderDeliveryListInfoRequest request) {
        return orderDeliveryAndReceiveProcessService.delivery(request);
    }

    /**
     * 关闭发货
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public Boolean closeDelivery(Long opUserId,Long orderId) {
        return orderDeliveryAndReceiveProcessService.closeDelivery(opUserId,orderId);
    }

    /**
     * 页面发货接口
     *
     * @param request
     * @return
     */
    @Override
    public Boolean frontDelivery(SaveOrderDeliveryListInfoRequest request) {
        return orderDeliveryAndReceiveProcessService.frontDelivery(request);
    }

    /**
     * 收货
     * @param request
     * @return
     */
    @Override
    public Boolean receive(SaveOrderReceiveListInfoRequest request){
        return orderDeliveryAndReceiveProcessService.receive(request);
    }

    @Override
    public Boolean secondBusinessAutomaticReceive(List<Long> eidList) {

        return orderDeliveryAndReceiveProcessService.secondBusinessAutomaticReceive(eidList);
    }

    /**
     * 破损退货单创建
     *
     * @param orderReturnApplyRequest
     * @return
     */
    @Override
    public Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, Integer fromWhere, OrderDTO orderDTO) {
        return returnOrderService.damageOrderReturn(orderReturnApplyRequest, fromWhere, orderDTO);
    }

    /**
     * 采购商退货单创建
     *
     * @param orderReturnApplyRequest
     * @return
     */
    @Override
    public Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest) {
        return returnOrderService.purchaseApplyReturnOrder(orderReturnApplyRequest);
    }

    /**
     * 供应商退货单创建
     *
     * @param orderReturnApplyRequest
     * @return
     */
    @Override
    public Boolean supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        return returnOrderService.supplierApplyOrderReturn(orderReturnApplyRequest, orderDTO);
    }

    /**
     * 退货单审核：1.采购商破损退货，2.采购商正常退货
     *
     * @param orderReturnApplyRequest
     * @return
     */
    @Override
    public Boolean checkOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest) {
        return returnOrderService.checkOrderReturn(orderReturnApplyRequest);
    }

    @Override
    public Result<Boolean> modifyOrderNotAudit(UpdateOrderNotAuditRequest updateOrderNotAuditRequest) throws Exception {

        return orderModifyAuditService.modifyOrderNotAudit(updateOrderNotAuditRequest);
    }

    @Override
    public Boolean modifyOrderNotAudit_v2(ModifyOrderNotAuditRequest modifyOrderNotAuditRequest) throws Exception {

        return orderModifyAuditService.modifyOrderNotAudit_v2(modifyOrderNotAuditRequest);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    public Boolean cancel(Long orderId, Long opUserId) {
        return orderDeliveryAndReceiveProcessService.cancel(orderId, opUserId);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    public Boolean cancelOrderB2BAdmin(Long orderId, Long opUserId) {
        return orderDeliveryAndReceiveProcessService.cancelOrderB2BAdmin(orderId, opUserId);
    }

    @Override
    public Boolean deliverOrderReturn(OrderReturnApplyRequest request) {
        return returnOrderService.deliverOrderReturn(request);
    }

    @Override
    public Boolean applyOrderReturn(OrderReturnApplyRequest request) {
        return returnOrderService.applyOrderReturn(request);
    }

    @Override
    public Boolean verifyOrderReturn(OrderReturnVerifyRequest request) {
        return returnOrderService.verifyOrderReturn(request);
    }

    @Override
    public Boolean applySaOrderReturn(B2BOrderReturnApplyRequest request) {
        return returnOrderService.applySaOrderReturn(request);
    }

    @Override
    public Boolean reTryRefund(RefundReTryRequest request) {
        return orderRefundService.reTryRefund(request);
    }

    /**
     * 收货
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    public Boolean B2BReceive(Long orderId, Long opUserId,Long buyerEid) {
        return orderDeliveryAndReceiveProcessService.B2BReceive(orderId,opUserId,buyerEid);
    }

    /**
     * 自动收货
     *
     * @param day
     * @return
     */
    @Override
    public Boolean activeB2BReceive(Integer day) {
        return orderDeliveryAndReceiveProcessService.activeB2BReceive(day);
    }
    @Override
    public Boolean activeB2BReceiveByOrderId(Long orderId){
        return orderDeliveryAndReceiveProcessService.activeB2BReceiveByOrderId(orderId);
    }

    /**
     * 获取订单信息(运营后台),要根据
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getBackOrderPage(QueryBackOrderInfoRequest request) {
        return orderDeliveryAndReceiveProcessService.getBackOrderPage(request);
    }

    /**
     * 统计前天收货数量
     *
     * @param orderTypeList 订单类型
     * @return
     */
    @Override
    public Boolean countReceiveOrder(List<Integer> orderTypeList) {
        return orderDeliveryAndReceiveProcessService.countReceiveOrder(orderTypeList);
    }


    @Override
    public boolean sendPresaleOrderSmsNotice(String orderNo, PreSalOrderReminderTypeEnum preSalOrderReminderTypeEnum) {


        return presaleOrderService.sendPresaleOrderSmsNotice(orderNo,preSalOrderReminderTypeEnum);
    }

    /**
     * 根据订单编号获取积分对象
     * @param orderNo
     * @param opUserId
     * @return
     */
    @Override
    public AddIntegralRequest getIntegralRecord(String orderNo, Long opUserId){
        return orderDeliveryAndReceiveProcessService.getIntegralRecord(orderNo,opUserId);
    }

    @Override
    public Boolean confirmPaymentDayRepayment(Long orderId, Long opUserId) {

        return orderDeliveryAndReceiveProcessService.confirmPaymentDayRepayment(orderId,opUserId);
    }
}
