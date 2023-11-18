package com.yiling.mall.order.event;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRecordRequest;
import com.yiling.marketing.promotion.dto.PromotionReduceStockDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;

import lombok.Getter;
import lombok.Setter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.event
 * @date: 2022/1/5
 */
public class CreateOrderEvent extends ApplicationEvent {


    /**
     * 是否保存用户设备信息
     */
    @Getter
    @Setter
    private Boolean saveOrderDevice;

    /**
     * 用户Id
     */
    @Getter
    @Setter
    private Long userId;

    /**
     * 下单IP
     */
    @Getter
    @Setter
    private String ip;

    /**
     * 下单用户代理
     */
    @Getter
    @Setter
    private String userAgent;

    /**
     * 下单来源
     */
    @Getter
    @Setter
    private OrderSourceEnum orderSourceEnum;


    /**
     * 订单类型
     */
    @Getter
    @Setter
    private OrderTypeEnum orderTypeEnum;

    /**
     * 订单信息
     */
    @Getter
    @Setter
    private List<OrderDTO> orderList;

    /**
     * 平台优惠劵ID
     */
    @Getter
    @Setter
    private Long platformCustomerCouponId;

    /**
     * 商家优惠劵活动Ids
     */
    @Getter
    @Setter
    private List<Long> shopCustomerCouponIds;


    /**
     * 赠品信息
     */
    @Getter
    @Setter
    private List<PromotionReduceStockDTO> promotionReduceStockList;


    /**
     * 支付促销活动扣减信息
     */
    @Getter
    @Setter
    private List<SavePayPromotionRecordRequest> paymentReduceStockList;

    /**
     * 是否扣减账期账户
     */
    @Getter
    @Setter
    private Boolean reducePaymentDayAccount;


    /**
     * 是否有特价商品
     */
    @Getter
    @Setter
    private Boolean haveSpecialProduct;



    public CreateOrderEvent(Object source, List<OrderDTO> orderList, Boolean reducePaymentDayAccount,Boolean haveSpecialProduct) {
        super(source);
        this.orderList = orderList;
        this.reducePaymentDayAccount=reducePaymentDayAccount;
        this.haveSpecialProduct = haveSpecialProduct;
    }
}
