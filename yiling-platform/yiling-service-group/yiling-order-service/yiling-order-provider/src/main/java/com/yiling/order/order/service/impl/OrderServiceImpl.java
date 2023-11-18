package com.yiling.order.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderMapper;
import com.yiling.order.order.dto.CashDiscountAgreementInfoDTO;
import com.yiling.order.order.dto.OrderAndDetailedAllInfoDTO;
import com.yiling.order.order.dto.OrderAssistantConfirmQuantityAmountDTO;
import com.yiling.order.order.dto.OrderB2BPaymentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryReportCountDTO;
import com.yiling.order.order.dto.OrderDetailByAgreementDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderEnterpriseDTO;
import com.yiling.order.order.dto.OrderExpectExportDTO;
import com.yiling.order.order.dto.OrderExportReportDTO;
import com.yiling.order.order.dto.OrderExportReportDetailDTO;
import com.yiling.order.order.dto.OrderHistoryGoodsDTO;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.dto.OrderMemberDiscountDTO;
import com.yiling.order.order.dto.OrderNumberDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.OrderSumDTO;
import com.yiling.order.order.dto.OrderTypeGoodsQuantityDTO;
import com.yiling.order.order.dto.request.CreateOrderCouponUseRequest;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderGiftRequest;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.dto.request.DeliveryInfoRequest;
import com.yiling.order.order.dto.request.GoodDetailSumQueryPageRequest;
import com.yiling.order.order.dto.request.OrderB2BPaymentRequest;
import com.yiling.order.order.dto.request.OrderDetailCashDiscountInfoDTO;
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
import com.yiling.order.order.dto.request.ReceiveInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveListInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderCashDiscountAmountRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentMethodRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.dto.request.UpdateOrderRemarkRequest;
import com.yiling.order.order.entity.OrderAddressDO;
import com.yiling.order.order.entity.OrderCashDiscountDO;
import com.yiling.order.order.entity.OrderCouponUseDO;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderDeliveryDO;
import com.yiling.order.order.entity.OrderDeliveryErpDO;
import com.yiling.order.order.entity.OrderDeliveryReceivableDO;
import com.yiling.order.order.entity.OrderDetailCashDiscountDO;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.entity.OrderDetailDO;
import com.yiling.order.order.entity.OrderGiftDO;
import com.yiling.order.order.entity.OrderInvoiceApplyDO;
import com.yiling.order.order.entity.OrderLogDO;
import com.yiling.order.order.entity.OrderPromotionActivityDO;
import com.yiling.order.order.entity.OrderStatusLogDO;
import com.yiling.order.order.entity.PresaleOrderDO;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderHideFlagEnum;
import com.yiling.order.order.enums.OrderInvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderRangeTypeEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.SaOrderStatusEnum;
import com.yiling.order.order.service.OrderAddressService;
import com.yiling.order.order.service.OrderAttachmentService;
import com.yiling.order.order.service.OrderCashDiscountService;
import com.yiling.order.order.service.OrderCouponUseService;
import com.yiling.order.order.service.OrderDeliveryErpService;
import com.yiling.order.order.service.OrderDeliveryReceivableService;
import com.yiling.order.order.service.OrderDeliveryService;
import com.yiling.order.order.service.OrderDetailCashDiscountService;
import com.yiling.order.order.service.OrderDetailChangeService;
import com.yiling.order.order.service.OrderDetailService;
import com.yiling.order.order.service.OrderGiftService;
import com.yiling.order.order.service.OrderInvoiceApplyService;
import com.yiling.order.order.service.OrderInvoiceService;
import com.yiling.order.order.service.OrderLogService;
import com.yiling.order.order.service.OrderPromotionActivityService;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.order.service.OrderStatusLogService;
import com.yiling.order.order.service.PresaleOrderService;
import com.yiling.order.payment.enums.PaymentErrorCode;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Service
@Slf4j
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, OrderDO> implements OrderService {

    @Autowired
    private OrderDetailService             orderDetailService;
    @Autowired
    private OrderDetailChangeService       orderDetailChangeService;
    @Autowired
    private OrderLogService                orderLogService;
    @Autowired
    private OrderDeliveryService           orderDeliveryService;
    @Autowired
    private OrderStatusLogService          orderStatusLogService;
    @Autowired
    private OrderDetailCashDiscountService orderDetailCashDiscountService;
    @Autowired
    private OrderCashDiscountService       orderCashDiscountService;
    @Autowired
    private OrderAttachmentService         orderAttachmentService;
    @Autowired
    private OrderAddressService            orderAddressService;
    @Autowired
    private OrderInvoiceService            orderInvoiceService;
    @Autowired
    private OrderDeliveryErpService        orderDeliveryErpService;
    @Autowired
    private OrderDeliveryReceivableService orderDeliveryReceivableService;
    @DubboReference
    private MqMessageSendApi mqMessageSendApi;
    @Autowired
    private OrderCouponUseService          orderCouponUseService;
    @Autowired
    private OrderGiftService               orderGiftService;
    @Autowired
    private OrderPromotionActivityService  orderPromotionActivityService;
    @Autowired
    private OrderInvoiceApplyService       orderInvoiceApplyService;
    @Autowired
    private PresaleOrderService            presaleOrderService;

    @Lazy
    @Autowired
    private OrderServiceImpl _this;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(List<CreateOrderRequest> createOrderRequestList) {
        createOrderRequestList.forEach(createOrderRequest -> {
            OrderDO order = PojoUtils.map(createOrderRequest, OrderDO.class);
            this.save(order);
            Map<Long, CreateOrderDetailRequest> skuIdMap = Maps.newHashMap();
            // 保存订单明细
            List<OrderDetailDO> orderDetailList = CollUtil.newArrayList();
            createOrderRequest.getOrderDetailList().forEach(createOrderDetailRequest -> {
                OrderDetailDO orderDetail = PojoUtils.map(createOrderDetailRequest, OrderDetailDO.class);
                orderDetail.setOrderId(order.getId());
                orderDetail.setPromotionActivityId(createOrderDetailRequest.getPromotionActivityId());
                orderDetail.setPromotionActivityType(createOrderDetailRequest.getPromotionActivityType());
                orderDetail.setPromotionGoodsPrice(createOrderDetailRequest.getPromotionActivityPrice());
                orderDetail.setDepositAmount(createOrderDetailRequest.getDepositAmount());
                orderDetail.setCashDiscountAmount(createOrderDetailRequest.getCashDiscountAmount());
                orderDetailList.add(orderDetail);
                skuIdMap.put(orderDetail.getGoodsSkuId(), createOrderDetailRequest);
            });
            orderDetailService.saveBatch(orderDetailList);

            // 保存订单变更信息
            List<OrderDetailChangeDO> orderDetailChangeList = CollUtil.newArrayList();
            orderDetailList.forEach(orderDetail -> {
                CreateOrderDetailRequest detailRequest = skuIdMap.get(orderDetail.getGoodsSkuId());
                OrderDetailChangeDO orderDetailChange = new OrderDetailChangeDO();
                orderDetailChange.setDetailId(orderDetail.getId());
                orderDetailChange.setPlatformCouponDiscountAmount(detailRequest.getPlatformCouponDiscountAmount());
                orderDetailChange.setCouponDiscountAmount(detailRequest.getCouponDiscountAmount());
                orderDetailChange.setPresaleDiscountAmount(detailRequest.getPresaleDiscountAmount());
                orderDetailChange.setCashDiscountAmount(detailRequest.getCashDiscountAmount());
                orderDetailChange.setPlatformPaymentDiscountAmount(detailRequest.getPlatformPaymentDiscountAmount());
                orderDetailChange.setShopPaymentDiscountAmount(detailRequest.getShopPaymentDiscountAmount());

                PojoUtils.map(orderDetail, orderDetailChange);
                orderDetailChangeList.add(orderDetailChange);
            });
            orderDetailChangeService.saveBatch(orderDetailChangeList);

            // 保存订单明细现折数据
            {
                List<OrderDetailCashDiscountDO> orderDetailCashDiscountDOList = CollUtil.newArrayList();
                List<OrderCashDiscountDO> orderCashDiscountDOList = CollUtil.newArrayList();
                orderDetailList.stream().filter(orderDetailDO -> skuIdMap.get(orderDetailDO.getGoodsSkuId()).getOrderDetailCashDiscountInfoDTO() != null).forEach(orderDetail -> {
                    OrderDetailCashDiscountInfoDTO orderDetailCashDiscountInfo = skuIdMap.get(orderDetail.getGoodsSkuId()).getOrderDetailCashDiscountInfoDTO();
                    for (CashDiscountAgreementInfoDTO cashDiscountAgreementInfo : orderDetailCashDiscountInfo.getCashDiscountAgreementInfoList()) {
                        OrderDetailCashDiscountDO orderDetailCashDiscountDO = new OrderDetailCashDiscountDO();
                        orderDetailCashDiscountDO.setOrderId(orderDetail.getOrderId());
                        orderDetailCashDiscountDO.setDetailId(orderDetail.getId());
                        orderDetailCashDiscountDO.setGoodsId(orderDetail.getGoodsId());
                        orderDetailCashDiscountDO.setGoodsAmount(orderDetail.getGoodsAmount());
                        orderDetailCashDiscountDO.setAgreementId(cashDiscountAgreementInfo.getAgreementId());
                        orderDetailCashDiscountDO.setAgreementVersion(cashDiscountAgreementInfo.getVersion());
                        orderDetailCashDiscountDO.setPolicyValue(cashDiscountAgreementInfo.getPolicyValue());
                        orderDetailCashDiscountDO.setDiscountAmount(cashDiscountAgreementInfo.getDiscountAmount());
                        orderDetailCashDiscountDO.setOpUserId(createOrderRequest.getOpUserId());
                        orderDetailCashDiscountDOList.add(orderDetailCashDiscountDO);

                        OrderCashDiscountDO orderCashDiscountDO = new OrderCashDiscountDO();
                        orderCashDiscountDO.setOrderId(orderDetail.getOrderId());
                        orderCashDiscountDO.setOrderNo(orderDetail.getOrderNo());
                        orderCashDiscountDO.setAgreementId(cashDiscountAgreementInfo.getAgreementId());
                        orderCashDiscountDO.setAgreementVersion(cashDiscountAgreementInfo.getVersion());
                        orderCashDiscountDO.setDiscountAmount(cashDiscountAgreementInfo.getDiscountAmount());
                        orderCashDiscountDO.setOpUserId(createOrderRequest.getOpUserId());

                        orderCashDiscountDOList.add(orderCashDiscountDO);
                    }
                });

                if (CollectionUtil.isNotEmpty(orderDetailCashDiscountDOList)) {

                    orderDetailCashDiscountService.saveBatch(orderDetailCashDiscountDOList);
                }

                // 存储订单现折协议
                if (CollectionUtil.isNotEmpty(orderCashDiscountDOList)) {
                    Map<Long, List<OrderCashDiscountDO>> collectMap = orderCashDiscountDOList.stream().collect(Collectors.groupingBy(OrderCashDiscountDO::getAgreementId));
                    List<OrderCashDiscountDO> insertCashDiscountDOList = Lists.newArrayList();
                    for (Map.Entry<Long, List<OrderCashDiscountDO>> entry : collectMap.entrySet()) {
                        List<OrderCashDiscountDO> cashDiscountDOList =  entry.getValue();
                        OrderCashDiscountDO orderCashDiscountDO = new OrderCashDiscountDO();
                        orderCashDiscountDO.setOrderId(entry.getValue().stream().findFirst().get().getOrderId());
                        orderCashDiscountDO.setOrderNo(createOrderRequest.getOrderNo());
                        orderCashDiscountDO.setAgreementId(entry.getKey());
                        orderCashDiscountDO.setAgreementVersion(cashDiscountDOList.stream().findFirst().get().getAgreementVersion());
                        orderCashDiscountDO.setDiscountAmount(cashDiscountDOList.stream().map(OrderCashDiscountDO::getDiscountAmount).reduce(BigDecimal::add).get());
                        orderCashDiscountDO.setOpUserId(createOrderRequest.getOpUserId());
                        insertCashDiscountDOList.add(orderCashDiscountDO);
                    }
                    orderCashDiscountService.saveBatch(insertCashDiscountDOList);
                }

            }


            // 收货地址信息
            OrderAddressDO orderAddressDO = PojoUtils.map(createOrderRequest.getOrderAddressInfo(), OrderAddressDO.class);
            orderAddressDO.setOrderId(order.getId());
            orderAddressService.save(orderAddressDO);

            // 保存预售订单商品信息
            if (createOrderRequest.getOrderCategory() != null && OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(createOrderRequest.getOrderCategory())) {
                PresaleOrderDO presaleOrderDO = PojoUtils.map(createOrderRequest.getCreatePresaleOrderRequest(), PresaleOrderDO.class);
                presaleOrderDO.setOrderId(order.getId());
                presaleOrderService.save(presaleOrderDO);
            }

            // 保存购销合同
            List<String> contractFileKeyList = createOrderRequest.getContractFileKeyList();
            if (CollUtil.isNotEmpty(contractFileKeyList)) {
                orderAttachmentService.saveBatch(order.getId(), order.getOrderNo(), OrderAttachmentTypeEnum.SALES_CONTRACT_FILE, contractFileKeyList, createOrderRequest.getOpUserId());
            }
            // 保存订单使用优惠劵记录
            List<CreateOrderCouponUseRequest> couponUseRequestList = createOrderRequest.getOrderCouponUseList();
            if (CollUtil.isNotEmpty(couponUseRequestList)) {
                List<OrderCouponUseDO> couponUseDOList = couponUseRequestList.stream().map(coupon -> {
                    coupon.setOrderId(order.getId());
                    OrderCouponUseDO couponUseDO = PojoUtils.map(coupon, OrderCouponUseDO.class);
                    couponUseDO.setIsReturn(1);
                    return couponUseDO;
                }).collect(Collectors.toList());
                orderCouponUseService.saveBatch(couponUseDOList);
            }

            // 保存订单赠品记录
            List<CreateOrderGiftRequest> orderGiftRequests = createOrderRequest.getOrderGiftRequestList();
            if (CollUtil.isNotEmpty(orderGiftRequests)) {
                List<OrderGiftDO> orderGiftList = orderGiftRequests.stream().map(giftRequest -> {
                    giftRequest.setOrderId(order.getId());
                    OrderGiftDO orderGiftDO = PojoUtils.map(giftRequest, OrderGiftDO.class);
                    return orderGiftDO;

                }).collect(Collectors.toList());

                orderGiftService.saveBatch(orderGiftList);
            }
            // 保存订单促销活动信息
            List<CreateOrderPromotionActivityRequest> promotionActivityRequestList = createOrderRequest.getPromotionActivityRequestList();
            if (CollUtil.isNotEmpty(promotionActivityRequestList)) {
                List<OrderPromotionActivityDO> orderPromotionActivityDOS = Lists.newArrayList();
                promotionActivityRequestList.forEach(t -> {
                    OrderPromotionActivityDO orderPromotionActivityDO = PojoUtils.map(t, OrderPromotionActivityDO.class);
                    orderPromotionActivityDO.setOrderId(order.getId());
                    orderPromotionActivityDOS.add(orderPromotionActivityDO);
                });
                orderPromotionActivityService.saveBatch(orderPromotionActivityDOS);
            }

            // 添加订单日志
            orderLogService.add(order.getId(), "提交订单",createOrderRequest.getOpUserId());
        });

        return true;
    }

    @Override
    public List<OrderDO> listByOrderNos(List<String> orderNos) {
        if (CollUtil.isEmpty(orderNos)) {
            return ListUtil.empty();
        }

        QueryWrapper<OrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(OrderDO::getOrderNo, orderNos);
        List<OrderDO> list = this.list(queryWrapper);
        return CollUtil.isEmpty(list) ? ListUtil.empty() : list;
    }

    /**
     * 根据类型查询出对应订单数量
     *
     * @param type
     * @param eids
     * @param userId
     * @return
     */
    @Override
    public OrderNumberDTO getOrderNumber(Integer type, List<Long> eids, Long userId, Integer orderType,Long departmentId) {

        OrderNumberDTO orderNumber = setOrderNumber(type, eids, userId, orderType,departmentId);
        return orderNumber;
    }

    /**
     * 中台获取数量和金额
     *
     * @param request
     * @return
     */
    @Override
    public OrderNumberDTO getCentreOrderNumberCount(QueryBackOrderInfoRequest request) {

        if (request.getStartDeliveryTime() != null) {
            request.setStartDeliveryTime(DateUtil.beginOfDay(request.getStartDeliveryTime()));
        }

        if (request.getEndDeliveryTime() != null) {
            request.setEndDeliveryTime(DateUtil.endOfDay(request.getEndDeliveryTime()));
        }

        if (request.getStartCreateTime() != null) {
            request.setStartCreateTime(DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (request.getEndCreateTime() != null) {
            request.setEndCreateTime(DateUtil.endOfDay(request.getEndCreateTime()));
        }

        OrderNumberDTO result = baseMapper.getTotalAmount(request);

        if(result == null){
            result = new OrderNumberDTO();
        }else{
            result.setPaymentAmount(result.getTotalAmount().subtract(result.getDiscountAmount()));
        }

        int count = getCentreOrder( DateUtil.parse(DateUtil.today()),request,false);
        result.setTodayOrderNum(count);

        int yesterdayCount = getCentreOrder( DateUtil.offsetDay(DateUtil.parse(DateUtil.today()), -1), request,true);
        result.setYesterdayOrderNum(yesterdayCount);

        int yearOrderCount = getCentreOrder( DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -12), request,false);
        result.setYearOrderNum(yearOrderCount);

        return result;

    }


    private Integer getCentreOrder(Date date,QueryBackOrderInfoRequest request,Boolean flag){
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .ge(OrderDO::getCreateTime, date);
        if (request.getOrderSource() != null && request.getOrderSource() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderSource, request.getOrderSource());
        }

        if (request.getOrderType() != null && request.getOrderType() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderType, request.getOrderType());
        }

        if (flag) {
            wrapper.lambda().lt(OrderDO::getCreateTime, DateUtil.parse(DateUtil.today()));
        }
        if(CollectionUtil.isNotEmpty(request.getBuyerEid())){
            wrapper.lambda().in(OrderDO :: getBuyerEid,request.getBuyerEid());
        }
        if (request.getEndDeliveryTime() != null && request.getStartDeliveryTime() != null ) {
            QueryWrapper<OrderDeliveryDO> deliveryDOQueryWrapper = new QueryWrapper<>();
            deliveryDOQueryWrapper.lambda().le(OrderDeliveryDO::getCreateTime,request.getEndDeliveryTime())
                    .ge(OrderDeliveryDO::getCreateTime, request.getStartDeliveryTime());
            List<OrderDeliveryDO> list = orderDeliveryService.list(deliveryDOQueryWrapper);
            if(CollectionUtil.isNotEmpty(list)){
                List<Long> ids = list.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
                wrapper.lambda().in(OrderDO :: getId,ids);
            }else{
                return 0;
            }
        }

        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, request.getStartCreateTime());
        }
        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, request.getEndCreateTime());
        }

        return count(wrapper);
    }


    /**
     * 销售助手查询代客下单的订单
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getSaOrderPage(QueryAssistanOrderPageRequest request) {
        if (request.getPaymentMethod() != null && 0 == request.getPaymentMethod()) {
            request.setPaymentMethod(null);
        }

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getBuyerEid, request.getCustomerEid());
        // B2B没有商务联系人,取创建人名下订单
        switch (OrderTypeEnum.getByCode(request.getOrderType())) {
            case POP:
                wrapper.lambda().in(OrderDO::getContacterId, request.getContacterIds());
                break;
            case B2B:
                wrapper.lambda().in(OrderDO::getCreateUser, request.getContacterIds());
                break;
            default:break;
        }
        if (request.getOrderStatus() != null && request.getOrderStatus() != 0) {
            switch (OrderTypeEnum.getByCode(request.getOrderType())) {
                case POP:
                    switch (SaOrderStatusEnum.getByCode(request.getOrderStatus())) {
                        case CANCELED:
                            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode());
                            break;
                        case FINISHED:
                            wrapper.lambda().in(OrderDO::getOrderStatus, ListUtil.toList(OrderStatusEnum.FINISHED.getCode(),OrderStatusEnum.RECEIVED.getCode()));
                            wrapper.lambda().ne(OrderDO::getPaymentMethod, 0);
                            break;
                        case UNAUDITED:
                            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.UNAUDITED.getCode());
                            wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.UNAUDIT.getCode());
                            break;
                        case UNSUBMIT:
                            wrapper.lambda().eq(OrderDO::getPaymentMethod, 0);
                            wrapper.lambda().ne(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_REJECT.getCode());
                            wrapper.lambda().ne(OrderDO::getOrderStatus,OrderStatusEnum.CANCELED.getCode());
                            break;
                        case ORDER_RETURN_REJECT:
                            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.UNAUDITED.getCode());
                            wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_REJECT.getCode());
                            break;
                        case UNDELIVERED:
                        case PARTDELIVERED:
                        case DELIVERED:
                        case RECEIVED:
                            wrapper.lambda().eq(OrderDO::getOrderStatus, request.getOrderStatus());
                            wrapper.lambda().ne(OrderDO::getPaymentMethod, 0);
                            break;
                        default:
                            break;
                    }
                    break;
                case B2B:
                    switch (SaOrderStatusEnum.getByCode(request.getOrderStatus())) {
                        case CANCELED:
                            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode());
                            break;
                        case FINISHED:
                            wrapper.lambda().in(OrderDO::getOrderStatus, ListUtil.toList(OrderStatusEnum.FINISHED.getCode(),OrderStatusEnum.RECEIVED.getCode()));
                            wrapper.lambda().ne(OrderDO::getPaymentMethod, 0);
                            break;
                        case NOTCONFIRM:
                            wrapper.lambda().in(OrderDO::getCustomerConfirmStatus, ListUtil.toList(CustomerConfirmEnum.NOTCONFIRM.getCode(),CustomerConfirmEnum.TRANSFER.getCode()));
                            wrapper.lambda().eq(OrderDO::getPaymentStatus,PaymentStatusEnum.UNPAID.getCode());
                            wrapper.lambda().eq(OrderDO::getOrderStatus,OrderStatusEnum.UNAUDITED.getCode());
                            wrapper.lambda().ne(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_REJECT.getCode());
                            break;
                        case WAIT_PAY:
                            wrapper.lambda().eq(OrderDO::getCustomerConfirmStatus, CustomerConfirmEnum.CONFIRMED.getCode());
                            wrapper.lambda().eq(OrderDO::getPaymentStatus,PaymentStatusEnum.UNPAID.getCode());
                            wrapper.lambda().eq(OrderDO::getOrderStatus,OrderStatusEnum.UNAUDITED.getCode());
                            wrapper.lambda().ne(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_REJECT.getCode());
                            break;
                        case ORDER_RETURN_REJECT:
                            wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_REJECT.getCode());
                            wrapper.lambda().eq(OrderDO::getOrderStatus,OrderStatusEnum.UNAUDITED.getCode());
                            break;
                        case UNAUDITED:
                            wrapper.lambda().ne(OrderDO::getAuditStatus, OrderAuditStatusEnum.UNAUDIT.getCode());
                            wrapper.lambda().eq(OrderDO::getOrderStatus,OrderStatusEnum.UNAUDITED.getCode());
                            break;
                        case UNDELIVERED:
                        case PARTDELIVERED:
                        case DELIVERED:
                        case RECEIVED:
                            wrapper.lambda().eq(OrderDO::getOrderStatus, request.getOrderStatus());
                            wrapper.lambda().ne(OrderDO::getPaymentMethod, 0);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().like(OrderDO::getOrderNo, request.getOrderNo());
        }
        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }

        if (request.getPaymentMethod() != null) {
            wrapper.lambda().eq(OrderDO::getPaymentMethod, request.getPaymentMethod());
        }

        if (request.getOrderType() != null && request.getOrderType() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderType, request.getOrderType());
        }

        wrapper.lambda().orderByDesc(OrderDO::getCreateTime);


        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> orderPage = PojoUtils.map(page, OrderDTO.class);
        return orderPage;
    }

    /**
     * 获取订单信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getOrderPage(QueryOrderPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().like(OrderDO::getOrderNo, request.getOrderNo());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            if (1 == request.getType()) {
                wrapper.lambda().like(OrderDO::getBuyerEname, request.getName());
            } else if (2 == request.getType()) {
                wrapper.lambda().like(OrderDO::getSellerEname, request.getName());
            }
        }
        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }

        if (request.getInvoiceStatus() != null && request.getInvoiceStatus() != 0) {
            if (request.getInvoiceStatus().equals(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode())) {
                wrapper.lambda().in(OrderDO::getInvoiceStatus, new ArrayList<Integer>() {{
                    add(OrderInvoiceApplyStatusEnum.DEFAULT_VALUE_APPLY.getCode());
                    add(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode());
                }});
            } else {
                wrapper.lambda().eq(OrderDO::getInvoiceStatus, request.getInvoiceStatus());
            }

        }

        if (request.getOrderStatus() != null && request.getOrderStatus() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderStatus, request.getOrderStatus());
        }

        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }

        if (request.getPaymentMethod() != null && request.getPaymentMethod() != 0) {
            wrapper.lambda().eq(OrderDO::getPaymentMethod, request.getPaymentMethod());
        }
        if(CollUtil.isNotEmpty(request.getPaymentMethodList())) {
            wrapper.lambda().in(OrderDO::getPaymentMethod, request.getPaymentMethodList());
        }

        if (request.getPaymentStatus() != null && request.getPaymentStatus() != 0) {
            wrapper.lambda().eq(OrderDO::getPaymentStatus, request.getPaymentStatus());
        }

        if (request.getOrderType() != null && request.getOrderType() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderType, request.getOrderType());
        }
        if(request.getOrderSource() != null && request.getOrderSource() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderSource, request.getOrderSource());
        }

        if(request.getType()!= null && request.getType() != 0 ){
            if (1 == request.getType()) {
                wrapper.lambda().in(OrderDO::getSellerEid, request.getEidList());
                if(request.getStateType() != null && request.getStateType() != 0){
                    if(request.getStateType() ==1){
                        List<Integer> statusList = new ArrayList<>();
                        statusList.add(OrderStatusEnum.UNDELIVERED.getCode());
                        statusList.add(OrderStatusEnum.PARTDELIVERED.getCode());
                        wrapper.lambda().in(OrderDO::getOrderStatus, statusList );
                    }else if(request.getStateType() ==2){
                        //待收货
                        wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode());
                    }else if(request.getStateType() ==4){
                        //已完成
                        wrapper.lambda().ge(OrderDO::getOrderStatus, OrderStatusEnum.RECEIVED.getCode());
                    }else if(request.getStateType() ==5){
                        //已取消
                        wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode());
                    }else if(request.getStateType() == 3){
                        wrapper.lambda().eq(OrderDO::getPaymentMethod, PaymentMethodEnum.PAYMENT_DAYS.getCode())
                                .eq(OrderDO :: getPaymentStatus,PaymentStatusEnum.UNPAID.getCode())
                                .ne(OrderDO :: getOrderStatus,OrderStatusEnum.CANCELED.getCode());
                    }
                }
            } else if (2 == request.getType()) {
                wrapper.lambda().in(OrderDO::getBuyerEid, request.getEidList());
            }
        }


        if(request.getDepartmentId() != null){
            wrapper.lambda().eq(OrderDO::getDepartmentId, request.getDepartmentId());
        }

        if (CollectionUtil.isNotEmpty(request.getContacterIdList())) {
            wrapper.lambda().in(OrderDO::getContacterId, request.getContacterIdList());
        }

        if(request.getId() != null  && request.getId() != 0){
            wrapper.lambda().eq(OrderDO::getId, request.getId());
        }

        if(StringUtils.isNotBlank(request.getProvinceCode())){
            wrapper.lambda().eq(OrderDO::getBuyerProvinceCode, request.getProvinceCode());
        }
        if(StringUtils.isNotBlank(request.getCityCode())){
            wrapper.lambda().eq(OrderDO::getBuyerCityCode, request.getCityCode());
        }
        if(StringUtils.isNotBlank(request.getRegionCode())){
            wrapper.lambda().eq(OrderDO::getBuyerRegionCode, request.getRegionCode());
        }

        wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .orderByDesc(OrderDO::getCreateTime);

        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> orderPage = PojoUtils.map(page, OrderDTO.class);
        return orderPage;
    }

    /**
     * 获取采购订单信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getB2BPurchaseOrderPage(QueryOrderPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().like(OrderDO::getOrderNo, request.getOrderNo());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            if (1 == request.getType()) {
                wrapper.lambda().like(OrderDO::getBuyerEname, request.getName());
            } else if (2 == request.getType()) {
                wrapper.lambda().like(OrderDO::getSellerEname, request.getName());
            }
        }
        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }


        if (request.getOrderStatus() != null && request.getOrderStatus() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderStatus, request.getOrderStatus());
        }

        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }

        if (request.getPaymentMethod() != null && request.getPaymentMethod() != 0) {
            wrapper.lambda().eq(OrderDO::getPaymentMethod, request.getPaymentMethod());
        }

        if (request.getPaymentStatus() != null && request.getPaymentStatus() != 0) {
            wrapper.lambda().eq(OrderDO::getPaymentStatus, request.getPaymentStatus());
        }

        if (request.getOrderType() != null && request.getOrderType() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderType, request.getOrderType());
        }

        if (1 == request.getType()) {
            wrapper.lambda().in(OrderDO::getSellerEid, request.getEidList());
        } else if (2 == request.getType()) {
            wrapper.lambda().in(OrderDO::getBuyerEid, request.getEidList());
        }

        if (CollectionUtil.isNotEmpty(request.getContacterIdList())) {
            wrapper.lambda().in(OrderDO::getContacterId, request.getContacterIdList());
        }

        if(request.getId() != null  && request.getId() != 0){
            wrapper.lambda().eq(OrderDO::getId, request.getId());
        }

        wrapper.lambda().eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .eq(OrderDO :: getHideFlag,OrderHideFlagEnum.SHOW.getCode())
                .orderByDesc(OrderDO::getCreateTime);

        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> orderPage = PojoUtils.map(page, OrderDTO.class);
        return orderPage;
    }

    /**
     * 获取订单信息(运营后台)
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getBackOrderPage(QueryBackOrderInfoRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getProvinceCode())) {
            wrapper.lambda().eq(OrderDO::getBuyerProvinceCode, request.getProvinceCode());
        }
        if (StringUtils.isNotBlank(request.getCityCode())) {
            wrapper.lambda().eq(OrderDO::getBuyerCityCode, request.getCityCode());
        }
        if (StringUtils.isNotBlank(request.getRegionCode())) {
            wrapper.lambda().eq(OrderDO::getBuyerRegionCode, request.getRegionCode());
        }
        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().like(OrderDO::getOrderNo, request.getOrderNo());
        }
        if (StringUtils.isNotBlank(request.getSellerEname())) {

            wrapper.lambda().like(OrderDO::getSellerEname, request.getSellerEname());
        }
        if (StringUtils.isNotBlank(request.getBuyerEname())) {
            wrapper.lambda().like(OrderDO::getBuyerEname, request.getBuyerEname());
        }
        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }

        if (request.getInvoiceStatus() != null && request.getInvoiceStatus() != 0) {
            wrapper.lambda().eq(OrderDO::getInvoiceStatus, request.getInvoiceStatus());
        }

        if (request.getOrderStatus() != null && request.getOrderStatus() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderStatus, request.getOrderStatus());
        }

        if (request.getPaymentMethod() != null && request.getPaymentMethod() != 0) {
            wrapper.lambda().eq(OrderDO::getPaymentMethod, request.getPaymentMethod());
        }

        if (request.getEndDeliveryTime() != null && request.getStartDeliveryTime() != null ) {
            QueryWrapper<OrderDeliveryDO> deliveryDOQueryWrapper = new QueryWrapper<>();
            deliveryDOQueryWrapper.lambda().le(OrderDeliveryDO::getCreateTime,DateUtil.endOfDay(request.getEndDeliveryTime()))
                    .ge(OrderDeliveryDO::getCreateTime, DateUtil.beginOfDay(request.getStartDeliveryTime()));
            List<OrderDeliveryDO> list = orderDeliveryService.list(deliveryDOQueryWrapper);
            if(CollectionUtil.isNotEmpty(list)){
                List<Long> ids = list.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
                wrapper.lambda().in(OrderDO :: getId,ids);
            }else{
                return new Page<>();
            }
        }

        if (request.getStartReceiveTime() != null) {
            wrapper.lambda().ge(OrderDO::getReceiveTime, DateUtil.beginOfDay(request.getStartReceiveTime()));
        }

        if (request.getEndReceiveTime() != null) {
            wrapper.lambda().le(OrderDO::getReceiveTime, DateUtil.endOfDay(request.getEndReceiveTime()));
        }

        if(request.getOrderType() !=null && request.getOrderType() !=0){
            wrapper.lambda().eq(OrderDO::getOrderType, request.getOrderType());
        }

        if (request.getPaymentStatus() != null && request.getPaymentStatus() != 0) {
            wrapper.lambda().eq(OrderDO::getPaymentStatus, request.getPaymentStatus());
        }

        if (CollectionUtil.isNotEmpty(request.getBuyerEid())) {
            wrapper.lambda().in(OrderDO::getBuyerEid, request.getBuyerEid());
        }

        if (request.getSellerEid()!= null && request.getSellerEid() != 0) {
            wrapper.lambda().eq(OrderDO::getSellerEid, request.getSellerEid());
        }

        if (request.getOrderSource() != null && request.getOrderSource() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderSource, request.getOrderSource());
        }

        if(CollectionUtil.isNotEmpty(request.getOrderStatusList())){
            wrapper.lambda().in(OrderDO::getOrderStatus, request.getOrderStatusList());
        }

        if(CollectionUtil.isNotEmpty(request.getContacterIdList())){
            wrapper.lambda().in(OrderDO::getContacterId, request.getContacterIdList());
        }

        if(request.getActivityType() != null && request.getActivityType() != 0){
            List<OrderPromotionActivityDTO> orderPromotionActivityList = orderPromotionActivityService.listByActivityType(request.getActivityType());
            if(CollectionUtil.isNotEmpty(orderPromotionActivityList)){
                List<Long> ids = orderPromotionActivityList.stream().map(OrderPromotionActivityDTO::getOrderId).collect(Collectors.toList());
                wrapper.lambda().in(OrderDO::getId, ids);
            }else{
                return new Page<OrderDTO>();
            }
        }

        wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .orderByDesc(OrderDO::getCreateTime);

        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> orderPage = PojoUtils.map(page, OrderDTO.class);
        return orderPage;
    }

    @Override
    public Page<OrderDTO> getAssistantConfirmOrderPage(QueryAssistantConfirmOrderPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        getAssistantWrapper(request, wrapper);

        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> orderPage = PojoUtils.map(page, OrderDTO.class);
        return orderPage;
    }

    @Override
    public OrderAssistantConfirmQuantityAmountDTO getAssistantConfirmQuantityAmount(QueryAssistantConfirmOrderPageRequest request) {
        OrderAssistantConfirmQuantityAmountDTO result = new OrderAssistantConfirmQuantityAmountDTO();
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        getAssistantWrapper(request, wrapper);
        Integer count = count(wrapper);
        result.setTotalQuantity(count);

        QueryWrapper<OrderDO> wrapperAmount = new QueryWrapper<>();
        getAssistantWrapper(request, wrapperAmount);
        wrapperAmount.select("sum(total_amount) as totalAmount ");
        Map<String, Object> map = getMap(wrapperAmount);
        result.setTotalAmount( map != null ? (BigDecimal)map.get("totalAmount"):BigDecimal.ZERO);
        return result;
    }


    private void getAssistantWrapper(QueryAssistantConfirmOrderPageRequest request, QueryWrapper<OrderDO> wrapper) {
        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        if(CollectionUtil.isNotEmpty(request.getContacterIdList())){
            wrapper.lambda().in(OrderDO::getContacterId, request.getContacterIdList());
        }

        if(request.getOrderSource() != null && request.getOrderSource() != 0){
            wrapper.lambda().eq(OrderDO::getOrderSource, request.getOrderSource());
        }

        if(request.getCreateUser() != null && request.getCreateUser() != 0){
            wrapper.lambda().eq(OrderDO::getCreateUser, request.getCreateUser());
        }
        wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus, CustomerConfirmEnum.CONFIRMED.getCode())
                .ne(OrderDO :: getOrderStatus , OrderStatusEnum.CANCELED.getCode())
                .orderByDesc(OrderDO::getCreateTime);
    }

    /**
     * 根据id获取商品基础
     *
     * @param id
     * @return
     */
    @Override
    public OrderDTO getOrderInfo(Long id) {
        OrderDO order = getById(id);
        if (order == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }
        return PojoUtils.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO getOrderInfo(String orderNo) {

        QueryWrapper<OrderDO> orderWrapper = new QueryWrapper<>();
        orderWrapper.lambda().eq(OrderDO::getOrderNo, orderNo);

        OrderDO order = this.getOne(orderWrapper);

        if (order == null) {

            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }

        return PojoUtils.map(order, OrderDTO.class);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean cancel(Long orderId, Long opUserId, Boolean isBeforeUpdate) {
        OrderDO order = getById(orderId);

        if (isBeforeUpdate) {
            validateOrderStatus(order);
        }

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getId, order.getId())
                .eq(OrderDO::getOrderStatus, order.getOrderStatus());
        OrderDO orderNew = new OrderDO();
        orderNew.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderNew.setOpUserId(opUserId);

        OrderLogDO orderLogInfo = new OrderLogDO();
        orderLogInfo.setLogContent("订单取消")
                .setLogTime(new Date())
                .setOrderId(orderId);
        orderLogService.save(orderLogInfo);

        OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
        orderStatusLog.setOrderId(order.getId()).setOrderStatus(OrderStatusEnum.CANCELED.getCode()).setRemark("订单取消")
                .setCreateUser(opUserId).setCreateTime(DateUtil.date());
        orderStatusLogService.save(orderStatusLog);
        return update(orderNew, wrapper);
    }


    /**
     * 发货
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delivery(SaveOrderDeliveryListInfoRequest request) {
        log.info("订单开始发货，订单入参,OrderNo:{}", JSON.toJSONString(request));
        OrderDO order = getById(request.getOrderId());
        //检验订单是否能发货
        validateDeliveryOrderStatus(order);
        List<OrderDetailDTO> orderDetailList = orderDetailService.getOrderDetailInfo(request.getOrderId());
        Map<Long, OrderDetailDTO> mapDetail = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, one -> one, (k1, k2) -> k1));

        //保存OrderDeliveryErp数据信息
        List<OrderDeliveryErpDO> orderDeliveryErpList = new ArrayList<>();
        //保存OrderDelivery数据信息
        List<OrderDeliveryDO> deliveryList = new ArrayList<>();

        //校验发货参数是否合理，并将同一批次，同一出库单同一erp录入ID
        SaveOrderDeliveryListInfoRequest param = validateDeliveryParam(request, mapDetail);

        //判断OrderDeliveryErp根据唯一值组装成map
        Map<String, OrderDeliveryErpDO> deliveryErpMap = getOrderDeliveryErpMap(request);

        //判断OrderDelivery根据唯一值组装成map
        Map<String, OrderDeliveryDO> orderDeliveryMap = getOrderDeliveryMap(request);

        List<OrderDeliveryReceivableDO> receivableList = orderDeliveryReceivableService.listByOrderIds(new ArrayList<Long>() {{
            add(request.getOrderId());
        }});
        Map<String, OrderDeliveryReceivableDO> receivableErpMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(receivableList)) {
            receivableErpMap = receivableList.stream().collect(Collectors.toMap(k -> k.getErpDeliveryNo(), one -> one, (k1, k2) -> k1));

        }

        //发货批次数量
        Map<Long, Integer> mapReturn = new HashMap<>();
        //保存数据
        Map<String,OrderDeliveryReceivableDO> receivableMap = new HashMap<>();

        for (SaveOrderDeliveryInfoRequest deliveryOne : param.getOrderDeliveryGoodsInfoList()) {

            //发货信息
            Map<String, List<DeliveryInfoRequest>> mapDelivery = new HashMap<>();
            //发货数量
            Integer deliveryQuantity = 0;
            for (DeliveryInfoRequest one : deliveryOne.getDeliveryInfoList()) {
                if (one.getDeliveryQuantity() > 0) {
                    if (StringUtils.isNotEmpty(one.getErpDeliveryNo())) {
                        OrderDeliveryErpDO orderDeliveryErpDO = deliveryErpMap.get(deliveryOne.getDetailId() + "_" + one.getErpDeliveryNo() + "_" + one.getBatchNo() +"_" + one.getEasSendOrderId());
                        if (orderDeliveryErpDO != null) {
                            orderDeliveryErpDO.setDeliveryQuantity(orderDeliveryErpDO.getDeliveryQuantity() + one.getDeliveryQuantity());

                        } else {
                            orderDeliveryErpDO = new OrderDeliveryErpDO();
                            orderDeliveryErpDO.setOrderId(param.getOrderId());
                            orderDeliveryErpDO.setDetailId(deliveryOne.getDetailId());
                            orderDeliveryErpDO.setBatchNo(one.getBatchNo());
                            orderDeliveryErpDO.setExpiryDate(one.getExpiryDate());
                            orderDeliveryErpDO.setProduceDate(one.getProduceDate());
                            orderDeliveryErpDO.setDeliveryQuantity(one.getDeliveryQuantity());
                            orderDeliveryErpDO.setErpDeliveryNo(one.getErpDeliveryNo());
                            orderDeliveryErpDO.setErpSendOrderId(one.getEasSendOrderId());
                            orderDeliveryErpDO.setCreateUser(request.getOpUserId());
                            orderDeliveryErpDO.setCreateTime(request.getOpTime());
                            orderDeliveryErpDO.setUpdateUser(request.getOpUserId());
                            orderDeliveryErpDO.setUpdateTime(request.getOpTime());
                        }
                        orderDeliveryErpList.add(orderDeliveryErpDO);

                        OrderDeliveryReceivableDO orderDeliveryReceivableDO = receivableErpMap.get(one.getErpDeliveryNo());
                        if(orderDeliveryReceivableDO == null){
                            if(!receivableMap.containsKey(one.getErpDeliveryNo())){
                                OrderDeliveryReceivableDO receivableOne = new OrderDeliveryReceivableDO();
                                receivableOne.setOrderId(param.getOrderId());
                                receivableOne.setErpDeliveryNo(one.getErpDeliveryNo());
                                receivableOne.setErpReceivableFlag(1);
                                receivableOne.setCreateUser(request.getOpUserId());
                                receivableOne.setCreateTime(request.getOpTime());
                                receivableOne.setUpdateUser(request.getOpUserId());
                                receivableOne.setUpdateTime(request.getOpTime());
                                receivableMap.put(one.getErpDeliveryNo(), receivableOne);

                            }
                        }
                    }

                }

                if (mapDelivery.containsKey(one.getBatchNo())) {
                    List<DeliveryInfoRequest> deliveryRequestList = mapDelivery.get(one.getBatchNo());
                    deliveryRequestList.add(one);

                } else {
                    mapDelivery.put(one.getBatchNo(), new ArrayList<DeliveryInfoRequest>() {{
                        add(one);
                    }});
                }
                deliveryQuantity = deliveryQuantity + one.getDeliveryQuantity();
            }
            mapReturn.put(mapDetail.get(deliveryOne.getDetailId()).getId(), deliveryQuantity);

            makeOrderDeliveryData(request, mapDetail.get(deliveryOne.getDetailId()), mapDelivery, deliveryList, orderDeliveryMap);

        }

        for (Map.Entry<Long, OrderDetailDTO> one : mapDetail.entrySet()) {
            Integer deliveryQuantity = mapReturn.get(one.getKey());
            orderDetailChangeService.updatePartDeliveryData(one.getKey(), deliveryQuantity == null ? 0 : deliveryQuantity);
            log.info("订单部分发货,调用DetailChange参数,detailId:{},deliveryQuantity;{}", one.getKey(), deliveryQuantity);
        }

        List<OrderDetailChangeDO> orderDetailChangeList = orderDetailChangeService.listByOrderId(request.getOrderId());
        Integer deliveryQuantity = orderDetailChangeList.stream().mapToInt(OrderDetailChangeDO::getDeliveryQuantity).sum();
        Integer goodsQuantity = orderDetailChangeList.stream().mapToInt(OrderDetailChangeDO::getGoodsQuantity).sum();
        Boolean allDelivery = false;
        if (deliveryQuantity.compareTo(goodsQuantity) == 0) {
            allDelivery = true;
        }

        if (CollectionUtil.isNotEmpty(orderDeliveryErpList)) {
            orderDeliveryErpService.saveOrUpdateBatch(orderDeliveryErpList);
        }

        Collection<OrderDeliveryReceivableDO> values = receivableMap.values();
        if (CollectionUtil.isNotEmpty(values)) {
            orderDeliveryReceivableService.saveBatch(values);
        }

        if(!allDelivery){
            OrderLogDO orderLogInfo = new OrderLogDO();
            orderLogInfo.setLogContent("订单部分发货")
                    .setLogTime(request.getOpTime())
                    .setOrderId(request.getOrderId())
                    .setOpUserId(request.getOpUserId());
            orderLogService.save(orderLogInfo);

            OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
            orderStatusLog.setOrderId(request.getOrderId())
                    .setOrderStatus(OrderStatusEnum.PARTDELIVERED.getCode())
                    .setCreateUser(request.getOrderId())
                    .setCreateTime(request.getOpTime())
                    .setRemark("订单部分发货");
            orderStatusLogService.save(orderStatusLog);
        }else{
            //全部发货
            sendCompleteDelivery(request.getOrderId(), request.getOpUserId(), orderDetailList);
        }


        orderDeliveryService.saveOrUpdateBatch(deliveryList);
        //修改订单信息
        updateOrderDeliverInfo(request, order, allDelivery);
        //分摊金额
        shareDeliveryErpCashAmount(new ArrayList<Long>(){{add(order.getId());}});

        log.info("订单结束发货，订单单号,OrderNo:{}", order.getOrderNo());
        return true;
    }

    @Override
    public Boolean shareAllDeliveryErpCashAmount(){
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().ge(OrderDO :: getOrderStatus,OrderStatusEnum.PARTDELIVERED.getCode())
                .eq(OrderDO :: getOrderType,OrderTypeEnum.POP.getCode());
        List<OrderDO> list = list(wrapper);
        List<Long> idList = list.stream().map(O -> O.getId()).collect(Collectors.toList());
        return shareDeliveryErpCashAmount(idList);
    }

    /**
     * 分摊现折金额
     *
     * @param orderId
     * @return
     */

    private Boolean shareDeliveryErpCashAmount(List<Long> orderId) {
        List<OrderDeliveryErpDO> saveList = new ArrayList<>();
        List<OrderDetailDO> orderDetailList = orderDetailService.getOrderDetailByOrderIds(orderId);
        Map<Long, OrderDetailDO> mapDetail = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDO::getId, one -> one, (k1, k2) -> k1));
        List<OrderDeliveryErpDO> orderDeliveryErpList = orderDeliveryErpService.listByOrderIds(orderId);
        if(CollectionUtil.isNotEmpty(orderDeliveryErpList)){
            Map<Long, List<OrderDeliveryErpDO>> deliveryErpMap= orderDeliveryErpList.stream().collect(Collectors.groupingBy(OrderDeliveryErpDO::getDetailId));

            for (Map.Entry<Long, List<OrderDeliveryErpDO>> one : deliveryErpMap.entrySet()) {
                //需要分摊的现折金额
                List<OrderDeliveryErpDO> shareDeliveryErp = new ArrayList<>();
                //已分摊的现折金额
                BigDecimal allShareCashAmount = BigDecimal.ZERO;
                //已发货数量
                Integer deliveryNumber =0;

                List<OrderDeliveryErpDO> list = one.getValue();
                OrderDetailDO orderDetail = mapDetail.get(one.getKey());

                for(OrderDeliveryErpDO deliveryErpOne : list){
                    if(deliveryErpOne.getCashDiscountAmount().compareTo(BigDecimal.ZERO) == 0){
                        shareDeliveryErp.add(deliveryErpOne);
                    }
                    deliveryNumber = deliveryNumber + deliveryErpOne.getDeliveryQuantity();
                    allShareCashAmount = allShareCashAmount.add(deliveryErpOne.getCashDiscountAmount());
                }

                if(orderDetail.getCashDiscountAmount().compareTo(allShareCashAmount) > 0 ){

                    for(int i=0;i<shareDeliveryErp.size();i++){
                        OrderDeliveryErpDO deliveryErpDO = shareDeliveryErp.get(i);
                        //当发货数量等于购买数量时做减法
                        if((i == shareDeliveryErp.size()-1) && (deliveryNumber.compareTo(orderDetail.getGoodsQuantity()) == 0) ){
                            deliveryErpDO.setCashDiscountAmount(orderDetail.getCashDiscountAmount().subtract(allShareCashAmount));

                        }else{
                            BigDecimal cashAmount = orderDetail.getCashDiscountAmount().multiply(new BigDecimal(deliveryErpDO.getDeliveryQuantity()))
                                    .divide(new BigDecimal(orderDetail.getGoodsQuantity()),2,BigDecimal.ROUND_HALF_UP);
                            deliveryErpDO.setCashDiscountAmount(cashAmount);
                            allShareCashAmount = allShareCashAmount.add(cashAmount);
                        }
                        saveList.add(deliveryErpDO);
                    }
                }

            }
        }
        log.info("订单发货分摊现折扣金额，分摊参数:{}", JSON.toJSONString(saveList));
        if(CollectionUtil.isNotEmpty(saveList)){
            orderDeliveryErpService.updateBatchById(saveList);
        }

        return true ;
    }

    /**
     * 校验发货订单状态
     *
     * @param order
     */
    private void validateDeliveryOrderStatus(OrderDO order) {
        if (order == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }

        if (!(OrderStatusEnum.UNDELIVERED == OrderStatusEnum.getByCode(order.getOrderStatus()) || OrderStatusEnum.PARTDELIVERED == OrderStatusEnum.getByCode(order.getOrderStatus()))) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_ERROR);
        }
    }

    /**
     * 关闭发货按钮
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean closeDelivery(Long orderId, Long opUserId) {
        log.info("订单发货关闭入参,orderId:{}", orderId);
        OrderDO order = getById(orderId);
        validateDeliveryOrderStatus(order);
        Integer orderStatus = order.getOrderStatus();

        order.setOrderStatus(OrderStatusEnum.DELIVERED.getCode());
        order.setDeliveryUser(opUserId);
        order.setDeliveryTime(new Date());
        order.setOpUserId(opUserId);
        order.setOpTime(new Date());
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getId, order.getId())
                .eq(OrderDO::getOrderStatus, orderStatus);
        boolean update = update(order, wrapper);
        if (update) {
            //关闭发货
            List<OrderDetailDTO> orderDetailList = orderDetailService.getOrderDetailInfo(orderId);
            sendCompleteDelivery(orderId, opUserId, orderDetailList);
        }

        return update;
    }

    private void sendCompleteDelivery(Long orderId, Long opUserId, List<OrderDetailDTO> orderDetailList) {
        for (OrderDetailDTO one : orderDetailList) {
            orderDetailChangeService.updatePartDeliveryReturnData(one.getId());
            log.info("订单全部发货,调用DetailChange参数,detailId:{}", one.getId());
        }
        OrderLogDO orderLogInfo = new OrderLogDO();
        orderLogInfo.setLogContent("订单全部发货")
                .setLogTime(new Date())
                .setOrderId(orderId)
                .setOpUserId(opUserId);
        orderLogService.save(orderLogInfo);

        OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
        orderStatusLog.setOrderId(orderId)
                .setOrderStatus(OrderStatusEnum.DELIVERED.getCode())
                .setCreateUser(opUserId)
                .setCreateTime(new Date())
                .setRemark("订单全部发货");
        orderStatusLogService.save(orderStatusLog);
    }

    private Map<String, OrderDeliveryDO> getOrderDeliveryMap(SaveOrderDeliveryListInfoRequest request) {
        List<OrderDeliveryDO> orderDeliveryList = orderDeliveryService.getOrderDeliveryList(request.getOrderId());
        Map<String, OrderDeliveryDO> deliveryMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderDeliveryList)) {
            deliveryMap = orderDeliveryList.stream().collect(Collectors.toMap(k -> k.getDetailId() + "_" + k.getBatchNo(), one -> one, (k1, k2) -> k1));
        }
        return deliveryMap;
    }

    private Map<String, OrderDeliveryErpDO> getOrderDeliveryErpMap(SaveOrderDeliveryListInfoRequest request) {
        List<OrderDeliveryErpDO> deliveryErpList = orderDeliveryErpService.listByOrderIds(new ArrayList<Long>() {{
            add(request.getOrderId());
        }});
        Map<String, OrderDeliveryErpDO> deliveryErpMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(deliveryErpList)) {
            deliveryErpMap = deliveryErpList.stream().collect(Collectors.toMap(k -> k.getDetailId() + "_" + k.getErpDeliveryNo() + "_" + k.getBatchNo()+ "_" + k.getErpSendOrderId(), one -> one, (k1, k2) -> k1));
        }
        return deliveryErpMap;
    }

    /**
     * 组装order_Delivery表中数据
     *
     * @param request
     * @param detail
     * @param mapDelivery
     * @para deliveryList
     */
    private void makeOrderDeliveryData(SaveOrderDeliveryListInfoRequest request,
                                       OrderDetailDTO detail,
                                       Map<String, List<DeliveryInfoRequest>> mapDelivery,
                                       List<OrderDeliveryDO> deliveryList,
                                       Map<String, OrderDeliveryDO> orderDeliveryMap) {

        for (Map.Entry<String, List<DeliveryInfoRequest>> batch : mapDelivery.entrySet()) {
            Integer deliveryBatchQuantity = 0;
            List<DeliveryInfoRequest> batchList = batch.getValue();
            Date expiryDate = null;
            Date ProduceDate = null;

            OrderDeliveryDO orderDeliveryOne = orderDeliveryMap.get(detail.getId() + "_" + batch.getKey());
            if (CollectionUtil.isNotEmpty(batchList)) {
                for (DeliveryInfoRequest deliveryInOne : batchList) {
                    deliveryBatchQuantity = deliveryBatchQuantity + deliveryInOne.getDeliveryQuantity();
                }
                expiryDate = batchList.get(0).getExpiryDate();
                ProduceDate = batchList.get(0).getProduceDate();
            }

            if (orderDeliveryOne != null) {
                orderDeliveryOne.setDeliveryQuantity(deliveryBatchQuantity + orderDeliveryOne.getDeliveryQuantity());
            } else {
                orderDeliveryOne = new OrderDeliveryDO();
                orderDeliveryOne.setOrderId(detail.getOrderId());
                orderDeliveryOne.setDetailId(detail.getId());
                orderDeliveryOne.setStandardId(detail.getStandardId());
                orderDeliveryOne.setGoodsId(detail.getGoodsId());
                orderDeliveryOne.setGoodsErpCode(detail.getGoodsErpCode());
                orderDeliveryOne.setBatchNo(batch.getKey());
                orderDeliveryOne.setGoodsQuantity(detail.getGoodsQuantity());
                orderDeliveryOne.setCreateUser(request.getOpUserId());
                orderDeliveryOne.setUpdateUser(request.getOpUserId());
                orderDeliveryOne.setCreateTime(request.getOpTime());
                orderDeliveryOne.setUpdateTime(request.getOpTime());
                orderDeliveryOne.setDeliveryQuantity(deliveryBatchQuantity);
                orderDeliveryOne.setExpiryDate(expiryDate);
                orderDeliveryOne.setProduceDate(ProduceDate);
            }
            deliveryList.add(orderDeliveryOne);
        }
    }

    private void updateOrderDeliverInfo(SaveOrderDeliveryListInfoRequest request, OrderDO order, Boolean allDelivery) {

        if (StringUtils.isNotBlank(request.getDeliveryNo()) && StringUtils.isNotBlank(request.getDeliveryCompany())) {
            order.setDeliveryCompany(request.getDeliveryCompany())
                    .setDeliveryNo(request.getDeliveryNo());
        }
        Integer orderStatus = order.getOrderStatus();
        if (allDelivery) {
            order.setOrderStatus(OrderStatusEnum.DELIVERED.getCode());
        } else {
            order.setOrderStatus(OrderStatusEnum.PARTDELIVERED.getCode());
        }
        //自动设置默认物流
        if (request.getDeliveryType() != null) {
            order.setDeliveryType(request.getDeliveryType());
        }else{
            order.setDeliveryType(1);
        }

        order.setDeliveryUser(request.getOpUserId());
        order.setDeliveryTime(request.getOpTime());
        order.setOpUserId(request.getOpUserId());
        order.setOpTime(request.getOpTime());

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getId, order.getId())
                .eq(OrderDO::getOrderStatus, orderStatus);
        Boolean flag = update(order, wrapper);
        if (!flag) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_CHANGE);
        }
    }

    /**
     * 校验订单状态
     *
     * @param order
     */
    private void validateOrderStatus(OrderDO order) {
        if (order == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }

        if (OrderStatusEnum.UNDELIVERED != OrderStatusEnum.getByCode(order.getOrderStatus())
                && OrderStatusEnum.UNAUDITED != OrderStatusEnum.getByCode(order.getOrderStatus())) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_ERROR);
        }
    }


    /**
     * 校验发货参数是否合理，并将同一批次，同一出库单的发货数量相加
     *
     * @param request
     */
    private SaveOrderDeliveryListInfoRequest validateDeliveryParam(SaveOrderDeliveryListInfoRequest request,
                                                                   Map<Long, OrderDetailDTO> mapDetail) {


        List<OrderDetailChangeDO> orderDetailChangeList = orderDetailChangeService.listByOrderId(request.getOrderId());
        Map<Long, OrderDetailChangeDO> changeList = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDO::getDetailId, one -> one, (k1, k2) -> k1));

        for (SaveOrderDeliveryInfoRequest deliveryRequest : request.getOrderDeliveryGoodsInfoList()) {
            OrderDetailDTO detail = mapDetail.get(deliveryRequest.getDetailId());
            OrderDetailChangeDO orderDetailChangeOne = changeList.get(deliveryRequest.getDetailId());
            if (detail == null) {
                throw new BusinessException(OrderErrorCode.ORDER_DETAIL_NOT_INFO);
            }
            Map<String, DeliveryInfoRequest> mapRequest = new HashMap<>();
            //同一商品发货数量
            Integer deliveryAllQuantity = 0;
            for (DeliveryInfoRequest one : deliveryRequest.getDeliveryInfoList()) {

                if (one.getDeliveryQuantity() > 0) {
                    if (StringUtils.isEmpty(one.getBatchNo()) || one.getExpiryDate() == null) {
                        throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
                    }
                }
                if (mapRequest.containsKey(one.getErpDeliveryNo() + "_" + one.getBatchNo() + "_" + one.getEasSendOrderId() )) {
                    DeliveryInfoRequest deliveryInfoRequest = mapRequest.get(one.getErpDeliveryNo() + "_" + one.getBatchNo() + "_" + one.getEasSendOrderId());
                    deliveryInfoRequest.setDeliveryQuantity(deliveryInfoRequest.getDeliveryQuantity() + one.getDeliveryQuantity());
                } else {
                    mapRequest.put(one.getErpDeliveryNo() + "_" + one.getBatchNo() + "_" + one.getEasSendOrderId(), one);
                }

                deliveryAllQuantity = deliveryAllQuantity + one.getDeliveryQuantity();
            }

            if (detail.getGoodsQuantity().compareTo(deliveryAllQuantity + orderDetailChangeOne.getDeliveryQuantity()) < 0) {
                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
            //将同一批次，同一出库单的发货数量相加
            deliveryRequest.setDeliveryInfoList(mapRequest.values().stream().collect(Collectors.toList()));
        }
        return request;
    }


    /**
     * 收货
     *
     * @param request
     * @return
     */
    @Override
    public Boolean receive(SaveOrderReceiveListInfoRequest request) {
        MqMessageBO mqMessageBO = _this.receiveMessage(request);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO receiveMessage(SaveOrderReceiveListInfoRequest request) {

        OrderDO order = getById(request.getOrderId());
        log.info("订单开始收货，订单单号,OrderNo:{}", order.getOrderNo());
        //检查收货状态
        validateReceiveOrderStatus(order);

        //订单状态修改
        updateOrderReceiveInfo(request.getOpUserId(), order);

        List<OrderDeliveryDO> deliveryList = new ArrayList<>();
        //校验并组装数据
        assembleResult(request, deliveryList, order.getCreateTime());

        if (CollectionUtil.isNotEmpty(request.getReceiveReceiptList())) {
            //删除里面的附件
            orderAttachmentService.deleteByOrderId(order.getId(), OrderAttachmentTypeEnum.RECEIPT_FILE, request.getOrderId());
            //保存文件附件
            orderAttachmentService.saveBatch(order.getId(), order.getOrderNo(), OrderAttachmentTypeEnum.RECEIPT_FILE, request.getReceiveReceiptList(), request.getOpUserId());

        }

        orderDeliveryService.updateBatchById(deliveryList);
        callOrderDetailChange(request);

        OrderLogDO orderLogInfo = new OrderLogDO();
        orderLogInfo.setLogContent("订单收货").setLogTime(request.getOpTime())
                .setOrderId(request.getOrderId());
        orderLogService.save(orderLogInfo);

        OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
        orderStatusLog.setOrderId(request.getOrderId()).setOrderStatus(OrderStatusEnum.RECEIVED.getCode()).setRemark("订单收货");
        orderStatusLogService.save(orderStatusLog);

        MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_ORDER_RETURN, Constants.TAG_ORDER_RECEIVED, order.getOrderNo(),order.getId().intValue());
        mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);


        log.info("订单结束收货，订单单号,OrderNo:{}", order.getOrderNo());
        return mqMessageTwoBO;
    }


    /**
     * @param request 明细集合
     */
    private void callOrderDetailChange(SaveOrderReceiveListInfoRequest request) {
        for (SaveOrderReceiveInfoRequest detailOne : request.getOrderReceiveGoodsInfoList()) {
            Integer receiveQuantity = 0;
            if (CollectionUtil.isNotEmpty(detailOne.getReceiveInfoList())) {
                for (ReceiveInfoRequest deliveryOne : detailOne.getReceiveInfoList()) {
                    receiveQuantity = receiveQuantity + deliveryOne.getReceiveQuantity();
                }
            }
            orderDetailChangeService.updateReceiveData(detailOne.getDetailId(), receiveQuantity);
            log.info("订单结束收货,调用DetailChange参数,detailId:{},receiveQuantity;{}", detailOne.getDetailId(), receiveQuantity);
        }
    }

    /**
     * 订单状态修改
     *
     * @param opUserId
     * @param order
     */
    private void updateOrderReceiveInfo(Long opUserId, OrderDO order) {
        order.setOrderStatus(OrderStatusEnum.RECEIVED.getCode());
        order.setReceiveUser(opUserId);
        order.setReceiveTime(new Date());

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getId, order.getId())
                .eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode());
        Boolean flag = update(order, wrapper);
        if (!flag) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_CHANGE);
        }
    }


    /**
     * 校验订单状态
     *
     * @param order
     */
    private void validateReceiveOrderStatus(OrderDO order) {
        if (order == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }

        if (!OrderStatusEnum.DELIVERED.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_ERROR);
        }
    }

    /**
     * 校验并组装参数
     *
     * @param request      收货入参
     * @param deliveryList 批次明细数据
     * @param createTime   下单时间
     */
    private void assembleResult(SaveOrderReceiveListInfoRequest request,
                                List<OrderDeliveryDO> deliveryList,
                                Date createTime) {
        //参数传入数据必须一致
        Boolean flag = false;

        List<OrderDeliveryDO> orderDeliveryList = orderDeliveryService.getOrderDeliveryList(request.getOrderId());
        Map<Long, OrderDeliveryDO> map = orderDeliveryList.stream().collect(Collectors.toMap(OrderDeliveryDO::getId, O -> O, (k1, k2) -> k1));
        for (SaveOrderReceiveInfoRequest detailOne : request.getOrderReceiveGoodsInfoList()) {
            Integer receiveQuantity = 0;
            if (CollectionUtil.isNotEmpty(detailOne.getReceiveInfoList())) {
                for (ReceiveInfoRequest deliveryOne : detailOne.getReceiveInfoList()) {
                    OrderDeliveryDO orderDeliveryOne = map.get(deliveryOne.getId());
                    if (orderDeliveryOne.getDeliveryQuantity() < deliveryOne.getReceiveQuantity()) {
                        throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
                    }

                    if (orderDeliveryOne.getDeliveryQuantity().compareTo(deliveryOne.getReceiveQuantity()) != 0) {
                        flag = true;
                    }

                    if (orderDeliveryOne == null) {
                        throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
                    }
                    receiveQuantity = receiveQuantity + deliveryOne.getReceiveQuantity();

                    OrderDeliveryDO receive = PojoUtils.map(deliveryOne, OrderDeliveryDO.class);
                    deliveryList.add(receive);
                }
            }
        }
        //跨年不允许退货
        if (flag && getCompareYear(createTime, new Date())) {
            throw new BusinessException(OrderErrorCode.ORDER_RETURN_GOODS_NOT_PASS_YEAR);
        }

    }

    /**
     * 比较年是否相同
     *
     * @param dateOne
     * @param dateTwo
     * @return
     */
    private Boolean getCompareYear(Date dateOne, Date dateTwo) {
        int yearOne = DateUtil.year(dateOne);
        int yearTwo = DateUtil.year(dateTwo);
        return yearOne != yearTwo;
    }

    /**
     * 获取3个月前订单商品 POP前台接口
     *
     * @param eid
     * @return
     */
    @Override
    public List<Long> getOrderGoodsByEid(Long eid) {
        List<Long> goodsList = new ArrayList<>();
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getBuyerEid, eid)
                .eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode())
                .ge(OrderDO::getCreateTime, DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -3))
                .orderByAsc(OrderDO::getCreateTime);
        List<OrderDO> list = list(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> ids = list.stream().map(order -> order.getId()).collect(Collectors.toList());
            List<OrderDetailDO> detailList = orderDetailService.getOrderDetailByOrderIds(ids);

            for (OrderDO order : list) {
                for (OrderDetailDO one : detailList) {
                    if (order.getId().equals(one.getOrderId())) {
                        if (!goodsList.contains(one.getGoodsId())) {
                            goodsList.add(one.getGoodsId());
                        }
                    }
                }
            }
        }

        return goodsList;
    }

    /**
     * 获取3个月前订单商品POP后端
     *
     * @param eid
     * @return
     */
    @Override
    public List<OrderHistoryGoodsDTO> getOrderGoodsByEidPOPAdmin(Long eid) {
        Map<String, OrderHistoryGoodsDTO> map = new LinkedMap();
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getBuyerEid, eid)
                .eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode())
                .ge(OrderDO::getCreateTime, DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -3))
                .orderByDesc(OrderDO::getCreateTime);
        List<OrderDO> list = list(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> ids = list.stream().map(order -> order.getId()).collect(Collectors.toList());
            List<OrderDetailDO> detailList = orderDetailService.getOrderDetailByOrderIds(ids);

            for (OrderDO order : list) {
                for (OrderDetailDO one : detailList) {
                    if (order.getId().equals(one.getOrderId())) {
                        if (!map.containsKey(one.getGoodsSkuId() + "__" + order.getDistributorEid())) {
                            OrderHistoryGoodsDTO dto = new OrderHistoryGoodsDTO();
                            dto.setGoodsId(one.getGoodsId());
                            dto.setDistributorEid(order.getDistributorEid());
                            dto.setDistributorGoodsId(one.getDistributorGoodsId());
                            dto.setGoodsSkuId(one.getGoodsSkuId());
                            map.put(one.getGoodsSkuId() + "__" + order.getDistributorEid(), dto);
                        }
                    }
                }
            }
        }

        List<OrderHistoryGoodsDTO> result = map.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
        return result;
    }

    @Override
    public boolean updatePaymentStatus(UpdateOrderPaymentStatusRequest request) {
        OrderDO entity = new OrderDO();
        entity.setPaymentStatus(request.getPaymentStatus());
        if (request.getPaymentTime() != null) {
            entity.setPaymentTime(request.getPaymentTime());
        }
        if (StringUtils.isNotBlank(request.getPaySource())) {
            entity.setPaySource(request.getPaySource());
        }
        if (StringUtils.isNotBlank(request.getPayWay())) {
            entity.setPayChannel(request.getPayWay());
        }
        LambdaQueryWrapper<OrderDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(OrderDO::getId, request.getOrderId());
        return this.update(entity, lambdaQueryWrapper);
    }


    @Override
    public boolean updatePaymentPayMethod(UpdateOrderPaymentMethodRequest request) {
        OrderDO orderDO = this.getById(request.getOrderId());
        if (orderDO.getPaymentMethod() != 0) {
            throw new BusinessException(PaymentErrorCode.ORDER_HAD_PAID);
        }
        OrderDO entity = new OrderDO();
        entity.setPaymentAmount(request.getPaymentAmount());
        entity.setPaymentMethod(request.getPaymentMethodId());
        if (request.getPaymentAmount() != null) {
            entity.setPaymentAmount(request.getPaymentAmount());
        }
        LambdaQueryWrapper<OrderDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(OrderDO::getId, request.getOrderId());

        return this.update(entity, lambdaQueryWrapper);
    }

    /**
     * @param ticketDiscountNos
     * @return
     */
    @Override
    public Map<String, List<OrderDTO>> getOrderByTicketDiscountNo(List<String> ticketDiscountNos) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDO::getTicketDiscountNo, ticketDiscountNos);
        List<OrderDTO> list = PojoUtils.map(list(wrapper), OrderDTO.class);
        Map<String, List<OrderDTO>> result = new HashMap<>();
        for (OrderDTO one : list) {
            if (result.containsKey(one.getTicketDiscountNo())) {
                List<OrderDTO> order = result.get(one.getTicketDiscountNo());
                order.add(one);
            } else {
                List<OrderDTO> order = new ArrayList<>();
                order.add(one);
                result.put(one.getTicketDiscountNo(), order);
            }
        }
        return result;
    }


    /**
     * 发票管理详情
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getOrderInvoiceInfoPage(QueryInvoicePageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        List<Long> list = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getInvoiceNo())) {
            List<OrderInvoiceDTO> orderInvoiceList = orderInvoiceService.getOrderInvoiceLikeInvoiceNo(request.getInvoiceNo());
            list = orderInvoiceList.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(request.getBuyerEname())) {
            wrapper.lambda().like(OrderDO::getBuyerEname, request.getBuyerEname());
        }

       if(request.getStartInvoiceTime() != null && request.getEndInvoiceTime() != null ){
           QueryWrapper<OrderInvoiceApplyDO> applyWrapper = new QueryWrapper<>();
           applyWrapper.lambda().le(OrderInvoiceApplyDO :: getCreateTime,DateUtil.endOfDay(request.getEndInvoiceTime()))
                   .ge(OrderInvoiceApplyDO::getCreateTime, DateUtil.beginOfDay(request.getStartInvoiceTime()));
           List<OrderInvoiceApplyDO> applyList = orderInvoiceApplyService.list(applyWrapper);
           if(CollectionUtil.isNotEmpty(applyList)){
               List<Long> ids = applyList.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
               wrapper.lambda().in(OrderDO::getId, ids);
           }else{
              return new Page<>();
           }
       }

        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().like(OrderDO::getOrderNo, request.getOrderNo());
        }
        if (request.getStartOrderTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartOrderTime()));
        }
        if (request.getEndOrderTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndOrderTime()));
        }

        if (request.getInvoiceStatus() != null && request.getInvoiceStatus() != 0) {
            if (request.getInvoiceStatus().equals(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode())) {
                wrapper.lambda().in(OrderDO::getInvoiceStatus, new ArrayList<Integer>() {{
                    add(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode());
                    add(OrderInvoiceApplyStatusEnum.DEFAULT_VALUE_APPLY.getCode());
                }}).orderByDesc(OrderDO::getCreateTime);
            } else if (request.getInvoiceStatus() == 7) {
                wrapper.lambda().eq(OrderDO::getInvoiceStatus, OrderInvoiceApplyStatusEnum.PART_APPLIED.getCode())
                        .eq(OrderDO::getErpReceivableStatus, 2)
                        .orderByDesc(OrderDO::getInvoiceTime);
            } else {
                wrapper.lambda().eq(OrderDO::getInvoiceStatus, request.getInvoiceStatus())
                        .orderByDesc(OrderDO::getInvoiceTime);
            }
        } else {
            wrapper.lambda().orderByDesc(OrderDO::getInvoiceTime);
        }

        if (request.getPaymentMethod() != null && request.getPaymentMethod() != 0) {
            wrapper.lambda().eq(OrderDO::getPaymentMethod, request.getPaymentMethod());
        }
        if (CollectionUtil.isNotEmpty(request.getEidLists())) {
            wrapper.lambda().in(OrderDO::getSellerEid, request.getEidLists());
        } else if (request.getDistributorEid() != null && request.getDistributorEid() != 0) {
            wrapper.lambda().in(OrderDO::getSellerEid, request.getDistributorEid());
        }

        if(request.getDepartmentId() != null){
            wrapper.lambda().eq(OrderDO::getDepartmentId, request.getDepartmentId());
        }

        if (request.getOrderType() != null && request.getOrderType() != 0) {
            wrapper.lambda().eq(OrderDO::getOrderType, request.getOrderType());
        }
        if (CollectionUtil.isNotEmpty(request.getContacterIdList())) {
            wrapper.lambda().in(OrderDO::getContacterId, request.getContacterIdList());
        }

        if(request.getOrderStatus() != null && request.getOrderStatus() != 0 ){
            wrapper.lambda().eq(OrderDO::getOrderStatus, request.getOrderStatus());
        }else{
            wrapper.lambda().ge(OrderDO::getOrderStatus, OrderStatusEnum.PARTDELIVERED.getCode())
                    .le(OrderDO::getOrderStatus,OrderStatusEnum.RECEIVED.getCode());
        }

        if(StringUtils.isNotBlank(request.getCityCode()) ){
            wrapper.lambda().eq(OrderDO::getBuyerCityCode, request.getCityCode());
        }
        if(StringUtils.isNotBlank(request.getProvinceCode()) ){
            wrapper.lambda().eq(OrderDO::getBuyerProvinceCode, request.getProvinceCode());
        }
        if(StringUtils.isNotBlank(request.getRegionCode()) ){
            wrapper.lambda().eq(OrderDO::getBuyerRegionCode, request.getRegionCode());
        }

        wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO::getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode());

        if (CollectionUtil.isNotEmpty(list)) {
            wrapper.lambda().in(OrderDO::getId, list);
        }
        if(request.getId() != null && request.getId() != 0){
            wrapper.lambda().eq(OrderDO::getId, request.getId());
        }

        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> orderPage = PojoUtils.map(page, OrderDTO.class);

        return orderPage;
    }


    /**
     * 根据采购商信息和下单时间获取已发货订单信息
     *
     * @param request
     * @return
     */
    @Override
    public List<OrderAndDetailedAllInfoDTO> getOrderAllInfoByBuyerEidAndCreateTime(QueryOrderUseAgreementRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDO::getBuyerEid, request.getBuyerEids())
                .eq(OrderDO::getCustomerErpCode, request.getEasAccount())
                .eq(OrderDO::getSellerEid, request.getDistributorEid())
                .ge(OrderDO::getCreateTime, request.getStartCreateTime())
                .le(OrderDO::getCreateTime, request.getEndCreateTime())
                .ge(OrderDO::getOrderStatus, OrderStatusEnum.RECEIVED.getCode())
                .orderByAsc(OrderDO::getId);
        List<OrderDO> orderList = list(wrapper);
        List<OrderAndDetailedAllInfoDTO> result = PojoUtils.map(orderList, OrderAndDetailedAllInfoDTO.class);
        if (CollectionUtil.isNotEmpty(result)) {
            List<Long> ids = orderList.stream().map(order -> order.getId()).collect(Collectors.toList());
            List<OrderDetailDO> detailList = orderDetailService.getOrderDetailByOrderIds(ids);
            List<OrderDetailChangeDO> orderDetailChangeList = orderDetailChangeService.listByOrderIds(ids);
            Map<Long, OrderDetailChangeDO> changeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDO::getDetailId, o -> o));
            for (OrderAndDetailedAllInfoDTO orderOne : result) {
                List<OrderDetailByAgreementDTO> detailLists = new ArrayList<>();
                for (OrderDetailDO detailOne : detailList) {
                    if (orderOne.getId().equals(detailOne.getOrderId())) {
                        OrderDetailChangeDO orderDetailChangeDO = changeMap.get(detailOne.getId());
                        OrderDetailByAgreementDTO one = PojoUtils.map(detailOne, OrderDetailByAgreementDTO.class);
                        one.setDeliveryAmount(orderDetailChangeDO.getDeliveryAmount());
                        one.setDeliveryQuantity(orderDetailChangeDO.getDeliveryQuantity());
                        detailLists.add(one);
                    }
                }
                orderOne.setDetailLists(detailLists);
            }
        }
        return result;
    }


    @Override
    public boolean updateOrderCashDiscountAmount(UpdateOrderCashDiscountAmountRequest request) {
        Long orderId = request.getOrderId();

        List<OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoList = request.getOrderDetailCashDiscountInfoList();
        Map<Long, OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoMap = orderDetailCashDiscountInfoList.stream().collect(Collectors.toMap(OrderDetailCashDiscountInfoDTO::getOrderDetailId, Function.identity()));

        List<OrderCashDiscountDO> orderCashDiscountDOList = CollUtil.newArrayList();
        List<OrderDetailDTO> orderDetailList = orderDetailService.getOrderDetailInfo(orderId);
        for (OrderDetailDTO orderDetail : orderDetailList) {
            OrderDetailCashDiscountInfoDTO orderDetailCashDiscountInfo = orderDetailCashDiscountInfoMap.get(orderDetail.getId());
            if (orderDetailCashDiscountInfo == null) {
                continue;
            }

            List<OrderDetailCashDiscountDO> orderDetailCashDiscountDOList = CollUtil.newArrayList();
            for (CashDiscountAgreementInfoDTO cashDiscountAgreementInfo : orderDetailCashDiscountInfo.getCashDiscountAgreementInfoList()) {
                OrderDetailCashDiscountDO orderDetailCashDiscountDO = new OrderDetailCashDiscountDO();
                orderDetailCashDiscountDO.setOrderId(orderId);
                orderDetailCashDiscountDO.setDetailId(orderDetail.getId());
                orderDetailCashDiscountDO.setGoodsId(orderDetail.getGoodsId());
                orderDetailCashDiscountDO.setGoodsAmount(orderDetail.getGoodsAmount());
                orderDetailCashDiscountDO.setAgreementId(cashDiscountAgreementInfo.getAgreementId());
                orderDetailCashDiscountDO.setAgreementVersion(cashDiscountAgreementInfo.getVersion());
                orderDetailCashDiscountDO.setPolicyValue(cashDiscountAgreementInfo.getPolicyValue());
                orderDetailCashDiscountDO.setDiscountAmount(cashDiscountAgreementInfo.getDiscountAmount());
                orderDetailCashDiscountDO.setOpUserId(request.getOpUserId());
                orderDetailCashDiscountDOList.add(orderDetailCashDiscountDO);

                OrderCashDiscountDO orderCashDiscountDO = new OrderCashDiscountDO();
                orderCashDiscountDO.setOrderId(orderId);
                orderCashDiscountDO.setOrderNo(request.getOrderNo());
                orderCashDiscountDO.setAgreementId(cashDiscountAgreementInfo.getAgreementId());
                orderCashDiscountDO.setAgreementVersion(cashDiscountAgreementInfo.getVersion());
                orderCashDiscountDO.setDiscountAmount(cashDiscountAgreementInfo.getDiscountAmount());
                orderCashDiscountDO.setOpUserId(request.getOpUserId());
                orderCashDiscountDOList.add(orderCashDiscountDO);
            }
            orderDetailCashDiscountService.saveBatch(orderDetailCashDiscountDOList);

            OrderDetailDO orderDetailDO = new OrderDetailDO();
            orderDetailDO.setId(orderDetail.getId());
            orderDetailDO.setCashDiscountAmount(orderDetailCashDiscountDOList.stream().map(OrderDetailCashDiscountDO::getDiscountAmount).reduce(BigDecimal::add).get());
            orderDetailDO.setOpUserId(request.getOpUserId());
            orderDetailService.updateById(orderDetailDO);

            orderDetailChangeService.updateCashDiscountAmountByDetailId(orderDetail.getId(), orderDetailDO.getCashDiscountAmount());
        }
        Map<Long, List<OrderCashDiscountDO>> collectMap = orderCashDiscountDOList.stream().collect(Collectors.groupingBy(OrderCashDiscountDO::getAgreementId));
        List<OrderCashDiscountDO> insertCashDiscountDOList = Lists.newArrayList();
        for (Map.Entry<Long, List<OrderCashDiscountDO>> entry : collectMap.entrySet()) {
            List<OrderCashDiscountDO> cashDiscountDOList =  entry.getValue();
            OrderCashDiscountDO orderCashDiscountDO = new OrderCashDiscountDO();
            orderCashDiscountDO.setOrderId(orderId);
            orderCashDiscountDO.setOrderNo(request.getOrderNo());
            orderCashDiscountDO.setAgreementId(entry.getKey());
            orderCashDiscountDO.setAgreementVersion(cashDiscountDOList.stream().findFirst().get().getAgreementVersion());
            orderCashDiscountDO.setDiscountAmount(cashDiscountDOList.stream().map(OrderCashDiscountDO::getDiscountAmount).reduce(BigDecimal::add).get());
            orderCashDiscountDO.setOpUserId(request.getOpUserId());
            insertCashDiscountDOList.add(orderCashDiscountDO);
        }
        orderCashDiscountService.saveBatch(insertCashDiscountDOList);

        OrderDO orderDO = new OrderDO();
        orderDO.setId(orderId);
        orderDO.setCashDiscountAmount(orderCashDiscountDOList.stream().map(OrderCashDiscountDO::getDiscountAmount).reduce(BigDecimal::add).get());
        orderDO.setOpUserId(request.getOpUserId());
        this.updateById(orderDO);

        return true;
    }


    /**
     * 修改回执单信息
     *
     * @param orderId            订单id
     * @param receiveReceiptList 回执单信息
     * @param opUserId           操作人
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateReceiveReceipt(Long orderId, List<String> receiveReceiptList, Long opUserId) {
        OrderDO order = getById(orderId);
        if (order == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }
        //删除里面的附件
        orderAttachmentService.deleteByOrderId(order.getId(), OrderAttachmentTypeEnum.RECEIPT_FILE, order.getId());
        //保存文件附件
        orderAttachmentService.saveBatch(order.getId(), order.getOrderNo(), OrderAttachmentTypeEnum.RECEIPT_FILE, receiveReceiptList, opUserId);
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long id, OrderStatusEnum originalStatus, OrderStatusEnum newStatus, Long opUserId, String remark) {
        OrderDO orderDO = this.getById(id);

        if (originalStatus != null && OrderStatusEnum.getByCode(orderDO.getOrderStatus()) != originalStatus) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_CHANGE);
        }

        OrderDO entity = new OrderDO();
        entity.setOrderStatus(newStatus.getCode());
        entity.setOpUserId(opUserId);

        QueryWrapper<OrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderDO::getId, id)
                .eq(OrderDO::getOrderStatus, originalStatus.getCode())
                .last("limit 1");

        boolean result = this.update(entity, queryWrapper);
        if (!result) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_CHANGE);
        }

        // 记录订单状态变更日志
        OrderStatusLogDO orderStatusLogDO = new OrderStatusLogDO();
        orderStatusLogDO.setOrderId(id);
        orderStatusLogDO.setOrderStatus(newStatus.getCode());
        orderStatusLogDO.setRemark(remark);
        orderStatusLogDO.setOpUserId(opUserId);
        orderStatusLogService.save(orderStatusLogDO);

        return true;
    }

    /**
     * 根据订单状态获取卖家订单数量
     *
     * @param orderStatus 订单状态
     * @param eidList     企业id
     * @param type        1：以岭管理员 2：以岭本部非管理员 3：非以岭人员
     * @param userId      当前登录用户
     * @return
     */
    @Override
    public Integer getSellerOrderNumberByStatus(Integer orderStatus, List<Long> eidList, Integer type, Long userId) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderStatus, orderStatus)
                .eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode())
                .eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO::getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode());
        if (2 == type) {
            wrapper.lambda().eq(OrderDO::getContacterId, userId);
        } else {
            wrapper.lambda().in(OrderDO::getSellerEid, eidList);
        }
        return count(wrapper);
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
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderStatus, orderStatus)
                .eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO::getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode())
                .in(OrderDO::getBuyerEid, eidList);
        return count(wrapper);
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
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getPaymentStatus, paymentStatus)
                .eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode())
                .eq(OrderDO::getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .in(OrderDO::getBuyerEid, eidList);
        return count(wrapper);
    }

    @Override
    public OrderDTO getOrderByErpReceivableNo(String erpReceivableNo) {
        QueryWrapper<OrderDeliveryReceivableDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryReceivableDO::getErpReceivableNo, erpReceivableNo)
                .orderByDesc(OrderDeliveryReceivableDO::getUpdateTime);
        List<OrderDeliveryReceivableDO> list = orderDeliveryReceivableService.list(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            Long orderId = list.get(0).getOrderId();
            OrderDO order = getById(orderId);
            return PojoUtils.map(order, OrderDTO.class);
        }

        return null;
    }

    /**
     * 二级商5天后直接收货
     *
     * @param eidList 以岭直采企业
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> secondBusinessAutomaticReceive(List<Long> eidList) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode())
                .eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode())
                .notIn(OrderDO::getSellerEid, eidList);
        List<OrderDO> orderList = list(wrapper);
        List<Long> orderIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderList)) {

            for (OrderDO one : orderList) {
                if (DateUtil.offsetDay(one.getDeliveryTime(), 5).compareTo(new Date()) <= 0) {
                    orderIdList.add(one.getId());
                }
            }
            if (CollectionUtil.isNotEmpty(orderIdList)) {
                List<Long> idList = new ArrayList<>();
                for (Long id : orderIdList) {
                    automaticReceiveUpdateOrder(id, idList);
                }
                if (CollectionUtil.isNotEmpty(idList)) {
                    List<OrderDeliveryDO> orderDeliveryList = orderDeliveryService.listByOrderIds(idList);
                    for (OrderDeliveryDO one : orderDeliveryList) {
                        one.setReceiveQuantity(one.getDeliveryQuantity());
                    }
                    orderDeliveryService.updateBatchById(orderDeliveryList);
                    automaticReceiveSaveOrderLog(idList);
                    automaticReceiveCallOrderDetailChange(orderDeliveryList);
                }

            }
        }
        return orderIdList;
    }

    /**
     * 预订单导出
     *
     * @param request
     * @return
     */
    @Override
    public List<OrderExpectExportDTO> orderExpectExport(QueryOrderExpectPageRequest request) {
        if (request.getStartCreatTime() != null) {
            request.setStartCreatTime(DateUtil.beginOfDay(request.getStartCreatTime()));
        }
        if (request.getEndCreatTime() != null) {
            request.setEndCreatTime(DateUtil.endOfDay(request.getEndCreatTime()));
        }

        List<OrderExpectExportDTO> orderExpectList = this.baseMapper.orderExpectExport(request);
        return orderExpectList;
    }

    @Override
    public OrderExportReportDTO orderExportReport(QueryOrderExportReportPageRequest request) {
        OrderExportReportDTO result = new OrderExportReportDTO();
        List<OrderExportReportDetailDTO> orderPaymentReport = this.baseMapper.getOrderPaymentReport(request);
        List<OrderExportReportDetailDTO> orderQuantityReport = this.baseMapper.getOrderQuantityReport(request);
        result.setOrderPaymentReportList(orderPaymentReport);
        result.setOrderQuantityReportList(orderQuantityReport);
        return result;
    }






    @Override
    public List<OrderDO> listCustomerNotConfirmByOrderNos(List<String> orderNos) {

        QueryWrapper<OrderDO> orderWrapper = new QueryWrapper<>();
        orderWrapper.lambda().in(OrderDO::getCustomerConfirmStatus,CustomerConfirmEnum.TRANSFER.getCode(),CustomerConfirmEnum.NOTCONFIRM.getCode());
        orderWrapper.lambda().le(OrderDO::getCustomerConfirmTime, DateUtil.yesterday());
        orderWrapper.lambda().eq(OrderDO::getOrderSource, OrderSourceEnum.SA.getCode());
        orderWrapper.lambda().eq(OrderDO::getOrderType, OrderTypeEnum.B2B.getCode());
        orderWrapper.lambda().eq(OrderDO::getOrderStatus,OrderStatusEnum.UNAUDITED.getCode());

        if (CollectionUtil.isNotEmpty(orderNos)) {

            orderWrapper.lambda().in(OrderDO::getOrderNo, orderNos);
        }
        // 默认一次执行100条，防止自动取消时出现事务超时
        orderWrapper.last("limit 100");

        List<OrderDO> orderDOList = this.getBaseMapper().selectList(orderWrapper);

        return orderDOList;
    }

    private void automaticReceiveCallOrderDetailChange(List<OrderDeliveryDO> orderDeliveryList) {
        Map<Long, Integer> map = new HashMap<>();
        for (OrderDeliveryDO one : orderDeliveryList) {
            if (map.containsKey(one.getDetailId())) {
                Integer integer = map.get(one.getDetailId());
                map.put(one.getDetailId(), integer + one.getDeliveryQuantity());
            } else {
                map.put(one.getDetailId(), one.getDeliveryQuantity());
            }
        }
        if (map != null) {
            for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                orderDetailChangeService.updateReceiveData(entry.getKey(), entry.getValue());
            }
        }

    }

    private void automaticReceiveSaveOrderLog(List<Long> idList) {
        List<OrderLogDO> listLog = new ArrayList<>();
        List<OrderStatusLogDO> statusLogList = new ArrayList<>();
        for (Long id : idList) {
            OrderLogDO orderLogInfo = new OrderLogDO();
            orderLogInfo.setLogContent("订单收货")
                    .setLogTime(new Date())
                    .setOrderId(id);
            listLog.add(orderLogInfo);

            OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
            orderStatusLog.setOrderId(id)
                    .setOrderStatus(OrderStatusEnum.RECEIVED.getCode())
                    .setRemark("订单收货");
            statusLogList.add(orderStatusLog);
        }
        orderLogService.saveBatch(listLog);
        orderStatusLogService.saveBatch(statusLogList);
    }


    private void automaticReceiveUpdateOrder(Long orderId, List<Long> idList) {
        OrderDO order = new OrderDO();
        order.setOrderStatus(OrderStatusEnum.RECEIVED.getCode());
        order.setReceiveUser(0L);
        order.setReceiveTime(new Date());

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getId, orderId)
                .eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode());
        Boolean flag = update(order, wrapper);
        if (flag) {
            idList.add(orderId);
        }
    }


    private OrderNumberDTO setOrderNumber(Integer type, List<Long> eids, Long userId, Integer orderType, Long departmentId) {
        OrderNumberDTO result = new OrderNumberDTO();
        int count = getOrderCountByDate(type, eids, userId, DateUtil.parse(DateUtil.today()), false, orderType,departmentId);
        result.setTodayOrderNum(count);

        int yesterdayCount = getOrderCountByDate(type, eids, userId, DateUtil.offsetDay(DateUtil.parse(DateUtil.today()), -1), true, orderType,departmentId);
        result.setYesterdayOrderNum(yesterdayCount);

        int yearOrderCount = getOrderCountByDate(type, eids, userId, DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -12), false, orderType,departmentId);
        result.setYearOrderNum(yearOrderCount);

        return result;
    }

    private int getOrderCountByDate(Integer type, List<Long> eids, Long userId, Date date, Boolean flag, Integer orderType,Long departmentId) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .ne(OrderDO::getOrderStatus, OrderStatusEnum.UNAUDITED.getCode())
                .ge(OrderDO::getCreateTime, date);
        if (orderType != null && orderType != 0) {
            wrapper.lambda().eq(OrderDO::getOrderType, orderType);
        }
        if (flag) {
            wrapper.lambda().lt(OrderDO::getCreateTime, DateUtil.parse(DateUtil.today()));
        }

        if (OrderRangeTypeEnum.ORDER_PURCHASE_RANGE_TYPE.getCode().equals(type)) {
            wrapper.lambda().in(OrderDO::getBuyerEid, eids);
        } else if (OrderRangeTypeEnum.ORDER_SALE_ADMIN_RANGE_TYPE.getCode().equals(type)) {
            wrapper.lambda().in(OrderDO::getSellerEid, eids);
        } else if (OrderRangeTypeEnum.ORDER_SALE_YILINGH_RANGE_TYPE.getCode().equals(type)) {
            wrapper.lambda().eq(OrderDO::getContacterId, userId)
                    .in(OrderDO::getSellerEid, eids);
        } else if (OrderRangeTypeEnum.ORDER_SALE_NOT_YILINGH_RANGE_TYPE.getCode().equals(type)) {
            wrapper.lambda().in(OrderDO::getSellerEid, eids);
            if(departmentId != null){
                wrapper.lambda().eq(OrderDO :: getDepartmentId,departmentId);
            }

        }
        return count(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderCustomerConfirmStatus(Long id, CustomerConfirmEnum originalStatus, CustomerConfirmEnum newStatus, Long opUserId, String remark) {
        OrderDO orderDO = this.getById(id);

        if (originalStatus != null && CustomerConfirmEnum.getByCode(orderDO.getCustomerConfirmStatus()) != originalStatus) {

            throw new BusinessException(OrderErrorCode.ORDER_INFO_CUSTOMER_STATUS_ERROR);
        }

        OrderDO entity = new OrderDO();
        entity.setCustomerConfirmStatus(newStatus.getCode());
        entity.setOpUserId(opUserId);
        entity.setCustomerConfirmTime(new Date());

        QueryWrapper<OrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderDO::getId, id)
                .eq(OrderDO::getCustomerConfirmStatus, originalStatus.getCode())
                .last("limit 1");

        boolean result = this.update(entity, queryWrapper);

        if (!result) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_CUSTOMER_STATUS_ERROR);
        }

        return true;
    }

    /**
     * 根据下单人或者商务联系人统计金额(返回下单总金额，下单数量,支付总金额)
     *
     * @param queryRequest 查询条件
     * @param type         1，根据下单人查询，2，根据商务联系
     * @return
     */
    @Override
    public OrderSumDTO sumOrderReportByUserIdList(OrderSumQueryRequest queryRequest, Integer type) {

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        if (CollectionUtil.isNotEmpty(queryRequest.getUserIdList())) {

            if (CompareUtil.compare(1, type) == 0) {
                wrapper.lambda().in(OrderDO::getCreateUser, queryRequest.getUserIdList());
            } else {
                wrapper.lambda().in(OrderDO::getContacterId, queryRequest.getUserIdList());
            }
        }

        if (Objects.nonNull(queryRequest.getEid()) && queryRequest.getEid() != 0) {
            wrapper.lambda().eq(OrderDO::getBuyerEid, queryRequest.getEid());
        }
        if (ObjectUtil.isNotNull(queryRequest.getStartCreateTime())) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(queryRequest.getStartCreateTime()));
        }
        if (ObjectUtil.isNotNull(queryRequest.getEndCreateTime())) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(queryRequest.getEndCreateTime()));
        }

        wrapper.lambda().in(OrderDO::getOrderStatus, queryRequest.getOrderStatusList());
        wrapper.select("IFNULL(sum(total_amount),0.00) as 'total_amount',IFNULL(sum(payment_amount),0.00) as 'total_pay_amount',count(1) as order_num ");

        Map<String, Object> result = this.getMap(wrapper);
        BigDecimal totalAmount = new BigDecimal(String.valueOf(result.get("total_amount")));
        BigDecimal total_pay_amount = new BigDecimal(String.valueOf(result.get("total_pay_amount")));
        Integer orderNum = Integer.valueOf(String.valueOf(result.get("order_num")));

        return OrderSumDTO.builder().totalOrderMoney(totalAmount).orderTotalNumer(orderNum).totalPayAmount(total_pay_amount).build();
    }

    @Override
    public List<OrderDO> selectOrderListByCreateUserId(OrderSumQueryRequest queryRequest) {

        QueryWrapper<OrderDO> queryWrapper = getOrderQueryWrapper(queryRequest.getUserIdList(), queryRequest.getStartCreateTime(),
                queryRequest.getEndCreateTime(), queryRequest.getIsAscByCreateTime(), queryRequest.getOrderStatusList());

        return this.getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public Page<OrderDO> selectOrderPageByCreateUserId(OrderSumQueryPageRequest queryRequest) {

        QueryWrapper<OrderDO> wrapper = getOrderQueryWrapper(queryRequest.getUserIdList(), queryRequest.getStartCreateTime(),
                queryRequest.getEndCreateTime(), queryRequest.getIsAscByCreateTime(), queryRequest.getOrderStatusList());

        return this.getBaseMapper().selectPage(queryRequest.getPage(), wrapper);
    }

    private QueryWrapper<OrderDO> getOrderQueryWrapper(List<Long> createOrderUserIdList, Date startCreateTime, Date endCreateTime, Boolean isAscByCreateTime, List<Integer> orderStatusList) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDO::getCreateUser, createOrderUserIdList);
        if (ObjectUtil.isNotNull(startCreateTime)) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(startCreateTime));
        }
        if (ObjectUtil.isNotNull(endCreateTime)) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(endCreateTime));
        }
        if (isAscByCreateTime) {
            wrapper.lambda().orderByAsc(OrderDO::getCreateTime);
        } else {
            wrapper.lambda().orderByDesc(OrderDO::getCreateTime);
        }
        wrapper.lambda().in(OrderDO::getOrderStatus, orderStatusList);

        return wrapper;
    }

    @Override
    public List<OrderDO> selectOrderListByContacterId(OrderSumQueryRequest queryRequest) {

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();

        wrapper.lambda().in(OrderDO::getContacterId, queryRequest.getUserIdList());
        if (ObjectUtil.isNotNull(queryRequest.getStartCreateTime())) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(queryRequest.getStartCreateTime()));
        }
        if (ObjectUtil.isNotNull(queryRequest.getEndCreateTime())) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(queryRequest.getEndCreateTime()));
        }
        if (queryRequest.getIsAscByCreateTime()) {
            wrapper.lambda().orderByAsc(OrderDO::getCreateTime);
        } else {
            wrapper.lambda().orderByDesc(OrderDO::getCreateTime);
        }
        wrapper.lambda().in(OrderDO::getOrderStatus, queryRequest.getOrderStatusList());

        return this.getBaseMapper().selectList(wrapper);
    }

    @Override
    public Page<OrderDO> selectOrderPageByContacterId(OrderSumQueryPageRequest queryRequest) {

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDO::getContacterId, queryRequest.getUserIdList());
        if (ObjectUtil.isNotNull(queryRequest.getStartCreateTime())) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(queryRequest.getStartCreateTime()));
        }
        if (ObjectUtil.isNotNull(queryRequest.getEndCreateTime())) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(queryRequest.getEndCreateTime()));
        }
        if (queryRequest.getIsAscByCreateTime()) {
            wrapper.lambda().orderByAsc(OrderDO::getCreateTime);
        } else {
            wrapper.lambda().orderByDesc(OrderDO::getCreateTime);
        }
        wrapper.lambda().in(OrderDO::getOrderStatus, queryRequest.getOrderStatusList());

        return this.getBaseMapper().selectPage(queryRequest.getPage(), wrapper);
    }

    @Override
    public List<OrderDO> selectReceiveOrderByDistributorGoodId(Long distributorGoodsId) {

        return this.getBaseMapper().selectReceiveOrderByDistributorGoodId(distributorGoodsId);
    }


    @Override
    public List<OrderDO> selectReceiveOrderByGoodId(Long goodsId, List<Long> buyerEidList) {

        return this.getBaseMapper().selectReceiveOrderByGoodId(goodsId, buyerEidList);
    }

    @Override
    public Page<OrderDO> pageReceiveOrderByGoodId(GoodDetailSumQueryPageRequest goodDetailPageRequest) {

        return this.getBaseMapper().pageReceiveOrderByGoodId(goodDetailPageRequest.getPage(), goodDetailPageRequest);
    }

    @Override
    public Page<OrderDO> selectBuyerOrdersByEids(QueryBuyerOrderPageRequest request) {

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDO::getBuyerEid, request.getBuyerEidList());
        wrapper.lambda().in(OrderDO::getOrderStatus, request.getOrderStatusList());

        if (request.getIsAscByCreateTime()) {

            wrapper.lambda().orderByAsc(OrderDO::getCreateTime);
        } else {
            wrapper.lambda().orderByDesc(OrderDO::getCreateTime);
        }

        if (Objects.nonNull(request.getOrderNo())) {
            wrapper.lambda().eq(OrderDO::getOrderNo, request.getOrderNo());
        }
        if (Objects.nonNull(request.getStartCreateTime())) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (Objects.nonNull(request.getEndCreateTime())) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }

        return this.getBaseMapper().selectPage(request.getPage(), wrapper);
    }

    /**
     * 获取订单账期列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderB2BPaymentDTO> getOrderB2BPaymentList(OrderB2BPaymentRequest request) {
        return this.getBaseMapper().getOrderB2BPaymentList(request.getPage(), request);
    }

    @Override
    public OrderSumDTO sumOrderReportByBuyerEid(Long buyerEid, List<Integer> orderStatusList) {


        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDO::getBuyerEid, buyerEid);

        wrapper.lambda().in(OrderDO::getOrderStatus, orderStatusList);
        wrapper.select("IFNULL(sum(total_amount),0.00) as 'total_amount',IFNULL(sum(payment_amount),0.00) as 'total_pay_amount',count(1) as order_num ");

        Map<String, Object> result = this.getMap(wrapper);
        BigDecimal totalAmount = new BigDecimal(String.valueOf(result.get("total_amount")));
        BigDecimal total_pay_amount = new BigDecimal(String.valueOf(result.get("total_pay_amount")));
        Integer orderNum = Integer.valueOf(String.valueOf(result.get("order_num")));

        return OrderSumDTO.builder().totalOrderMoney(totalAmount).orderTotalNumer(orderNum).totalPayAmount(total_pay_amount).build();
    }

    @Override
    public Page<OrderDO> pageB2bSettlementList(QueryB2BSettlementPageReuest queryB2BSettlementPageReuest) {

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderType, OrderTypeEnum.B2B.getCode());
        wrapper.lambda().in(OrderDO::getPaymentMethod, PaymentMethodEnum.PAYMENT_DAYS.getCode(), PaymentMethodEnum.ONLINE.getCode(),PaymentMethodEnum.OFFLINE.getCode());
        wrapper.lambda().in(OrderDO::getOrderStatus, ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(), OrderStatusEnum.FINISHED.getCode()));
        if (queryB2BSettlementPageReuest.getStartReceiveTime() != null) {
            wrapper.lambda().ge(OrderDO::getReceiveTime, queryB2BSettlementPageReuest.getStartReceiveTime());
        }
        if (queryB2BSettlementPageReuest.getEndReceiveTime() != null) {
            wrapper.lambda().le(OrderDO::getReceiveTime, queryB2BSettlementPageReuest.getEndReceiveTime());
        }
        if (queryB2BSettlementPageReuest.getSellerEid() != null) {
            wrapper.lambda().eq(OrderDO::getSellerEid, queryB2BSettlementPageReuest.getSellerEid());
        }
        return this.getBaseMapper().selectPage(queryB2BSettlementPageReuest.getPage(), wrapper);
    }

    @Override
    public BigDecimal getTotalAmountByBuyerEid(Long buyerEid) {
        return this.baseMapper.getTotalAmountByBuyerEid(buyerEid);
    }

    /**
     * 统计前天收货订单
     *
     * @param orderTypeList 订单来源
     */
    @Override
    public List<Long> countReceiveOrder(List<Integer> orderTypeList, String date) {
        List<Long> result = this.baseMapper.countReceiveOrder(orderTypeList, date);

        return result;
    }


    /**
     * 修改备注
     * @param remarkRequest
     * @return
     */
    @Override
    public Boolean updateOrderRemark(UpdateOrderRemarkRequest remarkRequest) {
        List<OrderDO> paramList = Lists.newArrayList();
        for (UpdateOrderRemarkRequest.OrderRemarkRequest remarkVo :remarkRequest.getRemarkRequests()) {
            OrderDO orderDO = new OrderDO();
            orderDO.setId(remarkVo.getOrderId());
            orderDO.setOrderNote(remarkVo.getOrderRemark());
            paramList.add(orderDO);
        }
        return this.updateBatchById(paramList);
    }

    @Override
    public Boolean updateCustomerErpCode(Long buyerEid, Long sellerEid, String customerErpCode) {
        QueryWrapper<OrderDO> wrapper =  new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO :: getBuyerEid,buyerEid)
                .eq(OrderDO :: getSellerEid,sellerEid)
                .eq(OrderDO :: getCustomerErpCode,"" );
        List<OrderDO> orderList = list(wrapper);
        if(CollectionUtil.isNotEmpty(orderList)){
            List<OrderDO> list = new ArrayList<>();
            for(OrderDO one : orderList){
                OrderDO order = new OrderDO();
                order.setId(one.getId());
                order.setCustomerErpCode(customerErpCode);
                order.setErpPushStatus(1);
                list.add(order);
            }
            List<Long> ids = list.stream().map(o -> o.getId()).collect(Collectors.toList());
            MqMessageBO orderErpPushBo = new MqMessageBO(Constants.TOPIC_ORDER_PUSH_ERP, Constants.TAG_ORDER_PUSH_ERP, JSON.toJSONString(ids));
            orderErpPushBo = mqMessageSendApi.prepare(orderErpPushBo);
            mqMessageSendApi.send(orderErpPushBo);
            return  updateBatchById(list);
        }

        return true;
    }

    @Override
    public List<OrderDeliveryReportCountDTO> getOrderDeliveryReportCount(QueryOrderDeliveryReportRequest request) {
        return this.baseMapper.getOrderDeliveryReportCount(request);
    }

    @Override
    public Boolean verificationReceiveB2BOrder(QueryAssistantOrderFirstRequest request) {
        List<Long> list = this.baseMapper.verificationReceiveB2BOrder(request);
        return list.size() > 0;
    }

    @Override
    public OrderDO getAssistantReceiveFirstOrder(QueryAssistantOrderFirstRequest request) {

        return this.baseMapper.getAssistantReceiveFirstOrder(request);
    }

    @Override
    public BigDecimal getMemberOrderAllDiscountAmount(QueryDiscountOrderBuyerEidPagRequest request) {
        return this.baseMapper.getMemberOrderAllDiscountAmount(request);
    }

    @Override
    public Page<OrderMemberDiscountDTO> getMemberOrderDiscountInfo(QueryDiscountOrderBuyerEidPagRequest request) {
        return this.baseMapper.getMemberOrderDiscountInfo(request.getPage(),request);
    }

    @Override
    public Boolean getDeliveryFiveDayTips(List<Long> goodsIdList) {
        List<Long> deliveryFiveDayTips = this.baseMapper.getDeliveryFiveDayTips(goodsIdList);
        return CollectionUtil.isNotEmpty(deliveryFiveDayTips);
    }

    @Override
    public Boolean hideB2BOrder(Long id) {
        OrderDO order = getById(id);
        if(order == null){
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }
        if(!(OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(order.getOrderStatus()) ||
                OrderStatusEnum.FINISHED == OrderStatusEnum.getByCode(order.getOrderStatus())
                || OrderStatusEnum.RECEIVED == OrderStatusEnum.getByCode(order.getOrderStatus()))){
        }
        QueryWrapper<OrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDO::getId, id).eq(OrderDO::getHideFlag, OrderHideFlagEnum.SHOW.getCode());

        OrderDO one = new OrderDO();
        one.setHideFlag(OrderHideFlagEnum.HIDE.getCode());
        return this.update(one, queryWrapper);
    }



    @Override
    public Page<OrderDTO> getPOPWebOrderListPage(OrderPOPWebListPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().like(OrderDO::getOrderNo, request.getOrderNo());
        }
        if (StringUtils.isNotBlank(request.getSellerEname())) {

            wrapper.lambda().like(OrderDO::getSellerEname, request.getSellerEname());
        }
        if(request.getOrderType() != null && request.getOrderType() != 0 ){
            wrapper.lambda().eq(OrderDO :: getOrderType, request.getOrderType());
        }

        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        if(CollectionUtil.isNotEmpty(request.getBuyerEidList())){
            wrapper.lambda().in(OrderDO :: getBuyerEid, request.getBuyerEidList());
        }
        if(request.getId() != null && request.getId() != 0 ){
            wrapper.lambda().eq(OrderDO :: getId, request.getId());
        }
        if(request.getType() != null ){
            if(request.getType() == 1 ){
                wrapper.lambda().eq(OrderDO :: getAuditStatus,OrderAuditStatusEnum.UNSUBMIT.getCode())
                        .eq(OrderDO :: getOrderStatus,OrderStatusEnum.UNAUDITED.getCode());
            }else if (request.getType() == 2 ){
                wrapper.lambda().eq(OrderDO :: getAuditStatus,OrderAuditStatusEnum.UNAUDIT.getCode());
            }else if (request.getType() == 3 ){
                wrapper.lambda().eq(OrderDO :: getOrderStatus,OrderStatusEnum.UNDELIVERED.getCode());
            }else if (request.getType() == 4){
                wrapper.lambda().eq(OrderDO :: getOrderStatus,OrderStatusEnum.PARTDELIVERED.getCode());
            }else if (request.getType() == 5){
                wrapper.lambda().eq(OrderDO :: getOrderStatus,OrderStatusEnum.DELIVERED.getCode());
            }else if (request.getType() == 6){
                wrapper.lambda().eq(OrderDO :: getOrderStatus,OrderStatusEnum.RECEIVED.getCode());
            }else if (request.getType() == 7){
                wrapper.lambda().eq(OrderDO :: getOrderStatus,OrderStatusEnum.CANCELED.getCode());
            }else if (request.getType() == 8){
                wrapper.lambda().eq(OrderDO :: getAuditStatus,OrderAuditStatusEnum.AUDIT_REJECT.getCode());
            }
        }

        wrapper.lambda().eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .orderByDesc(OrderDO::getCreateTime);

        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> orderPage = PojoUtils.map(page, OrderDTO.class);
        return orderPage;
    }

    @Override
    public Page<OrderEnterpriseDTO> getOrderEnterprisePage(QueryOrderPageRequest request) {
        if (request.getStartCreateTime() != null) {
            request.setStartCreateTime(DateUtil.beginOfDay(request.getStartCreateTime()));
        }

        if (request.getEndCreateTime() != null) {
            request.setEndCreateTime( DateUtil.endOfDay(request.getEndCreateTime()));
        }

        return this.baseMapper.getOrderEnterprisePage(request.getPage(),request);
    }

    @Override
    public Long getB2BAdminNumber (QueryOrderPageRequest request){

        if (request.getStartCreateTime() != null) {
            request.setStartCreateTime(DateUtil.beginOfDay(request.getStartCreateTime()));
        }

        if (request.getEndCreateTime() != null) {
            request.setEndCreateTime( DateUtil.endOfDay(request.getEndCreateTime()));
        }

        return this.baseMapper.getB2BAdminNumber(request);
    }

    @Override
    public List<OrderTypeGoodsQuantityDTO> getCountOrderTypeQuantity(QueryOrderPageRequest request){
        return this.baseMapper.getCountOrderTypeQuantity(request);
    }
}
