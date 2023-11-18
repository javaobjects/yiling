package com.yiling.order.order.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.request.QueryPromotionNumberRequest;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.entity.OrderDetailDO;
import com.yiling.order.order.service.OrderDetailChangeService;
import com.yiling.order.order.service.OrderDetailService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;

/**
 * 订单明细api查询
 * @author:wei.wang
 * @date:2021/6/22
 */
@DubboService
public class OrderDetailApiImpl implements OrderDetailApi {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderDetailChangeService orderDetailChangeService;

    /**
     * 根据orderId批量获取明细信息
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderDetailDTO> getOrderDetailByOrderIds(List<Long> orderIds) {
        List<OrderDetailDO> orderDetail = orderDetailService.getOrderDetailByOrderIds(orderIds);
        return PojoUtils.map(orderDetail,OrderDetailDTO.class);
    }

    /**
     * 根据订单查询购买收货返货数量和种类
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderGoodsTypeAndNumberDTO getOrderGoodsTypeAndNumber(Long orderId) {

        return orderDetailService.getOrderGoodsTypeAndNumber(orderId);
    }

    /**
     * 获取明细信息
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDetailDTO> getOrderDetailInfo(Long orderId) {
        List<OrderDetailDTO> orderDetailInfo = orderDetailService.getOrderDetailInfo(orderId);

        if (CollectionUtil.isEmpty(orderDetailInfo)) {

            return ListUtil.empty();
        }

        Map<Long,OrderDetailChangeDO> resultChangeMap = orderDetailChangeService.listByOrderId(orderId).stream().collect(Collectors.toMap(OrderDetailChangeDO::getDetailId, Function.identity()));

        return  orderDetailInfo.stream().map(e ->{

            Optional.ofNullable(resultChangeMap.get(e.getId())).ifPresent( t -> {
                e.setDeliveryAmount(t.getDeliveryAmount());
                e.setDeliveryQuantity(t.getDeliveryQuantity());
                e.setReceiveAmount(t.getReceiveAmount());
                e.setReceiveQuantity(t.getReceiveQuantity());
                e.setCashDiscountAmount(t.getCashDiscountAmount().subtract(t.getSellerReturnCashDiscountAmount()));
                e.setTicketDiscountAmount(t.getTicketDiscountAmount().subtract(t.getSellerReturnTicketDiscountAmount()));
                e.setReceivePlatformCouponDiscountAmount(t.getReceivePlatformCouponDiscountAmount());
                e.setReceiveCouponDiscountAmount(t.getReceiveCouponDiscountAmount());
                e.setReturnCouponDiscountAmount(t.getReturnCouponDiscountAmount());
                e.setDeliveryCouponDiscountAmount(t.getDeliveryCouponDiscountAmount());
                e.setDeliveryPlatformCouponDiscountAmount(t.getDeliveryPlatformCouponDiscountAmount());
                e.setPlatformCouponDiscountAmount(t.getPlatformCouponDiscountAmount());
                e.setCouponDiscountAmount(t.getCouponDiscountAmount());
            });

            return  e;

        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailDTO> selectlistByOrderNo(String orderNo,List goodsIdList) {
        QueryWrapper<OrderDetailDO> orderReturnDetailDOlWrapper = new QueryWrapper<>();
        orderReturnDetailDOlWrapper.lambda().eq(OrderDetailDO::getOrderNo, orderNo);
        orderReturnDetailDOlWrapper.lambda().in(OrderDetailDO::getGoodsId, goodsIdList);
        return PojoUtils.map(orderDetailService.list(orderReturnDetailDOlWrapper),OrderDetailDTO.class);
    }

    @Override
    public List<OrderDetailDTO> listByIdList(List<Long> idList) {
        return PojoUtils.map(orderDetailService.listByIds(idList), OrderDetailDTO.class);
    }

    /**
     * 根据订单编号查询明细信息
     *
     * @param id
     * @return
     */
    @Override
    public OrderDetailDTO getOrderDetailById(Long id) {
        OrderDetailDO detailDO = orderDetailService.getById(id);
        return PojoUtils.map(detailDO,OrderDetailDTO.class);
    }


    @Override
    public Integer getPromotionNumberByDistributorGoodsId(QueryPromotionNumberRequest promotionNumberRequest) {
        Assert.notNull(promotionNumberRequest.getBuyerEid(), "商品EID为空!");
        Assert.notNull(promotionNumberRequest.getDistributorGoodsId(), "商家商品EID为空!");
        Assert.notNull(promotionNumberRequest.getActivityId(), "活动ID为空!");

        return orderDetailService.getPromotionNumberByDistributorGoodsId(promotionNumberRequest);
    }
}
