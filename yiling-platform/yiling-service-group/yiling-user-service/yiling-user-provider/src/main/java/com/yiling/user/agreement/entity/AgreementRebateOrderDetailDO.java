package com.yiling.user.agreement.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议兑付订单明细表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_order_detail")
public class AgreementRebateOrderDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议计算单据表ID
     */
    private Long rebateOrderId;

    /**
     * 销售主体Eid
     */
    private Long eid;

    /**
     * 采购方eid
     */
    private Long secondEid;

    /**
     * 协议Id
     */
    private Long agreementId;

    /**
     * 协议条件Id
     */
    private Long agreementConditionId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单Id
     */
    private Long returnId;

    private Long settlementId;

    private Date deliveryTime;

    private Integer type;

    /**
     * eas账号
     */
    private String easAccount;

    /**
     * 订单明细Id
     */
    private Long orderDetailId;

    /**
     * 以岭商品Id
     */
    private Long goodsId;

    /**
     * 商品购买总额
     */
    private BigDecimal goodsAmount;

    /**
     * 购买数量
     */
    private Long goodsQuantity;

	/**
	 * 是否已经满足条件状态1未满足2已经满足
	 */
	private Integer conditionStatus;

    /**
     * 协议返还金额
     */
    private BigDecimal discountAmount;

    /**
     * 协议政策
     */
    private BigDecimal policyValue;

    /**
     * 兑付状态
     */
    private Integer cashStatus;


    private Date comparisonTime;

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
