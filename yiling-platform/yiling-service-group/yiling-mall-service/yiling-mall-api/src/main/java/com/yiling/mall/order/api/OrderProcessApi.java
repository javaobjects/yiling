package com.yiling.mall.order.api;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yiling.framework.common.pojo.Result;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.mall.order.dto.request.PresaleOrderSubmitRequest;
import com.yiling.mall.order.dto.request.RefundReTryRequest;
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


/**
 * 订单处理 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
public interface OrderProcessApi {

    /**
     * 预售订单下单提交
     * @param request 请求信息
     * @return
     */
    OrderSubmitBO preSaleOrderSubmit(PresaleOrderSubmitRequest request);

    /**
     * 提交订单
     *
     * @param request
     * @return
     */
    OrderSubmitBO submit(OrderSubmitRequest request);


    /**
     * 取消订单
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean cancelOrderExpect(Long orderId, Long opUserId);

    /**
     * 发货
     *
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
     * 破损单退货单申请
     *
     * @param
     */
    Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, Integer fromWhere, OrderDTO orderDTO);

    /**
     * 采购商退货单申请
     *
     * @param orderReturnApplyRequest
     * @return
     */
    Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest);

    /**
     * 供应商退货单申请
     *
     * @param orderReturnApplyRequest
     * @return
     */
    Boolean supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO);

    /**
     * 销售订单退货单审核
     *
     * @param orderReturnApplyRequest
     */
    Boolean checkOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest);


    /**
     * 订单反审核
     *
     * @param updateOrderNotAuditRequest
     * @return
     * @throws Exception
     */
    @Deprecated
    Result<Boolean> modifyOrderNotAudit(UpdateOrderNotAuditRequest updateOrderNotAuditRequest) throws Exception;

    /**
     * 订单反审优化版本V2
     * @param modifyOrderNotAuditRequest
     * @return
     * @throws Exception
     */
    Boolean modifyOrderNotAudit_v2(ModifyOrderNotAuditRequest modifyOrderNotAuditRequest) throws Exception;

    /**
     * 取消订单 B2B移动端和POP商家后端
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean cancel(Long orderId, Long opUserId);

    /**
     * B2B商家后台取消订单
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean cancelOrderB2BAdmin(Long orderId, Long opUserId);

    /**
     * 申请退货-部分发货
     *
     * @param request 申请退货请求参数
     * @return 成功/失败
     */
    Boolean deliverOrderReturn(OrderReturnApplyRequest request);

    /**
     * 申请退货--采购商申请
     *
     * @param request 申请退货请求参数
     * @return 成功/失败
     */
    Boolean applyOrderReturn(OrderReturnApplyRequest request);

    /**
     * 退货单审核
     *
     * @param request
     * @return
     */
    Boolean verifyOrderReturn(OrderReturnVerifyRequest request);

    /**
     * 销售助手退货单申请
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    Boolean applySaOrderReturn(B2BOrderReturnApplyRequest request);

    /**
     * 退款失败财务处理
     *
     * @param request
     * @return
     */
    Boolean reTryRefund(RefundReTryRequest request);

    /**
     * 收货
     *
     * @param orderId
     * @param opUserId
     * @param buyerEid
     * @return
     */
    Boolean B2BReceive(Long orderId, Long opUserId, Long buyerEid);


    /**
     * B2B 7天自动收货
     * @param day
     * @return
     */
    Boolean activeB2BReceive(Integer day);

    /**
     * 自动收货逻辑处理
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
     * 预售订单发送短信提醒
     * @param orderNo 订单号
     * @param preSalOrderReminderTypeEnum 提醒类型
     * @return
     */
    boolean sendPresaleOrderSmsNotice(String orderNo, PreSalOrderReminderTypeEnum preSalOrderReminderTypeEnum) ;

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
