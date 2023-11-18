package com.yiling.order.order.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.B2BOrderQuantityDTO;
import com.yiling.order.order.dto.B2BSettlementDTO;
import com.yiling.order.order.dto.B2BSettlementDetailDTO;
import com.yiling.order.order.dto.OrderB2BPaymentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.B2bOrderConfirmRequest;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.OrderB2BPaymentRequest;
import com.yiling.order.order.dto.request.QueryB2BSettlementPageReuest;
import com.yiling.order.order.dto.request.UpdateOrderSettlementStatusRequest;
import com.yiling.order.order.enums.OrderPlatformTypeEnum;

/**
 * B2B订单API
 * @author:wei.wang
 * @date:2021/10/19
 */
public interface OrderB2BApi {

    /**
     * 全部订单查询接口
     * @param request
     * @return
     */
    Page<OrderDTO> getB2BAppOrder(OrderB2BPageRequest request);

    /**
     * 根据id获取订单信息
     * @param orderId
     * @return
     */
    OrderDTO getB2BOrderOne(Long orderId);

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
     * 给姚德熙 提供结算的接口
     * @param queryB2BSettlementPageReuest
     * @return
     */
    Page<B2BSettlementDTO> pageB2bSettlementList(QueryB2BSettlementPageReuest queryB2BSettlementPageReuest);

    /**
     * 给姚德熙 提供结算的接口
     * @param orderNo 订单编号
     * @return
     */
    B2BSettlementDTO getB2bSettlementOne(String orderNo);

    /**
     * 批量修改订单结算状态
     * @param request 修改状态的集合
     * @return
     */
    Boolean batchB2bOrderSettlementStatus(UpdateOrderSettlementStatusRequest request);

    /**
     * 通过订单获取货结算明细信息
     * @param orderId 订单ID
     * @return
     */
    List<B2BSettlementDetailDTO> listSettleOrderDetailByOrderId(Long orderId);


    /**
     * B2B首页统计订单数量
     * @param eid
     * @param orderPlatformTypeEnum
     * @return
     */
    B2BOrderQuantityDTO countB2BOrderQuantity(Long eid, OrderPlatformTypeEnum orderPlatformTypeEnum);


    /**
     * 查询48小时，在线未支付的订单
     * @return
     */
    List<Long> selectOnlineNotPayOrder(Integer minute);

    /**
     * b2b确认订单
     * @param confirmRequests
     * @return
     */
    Boolean b2bOrderConfirm(List<B2bOrderConfirmRequest> confirmRequests);

}
