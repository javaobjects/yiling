package com.yiling.order.order.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderAndDetailedAllInfoDTO;
import com.yiling.order.order.dto.OrderAssistantConfirmQuantityAmountDTO;
import com.yiling.order.order.dto.OrderB2BPaymentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryReportCountDTO;
import com.yiling.order.order.dto.OrderEnterpriseDTO;
import com.yiling.order.order.dto.OrderExpectExportDTO;
import com.yiling.order.order.dto.OrderExportReportDTO;
import com.yiling.order.order.dto.OrderHistoryGoodsDTO;
import com.yiling.order.order.dto.OrderMemberDiscountDTO;
import com.yiling.order.order.dto.OrderNumberDTO;
import com.yiling.order.order.dto.OrderSumDTO;
import com.yiling.order.order.dto.OrderTypeGoodsQuantityDTO;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.dto.request.GoodDetailSumQueryPageRequest;
import com.yiling.order.order.dto.request.OrderB2BPaymentRequest;
import com.yiling.order.order.dto.request.OrderPOPWebListPageRequest;
import com.yiling.order.order.dto.request.OrderSumQueryPageRequest;
import com.yiling.order.order.dto.request.OrderSumQueryRequest;
import com.yiling.order.order.dto.request.QueryAssistanOrderPageRequest;
import com.yiling.order.order.dto.request.QueryAssistantConfirmOrderPageRequest;
import com.yiling.order.order.dto.request.QueryAssistantOrderFirstRequest;
import com.yiling.order.order.dto.request.QueryB2BSettlementPageReuest;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.dto.request.QueryBuyerOrderPageRequest;
import com.yiling.order.order.dto.request.QueryDiscountOrderBuyerEidPagRequest;
import com.yiling.order.order.dto.request.QueryInvoicePageRequest;
import com.yiling.order.order.dto.request.QueryOrderDeliveryReportRequest;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.dto.request.QueryOrderExportReportPageRequest;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveListInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderCashDiscountAmountRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentMethodRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.dto.request.UpdateOrderRemarkRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.OrderStatusEnum;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
public interface OrderService extends BaseService<OrderDO> {

    /**
     * 创建订单
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
    List<OrderDO> listByOrderNos(List<String> orderNos);

    /**
     * 根据类型查询出对应订单数量
     *
     * @param type
     * @param eids
     * @param userId
     * @param orderType
     * @return
     */
    OrderNumberDTO getOrderNumber(Integer type, List<Long> eids, Long userId,Integer orderType,Long departmentId);

    /**
     * 中台获取数量和金额
     * @param request
     * @return
     */
    OrderNumberDTO getCentreOrderNumberCount(QueryBackOrderInfoRequest request);


    /**
     * 获取订单信息
     *
     * @param request
     * @return
     */
    Page<OrderDTO> getOrderPage(QueryOrderPageRequest request);

    /**
     * B2B采购订单
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
     * 获取订单信息(运营后台)
     *
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
     * 根据id获取商品基础
     *
     * @param id
     * @return
     */
    OrderDTO getOrderInfo(Long id);

    /**
     * 根据订单编号查询订单信息
     * @param orderNo
     * @return
     */
    OrderDTO getOrderInfo(String orderNo);

    /**
     * 取消订单，判断条件
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean cancel(Long orderId, Long opUserId,Boolean isBeforeUpdate);


    /**
     * 发货
     *
     * @param request
     * @return
     */
    Boolean delivery(SaveOrderDeliveryListInfoRequest request);

    /**
     * 分摊现折金额
     *
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
     *
     * @param request
     * @return
     */
    Boolean receive(SaveOrderReceiveListInfoRequest request);

    /**
     * 获取3个月前订单商品POP首页
     *
     * @param eid
     * @return
     */
    List<Long> getOrderGoodsByEid(Long eid);


    /**
     * 获取3个月前订单商品POP后端
     *
     * @param eid
     * @return
     */
    List<OrderHistoryGoodsDTO> getOrderGoodsByEidPOPAdmin(Long eid);

    /**
     * 更新支付状态
     *
     * @param request
     * @return
     */
    boolean updatePaymentStatus(UpdateOrderPaymentStatusRequest request);

    /**
     * 修改支付方式以及金额
     * @param request
     * @return
     */
    boolean updatePaymentPayMethod(UpdateOrderPaymentMethodRequest request);

    /**
     * 根据票折单号获取订单信息
     *
     * @param ticketDiscountNos
     * @return
     */
    Map<String, List<OrderDTO>> getOrderByTicketDiscountNo(List<String> ticketDiscountNos);

    /**
     * 发票管理详情查询
     *
     * @param request
     * @return
     */
    Page<OrderDTO> getOrderInvoiceInfoPage(QueryInvoicePageRequest request);


    /**
     * 根据采购商信息和下单时间获取已发货订单信息
     *
     * @param request
     * @return
     */
    List<OrderAndDetailedAllInfoDTO> getOrderAllInfoByBuyerEidAndCreateTime(QueryOrderUseAgreementRequest request);

    /**
     * 更新订单现折金额
     *
     * @param request
     * @return
     */
    boolean updateOrderCashDiscountAmount(UpdateOrderCashDiscountAmountRequest request);


    /**
     * 修改回执单信息
     *
     * @param orderId            订单id
     * @param receiveReceiptList 回执单信息
     * @param opUserId           操作人
     * @return
     */
    Boolean updateReceiveReceipt(Long orderId, List<String> receiveReceiptList, Long opUserId);


    /**
     * 修改订单状态
     *
     * @param id             订单ID
     * @param originalStatus 原始状态
     * @param newStatus      新状态
     * @param opUserId       操作人ID
     * @param remark         备注
     * @return
     */
    boolean updateOrderStatus(Long id, OrderStatusEnum originalStatus, OrderStatusEnum newStatus, Long opUserId, String remark);

    /**
     * 修改订单客户确认状态
     *
     * @param id 订单ID
     * @param originalStatus 原始状态
     * @param newStatus 新状态
     * @param opUserId 操作人ID
     * @param remark 备注
     * @return
     */
    boolean updateOrderCustomerConfirmStatus(Long id, CustomerConfirmEnum originalStatus, CustomerConfirmEnum newStatus, Long opUserId, String remark);


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
     * 二级商5天后直接收货
     * @param eidList 以岭直采企业
     * @return
     */
    List<Long> secondBusinessAutomaticReceive(List<Long> eidList);

    /**
     * 预订单导出
     * @param request
     * @return
     */
    List<OrderExpectExportDTO> orderExpectExport(QueryOrderExpectPageRequest request);

    /**
     * 导出报表接口
     * @param request
     * @return
     */
    OrderExportReportDTO orderExportReport(QueryOrderExportReportPageRequest request);


    /**
     * 查询代客下单，已转发，客户24小时未确认的订单
     * @param orderNos 订单编号
     * @return
     */
    List<OrderDO> listCustomerNotConfirmByOrderNos(List<String> orderNos);


    /**
     * 根据下单人或者商务联系人统计金额(返回下单总金额，下单数量,支付总金额)
     * @param queryRequest 查询条件
     * @param type 1，根据下单人查询，2，根据商务联系
     * @return
     */
    OrderSumDTO sumOrderReportByUserIdList(OrderSumQueryRequest queryRequest,Integer type);

    /**
     * 通过买计订单金额
     * @param buyerEid 企业ID
     * @param orderStatusList 订单状态
     * @return
     */
    OrderSumDTO sumOrderReportByBuyerEid(Long buyerEid,List<Integer> orderStatusList);

    /**
     * 查询下单人的订单信息
     * @param queryRequest 下单人信息
     * @return
     */
    List<OrderDO> selectOrderListByCreateUserId(OrderSumQueryRequest queryRequest);

    /**
     * 查询下单人的订单信息分页
     * @param queryRequest 下单人信息
     * @return
     */
    Page<OrderDO> selectOrderPageByCreateUserId(OrderSumQueryPageRequest queryRequest);

    /**
     * 查询下单人的订单信息
     * @param queryRequest 商务联系人信息
     * @return
     */
    List<OrderDO> selectOrderListByContacterId(OrderSumQueryRequest queryRequest);

    /**
     * 查询商务联系人的订单信息分页
     * @param queryRequest 商务联系人信息
     * @return
     */
    Page<OrderDO> selectOrderPageByContacterId(OrderSumQueryPageRequest queryRequest);

    /**
     * 通过配送商商品ID查询已发货的订单
     * @param distributorGoodsId 配送商商品ID
     * @return
     */
    List<OrderDO> selectReceiveOrderByDistributorGoodId(Long distributorGoodsId);

    /**
     * 通过以岭商品ID查询订单信息
     * @param goodsId 以岭商品ID
     * @param buyerEidList 买家企业ID
     * @return
     */
    List<OrderDO> selectReceiveOrderByGoodId(Long goodsId,List<Long> buyerEidList);

    /**
     * 通过以岭商品ID查询订单信息(分页)
     * @param goodDetailPageRequest 商品ID
     * @return
     */
    Page<OrderDO> pageReceiveOrderByGoodId(GoodDetailSumQueryPageRequest goodDetailPageRequest) ;

    /**
     * 根据企业ID查询卖家订单信息
     * @param request 买家信息
     * @return
     */
    Page<OrderDO> selectBuyerOrdersByEids(QueryBuyerOrderPageRequest request);

    /**
     * 获取订单账期列表
     * @param request
     * @return
     */
    Page<OrderB2BPaymentDTO> getOrderB2BPaymentList(OrderB2BPaymentRequest request);


    /**
     * 查询B2B结算订单
     * @param queryB2BSettlementPageReuest
     * @return
     */
    Page<OrderDO> pageB2bSettlementList(QueryB2BSettlementPageReuest queryB2BSettlementPageReuest);

    /**
     * 根据买家企业id统计累计订单金额
     * @param buyerEid
     * @return
     */
    BigDecimal getTotalAmountByBuyerEid(Long buyerEid);

    /**
     * 统计前天收货订单
     * @param orderTypeList 订单类型
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
    List<OrderDeliveryReportCountDTO>getOrderDeliveryReportCount(QueryOrderDeliveryReportRequest request);

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
    OrderDO getAssistantReceiveFirstOrder(QueryAssistantOrderFirstRequest request);

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
     * POP前台获取采购订单信息
     * @param request
     * @return
     */
    Page<OrderDTO> getPOPWebOrderListPage(OrderPOPWebListPageRequest request);

    /**
     * 商家后台企业订单查询
     * @param request
     * @return
     */
    Page<OrderEnterpriseDTO> getOrderEnterprisePage(QueryOrderPageRequest request);

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
    List<OrderTypeGoodsQuantityDTO> getCountOrderTypeQuantity( QueryOrderPageRequest request);

}
