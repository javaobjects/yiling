package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/27
 */
@Data
public class HmcExportMarketOrderBO {

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private String orderStatus;


    /**
     * 药品服务终端名称
     */
    private String ename;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 支付状态:1-未支付，2-已支付
     */
    private String paymentStatus;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 发货时间
     */
    private String deliverTime;

    /**
     * 收货时间
     */
    private String receiveTime;

    /**
     * 取消时间
     */
    private String cancelTime;

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
     * 配送方式：1-快递 2-自提
     */
    private String deliverType;

    /**
     * 快递单号
     */
    private String deliverNo;

    /**
     * 快递公司名称
     */
    private String deliverCompanyName;

    /**
     * 平台运营备注
     */
    private String platformRemark;

    /**
     * 商家备注
     */
    private String merchantRemark;


    /**
     * 下单人
     */
    private Long createUser;

    /**
     * 下单时间
     */
    private String createTime;

    /**
     * 用户留言
     */
    private String remark;

    /**
     * 支付票据
     */
    private String payTicket;

    /**
     * 收货人姓名
     */
    private String name;
    /**
     * 收货人手机
     */
    private String mobile;
    /**
     * 收货人省份
     */
    private String provinceName;

    /**
     * 收货人城市
     */
    private String cityName;

    /**
     * 收货人地区
     */
    private String regionName;

    /**
     * 收货人详细地址/提货地址
     */
    private String address;

    /**
     * 买家昵称
     */
    private String nickName;

    /**
     * 买家手机号
     */
    private String userMobile;

    /**
     * 邀请医生
     */
    private String doctor;

}
