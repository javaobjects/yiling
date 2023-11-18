package com.yiling.order.order.api.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.dto.B2BOrderQuantityDTO;
import com.yiling.order.order.dto.B2BSettlementDTO;
import com.yiling.order.order.dto.B2BSettlementDetailDTO;
import com.yiling.order.order.dto.OrderB2BPaymentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.request.B2bOrderConfirmRequest;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.OrderB2BPaymentRequest;
import com.yiling.order.order.dto.request.QueryB2BSettlementPageReuest;
import com.yiling.order.order.dto.request.UpdateOrderSettlementStatusRequest;
import com.yiling.order.order.entity.OrderCouponUseDO;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.enums.OrderGoodsTypeEnum;
import com.yiling.order.order.enums.OrderPlatformTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.order.order.service.OrderB2BService;
import com.yiling.order.order.service.OrderCouponUseService;
import com.yiling.order.order.service.OrderDetailChangeService;
import com.yiling.order.order.service.OrderDetailService;
import com.yiling.order.order.service.OrderPromotionActivityService;
import com.yiling.order.order.service.OrderService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * B2B订单API查询
 * @author:wei.wang
 * @date:2021/10/19
 */
@DubboService
public class OrderB2BApiImpl implements OrderB2BApi {

    @Autowired
    private OrderB2BService orderB2BService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderDetailChangeService orderDetailChangeService;

    @Autowired
    private OrderCouponUseService orderCouponUseService;

    @Autowired
    private OrderPromotionActivityService orderPromotionActivityService;


    /**
     * 全部订单查询接口
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getB2BAppOrder(OrderB2BPageRequest request) {
        Page<OrderDO> result = orderB2BService.getB2BAppOrder(request);
        return PojoUtils.map(result,OrderDTO.class);
    }

    /**
     * 根据id获取订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderDTO getB2BOrderOne(Long orderId) {
        OrderDO result = orderB2BService.getB2BOrderOne(orderId);
        return PojoUtils.map(result,OrderDTO.class);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    public Boolean B2BCancel(Long orderId, Long opUserId) {

        return orderB2BService.B2BCancel(orderId,opUserId);
    }

    /**
     * 收货
     *
     * @param orderId
     * @param opUserId
     * @return
     */
    @Override
    public Boolean B2BReceive(Long orderId, Long opUserId) {
        return orderB2BService.B2BReceive(orderId,opUserId);
    }

    /**
     * 超过7天自动收货
     *
     * @param day
     * @return
     */
    @Override
    public void activeB2BReceive(Integer day) {
        orderB2BService.activeB2BReceive(day);
    }

    /**
     * 自动收货根据订单ID
     * @param orderId
     * @return
     */
    @Override
    public Boolean activeB2BReceiveByOrderId(Long orderId){
        return orderB2BService.activeB2BReceiveByOrderId(orderId);
    }



    /**
     * 获取订单账期列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderB2BPaymentDTO> getOrderB2BPaymentList(OrderB2BPaymentRequest request) {
        return orderB2BService.getOrderB2BPaymentList(request);
    }

    @Override
    public Page<B2BSettlementDTO> pageB2bSettlementList(QueryB2BSettlementPageReuest queryB2BSettlementPageReuest) {
        Page<OrderDO>  page = orderService.pageB2bSettlementList(queryB2BSettlementPageReuest);
        if (page == null || CollectionUtil.isEmpty(page.getRecords())) {
            return new Page(queryB2BSettlementPageReuest.getCurrent(),queryB2BSettlementPageReuest.getSize());
        }
        Page<B2BSettlementDTO> pageResult =  PojoUtils.map(page,B2BSettlementDTO.class);
        pageResult.getRecords().stream().forEach(e -> {
            // 设置赠品信息
            List<OrderPromotionActivityDTO> fullGiftPromotionActivitys = orderPromotionActivityService.listByOrderId(e.getId(), PromotionActivityTypeEnum.FULL_GIFT);
            e.setFullGiftPromotionActivitys(fullGiftPromotionActivitys);

            List<OrderCouponUseDO>  couponUseDOList = orderCouponUseService.selectOrderCouponList(e.getId(),null);
            if (CollectionUtil.isEmpty(couponUseDOList)) {
                return;
            }
            couponUseDOList.stream().filter(t -> t.getCouponType() == 1).findFirst().ifPresent( platformCoupon -> {
                e.setPlatformRatio(platformCoupon.getPlatformRatio());
                e.setPlatformBusinessRatio(platformCoupon.getBusinessRatio());
            });
            couponUseDOList.stream().filter(t -> t.getCouponType() == 2).findFirst().ifPresent( shopCoupon -> {
                e.setShopPlatformRatio(shopCoupon.getPlatformRatio());
                e.setShopBusinessRatio(shopCoupon.getBusinessRatio());
            });
        });

        return pageResult;
    }

    @Override
    public B2BSettlementDTO getB2bSettlementOne(String orderNo) {

        OrderDTO orderInfo = orderService.getOrderInfo(orderNo);
        B2BSettlementDTO result = PojoUtils.map(orderInfo, B2BSettlementDTO.class);
        List<OrderPromotionActivityDTO> fullGiftPromotionActivitys = orderPromotionActivityService.listByOrderId(result.getId(), PromotionActivityTypeEnum.FULL_GIFT);
        result.setFullGiftPromotionActivitys(fullGiftPromotionActivitys);
        List<OrderCouponUseDO>  couponUseDOList = orderCouponUseService.selectOrderCouponList(result.getId(),null);
        if (CollectionUtil.isNotEmpty(couponUseDOList)) {
            couponUseDOList.stream().filter(t -> t.getCouponType() == 1).findFirst().ifPresent( platformCoupon -> {
                result.setPlatformRatio(platformCoupon.getPlatformRatio());
                result.setPlatformBusinessRatio(platformCoupon.getBusinessRatio());
            });
            couponUseDOList.stream().filter(t -> t.getCouponType() == 2).findFirst().ifPresent( shopCoupon -> {
                result.setShopPlatformRatio(shopCoupon.getPlatformRatio());
                result.setShopBusinessRatio(shopCoupon.getBusinessRatio());
            });
        }
        return result;
    }

    @Override
    public Boolean batchB2bOrderSettlementStatus(UpdateOrderSettlementStatusRequest request) {

        List<OrderDO> updateOrderList = request.getOrderSettlementStatusRequestList().stream().map(e -> {
            OrderDO updateOrder = PojoUtils.map(e,OrderDO.class);
            updateOrder.setId(e.getOrderId());
            return updateOrder;

        }).collect(Collectors.toList());

        return orderService.updateBatchById(updateOrderList);
    }


    @Override
    public List<B2BSettlementDetailDTO> listSettleOrderDetailByOrderId(Long orderId) {
        OrderDTO orderDto = orderService.getOrderInfo(orderId);
        if (orderDto == null) {
            return Collections.emptyList();
        }
        List<OrderCouponUseDO> couponUseDOList = orderCouponUseService.selectOrderCouponList(orderId,null);
        OrderCouponUseDO platformCoupon = CollectionUtil.isEmpty(couponUseDOList) || !couponUseDOList.stream().anyMatch(t -> t.getCouponType() == 1) ? null : couponUseDOList.stream().filter(t -> t.getCouponType() == 1).findFirst().get();
        OrderCouponUseDO shopCoupon = CollectionUtil.isEmpty(couponUseDOList) || !couponUseDOList.stream().anyMatch(t -> t.getCouponType() == 2) ?  null : couponUseDOList.stream().filter(t -> t.getCouponType() == 2).findFirst().get();
        List<OrderDetailDTO> orderDetailDTOList = orderDetailService.getOrderDetailInfo(orderId);
        Map<Long, OrderDetailChangeDO> resultChangeMap = orderDetailChangeService.listByOrderId(orderId).stream().collect(Collectors.toMap(OrderDetailChangeDO::getDetailId, Function.identity()));
        return  orderDetailDTOList.stream().map(p -> {
            B2BSettlementDetailDTO e = PojoUtils.map(p,B2BSettlementDetailDTO.class);
            e.setOrderId(orderDto.getId());
            e.setOrderNo(orderDto.getOrderNo());
            e.setPaymentType(orderDto.getPaymentType());
            e.setPaymentMethod(orderDto.getPaymentMethod());
            e.setPayChannel(orderDto.getPayChannel());
            e.setCreateTime(orderDto.getCreateTime());
            e.setCustomerErpCode(orderDto.getCustomerErpCode());
            //促销活动优惠小计=（originalPrice - goodsPrice） * goodsQuantity
            e.setPromotionSaleSubTotal(p.getOriginalPrice().subtract(p.getGoodsPrice()).multiply(new BigDecimal(p.getGoodsQuantity())));
            //如果参加组合商品活动
            if (ObjectUtil.equal(p.getPromotionActivityType(),PromotionActivityTypeEnum.COMBINATION.getCode())){
                //如果是非以岭品
                if (ObjectUtil.equal(OrderGoodsTypeEnum.NORMAL.getCode(),p.getGoodsType())){
                    //组合促销优惠金额小计=（originalPrice - goodsPrice） * goodsQuantity
                    e.setComPacAmount(p.getOriginalPrice().subtract(p.getGoodsPrice()).multiply(new BigDecimal(p.getGoodsQuantity())));
                }else {
                    //如果是以岭品
                    //组合促销优惠金额小计=（limitPrice - goodsPrice） * goodsQuantity
                    e.setComPacAmount(p.getLimitPrice().subtract(p.getGoodsPrice()).multiply(new BigDecimal(p.getGoodsQuantity())));
                }
            }
            Optional.ofNullable(resultChangeMap.get(p.getId())).ifPresent(t -> {
                e.setDetailId(t.getDetailId());
                e.setDeliveryAmount(t.getDeliveryAmount());
                e.setDeliveryQuantity(t.getDeliveryQuantity());
                e.setReceiveAmount(t.getReceiveAmount());
                e.setReceiveQuantity(t.getReceiveQuantity());
                e.setReceivePlatformCouponDiscountAmount(t.getReceivePlatformCouponDiscountAmount());
                e.setReceiveCouponDiscountAmount(t.getReceiveCouponDiscountAmount());
                e.setReceivePresaleDiscountAmount(t.getReceivePresaleDiscountAmount());
                e.setReturnCouponDiscountAmount(t.getReturnCouponDiscountAmount().add(t.getSellerCouponDiscountAmount()));
                e.setReturnPlatformCouponDiscountAmount(t.getReturnPlatformCouponDiscountAmount().add(t.getSellerPlatformCouponDiscountAmount()));
                e.setReturnPresaleDiscountAmount(t.getReturnPresaleDiscountAmount().add(t.getSellerPresaleDiscountAmount()));
                e.setReturnShopPaymentDiscountAmount(t.getReturnShopPaymentDiscountAmount());
                e.setReturnPlatformPaymentDiscountAmount(t.getReturnPlatformPaymentDiscountAmount());
                e.setDeliveryCouponDiscountAmount(t.getDeliveryCouponDiscountAmount());
                e.setDeliveryPlatformCouponDiscountAmount(t.getDeliveryPlatformCouponDiscountAmount());
                e.setDeliveryPresaleDiscountAmount(t.getDeliveryPresaleDiscountAmount());
                e.setPlatformCouponDiscountAmount(t.getPlatformCouponDiscountAmount());
                e.setCouponDiscountAmount(t.getCouponDiscountAmount());
                e.setPresaleDiscountAmount(t.getPresaleDiscountAmount());
                e.setPaymentPlatformDiscountAmount(t.getPlatformPaymentDiscountAmount());
                e.setPaymentShopDiscountAmount(t.getShopPaymentDiscountAmount());
                e.setReturnAmount(t.getReturnAmount().add(t.getSellerReturnAmount()));
                //退货促销活动优惠小计=（originalPrice - goodsPrice） * (sellerReturnQuantity + returnQuantity)
                e.setReturnPromotionSaleSubTotal(p.getOriginalPrice().subtract(p.getGoodsPrice()).multiply(new BigDecimal(t.getSellerReturnQuantity()+t.getReturnQuantity())));
                //如果参加组合商品活动
                if (ObjectUtil.equal(p.getPromotionActivityType(),PromotionActivityTypeEnum.COMBINATION.getCode())){
                    //如果是非以岭品
                    if (ObjectUtil.equal(OrderGoodsTypeEnum.NORMAL.getCode(),p.getGoodsType())){
                        //退回组合促销优惠的金额小计=（originalPrice - goodsPrice） * (sellerReturnQuantity + returnQuantity)
                        e.setReturnComPacAmount(p.getOriginalPrice().subtract(p.getGoodsPrice()).multiply(new BigDecimal(t.getSellerReturnQuantity()+t.getReturnQuantity())));
                    }else {
                        //如果是以岭品
                        //退回组合促销优惠的金额小计=（limitPrice - goodsPrice） * (sellerReturnQuantity + returnQuantity)
                        e.setReturnComPacAmount(p.getLimitPrice().subtract(p.getGoodsPrice()).multiply(new BigDecimal(t.getSellerReturnQuantity()+t.getReturnQuantity())));
                    }
                }
                Optional.ofNullable(platformCoupon).ifPresent(z -> {
                    e.setPlatformRatio(z.getPlatformRatio());
                    e.setPlatformBusinessRatio(z.getBusinessRatio());
                });
                Optional.ofNullable(shopCoupon).ifPresent(z -> {
                    e.setShopPlatformRatio(z.getPlatformRatio());
                    e.setShopBusinessRatio(z.getBusinessRatio());
                });
            });
            return e;
        }).collect(Collectors.toList());
    }

    /**
     * B2B首页统计订单数量
     *
     * @param eid
     * @return
     */
    @Override
    public B2BOrderQuantityDTO countB2BOrderQuantity(Long eid, OrderPlatformTypeEnum orderPlatformTypeEnum) {
        return orderB2BService.countB2BOrderQuantity(eid,orderPlatformTypeEnum);
    }

    @Override
    public List<Long> selectOnlineNotPayOrder(Integer minute) {
        return orderB2BService.selectOnlineNotPayOrder(minute);
    }


    @Override
    public Boolean b2bOrderConfirm(List<B2bOrderConfirmRequest> confirmRequests) {

        return orderB2BService.b2bOrderConfirm(confirmRequests);
    }
}
