package com.yiling.hmc.order.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * HMC订单表
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_market_order")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 订单状态:1-待确认/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
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
     * IH 药品服务终端id
     */
    private Long ihEid;

    /**
     * IH 药品服务终端名称
     */
    private String ihEname;

    /**
     * IH配送商来源 1：以岭互联网医院IH 2：健康中心HMC
     */
    private Integer ihPharmacySource;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 医生id
     */
    private Long doctorId;

    /**
     * 支付方式:2-微信支付
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
     * 商户交易编号
     */
    private String merTranNo;

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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 下单人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 下单时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 用户留言
     */
    private String remark;

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
     * 处方类型 1：西药 0：中药
     */
    private Integer prescriptionType;

}
