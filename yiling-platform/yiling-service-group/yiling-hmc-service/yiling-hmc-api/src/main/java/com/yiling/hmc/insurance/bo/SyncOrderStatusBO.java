package com.yiling.hmc.insurance.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 同步订单状态
 *
 * @author: fan.shen
 * @date: 2022/7/8
 */
@Data
public class SyncOrderStatusBO implements Serializable {

    /**
     * 渠道服务订单号
     */
    private String channelMainId;

    /**
     * 渠道方订单号
     */
    private String channelOrderId;

    /**
     * 0新订单,
     * 11订单待确认(如果存在支付环节也在此状态后进行扭转),
     * 14订单取消,
     * 15支付退款
     * 21待发货,
     * 22待收货,
     * 23签收成功,
     * 24发货失败,
     * 25拒收,
     * 26退货
     */
    private Integer orderStatus;

    /**
     * 支付时间，格式yyyy-MM-dd HH:mm:ss
     * 状态为已支付时  必传
     */
    private Date paymentTime;

    /**
     * 申请退款时间,格式yyyy-MM-dd HH:mm:ss
     * 状态为退货已完成时必传
     */
    private Date applyRefundTime;

    /**
     * 退款完成时间,格式yyyy-MM-dd HH:mm:ss
     * 状态为退货已完成时必传
     */
    private Date refundFinishTime;

    /**
     * 发货时间,格式yyyy-MM-dd HH:mm:ss
     * 状态为配送中时必传
     */
    private Date sendGoodsTime;

    /**
     * 收货时间,格式yyyy-MM-dd HH:mm:ss
     * 状态为已收货时必传
     */
    private Date getGoodsTime;

}
