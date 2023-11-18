package com.yiling.order.order.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.pojo.Result;
import com.yiling.order.order.bo.OrderModifyAuditChangeBO;
import com.yiling.order.order.dto.CountContinueOrderAndReturnDTO;
import com.yiling.order.order.dto.OrderAndDetailedAllInfoDTO;
import com.yiling.order.order.dto.OrderAssistantConfirmQuantityAmountDTO;
import com.yiling.order.order.dto.OrderAttachmentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryReportCountDTO;
import com.yiling.order.order.dto.OrderEnterpriseDTO;
import com.yiling.order.order.dto.OrderHistoryGoodsDTO;
import com.yiling.order.order.dto.OrderManageStatusNumberDTO;
import com.yiling.order.order.dto.OrderMemberDiscountDTO;
import com.yiling.order.order.dto.OrderNumberDTO;
import com.yiling.order.order.dto.OrderTypeGoodsQuantityDTO;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.dto.request.ModifyOrderNotAuditRequest;
import com.yiling.order.order.dto.request.OrderPOPWebListPageRequest;
import com.yiling.order.order.dto.request.QueryAssistanOrderPageRequest;
import com.yiling.order.order.dto.request.QueryAssistantConfirmOrderPageRequest;
import com.yiling.order.order.dto.request.QueryAssistantOrderFirstRequest;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.dto.request.QueryDiscountOrderBuyerEidPagRequest;
import com.yiling.order.order.dto.request.QueryInvoicePageRequest;
import com.yiling.order.order.dto.request.QueryOrderDeliveryReportRequest;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.dto.request.QueryOrderExportReportPageRequest;
import com.yiling.order.order.dto.request.QueryOrderManagePageRequest;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.dto.request.QueryOrderTimeIntervalRequest;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveListInfoRequest;
import com.yiling.order.order.dto.request.UpdateCustomerConfirmStatusRequest;
import com.yiling.order.order.dto.request.UpdateOrderCashDiscountAmountRequest;
import com.yiling.order.order.dto.request.UpdateOrderInvoiceInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderNotAuditRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentMethodRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.dto.request.UpdateOrderRemarkRequest;
import com.yiling.order.order.dto.request.UpdateTransportInfoRequest;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderStatusEnum;

import cn.hutool.core.date.DateTime;

/**
 * 订单 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/18
 */
public interface OrderApi {

    /**
     * 创建订单updateAuditStatus
     *
     * @param createOrderRequestList
     * @return
     * @author xuan.zhou
     * @date 2021/6/25
     */
    boolean create(List<CreateOrderRequest> createOrderRequestList);

    /**
     * 批量根据订单号获取订单信息
     *
     * @param orderNos 订单号列表
     * @return
     */
    List<OrderDTO> listByOrderNos(List<String> orderNos);

    /**
     * 批量根据订单ID获取订单信息
     *
     * @param ids 订单ID列表
     * @return
     */
    List<OrderDTO> listByIds(List<Long> ids);

    /**
     * 根据类型判断使用eid或者userId
     * @param type
     * @param eids
     * @param userId
     * @param orderType
     * @return
     */
    OrderNumberDTO getOrderNumber(Integer type,List<Long> eids,Long userId ,Integer orderType,Long departmentId);

    /**
     * 获取中台数量
     * @param request
     * @return
     */
    OrderNumberDTO getCentreOrderNumberCount(QueryBackOrderInfoRequest request);

    /**
     * 获取订单列表信息
     * @param request
     * @return
     */
    Page<OrderDTO> getOrderPage(QueryOrderPageRequest request);

    /**
     *B2B采购订单
     * @param request
     * @return
     */
    Page<OrderDTO> getB2BPurchaseOrderPage(QueryOrderPageRequest request);


    /**
     * 销售助手查询订单信息
     * @param queryAssistanOrderPageRequest 查询订单参数
     * @return
     */
    Page<OrderDTO> getSaOrderPage(QueryAssistanOrderPageRequest queryAssistanOrderPageRequest);

    /**
     *  获取订单信息(运营后台)
     * @param request
     * @return
     */
    Page<OrderDTO> getBackOrderPage(QueryBackOrderInfoRequest request);

    /**
     * 根据条件查询所有确认和审核的订单
     * @param request
     * @return
     */
    Page<OrderDTO> getAssistantConfirmOrderPage(QueryAssistantConfirmOrderPageRequest request);

    /**
     * 获取销售助手订单总数量和金额
     * @param request
     * @return
     */
    OrderAssistantConfirmQuantityAmountDTO getAssistantConfirmQuantityAmount(QueryAssistantConfirmOrderPageRequest request);

    /**
     * 获取订单基础信息
     * @param id
     * @return
     */
    OrderDTO getOrderInfo(Long id);

    /**
     * 获取订单基础信息
     *
     * @param orderNo
     * @return
     */
    OrderDTO selectByOrderNo(String orderNo);

    /**
     * 取消订单
     * @param orderId 订单id
     * @param opUserId 操作人id
     * @param isBeforeUpdate 是否需要校验
     * @return
     */
    Boolean  cancel(Long orderId,Long opUserId,Boolean isBeforeUpdate);


    /**
     * 发货
     * @param request
     * @return
     */
    Boolean delivery(SaveOrderDeliveryListInfoRequest request);

    /**
     *
     * 分摊现折金额
     * @return
     */
    Boolean shareAllDeliveryErpCashAmount();

    /**
     * 关闭发货
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean closeDelivery(Long orderId,Long opUserId);

    /**
     * 收货
     * @param request
     * @return
     */
    Boolean receive(SaveOrderReceiveListInfoRequest request);

    /**
     * 获取3个月前订单商品信息 智能推荐
     * @param eid
     * @return
     */
   List<Long> getOrderGoodsByEid(Long eid);

    /**
     * 获取3个月前订单商品POP后端
     * @param eid
     * @return
     */
    List<OrderHistoryGoodsDTO> getOrderGoodsByEidPOPAdmin(Long eid);

    /**
     * 修改支付方式
     * @param request
     * @return
     */
    boolean updatePaymentPayMethod(UpdateOrderPaymentMethodRequest request);

    /**
     * 更新支付状态
     *
     * @param request
     * @return
     */
    boolean updatePaymentStatus(UpdateOrderPaymentStatusRequest request);

    /**
     * 根据票折单号获取订单信息
     * @param ticketDiscountNos
     * @return
     */
    Map<String,List<OrderDTO>> getOrderByTicketDiscountNo(List<String> ticketDiscountNos);

    /**
     * 发票管理详情
     * @param request
     * @return
     */
    Page<OrderDTO> getOrderInvoiceInfoPage(QueryInvoicePageRequest request);

    /**
     * 根据采购商信息和下单时间获取已发货订单信息
     * @param request
     * @return
     */
    List<OrderAndDetailedAllInfoDTO> getOrderAllInfoByBuyerEidAndCreateTime(QueryOrderUseAgreementRequest request);


    /**
     * 获取除取消订单外的所有审核订单数量
     * @param sellerEidList
     * @param
     * @param departmentType
     * @return
     */
    OrderManageStatusNumberDTO getOrderReviewStatusNumber(List<Long> sellerEidList,Long departmentId,Integer departmentType);

    /**
     * 订单审核列表
     * @param request
     * @return
     */
    Page<OrderDTO> getOrderManagePage(QueryOrderManagePageRequest request);

    /**
     * 修改订单现折金额
     *
     * @param request
     * @return
     */
    boolean updateOrderCashDiscountAmount(UpdateOrderCashDiscountAmountRequest request);


    /**
     * 修改回执单信息
     * @param orderId 订单id
     * @param receiveReceiptList 回执单信息
     * @param opUserId 操作人
     * @return
     */
    Boolean updateReceiveReceipt(Long orderId,List<String> receiveReceiptList,Long opUserId);

    /**
     * 获取预期订单列表
     * @param request
     * @return
     */
    Page<OrderDTO> getOrderExpectInfo(QueryOrderExpectPageRequest request);



    /**
     * 预订单取消
     * @param orderId 订单id
     * @param opUserId 操作人
     * @return
     */
    Boolean cancelOrderExpect(Long orderId,Long opUserId);

    /**
     * 修改审核状态为待审核
     *
     * @param id 订单ID
     * @param originalStatus 原始状态
     * @param newStatus 新状态
     * @param opUserId 操作人ID
     * @param remark 备注
     * @return
     */
    boolean updateAuditStatus(Long id, OrderAuditStatusEnum originalStatus, OrderAuditStatusEnum newStatus, Long opUserId, String remark);

    /**
     * 修改订单状态
     *
     * @param id 订单ID
     * @param originalStatus 原始状态
     * @param newStatus 新状态
     * @param opUserId 操作人ID
     * @param remark 备注
     * @return
     */
    boolean updateOrderStatus(Long id, OrderStatusEnum originalStatus, OrderStatusEnum newStatus, Long opUserId, String remark);

    /**
     *  修改B2B订单确认状态
     * @param request
     * @return
     */
    boolean updateOrderCustomerConfirmStatus(UpdateCustomerConfirmStatusRequest request);

    /**
     * 修改订单开票信息
     * @param order
     * @return
     */
    Boolean updateOrderInvoiceInfo(UpdateOrderInvoiceInfoRequest order);

    /**
     * 根据类型获取订单附件列表
     *
     * @param orderId 订单ID
     * @param attachmentTypeEnum 附件类型枚举
     * @return
     */
    List<OrderAttachmentDTO> listOrderAttachmentsByType(Long orderId, OrderAttachmentTypeEnum attachmentTypeEnum);

    /**
     * 订单反审核
     * @param updateOrderNotAuditRequest
     * @return
     */
    @Deprecated
    Result<OrderModifyAuditChangeBO> modifyOrderNotAudit(UpdateOrderNotAuditRequest updateOrderNotAuditRequest);

    /**
     * 订单反审核版本V2
     * @param modifyOrderNotAuditRequest
     * @return
     */
    Result<OrderModifyAuditChangeBO> modifyOrderNotAudit_v2(ModifyOrderNotAuditRequest modifyOrderNotAuditRequest);

    /**
     * 根据订单状态获取卖家订单数量
     * @param orderStatus 订单状态
     * @param eidList 企业id
     * @param type 1：以岭管理员 2：以岭本部非管理员 3：非以岭人员
     * @param userId 当前登录用户
     * @return
     */
    Integer getSellerOrderNumberByStatus(Integer orderStatus,List<Long> eidList,Integer type,Long userId);

    /**
     * 根据订单状态获取买家订单数量
     * @param orderStatus 订单状态
     * @param eidList 买家eid集合
     * @return
     */
    Integer getBuyerOrderNumberByStatus(Integer orderStatus,List<Long> eidList);

    /**
     * 根据支付状态获取买家订单数量
     * @param paymentStatus 支付状态
     * @param eidList 买家eid集合
     * @return
     */
    Integer getBuyerOrderNumberByPaymentStatus(Integer paymentStatus,List<Long> eidList);

    /**
     * 根据应收单号获取订单
     * @param erpReceivableNo
     * @return
     */
    OrderDTO getOrderByErpReceivableNo(String erpReceivableNo);

    /**
     * 二级商直接收货
     * @param eidList 以岭直采企业
     * @return
     */
    List<Long> secondBusinessAutomaticReceive(List<Long> eidList);

    /**
     * 查询代客下单，已转发，客户24小时未确认的订单
     * @param orderNos 订单编号
     * @return
     */
    List<OrderDTO> listCustomerNotConfirmByOrderNos(List<String> orderNos);

    /**
     * 根据买家企业id统计累计订单金额
     * @param buyerEid
     * @return
     */
    BigDecimal getTotalAmountByBuyerEid(Long buyerEid);

    /**
     *统计前天收货订单
     * @param orderTypeList 订单来源
     * @param date
     * @return
     */
    List<Long> countReceiveOrder(List<Integer> orderTypeList, String date);


    /**
     * 更新订单备注
     * @param remarkRequest
     * @return
     */
    Boolean updateOrderRemark(UpdateOrderRemarkRequest remarkRequest);

    /**
     * 修改客户内码
     * @param buyerEid 采购商eid
     * @param sellerEid 配送商eid
     * @param customerErpCode 客户内码
     * @return
     */
    Boolean updateCustomerErpCode(Long buyerEid,Long sellerEid,String customerErpCode);


    /**
     * 大屏订单发货数据统计
     * @return
     */
    List<OrderDeliveryReportCountDTO> getOrderDeliveryReportCount(QueryOrderDeliveryReportRequest request);

    /**
     * 获取进行中的订单和退货单
     * @param buyerEid
     */
    CountContinueOrderAndReturnDTO getCountContinueOrderAndReturn(Long buyerEid);

    /**
     * 注销销售助手管理员用户时需要判断是否有未完成的订单
     */
    CountContinueOrderAndReturnDTO getCountContinueOrderAssistant(Long eid);

    /**
     * B2B确认是否有收货且是以岭品的订单
     * @param request
     * @return
     */
    Boolean verificationReceiveB2BOrder(QueryAssistantOrderFirstRequest request);

    /**
     * 获取销售助手下单收货第一单
     * @param request
     * @return
     */
    OrderDTO getAssistantReceiveFirstOrder(QueryAssistantOrderFirstRequest request);

    /**
     * 获取一段时间内的订单数据
     * @return
     */
    Map<Long, List<OrderDTO>>  getTimeIntervalTypeOrder(QueryOrderTimeIntervalRequest request);

    /**
     * 超过hour小时没有选择支付方式的订单自动取消
     * @param hour
     * @return
     */
    List<OrderDTO> overTimeCancelExpectOrder(Integer hour);
    /**
     * 会员获取总折扣优惠金额
     * @param request
     * @return
     */
    BigDecimal getMemberOrderAllDiscountAmount(QueryDiscountOrderBuyerEidPagRequest request);

    /**
     * 会员获取总折扣优惠订单信息
     * @param request
     * @return
     */
    Page<OrderMemberDiscountDTO> getMemberOrderDiscountInfo(QueryDiscountOrderBuyerEidPagRequest request);


    /**
     * 根据条件查询用户自定义购买数量
     * @param queryUserBuyNumberRequest
     * @return
     */
    Long getUserBuyNumber(QueryUserBuyNumberRequest queryUserBuyNumberRequest);

    /**
     * 根据条件查询用户自定义购买数量
     * @param queryUserBuyNumberRequests
     * @return
     */
    Map<Long,Long> getUserBuyNumber(List<QueryUserBuyNumberRequest> queryUserBuyNumberRequests);

    /**
     * 前端获取
     * @param request
     * @return
     */
    Page<OrderDTO> getPOPWebOrderListPage(OrderPOPWebListPageRequest request);

    /**
     * 根据商品id获取5天内的发货
     * @param goodsIdList 商品id
     * @return
     */
    Boolean getDeliveryFiveDayTips(List<Long> goodsIdList);

    /**
     * 隐藏B2B订单
     * @param id
     * @return
     */
    Boolean hideB2BOrder(Long id);

    /**
     * 商家后台企业订单查询
     * @param request
     * @return
     */
    Page<OrderEnterpriseDTO> getOrderEnterprisePage(QueryOrderPageRequest request);

    /**
     * 修改物流信息
     * @param request
     * @return
     */
    Boolean updateOrderTransportInfo(UpdateTransportInfoRequest request);

    /**
     * 获取B2B商家后台数量
     * @param request
     * @return
     */
    Long getB2BAdminNumber (QueryOrderPageRequest request);

    /**
     * 根据订单类型统计商品销量
     * @param request
     * @return
     */
    List<OrderTypeGoodsQuantityDTO> getCountOrderTypeQuantity(QueryOrderPageRequest request);
}
