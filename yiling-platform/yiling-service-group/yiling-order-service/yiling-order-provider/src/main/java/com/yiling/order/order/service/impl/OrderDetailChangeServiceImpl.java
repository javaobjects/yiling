package com.yiling.order.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.order.order.bo.OrderDetailChangeBO;
import com.yiling.order.order.dao.OrderDetailChangeMapper;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.service.OrderDetailChangeService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;

/**
 * <p>
 * 订单明细变更信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-08-04
 */
@Service
public class OrderDetailChangeServiceImpl extends BaseServiceImpl<OrderDetailChangeMapper, OrderDetailChangeDO> implements OrderDetailChangeService {

    @Override
    public OrderDetailChangeDO getByDetailId(Long detailId) {
        QueryWrapper<OrderDetailChangeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderDetailChangeDO::getDetailId, detailId)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<OrderDetailChangeDO> listByOrderId(Long orderId) {
        QueryWrapper<OrderDetailChangeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetailChangeDO::getOrderId, orderId);
        List<OrderDetailChangeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    /**
     * 根据订单IDS获取订单明细变更信息
     *
     * @param orderIds 订单集合
     * @return
     */
    @Override
    public List<OrderDetailChangeDO> listByOrderIds(List<Long> orderIds) {
        QueryWrapper<OrderDetailChangeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(OrderDetailChangeDO::getOrderId, orderIds);
        List<OrderDetailChangeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public boolean updateCashDiscountAmountByDetailId(Long detailId, BigDecimal cashDiscountAmount) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        entity.setCashDiscountAmount(cashDiscountAmount);
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTicketDiscountAmountByDetailId(Long detailId, BigDecimal ticketDiscountAmount, boolean afterReceiveFlag) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        entity.setTicketDiscountAmount(ticketDiscountAmount);
        entity.setDeliveryTicketDiscountAmount(ticketDiscountAmount);
        if (afterReceiveFlag) {
            if (entity.getReceiveQuantity().compareTo(entity.getDeliveryQuantity()) == 0) {
                // 全部收货
                entity.setReceiveTicketDiscountAmount(ticketDiscountAmount);
            } else {
                // 部分收货
                entity.setReceiveTicketDiscountAmount(NumberUtil.mul(ticketDiscountAmount, NumberUtil.div(new BigDecimal(entity.getReceiveQuantity()), new BigDecimal(entity.getDeliveryQuantity()))).setScale(2, RoundingMode.HALF_UP));
            }
        }
        return this.updateById(entity);
    }

    @Override
    public Map<Long, OrderDetailChangeDO> listMapByOrderId(Long orderId) {
        List<OrderDetailChangeDO> list = this.listByOrderId(orderId);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        return list.stream().collect(Collectors.toMap(OrderDetailChangeDO::getDetailId, Function.identity()));
    }

    @Override
    public boolean updateDeliveryData(Long detailId, int deliveryQuantity) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        // 发货部分
        entity.setDeliveryQuantity(deliveryQuantity);
        entity.setDeliveryAmount(NumberUtil.mul(deliveryQuantity, entity.getGoodsPrice()));
        entity.setDeliveryCashDiscountAmount(NumberUtil.mul(entity.getCashDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryTicketDiscountAmount(NumberUtil.mul(entity.getTicketDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        //B2B部分
        entity.setDeliveryPlatformCouponDiscountAmount(NumberUtil.mul(entity.getPlatformCouponDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryCouponDiscountAmount(NumberUtil.mul(entity.getCouponDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryPresaleDiscountAmount(NumberUtil.mul(entity.getPresaleDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

        entity.setDeliveryPlatformPaymentDiscountAmount(NumberUtil.mul(entity.getPlatformPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryShopPaymentDiscountAmount(NumberUtil.mul(entity.getShopPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

        // 卖家退货部分
        entity.setSellerReturnQuantity(entity.getGoodsQuantity() - entity.getDeliveryQuantity());
        entity.setSellerReturnAmount(NumberUtil.sub(entity.getGoodsAmount(), entity.getDeliveryAmount()));
        entity.setSellerReturnCashDiscountAmount(NumberUtil.sub(entity.getCashDiscountAmount(), entity.getDeliveryCashDiscountAmount()));
        entity.setSellerReturnTicketDiscountAmount(NumberUtil.sub(entity.getTicketDiscountAmount(), entity.getDeliveryTicketDiscountAmount()));

        entity.setSellerPlatformCouponDiscountAmount(NumberUtil.sub(entity.getPlatformCouponDiscountAmount(), entity.getDeliveryPlatformCouponDiscountAmount()));
        entity.setSellerCouponDiscountAmount(NumberUtil.sub(entity.getCouponDiscountAmount(), entity.getDeliveryCouponDiscountAmount()));
        entity.setSellerPresaleDiscountAmount(NumberUtil.sub(entity.getPresaleDiscountAmount(), entity.getDeliveryPresaleDiscountAmount()));

        entity.setSellerPlatformPaymentDiscountAmount(NumberUtil.sub(entity.getPlatformPaymentDiscountAmount(), entity.getDeliveryPlatformPaymentDiscountAmount()));
        entity.setSellerShopPaymentDiscountAmount(NumberUtil.sub(entity.getShopPaymentDiscountAmount(), entity.getDeliveryShopPaymentDiscountAmount()));

        return this.updateById(entity);
    }

    /**
     * @param detailId         订单明细ID
     * @param deliveryQuantity 发货数量
     * @return
     */
    @Override
    public boolean updatePartDeliveryData(Long detailId, int deliveryQuantity) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        entity.setDeliveryQuantity(deliveryQuantity + entity.getDeliveryQuantity() );
        entity.setDeliveryAmount(NumberUtil.mul(entity.getDeliveryQuantity(), entity.getGoodsPrice()));
        entity.setDeliveryCashDiscountAmount(NumberUtil.mul(entity.getCashDiscountAmount(), NumberUtil.div(new BigDecimal(entity.getDeliveryQuantity()), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryTicketDiscountAmount(NumberUtil.mul(entity.getTicketDiscountAmount(), NumberUtil.div(new BigDecimal(entity.getDeliveryQuantity()), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        //B2B部分
        entity.setDeliveryPlatformCouponDiscountAmount(NumberUtil.mul(entity.getPlatformCouponDiscountAmount(), NumberUtil.div(new BigDecimal(entity.getDeliveryQuantity()), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryCouponDiscountAmount(NumberUtil.mul(entity.getCouponDiscountAmount(), NumberUtil.div(new BigDecimal(entity.getDeliveryQuantity()), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryPresaleDiscountAmount(NumberUtil.mul(entity.getPresaleDiscountAmount(), NumberUtil.div(new BigDecimal(entity.getDeliveryQuantity()), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

        entity.setDeliveryPlatformPaymentDiscountAmount(NumberUtil.mul(entity.getPlatformPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(entity.getDeliveryQuantity()), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryShopPaymentDiscountAmount(NumberUtil.mul(entity.getShopPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(entity.getDeliveryQuantity()), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

        return this.updateById(entity);
    }

    /**
     * 更新部分发货退货数据
     *
     * @param detailId 订单明细ID
     * @return
     */
    @Override
    public boolean updatePartDeliveryReturnData(Long detailId) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        entity.setSellerReturnQuantity(entity.getGoodsQuantity() - entity.getDeliveryQuantity());
        entity.setSellerReturnAmount(NumberUtil.sub(entity.getGoodsAmount(), entity.getDeliveryAmount()));
        entity.setSellerReturnCashDiscountAmount(NumberUtil.sub(entity.getCashDiscountAmount(), entity.getDeliveryCashDiscountAmount()));
        entity.setSellerReturnTicketDiscountAmount(NumberUtil.sub(entity.getTicketDiscountAmount(), entity.getDeliveryTicketDiscountAmount()));

        entity.setSellerPlatformCouponDiscountAmount(NumberUtil.sub(entity.getPlatformCouponDiscountAmount(), entity.getDeliveryPlatformCouponDiscountAmount()));
        entity.setSellerCouponDiscountAmount(NumberUtil.sub(entity.getCouponDiscountAmount(), entity.getDeliveryCouponDiscountAmount()));
        entity.setSellerPresaleDiscountAmount(NumberUtil.sub(entity.getPresaleDiscountAmount(), entity.getDeliveryPresaleDiscountAmount()));

        entity.setSellerPlatformPaymentDiscountAmount(NumberUtil.sub(entity.getPlatformPaymentDiscountAmount(), entity.getDeliveryPlatformPaymentDiscountAmount()));
        entity.setSellerShopPaymentDiscountAmount(NumberUtil.sub(entity.getShopPaymentDiscountAmount(), entity.getDeliveryShopPaymentDiscountAmount()));

        return this.updateById(entity);
    }

    @Override
    public boolean updateReceiveData(Long detailId, int receiveQuantity) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        entity.setReceiveQuantity(receiveQuantity);
        if (receiveQuantity == entity.getDeliveryQuantity()) {
            // 全部收货
            entity.setReceiveAmount(entity.getDeliveryAmount());
            entity.setReceiveCashDiscountAmount(entity.getDeliveryCashDiscountAmount());
            entity.setReceiveTicketDiscountAmount(entity.getDeliveryTicketDiscountAmount());
            //B2B部分
            entity.setReceiveCouponDiscountAmount(entity.getDeliveryCouponDiscountAmount());
            entity.setReceivePlatformCouponDiscountAmount(entity.getDeliveryPlatformCouponDiscountAmount());
            entity.setReceivePresaleDiscountAmount(entity.getDeliveryPresaleDiscountAmount());
            entity.setReceivePlatformPaymentDiscountAmount(entity.getDeliveryPlatformPaymentDiscountAmount());
            entity.setReceiveShopPaymentDiscountAmount(entity.getDeliveryShopPaymentDiscountAmount());
        } else {
            // 部分收货
            entity.setReceiveAmount(NumberUtil.mul(receiveQuantity, entity.getGoodsPrice()));
            entity.setReceiveCashDiscountAmount(NumberUtil.mul(entity.getCashDiscountAmount(), NumberUtil.div(new BigDecimal(receiveQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            entity.setReceiveTicketDiscountAmount(NumberUtil.mul(entity.getTicketDiscountAmount(), NumberUtil.div(new BigDecimal(receiveQuantity), new BigDecimal(entity.getDeliveryQuantity()))).setScale(2, RoundingMode.HALF_UP));
            //B2B部分
            entity.setReceiveCouponDiscountAmount(NumberUtil.mul(entity.getCouponDiscountAmount(), NumberUtil.div(new BigDecimal(receiveQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            entity.setReceivePlatformCouponDiscountAmount(NumberUtil.mul(entity.getPlatformCouponDiscountAmount(), NumberUtil.div(new BigDecimal(receiveQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            entity.setReceivePresaleDiscountAmount(NumberUtil.mul(entity.getPresaleDiscountAmount(), NumberUtil.div(new BigDecimal(receiveQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

            entity.setReceivePlatformPaymentDiscountAmount(NumberUtil.mul(entity.getPlatformPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(receiveQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            entity.setReceiveShopPaymentDiscountAmount(NumberUtil.mul(entity.getShopPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(receiveQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

        }
        return this.updateById(entity);
    }



    @Override
    public OrderDetailChangeBO updateReturnData(Long detailId, int returnQuantity, boolean saveFlag) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);

        OrderDetailChangeBO orderDetailChangeBO = new OrderDetailChangeBO();
        orderDetailChangeBO.setDetailId(entity.getDetailId());
        orderDetailChangeBO.setQuantity(returnQuantity);

        if (returnQuantity == entity.getDeliveryQuantity() - entity.getReturnQuantity()) {
            // 全部退货

            // 本次退货金额
            orderDetailChangeBO.setAmount(NumberUtil.sub(entity.getDeliveryAmount(), entity.getReturnAmount()));
            orderDetailChangeBO.setCashDiscountAmount(NumberUtil.sub(entity.getDeliveryCashDiscountAmount(), entity.getReturnCashDiscountAmount()));
            orderDetailChangeBO.setTicketDiscountAmount(NumberUtil.sub(entity.getDeliveryTicketDiscountAmount(), entity.getReturnTicketDiscountAmount()));
            orderDetailChangeBO.setPlatformCouponDiscountAmount(NumberUtil.sub(entity.getDeliveryPlatformCouponDiscountAmount(), entity.getReturnPlatformCouponDiscountAmount()));
            orderDetailChangeBO.setCouponDiscountAmount(NumberUtil.sub(entity.getDeliveryCouponDiscountAmount(), entity.getReturnCouponDiscountAmount()));
            orderDetailChangeBO.setPresaleDiscountAmount(NumberUtil.sub(entity.getDeliveryPresaleDiscountAmount(), entity.getReturnPresaleDiscountAmount()));
            orderDetailChangeBO.setPlatformPaymentDiscountAmount(NumberUtil.sub(entity.getDeliveryPlatformPaymentDiscountAmount(), entity.getReturnPlatformPaymentDiscountAmount()));
            orderDetailChangeBO.setShopPaymentDiscountAmount(NumberUtil.sub(entity.getDeliveryShopPaymentDiscountAmount(), entity.getReturnShopPaymentDiscountAmount()));

            // 累计退货金额
            entity.setReturnQuantity(entity.getDeliveryQuantity());
            entity.setReturnAmount(entity.getDeliveryAmount());
            entity.setReturnCashDiscountAmount(entity.getDeliveryCashDiscountAmount());
            entity.setReturnTicketDiscountAmount(entity.getDeliveryTicketDiscountAmount());
            entity.setReturnPlatformCouponDiscountAmount(entity.getDeliveryPlatformCouponDiscountAmount());
            entity.setReturnCouponDiscountAmount(entity.getDeliveryCouponDiscountAmount());
            entity.setReturnPresaleDiscountAmount(entity.getDeliveryPresaleDiscountAmount());
            entity.setReturnPlatformPaymentDiscountAmount(entity.getDeliveryPlatformPaymentDiscountAmount());
            entity.setReturnShopPaymentDiscountAmount(entity.getDeliveryShopPaymentDiscountAmount());

        } else {
            // 部分退货

            // 本次退货金额
            orderDetailChangeBO.setAmount(NumberUtil.mul(returnQuantity, entity.getGoodsPrice()));
            orderDetailChangeBO.setCashDiscountAmount(NumberUtil.mul(entity.getCashDiscountAmount(), NumberUtil.div(new BigDecimal(returnQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            orderDetailChangeBO.setTicketDiscountAmount(NumberUtil.mul(entity.getTicketDiscountAmount(), NumberUtil.div(new BigDecimal(returnQuantity), new BigDecimal(entity.getDeliveryQuantity()))).setScale(2, RoundingMode.HALF_UP));
            orderDetailChangeBO.setPlatformCouponDiscountAmount(NumberUtil.mul(entity.getPlatformCouponDiscountAmount(), NumberUtil.div(new BigDecimal(returnQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            orderDetailChangeBO.setCouponDiscountAmount(NumberUtil.mul(entity.getCouponDiscountAmount(), NumberUtil.div(new BigDecimal(returnQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            orderDetailChangeBO.setPresaleDiscountAmount(NumberUtil.mul(entity.getPresaleDiscountAmount(), NumberUtil.div(new BigDecimal(returnQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            orderDetailChangeBO.setPlatformPaymentDiscountAmount(NumberUtil.mul(entity.getPlatformPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(returnQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
            orderDetailChangeBO.setShopPaymentDiscountAmount(NumberUtil.mul(entity.getShopPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(returnQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

            // 累计退货金额
            entity.setReturnQuantity(entity.getReturnQuantity() + orderDetailChangeBO.getQuantity());
            entity.setReturnAmount(NumberUtil.add(entity.getReturnAmount(), orderDetailChangeBO.getAmount()));
            entity.setReturnCashDiscountAmount(NumberUtil.add(entity.getReturnCashDiscountAmount(), orderDetailChangeBO.getCashDiscountAmount()));
            entity.setReturnTicketDiscountAmount(NumberUtil.add(entity.getReturnTicketDiscountAmount(), orderDetailChangeBO.getTicketDiscountAmount()));
            entity.setReturnPlatformCouponDiscountAmount(NumberUtil.add(entity.getReturnPlatformCouponDiscountAmount(), orderDetailChangeBO.getPlatformCouponDiscountAmount()));
            entity.setReturnCouponDiscountAmount(NumberUtil.add(entity.getReturnCouponDiscountAmount(), orderDetailChangeBO.getCouponDiscountAmount()));
            entity.setReturnPresaleDiscountAmount(NumberUtil.add(entity.getReturnPresaleDiscountAmount(), orderDetailChangeBO.getPresaleDiscountAmount()));

            entity.setReturnPlatformPaymentDiscountAmount(NumberUtil.add(entity.getReturnPlatformPaymentDiscountAmount(), orderDetailChangeBO.getPlatformPaymentDiscountAmount()));
            entity.setReturnShopPaymentDiscountAmount(NumberUtil.add(entity.getReturnShopPaymentDiscountAmount(), orderDetailChangeBO.getShopPaymentDiscountAmount()));
        }

        if (saveFlag) {
            this.updateById(entity);
        }

        return orderDetailChangeBO;
    }

    @Override
    public boolean clearDeliveryDataByOrderId(Long orderId) {
        QueryWrapper<OrderDetailChangeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetailChangeDO::getOrderId, orderId);

        OrderDetailChangeDO entity = new OrderDetailChangeDO();
        entity.setDeliveryQuantity(0);
        entity.setDeliveryAmount(BigDecimal.ZERO);
        entity.setDeliveryCashDiscountAmount(BigDecimal.ZERO);
        entity.setDeliveryTicketDiscountAmount(BigDecimal.ZERO);
        entity.setSellerReturnQuantity(0);
        entity.setSellerReturnAmount(BigDecimal.ZERO);
        entity.setSellerReturnCashDiscountAmount(BigDecimal.ZERO);
        entity.setSellerReturnTicketDiscountAmount(BigDecimal.ZERO);
        entity.setReceiveQuantity(0);
        entity.setReceiveAmount(BigDecimal.ZERO);
        entity.setReceiveCashDiscountAmount(BigDecimal.ZERO);
        entity.setReceiveTicketDiscountAmount(BigDecimal.ZERO);
        entity.setReturnQuantity(0);
        entity.setReturnAmount(BigDecimal.ZERO);
        entity.setReturnCashDiscountAmount(BigDecimal.ZERO);
        entity.setReturnTicketDiscountAmount(BigDecimal.ZERO);

        return this.update(entity, queryWrapper);
    }

    @Override
    public boolean clearReturnDataByOrderId(Long orderId) {

        QueryWrapper<OrderDetailChangeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetailChangeDO::getOrderId, orderId);
        OrderDetailChangeDO entity = new OrderDetailChangeDO();
        entity.setSellerReturnQuantity(0);
        entity.setSellerReturnAmount(BigDecimal.ZERO);
        entity.setSellerReturnCashDiscountAmount(BigDecimal.ZERO);
        entity.setSellerReturnTicketDiscountAmount(BigDecimal.ZERO);
        entity.setReturnQuantity(0);
        entity.setReturnAmount(BigDecimal.ZERO);
        entity.setReturnCashDiscountAmount(BigDecimal.ZERO);
        entity.setReturnTicketDiscountAmount(BigDecimal.ZERO);
        entity.setReturnPlatformCouponDiscountAmount(BigDecimal.ZERO);
        entity.setReturnCouponDiscountAmount(BigDecimal.ZERO);
        entity.setReturnPresaleDiscountAmount(BigDecimal.ZERO);
        entity.setReturnPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        entity.setReturnShopPaymentDiscountAmount(BigDecimal.ZERO);
        return this.update(entity, queryWrapper);
    }


    @Override
    public boolean updateDeliveryInfo(Long detailId, int deliveryQuantity) {

        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        // 发货部分
        entity.setDeliveryQuantity(deliveryQuantity);
        entity.setDeliveryAmount(NumberUtil.mul(deliveryQuantity, entity.getGoodsPrice()));
        entity.setDeliveryCashDiscountAmount(NumberUtil.mul(entity.getCashDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryTicketDiscountAmount(NumberUtil.mul(entity.getTicketDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        //B2B部分
        entity.setDeliveryPlatformCouponDiscountAmount(NumberUtil.mul(entity.getPlatformCouponDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryCouponDiscountAmount(NumberUtil.mul(entity.getCouponDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryPresaleDiscountAmount(NumberUtil.mul(entity.getPresaleDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

        entity.setDeliveryPlatformPaymentDiscountAmount(NumberUtil.mul(entity.getPlatformPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));
        entity.setDeliveryShopPaymentDiscountAmount(NumberUtil.mul(entity.getShopPaymentDiscountAmount(), NumberUtil.div(new BigDecimal(deliveryQuantity), new BigDecimal(entity.getGoodsQuantity()))).setScale(2, RoundingMode.HALF_UP));

        return this.updateById(entity);
    }

    @Override
    public boolean updateCouponDiscountAmountByDetailId(Long detailId, BigDecimal platformCouponDiscountAmount, BigDecimal couponDiscountAmount,BigDecimal goodsPrice) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        entity.setPlatformCouponDiscountAmount(platformCouponDiscountAmount);
        entity.setCouponDiscountAmount(couponDiscountAmount);
        if (goodsPrice != null) {
            entity.setGoodsPrice(goodsPrice);
            entity.setGoodsAmount(NumberUtil.round(NumberUtil.mul(entity.getGoodsPrice(),entity.getGoodsQuantity()),2));
        }
        return this.updateById(entity);
    }

    /**
     * 转应收单是更新票折金额
     *
     * @param detailId             订单明细ID
     * @param ticketDiscountAmount 票折金额
     * @param afterReceiveFlag     是否在收货后
     * @return
     */
    @Override
    public boolean updateErpReceivableNoTicketAmount(Long detailId, BigDecimal ticketDiscountAmount, boolean afterReceiveFlag) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        BigDecimal ticketAmount = ticketDiscountAmount.add(entity.getTicketDiscountAmount());
        entity.setTicketDiscountAmount(ticketAmount);
        entity.setDeliveryTicketDiscountAmount(ticketAmount);
        if (afterReceiveFlag) {
            if (entity.getReceiveQuantity().compareTo(entity.getDeliveryQuantity()) == 0) {
                // 全部收货
                entity.setReceiveTicketDiscountAmount(ticketAmount);
            } else {
                // 部分收货
                entity.setReceiveTicketDiscountAmount(NumberUtil.mul(ticketAmount, NumberUtil.div(new BigDecimal(entity.getReceiveQuantity()), new BigDecimal(entity.getDeliveryQuantity()))).setScale(2, RoundingMode.HALF_UP));
            }
        }
        return this.updateById(entity);
    }

    /**
     * 删除应收单是更新票折金额
     *
     * @param detailId             订单明细ID
     * @param ticketDiscountAmount 票折金额
     * @param afterReceiveFlag     是否在收货后
     * @return
     */
    @Override
    public boolean updateErpReceivableNoCancelTicketAmount(Long detailId, BigDecimal ticketDiscountAmount, boolean afterReceiveFlag) {
        OrderDetailChangeDO entity = this.getByDetailId(detailId);
        BigDecimal ticketAmount = entity.getTicketDiscountAmount().subtract(ticketDiscountAmount);
        if(ticketAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new BusinessException(OrderErrorCode.ERP_DELIVERY_NO_TICKET_DISCOUNT_AMOUNT_ERROR);
        }
        entity.setTicketDiscountAmount(ticketAmount);
        entity.setDeliveryTicketDiscountAmount(ticketAmount);
        if (afterReceiveFlag) {
            if (entity.getReceiveQuantity().compareTo(entity.getDeliveryQuantity()) == 0) {
                // 全部收货
                entity.setReceiveTicketDiscountAmount(ticketAmount);
            } else {
                // 部分收货
                entity.setReceiveTicketDiscountAmount(NumberUtil.mul(ticketAmount, NumberUtil.div(new BigDecimal(entity.getReceiveQuantity()), new BigDecimal(entity.getDeliveryQuantity()))).setScale(2, RoundingMode.HALF_UP));
            }
        }
        return this.updateById(entity);
    }

}
