package com.yiling.order.order.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dto.B2BOrderQuantityDTO;
import com.yiling.order.order.dto.OrderB2BPaymentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.B2bOrderConfirmRequest;
import com.yiling.order.order.dto.request.CreateOrderCouponUseRequest;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderGiftRequest;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.OrderB2BPaymentRequest;
import com.yiling.order.order.dto.request.OrderReturnCountRequest;
import com.yiling.order.order.entity.OrderCouponUseDO;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderDeliveryDO;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.entity.OrderDetailDO;
import com.yiling.order.order.entity.OrderGiftDO;
import com.yiling.order.order.entity.OrderLogDO;
import com.yiling.order.order.entity.OrderPromotionActivityDO;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderStatusLogDO;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderHideFlagEnum;
import com.yiling.order.order.enums.OrderPlatformTypeEnum;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.order.order.service.OrderB2BService;
import com.yiling.order.order.service.OrderCouponUseService;
import com.yiling.order.order.service.OrderDeliveryService;
import com.yiling.order.order.service.OrderDetailChangeService;
import com.yiling.order.order.service.OrderDetailService;
import com.yiling.order.order.service.OrderGiftService;
import com.yiling.order.order.service.OrderLogService;
import com.yiling.order.order.service.OrderPromotionActivityService;
import com.yiling.order.order.service.OrderReturnService;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.order.service.OrderStatusLogService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:wei.wang
 * @date:2021/10/19
 */
@Service
@Slf4j
public class OrderB2BServiceImpl implements OrderB2BService {

    @Autowired
    private OrderService             orderService;
    @Autowired
    private OrderStatusLogService    orderStatusLogService;
    @Autowired
    private OrderDeliveryService     orderDeliveryService;
    @Autowired
    private OrderLogService          orderLogService;
    @Autowired
    private OrderDetailChangeService orderDetailChangeService;
    @Autowired
    private OrderReturnService       orderReturnService;
    @Autowired
    private OrderDetailService       orderDetailService;
    @Autowired
    private OrderCouponUseService    orderCouponUseService;
    @Autowired
    private OrderPromotionActivityService promotionActivityService;
    @Autowired
    private OrderGiftService         orderGiftService;
    @DubboReference
    private MqMessageSendApi mqMessageSendApi;


    /**
     * 全部订单接口
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDO> getB2BAppOrder(OrderB2BPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        Integer type = request.getType();
        String condition = request.getCondition();
        if (1 == type) {
            //待付款
            wrapper.lambda().eq(OrderDO ::getOrderStatus,OrderStatusEnum.UNAUDITED.getCode())
                    .eq(OrderDO::getPaymentMethod, PaymentMethodEnum.ONLINE.getCode())
                    .ne(OrderDO::getPaymentStatus,PaymentStatusEnum.PAID.getCode());
        } else if (2 == type) {
            //待发货
            List<Integer> statusList = new ArrayList<>();
            statusList.add(OrderStatusEnum.UNDELIVERED.getCode());
            statusList.add(OrderStatusEnum.PARTDELIVERED.getCode());
            wrapper.lambda().in(OrderDO::getOrderStatus, statusList );
        } else if (3 == type) {
            //待收货
            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode());
        } else if (4 == type) {
            //已完成
            wrapper.lambda().ge(OrderDO::getOrderStatus, OrderStatusEnum.RECEIVED.getCode());
        } else if (5 == type) {
            //已取消
            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode());
        }

        if(request.getSellerEid() != null && request.getSellerEid() != 0){
            wrapper.lambda().eq(OrderDO::getBuyerEid, request.getSellerEid());
        }
        wrapper.lambda().eq(OrderDO::getOrderType, OrderTypeEnum.B2B.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .eq(OrderDO::getBuyerEid, request.getEid())
                .eq(OrderDO::getHideFlag, OrderHideFlagEnum.SHOW.getCode())
                .orderByDesc(OrderDO::getCreateTime);
        if (StringUtils.isNotEmpty(condition)) {
            wrapper.lambda().and(Wrapper -> Wrapper.like(OrderDO::getOrderNo, condition).or().like(OrderDO::getSellerEname, condition));
        }
        Page<OrderDO> page = orderService.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return page;
    }

    /**
     * 根据id获取订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderDO getB2BOrderOne(Long orderId) {

        return orderService.getById(orderId);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean B2BCancel(Long orderId, Long opUserId) {
        OrderDO order = orderService.getById(orderId);
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        if (PaymentStatusEnum.PAID != PaymentStatusEnum.getByCode(order.getPaymentStatus())
                && OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(order.getOrderStatus()) ){
            wrapper.lambda().eq(OrderDO::getId, orderId)
                    .eq(OrderDO::getOrderStatus, OrderStatusEnum.UNAUDITED.getCode())
                    .ne(OrderDO :: getPaymentStatus,PaymentStatusEnum.PAID.getCode() );
        }else{
            List<Integer> list = new ArrayList<>();
            list.add(OrderErpPushStatusEnum.NOT_PUSH.getCode());
            list.add(OrderErpPushStatusEnum.PUSH_FAIL.getCode());
            wrapper.lambda().eq(OrderDO::getId, orderId)
                    .eq(OrderDO::getOrderStatus, OrderStatusEnum.UNDELIVERED.getCode())
                    .in(OrderDO :: getErpPushStatus,list);
        }

        OrderDO orderNew = new OrderDO();
        orderNew.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderNew.setOpUserId(opUserId);
        Boolean result = orderService.update(orderNew, wrapper);
        log.info("B2B取消订单,订单单号orderNo:{},取消结果result:{}", order.getOrderNo(),result);

        if (result){
            saveCancelLog(orderId,opUserId);
        }

        return result;
    }


    /**
     * 取消日志记录
     * @param orderId
     * @param opUserId
     */
    private void saveCancelLog(Long orderId,Long opUserId) {

        OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
        orderStatusLog.setOrderId(orderId).setOrderStatus(OrderStatusEnum.CANCELED.getCode()).setRemark("订单取消")
                .setCreateUser(opUserId).setCreateTime(DateUtil.date());
        orderStatusLogService.save(orderStatusLog);

        OrderLogDO orderLogInfo = new OrderLogDO();
        orderLogInfo.setLogContent("订单取消")
                .setLogTime(new Date())
                .setOrderId(orderId);
        orderLogService.save(orderLogInfo);
    }

    /**
     * 收货
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean B2BReceive(Long orderId, Long opUserId) {
        int count = orderReturnService.countByOrderIdAndStatus(orderId, OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
        if(count > 0){
            throw new BusinessException(OrderErrorCode.ORDER_GOODS_NOT_RETURN_NOW);
        }
        Boolean flag = receiveUpdateOrder(orderId, opUserId);
        if(flag){
            List<OrderDeliveryDO> orderDeliveryList = orderDeliveryService.getOrderDeliveryList(orderId);
            for (OrderDeliveryDO one : orderDeliveryList) {
                one.setReceiveQuantity(one.getDeliveryQuantity());
            }
            orderDeliveryService.updateBatchById(orderDeliveryList);
            receiveSaveOrderLog(orderId);
            receiveCallOrderDetailChange(new ArrayList<Long>(){{add(orderId);}});
        }
        return flag;
    }

    /**
     * 超过7天自动收货
     *
     * @param day 天数
     * @return
     */
    @Override
    public void activeB2BReceive(Integer day) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode())
                .eq(OrderDO :: getOrderType,OrderTypeEnum.B2B.getCode())
                .le(OrderDO::getDeliveryTime,DateUtil.offsetDay(new Date(), -day));

        List<OrderDO> orderList = orderService.list(wrapper);
        if(CollectionUtil.isNotEmpty(orderList)){
            List<Long> orders = orderList.stream().map(o -> o.getId()).collect(Collectors.toList());
            List<OrderReturnDO> orderReturnDOList = orderReturnService.orderReturnByOrderIdsAndStatus(orders, OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
            Map<Long, List<OrderReturnDO>> map = new HashMap<>();
            if(CollectionUtil.isNotEmpty(orderReturnDOList)){
               map = orderReturnDOList.stream().collect(Collectors.groupingBy(o -> o.getOrderId()));
            }
            for(OrderDO one : orderList){
                List<OrderReturnDO> orderReturnDOS = map.get(one.getId());
                if(CollectionUtil.isEmpty(orderReturnDOS)){
                    log.info("B2B自动收货,订单处理订单ID数据:{}", JSON.toJSONString(one.getId()));

                    MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_B2B_ORDER_ACTIVE_RECEIVE, Constants.TAG_B2B_ORDER_ACTIVE_RECEIVE, String.valueOf(one.getId()));
                    mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
                    mqMessageSendApi.send(mqMessageTwoBO);

                }

            }
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean activeB2BReceiveByOrderId(Long orderId) {
        //能自动收货的订单id
        OrderDTO orderInfo = orderService.getOrderInfo(orderId);
        Boolean result = false;
        if(OrderStatusEnum.getByCode(orderInfo.getOrderStatus()) == OrderStatusEnum.DELIVERED){
            Integer count = orderReturnService.countByOrderIdAndStatus(orderId, OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
            if(count.compareTo(0) == 0){
                result = receiveUpdateOrder(orderId, 0L);
                if(result){
                    List<OrderDeliveryDO> orderDeliveryList = orderDeliveryService.getOrderDeliveryList(orderId);
                    for (OrderDeliveryDO one : orderDeliveryList) {
                        one.setReceiveQuantity(one.getDeliveryQuantity());
                    }
                    log.info("B2B自动收货,订单处理数据:{}", JSON.toJSONString(orderDeliveryList));
                    orderDeliveryService.updateBatchById(orderDeliveryList);
                    receiveSaveOrderLog(orderId);
                    receiveCallOrderDetailChange(new ArrayList<Long>(){{add(orderId);}});
                }
            }
        }

        return result;
    }


    /**
     * 获取订单账期列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderB2BPaymentDTO> getOrderB2BPaymentList(OrderB2BPaymentRequest request) {
        return orderService.getOrderB2BPaymentList(request);
    }

    /**
     * 统计订单数量
     *
     * @param eid
     * @return
     */
    @Override
    public B2BOrderQuantityDTO countB2BOrderQuantity(Long eid, OrderPlatformTypeEnum orderPlatformTypeEnum) {
        B2BOrderQuantityDTO result = new B2BOrderQuantityDTO();
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getPaymentMethod, PaymentMethodEnum.ONLINE.getCode())
                .ne(OrderDO::getPaymentStatus, PaymentStatusEnum.PAID.getCode())
                .eq(OrderDO::getOrderStatus, OrderStatusEnum.UNAUDITED.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .eq(OrderDO::getOrderType, OrderTypeEnum.B2B.getCode());
        if (orderPlatformTypeEnum == OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_APP_WEB) {
            wrapper.lambda().eq(OrderDO::getBuyerEid, eid)
                    .eq(OrderDO :: getPaymentMethod,PaymentMethodEnum.ONLINE.getCode());
        } else if (orderPlatformTypeEnum == OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_MALL_B2B) {
            wrapper.lambda().eq(OrderDO::getSellerEid, eid);
        }
        int count = orderService.count(wrapper);

        wrapper = new QueryWrapper<>();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.UNDELIVERED.getCode());
        statusList.add(OrderStatusEnum.PARTDELIVERED.getCode());
        wrapper.lambda().in(OrderDO::getOrderStatus,statusList)
                .eq(OrderDO::getOrderType, OrderTypeEnum.B2B.getCode());

        if (orderPlatformTypeEnum == OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_APP_WEB) {
            wrapper.lambda().eq(OrderDO::getBuyerEid, eid);
        } else if (orderPlatformTypeEnum == OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_MALL_B2B) {
            wrapper.lambda().eq(OrderDO::getSellerEid, eid);
        }
        int unDeliveryQuantity = orderService.count(wrapper);

        wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode())
                .eq(OrderDO::getOrderType, OrderTypeEnum.B2B.getCode());
        if (orderPlatformTypeEnum == OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_APP_WEB) {
            wrapper.lambda().eq(OrderDO::getBuyerEid, eid);
        } else if (orderPlatformTypeEnum == OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_MALL_B2B) {
            wrapper.lambda().eq(OrderDO::getSellerEid, eid);
        }
        int unReceiveQuantity = orderService.count(wrapper);

        // 退货数量
        int returnQuantity = 0;
        if (orderPlatformTypeEnum == OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_APP_WEB) {
            OrderReturnCountRequest request = new OrderReturnCountRequest()
                    .setBuyerEid(eid)
                    .setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode())
                    .setReturnSource(ReturnSourceEnum.B2B_APP.getCode());
            returnQuantity = orderReturnService.countByCondition(request);
        } else if (orderPlatformTypeEnum == OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_MALL_B2B) {
            OrderReturnCountRequest request = new OrderReturnCountRequest()
                    .setSellerEid(eid)
                    .setReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode())
                    .setReturnSource(ReturnSourceEnum.B2B_APP.getCode());
            returnQuantity = orderReturnService.countByCondition(request);
        }

        result.setUnPaymentQuantity(count);
        result.setUnDeliveryQuantity(unDeliveryQuantity);
        result.setUnReceiveQuantity(unReceiveQuantity);
        result.setReturnQuantity(returnQuantity);
        return result;
    }

    private Boolean receiveUpdateOrder(Long orderId, Long opUserId) {
        OrderDO order = new OrderDO();
        order.setOrderStatus(OrderStatusEnum.RECEIVED.getCode());
        order.setReceiveUser(opUserId);
        order.setReceiveTime(new Date());

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getId, orderId)
                .eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode());
        Boolean flag = orderService.update(order, wrapper);
        return flag;
    }

    private void receiveSaveOrderLog(Long orderId) {
            OrderLogDO orderLogInfo = new OrderLogDO();
            orderLogInfo.setLogContent("订单收货")
                    .setLogTime(new Date())
                    .setOrderId(orderId);

            OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
            orderStatusLog.setOrderId(orderId)
                    .setOrderStatus(OrderStatusEnum.RECEIVED.getCode())
                    .setRemark("订单收货");

        orderLogService.save(orderLogInfo);
        orderStatusLogService.save(orderStatusLog);
    }

    private void receiveCallOrderDetailChange(List<Long> orderId) {
        List<OrderDetailChangeDO> changeList = orderDetailChangeService.listByOrderIds(orderId);
        for(OrderDetailChangeDO one : changeList){
            orderDetailChangeService.updateReceiveData(one.getDetailId(), one.getDeliveryQuantity() - one.getReturnQuantity());

        }

    }

    @Override
    public List<Long> selectOnlineNotPayOrder(Integer minute) {

        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getPaymentMethod, PaymentMethodEnum.ONLINE.getCode())
                .eq(OrderDO::getPaymentStatus, PaymentStatusEnum.UNPAID.getCode())
                .eq(OrderDO::getOrderStatus, OrderStatusEnum.UNAUDITED.getCode())
                .eq(OrderDO::getOrderType, OrderTypeEnum.B2B.getCode());

        wrapper.lambda().le(OrderDO::getCreateTime,DateUtil.offsetMinute(new Date(), -minute));
        wrapper.lambda().le(OrderDO::getCustomerConfirmTime,DateUtil.offsetMinute(new Date(), -minute));
        List<OrderDO> orderDOList = orderService.list(wrapper);

        if (CollectionUtil.isEmpty(orderDOList)) {
            return Collections.emptyList();
        }

        return orderDOList.stream().map(OrderDO::getId).collect(Collectors.toList());
    }


    @Override
    public Boolean b2bOrderConfirm(List<B2bOrderConfirmRequest> confirmRequests) {
        confirmRequests.forEach(confirmRequest -> {
            OrderDO order = PojoUtils.map(confirmRequest, OrderDO.class);
            order.setId(confirmRequest.getOrderId());
            this.orderService.updateById(order);
            Map<Long, CreateOrderDetailRequest> skuIdMap = Maps.newHashMap();
            // 保存订单明细
            List<OrderDetailDO> orderDetailList = CollUtil.newArrayList();
            confirmRequest.getOrderDetailList().forEach(orderConfirmDetailRequest -> {
                OrderDetailDO orderDetail = new OrderDetailDO();
                orderDetail.setOrderId(order.getId());
                orderDetail.setId(orderConfirmDetailRequest.getOrderDetailId());
                orderDetail.setGoodsPrice(orderConfirmDetailRequest.getGoodsPrice());
                orderDetail.setGoodsAmount(orderConfirmDetailRequest.getGoodsAmount());
                orderDetail.setGoodsType(orderConfirmDetailRequest.getGoodsType());
                orderDetail.setPromotionActivityId(orderConfirmDetailRequest.getPromotionActivityId());

                orderDetailList.add(orderDetail);
                skuIdMap.put(orderDetail.getId(),orderConfirmDetailRequest);
            });

            this.orderDetailService.updateBatchById(orderDetailList);

            orderDetailList.forEach(orderDetail -> {
                CreateOrderDetailRequest detailRequest = skuIdMap.get(orderDetail.getId());
                this.orderDetailChangeService.updateCouponDiscountAmountByDetailId(orderDetail.getId(),detailRequest.getPlatformCouponDiscountAmount(),detailRequest.getCouponDiscountAmount(),detailRequest.getGoodsPrice());
            });

            // 保存订单使用优惠劵记录
            List<CreateOrderCouponUseRequest> couponUseRequestList = confirmRequest.getOrderCouponUseList();
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
            List<CreateOrderGiftRequest> orderGiftRequests = confirmRequest.getOrderGiftRequestList();
            if (CollUtil.isNotEmpty(orderGiftRequests)) {
                List<OrderGiftDO> orderGiftList = orderGiftRequests.stream().map(giftRequest -> {
                    giftRequest.setOrderId(order.getId());
                    OrderGiftDO orderGiftDO = PojoUtils.map(giftRequest, OrderGiftDO.class);
                    return orderGiftDO;

                }).collect(Collectors.toList());

                orderGiftService.saveBatch(orderGiftList);
            }

            // 保存订单促销活动信息
            List<CreateOrderPromotionActivityRequest> promotionActivityRequestList = confirmRequest.getPromotionActivityRequestList();
            if (CollUtil.isNotEmpty(promotionActivityRequestList)) {
                List<OrderPromotionActivityDO> orderPromotionActivityDOS = Lists.newArrayList();
                promotionActivityRequestList.forEach(t -> {
                    OrderPromotionActivityDO orderPromotionActivityDO = PojoUtils.map(t, OrderPromotionActivityDO.class);
                    orderPromotionActivityDO.setOrderId(order.getId());
                    orderPromotionActivityDOS.add(orderPromotionActivityDO);
                });
                promotionActivityService.saveBatch(orderPromotionActivityDOS);
            }

            // 无需在线支付的支付方式
            EnumSet<PaymentMethodEnum> hasPayMethods = EnumSet.of(PaymentMethodEnum.OFFLINE,PaymentMethodEnum.PREPAYMENT,PaymentMethodEnum.PAYMENT_DAYS);
            if (hasPayMethods.contains(confirmRequest.getPaymentMethod())) {
                // 记录订单状态变更日志
                OrderStatusLogDO orderStatusLogDO = new OrderStatusLogDO();
                orderStatusLogDO.setOrderId(confirmRequest.getOrderId());
                orderStatusLogDO.setOrderStatus(confirmRequest.getOrderStatus());
                orderStatusLogDO.setRemark("客户确认订单");
                orderStatusLogDO.setOpUserId(confirmRequest.getOpUserId());
                orderStatusLogService.save(orderStatusLogDO);
            }
        });
        return true;
    }
}
