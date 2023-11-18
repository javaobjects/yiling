package com.yiling.hmc.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMarketOrderRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 订单状态:1-待确认/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
     */
    private Integer orderStatus;

    /**
     * 药品服务终端id
     */
    private Long eid;

    /**
     * 药品服务终端名称
     */
    private String ename;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 医生id
     */
    private Long doctorId;

    /**
     * 支付方式:1-微信支付
     */
    private Integer paymentMethod;

    /**
     * 支付状态:1-未支付，2-已支付
     */
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 发货时间
     */
    private Date deliverTime;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 第三方支付单号
     */
    private String thirdPayNo;

    /**
     * 第三方支付金额
     */
    private BigDecimal thirdPayAmount;

    /**
     * 运费
     */
    private BigDecimal freightAmount;

    /**
     * 商品总额
     */
    private BigDecimal goodsTotalAmount;

    /**
     * 订单总额 = 运费 + 商品总额
     */
    private BigDecimal orderTotalAmount;

    /**
     * 订单来源：1-商家后台，2-HMC小程序，3-HMC公众号
     */
    private Integer createSource;

    /**
     * 配送方式：1-快递 2-自提
     */
    private Integer deliverType;

    /**
     * 快递单号
     */
    private Integer deliverNo;

    /**
     * 快递公司名称
     */
    private Integer deliverCompanyName;

    /**
     * 平台运营备注
     */
    private String platformRemark;

    /**
     * 商家备注
     */
    private String merchantRemark;


    /**
     * 用户留言
     */
    private String remark;


}
