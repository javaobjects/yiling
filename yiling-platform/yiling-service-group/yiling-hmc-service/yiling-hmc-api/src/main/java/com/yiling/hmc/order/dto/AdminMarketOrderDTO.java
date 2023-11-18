package com.yiling.hmc.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class AdminMarketOrderDTO extends BaseDTO {

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 订单状态:10-待付款，20-待发货，30-待收货，40-已取消
     */
    private Integer orderStatus;

    /**
     * 订单类型 1-八子，2-处方订单
     */
    private Integer marketOrderType;

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
    private Integer doctorId;

    /**
     * 支付方式:1-微信支付
     */
    private Integer paymentMethod;

    /**
     * 支付状态:1-未支付，2-已支付,3-已全部退款
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
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

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
     * 收货地址
     */
    private String address;

    /**
     * 处方id
     */
    private Long prescriptionId;

    /**
     * 处方价格
     */
    private Long prescriptionPrice;

    /**
     * IH 订单id
     */
    private Long ihOrderId;

    /**
     * IH 处方编号
     */
    private String ihPrescriptionNo;

    /**
     * IH 处方订单编号
     */
    private String ihPrescriptionOrderNo;

    /**
     * IH配送商来源 1：以岭互联网医院IH 2：健康中心HMC
     */
    private Integer ihPharmacySource;

    /**
     * 处方类型 1：西药 0：中药
     */
    private Integer prescriptionType;

}
