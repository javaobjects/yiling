package com.yiling.hmc.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_order")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 平台订单编号
     */
    private String orderNo;

    /**
     * 购买保险id
     */
    private Long insuranceRecordId;

    /**
     * 保险单号
     */
    private String policyNo;

    /**
     * 第三方兑保编号
     */
    private String thirdConfirmNo;

    /**
     * 保险公司id
     */
    private Long insuranceCompanyId;

    /**
     * 开方状态 1-已开，2-未开
     */
    private Integer prescriptionStatus;

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
     * 支付方式:1-保险理赔结算
     */
    private Integer paymentMethod;

    /**
     * 支付状态:1-未支付/2-已支付/3-已退款/4-部分退款
     */
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 下单人
     */
    private Long orderUser;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 订单类型:1-其他/2-虚拟商品订单/3-普通商品/4-药品订单
     */
    private Integer orderType;

    /**
     * 是否为保险理赔兑付单1-是 2-否
     */
    private Integer isInsuranceOrder;

    /**
     * 订单额总额
     */
    private BigDecimal totalAmount;

    /**
     * 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
     */
    private Integer terminalSettleStatus;

    /**
     * 药品终端结算金额
     */
    private BigDecimal terminalSettleAmount;

    /**
     * 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
     */
    private Integer insuranceSettleStatus;

    /**
     * 保司跟以岭结算额
     */
    private BigDecimal insuranceSettleAmount;

    /**
     * 保司跟以岭试算结算额
     */
    private BigDecimal insuranceSettleAmountTrial;

    /**
     * 与保司数据同步状态:1-待同步/2-已同步/3-同步失败（异常）
     */
    private Integer synchronousType;

    /**
     * 配送方式 1-自提 2-物流
     */
    private Integer deliverType;

    /**
     * 条形码key
     */
    private String barCodeKey;

    /**
     * 创建来源 1-商家后台，2-C端小程序
     */
    private Integer createSource;

    /**
     * 订单票据，逗号分隔
     */
    private String orderReceipts;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
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
     * 备注
     */
    private String remark;


}
