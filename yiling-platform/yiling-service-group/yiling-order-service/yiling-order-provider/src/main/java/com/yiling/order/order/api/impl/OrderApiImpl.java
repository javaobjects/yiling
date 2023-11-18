package com.yiling.order.order.api.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
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
import com.yiling.order.order.dto.request.BatchQueryUserBuyNumberRequest;
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
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.service.OrderAttachmentService;
import com.yiling.order.order.service.OrderAuditService;
import com.yiling.order.order.service.OrderDetailService;
import com.yiling.order.order.service.OrderModifyAuditService;
import com.yiling.order.order.service.OrderReturnService;
import com.yiling.order.order.service.OrderService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/18
 */
@DubboService
@Slf4j
public class OrderApiImpl implements OrderApi {

    @Autowired
    private OrderService           orderService;
    @Autowired
    private OrderAuditService      orderAuditService;
    @Autowired
    private OrderAttachmentService orderAttachmentService;
    @Autowired
    private OrderModifyAuditService modifyOrderAuditService;
    @Autowired
    private OrderDetailService      orderDetailService;
    @Autowired
    private OrderReturnService orderReturnService;


    @Override
    public boolean create(List<CreateOrderRequest> createOrderRequestList) {
        return orderService.create(createOrderRequestList);
    }

    @Override
    public List<OrderDTO> listByOrderNos(List<String> orderNos) {
        return PojoUtils.map(orderService.listByOrderNos(orderNos), OrderDTO.class);
    }

    @Override
    public List<OrderDTO> listByIds(List<Long> ids) {
        return PojoUtils.map(orderService.listByIds(ids), OrderDTO.class);
    }

    /**
     * 根据类型判断使用eids或者userId
     * @param type
     * @param eids
     * @param userId
     * @return
     */
    @Override
    public OrderNumberDTO getOrderNumber(Integer type,List<Long> eids,Long userId,Integer orderType,Long departmentId) {
        return orderService.getOrderNumber(type,eids,userId,orderType,departmentId);
    }

    /**
     * 获取中台
     *
     * @param request
     * @return
     */
    @Override
    public OrderNumberDTO getCentreOrderNumberCount(QueryBackOrderInfoRequest request) {
        return orderService.getCentreOrderNumberCount(request);
    }

    /**
     * 获取订单列表信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getOrderPage(QueryOrderPageRequest request) {
        Page<OrderDTO> orderPage = orderService.getOrderPage(request);
        return orderPage;
    }

    /**
     * B2B采购订单
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getB2BPurchaseOrderPage(QueryOrderPageRequest request) {
        Page<OrderDTO> orderPage = orderService.getB2BPurchaseOrderPage(request);
        return orderPage;
    }


    /**
     * 销售订单信息查询
     * @param queryAssistanOrderPageRequest 查询订单参数
     * @return
     */
    @Override
    public Page<OrderDTO> getSaOrderPage(QueryAssistanOrderPageRequest queryAssistanOrderPageRequest) {

        return orderService.getSaOrderPage(queryAssistanOrderPageRequest);
    }

    /**
     * 获取订单信息(运营后台)OrderB2BServiceImpl
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getBackOrderPage(QueryBackOrderInfoRequest request) {
        return orderService.getBackOrderPage(request);
    }

    @Override
    public Page<OrderDTO> getAssistantConfirmOrderPage(QueryAssistantConfirmOrderPageRequest request) {
        return orderService.getAssistantConfirmOrderPage(request);
    }

    @Override
    public OrderAssistantConfirmQuantityAmountDTO getAssistantConfirmQuantityAmount(QueryAssistantConfirmOrderPageRequest request) {
        return orderService.getAssistantConfirmQuantityAmount(request);
    }

    /**
     * 获取订单基础信息
     *
     * @param id
     * @return
     */
    @Override
    public OrderDTO getOrderInfo(Long id) {

        return orderService.getOrderInfo(id);
    }
    /**
     * 获取订单基础信息
     *
     * @param orderNo
     * @return
     */
    @Override
    public OrderDTO selectByOrderNo(String orderNo) {
        QueryWrapper<OrderDO> orderWrapper = new QueryWrapper<>();
        orderWrapper.lambda().eq(OrderDO::getOrderNo, orderNo);
        return BeanUtil.copyProperties(orderService.getOne(orderWrapper),OrderDTO.class);
    }

    /**
     * 取消订单，先获取订单
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    public Boolean cancel(Long orderId,Long opUserId,Boolean isBeforeUpdate) {

        return orderService.cancel(orderId,opUserId,isBeforeUpdate);
    }

    /**
     * 发货
     *
     * @param request
     * @return
     */
    @Override
    public Boolean delivery(SaveOrderDeliveryListInfoRequest request) {
        return orderService.delivery(request);
    }

    /**
     * 分摊现折金额
     *
     * @return
     */
    @Override
    public Boolean shareAllDeliveryErpCashAmount() {
        return orderService.shareAllDeliveryErpCashAmount();
    }

    /**
     * 关闭发货
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    public Boolean closeDelivery(Long orderId,Long opUserId) {

        return orderService.closeDelivery(orderId,opUserId);
    }

    /**
     * 收货
     *
     * @param request
     * @return
     */
    @Override
    public Boolean receive(SaveOrderReceiveListInfoRequest request) {
        return orderService.receive(request);
    }

    /**
     * 获取3个月前订单商品
     * @param eid
     * @return
     */
    @Override
    public List<Long> getOrderGoodsByEid(Long eid){
        return orderService.getOrderGoodsByEid(eid);
    }

    /**
     * 获取3个月前订单商品POP后端
     *
     * @param eid
     * @return
     */
    @Override
    public List<OrderHistoryGoodsDTO> getOrderGoodsByEidPOPAdmin(Long eid) {
        return orderService.getOrderGoodsByEidPOPAdmin(eid);
    }

    @Override
    public boolean updatePaymentStatus(UpdateOrderPaymentStatusRequest request) {
        return orderService.updatePaymentStatus(request);
    }

    @Override
    public boolean updatePaymentPayMethod(UpdateOrderPaymentMethodRequest request) {

        return orderService.updatePaymentPayMethod(request);
    }

    /**
     * 根据票折单号获取订单信息
     *
     * @param ticketDiscountNos
     * @return
     */
    @Override
    public Map<String, List<OrderDTO>> getOrderByTicketDiscountNo(List<String> ticketDiscountNos) {
        return orderService.getOrderByTicketDiscountNo(ticketDiscountNos);
    }

    /**
     * 发票管理详情
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getOrderInvoiceInfoPage(QueryInvoicePageRequest request) {

        return orderService.getOrderInvoiceInfoPage(request);
    }

    /**
     * 根据采购商信息和下单时间获取已发货订单信息
     *
     * @param request
     * @return
     */
    @Override
    public List<OrderAndDetailedAllInfoDTO> getOrderAllInfoByBuyerEidAndCreateTime(QueryOrderUseAgreementRequest request) {
        return orderService.getOrderAllInfoByBuyerEidAndCreateTime(request);
    }


    /**
     * 获取除取消订单外的所有审核订单数量
     * @param sellerEidList
     * @param departmentId
     * @param departmentType
     * @return
     */
    @Override
    public OrderManageStatusNumberDTO getOrderReviewStatusNumber(List<Long> sellerEidList,Long departmentId,Integer departmentType) {
        return orderAuditService.getOrderReviewStatusNumber(sellerEidList,departmentId,departmentType);
    }

    /**
     * 订单审核列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getOrderManagePage(QueryOrderManagePageRequest request) {

        return orderAuditService.getOrderManagePage(request);
    }

    @Override
    public boolean updateOrderCashDiscountAmount(UpdateOrderCashDiscountAmountRequest request) {
        return orderService.updateOrderCashDiscountAmount(request);
    }


    /**
     * 修改回执单信息
     * @param orderId 订单id
     * @param receiveReceiptList 回执单信息
     * @param opUserId 操作人
     * @return
     */
    @Override
    public Boolean updateReceiveReceipt(Long orderId, List<String> receiveReceiptList,Long opUserId) {
        return orderService.updateReceiveReceipt(orderId,receiveReceiptList,opUserId);
    }

    /**
     * 获取预期订单列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getOrderExpectInfo(QueryOrderExpectPageRequest request) {

        return orderAuditService.getOrderExpectInfo(request);
    }


    /**
     * 预订单取消
     * @param orderId 订单id
     * @param opUserId 操作人
     * @return
     */
    @Override
    public Boolean cancelOrderExpect(Long orderId, Long opUserId) {
        return orderAuditService.cancelOrderExpect(orderId,opUserId);
    }

    @Override
    public boolean updateAuditStatus(Long id, OrderAuditStatusEnum originalStatus, OrderAuditStatusEnum newStatus, Long opUserId, String remark) {
        return orderAuditService.updateAuditStatus(id, originalStatus, newStatus, opUserId, remark);
    }

    @Override
    public boolean updateOrderStatus(Long id, OrderStatusEnum originalStatus, OrderStatusEnum newStatus, Long opUserId, String remark) {
        return orderService.updateOrderStatus(id, originalStatus, newStatus, opUserId, remark);
    }



    /**
     * 修改订单开票信息
     * @param order
     * @return
     */
    @Override
    public Boolean updateOrderInvoiceInfo(UpdateOrderInvoiceInfoRequest order) {
        OrderDO result = PojoUtils.map(order, OrderDO.class);
        return orderService.updateById(result);
    }

    @Override
    public List<OrderAttachmentDTO> listOrderAttachmentsByType(Long orderId, OrderAttachmentTypeEnum attachmentTypeEnum) {
        return PojoUtils.map(orderAttachmentService.listByOrderId(orderId, attachmentTypeEnum), OrderAttachmentDTO.class);
    }

    /**
     * 订单反审核
     * @param updateOrderNotAuditRequest
     * @return
     */
    @Override
    public Result<OrderModifyAuditChangeBO> modifyOrderNotAudit(UpdateOrderNotAuditRequest updateOrderNotAuditRequest) {

        return modifyOrderAuditService.modifyOrderNotAudit(updateOrderNotAuditRequest);
    }


    @Override
    public Result<OrderModifyAuditChangeBO> modifyOrderNotAudit_v2(ModifyOrderNotAuditRequest modifyOrderNotAuditRequest) {

        return modifyOrderAuditService.modifyOrderNotAudit_v2(modifyOrderNotAuditRequest);
    }

    /**
     * 根据订单状态获取卖家订单数量
     * @param orderStatus 订单状态
     * @param eidList 企业id
     * @param type 1：以岭管理员 2：以岭本部非管理员 3：非以岭人员
     * @param userId 当前登录用户
     * @return
     */
    @Override
    public Integer getSellerOrderNumberByStatus(Integer orderStatus, List<Long> eidList,Integer type,Long userId) {
        return orderService.getSellerOrderNumberByStatus(orderStatus,eidList,type,userId);
    }

    /**
     * 根据订单状态获取买家订单数量
     *
     * @param orderStatus 订单状态
     * @param eidList     买家eid集合
     * @return
     */
    @Override
    public Integer getBuyerOrderNumberByStatus(Integer orderStatus, List<Long> eidList) {
        return orderService.getBuyerOrderNumberByStatus(orderStatus,eidList);
    }

    /**
     * 根据支付状态获取买家订单数量
     *
     * @param paymentStatus 支付状态
     * @param eidList       买家eid集合
     * @return
     */
    @Override
    public Integer getBuyerOrderNumberByPaymentStatus(Integer paymentStatus, List<Long> eidList) {
        return orderService.getBuyerOrderNumberByPaymentStatus(paymentStatus,eidList);
    }

    /**
     * 根据应收单号获取订单
     *
     * @param erpReceivableNo 应收单号
     * @return
     */
    @Override
    public OrderDTO getOrderByErpReceivableNo(String erpReceivableNo) {
        return orderService.getOrderByErpReceivableNo(erpReceivableNo);
    }

    /**
     * 二级商直接收货
     *
     * @param eidList 以岭直采企业
     * @return
     */
    @Override
    public List<Long> secondBusinessAutomaticReceive(List<Long> eidList) {
        return orderService.secondBusinessAutomaticReceive(eidList);
    }

    /**
     * 修改订单客户确认状态
     * @param request
     * @return
     */
    @Override
    public boolean updateOrderCustomerConfirmStatus(UpdateCustomerConfirmStatusRequest request) {
        if (CollectionUtil.isEmpty(request.getConfirmRequestList())) {
            return true;
        }
        request.getConfirmRequestList().forEach(t -> {
            orderService.updateOrderCustomerConfirmStatus(t.getOrderId(),t.getOriginalStatus(),t.getNewStatus(),t.getUpdateUserId(),t.getRemark());
        });

        return true;
    }

    /**
     * 查询代客下单，已转发，客户24小时未确认的订单
     * @param orderNos 订单编号
     * @return
     */
    @Override
    public List<OrderDTO> listCustomerNotConfirmByOrderNos(List<String> orderNos) {

        List<OrderDO> list = orderService.listCustomerNotConfirmByOrderNos(orderNos);

        if (CollectionUtil.isEmpty(list)) {

            return  Collections.emptyList();
        }

       return PojoUtils.map(list,OrderDTO.class);
    }

    @Override
    public BigDecimal getTotalAmountByBuyerEid(Long buyerEid) {
        return orderService.getTotalAmountByBuyerEid(buyerEid);
    }

    /**
     * 统计前天收货订单
     *
     * @param orderTypeList 订单来源
     */
    @Override
    public List<Long> countReceiveOrder(List<Integer> orderTypeList, String date) {
        return orderService.countReceiveOrder(orderTypeList,date);
    }

    @Override
    public Boolean updateOrderRemark(UpdateOrderRemarkRequest remarkRequest) {

        return orderService.updateOrderRemark(remarkRequest);
    }

    @Override
    public Boolean updateCustomerErpCode(Long buyerEid, Long sellerEid, String customerErpCode) {
        return orderService.updateCustomerErpCode(buyerEid,sellerEid,customerErpCode);
    }

    @Override
    public List<OrderDeliveryReportCountDTO> getOrderDeliveryReportCount(QueryOrderDeliveryReportRequest request) {
        return orderService.getOrderDeliveryReportCount(request);
    }

    @Override
    public CountContinueOrderAndReturnDTO getCountContinueOrderAndReturn(Long buyerEid) {
        CountContinueOrderAndReturnDTO result = new CountContinueOrderAndReturnDTO();
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().ge(OrderDO :: getOrderStatus,OrderStatusEnum.UNDELIVERED.getCode())
                .le(OrderDO :: getOrderStatus,OrderStatusEnum.DELIVERED.getCode())
                .eq(OrderDO :: getBuyerEid,buyerEid);
        Integer count = orderService.count(wrapper);

        QueryWrapper<OrderReturnDO> returnWrapper = new QueryWrapper<OrderReturnDO>();
        returnWrapper.lambda().eq(OrderReturnDO ::getBuyerEid,buyerEid)
                .eq(OrderReturnDO :: getReturnStatus, OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode())
                .groupBy(OrderReturnDO :: getOrderId);
        Integer returnCount = orderReturnService.count(returnWrapper);
        result.setOrderContinueCount(count);
        result.setOrderReturnContinueCount(returnCount);
        return result;
    }

    @Override
    public CountContinueOrderAndReturnDTO getCountContinueOrderAssistant(Long eid) {
        CountContinueOrderAndReturnDTO result = new CountContinueOrderAndReturnDTO();
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().ge(OrderDO :: getOrderStatus,OrderStatusEnum.UNDELIVERED.getCode())
                .le(OrderDO :: getOrderStatus,OrderStatusEnum.DELIVERED.getCode())
                .eq(OrderDO :: getBuyerEid,eid);
        Integer count = orderService.count(wrapper);

        QueryWrapper<OrderDO> sellerWrapper = new QueryWrapper<>();
        sellerWrapper.lambda().ge(OrderDO :: getOrderStatus,OrderStatusEnum.UNDELIVERED.getCode())
                .le(OrderDO :: getOrderStatus,OrderStatusEnum.DELIVERED.getCode())
                .eq(OrderDO :: getSellerEid,eid);
        Integer sellerCount = orderService.count(sellerWrapper);

        QueryWrapper<OrderReturnDO> returnWrapper = new QueryWrapper<OrderReturnDO>();
        returnWrapper.lambda().eq(OrderReturnDO ::getBuyerEid,eid)
                .eq(OrderReturnDO :: getReturnStatus, OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode())
                .groupBy(OrderReturnDO :: getOrderId);
        Integer returnCount = orderReturnService.count(returnWrapper);
        result.setOrderContinueCount(count);
        result.setOrderReturnContinueCount(returnCount);
        result.setSellerOrderContinueCount(sellerCount);
        return result;
    }

    @Override
    public Boolean verificationReceiveB2BOrder(QueryAssistantOrderFirstRequest request) {
        return orderService.verificationReceiveB2BOrder(request);
    }

    @Override
    public OrderDTO getAssistantReceiveFirstOrder(QueryAssistantOrderFirstRequest request) {
        OrderDO order = orderService.getAssistantReceiveFirstOrder(request);

        return PojoUtils.map(order,OrderDTO.class);
    }

    @Override
    public Map<Long, List<OrderDTO>>  getTimeIntervalTypeOrder(QueryOrderTimeIntervalRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();

        if (Objects.nonNull(request.getStartCreateTime())) {
            wrapper.lambda().ge(OrderDO::getCreateTime,request.getStartCreateTime());
        }
        if (Objects.nonNull(request.getEndCreateTime())) {
            wrapper.lambda().le(OrderDO::getCreateTime, request.getEndCreateTime());
        }
        if(request.getOrderSource() != null && request.getOrderSource() != 0){
            wrapper.lambda().eq(OrderDO :: getOrderSource,request.getOrderSource());
        }
        if(request.getOrderType() != null && request.getOrderType() != 0){
            wrapper.lambda().eq(OrderDO :: getOrderType,request.getOrderType());
        }
        if(CollectionUtil.isNotEmpty(request.getOrderStatusList())){
            wrapper.lambda().in(OrderDO :: getOrderStatus,request.getOrderStatusList());
        }
        if(CollectionUtil.isNotEmpty(request.getPaymentMethodList())){
            wrapper.lambda().in(OrderDO :: getPaymentMethod,request.getPaymentMethodList());
        }

        if(CollectionUtil.isNotEmpty(request.getBuyerEidList())){
            wrapper.lambda().in(OrderDO :: getBuyerEid,request.getBuyerEidList());
        }

        if(CollectionUtil.isNotEmpty(request.getSellerEidList())){
            wrapper.lambda().in(OrderDO :: getSellerEid,request.getSellerEidList());
        }
        if(request.getCustomerConfirmStatus() != null && request.getCustomerConfirmStatus()!=0 ){
            wrapper.lambda().eq(OrderDO :: getCustomerConfirmStatus,request.getCustomerConfirmStatus());

        }

        List<OrderDO> list = orderService.list(wrapper);
        List<OrderDTO> orderList = PojoUtils.map(list, OrderDTO.class);
        Map<Long, List<OrderDTO>> result = new HashMap<>();
        if(CollectionUtil.isNotEmpty(orderList)){
            result = orderList.stream().collect(Collectors.groupingBy(s -> s.getBuyerEid()));
        }
        return result;
    }

    @Override
    public List<OrderDTO> overTimeCancelExpectOrder(Integer hour) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();

        wrapper.lambda().eq(OrderDO :: getPaymentMethod,0)
                .eq(OrderDO :: getOrderSource, OrderSourceEnum.POP_PC.getCode())
                .eq(OrderDO :: getAuditStatus,OrderAuditStatusEnum.UNSUBMIT.getCode())
                .ne(OrderDO :: getOrderStatus,OrderStatusEnum.CANCELED.getCode())
                .le(OrderDO::getCreateTime, DateUtil.offsetHour(new Date(), -hour))
                .ge(OrderDO::getCreateTime,DateUtil.parse("2022-10-13 00:00:00"));
        return PojoUtils.map(orderService.list(wrapper),OrderDTO.class);
    }

    @Override
    public BigDecimal getMemberOrderAllDiscountAmount(QueryDiscountOrderBuyerEidPagRequest request) {
        return orderService.getMemberOrderAllDiscountAmount(request);
    }

    @Override
    public Page<OrderMemberDiscountDTO> getMemberOrderDiscountInfo(QueryDiscountOrderBuyerEidPagRequest request) {
        return orderService.getMemberOrderDiscountInfo(request);
    }

    @Override
    public Long getUserBuyNumber(QueryUserBuyNumberRequest queryUserBuyNumberRequest) {
        queryUserBuyNumberRequest.convert();

        if (log.isDebugEnabled()) {

            log.debug("QueryUserBuyNumberRequest:{}",queryUserBuyNumberRequest);
        }

        return orderDetailService.getUserBuyNumber(queryUserBuyNumberRequest);
    }

    @Override
    public Map<Long, Long> getUserBuyNumber(List<QueryUserBuyNumberRequest> queryUserBuyNumberRequests) {

        return orderDetailService.getUserBuyNumber(queryUserBuyNumberRequests);
    }

    @Override
    public Page<OrderDTO> getPOPWebOrderListPage(OrderPOPWebListPageRequest request) {
        return orderService.getPOPWebOrderListPage(request);
    }

    @Override
    public Boolean getDeliveryFiveDayTips(List<Long> goodsIdList) {
        return orderService.getDeliveryFiveDayTips(goodsIdList);
    }

    @Override
    public Boolean hideB2BOrder(Long id) {

        return orderService.hideB2BOrder(id);
    }

    @Override
    public Page<OrderEnterpriseDTO> getOrderEnterprisePage(QueryOrderPageRequest request) {
        return orderService.getOrderEnterprisePage(request);
    }

    @Override
    public Boolean updateOrderTransportInfo(UpdateTransportInfoRequest request) {
        OrderDO order = PojoUtils.map(request, OrderDO.class);
        order.setId(request.getOrderId());
        return orderService.updateById(order);
    }

    @Override
    public Long getB2BAdminNumber(QueryOrderPageRequest request) {
        return orderService.getB2BAdminNumber(request);
    }



    @Override
    public List<OrderTypeGoodsQuantityDTO> getCountOrderTypeQuantity(QueryOrderPageRequest request) {
        return orderService.getCountOrderTypeQuantity(request);
    }
}