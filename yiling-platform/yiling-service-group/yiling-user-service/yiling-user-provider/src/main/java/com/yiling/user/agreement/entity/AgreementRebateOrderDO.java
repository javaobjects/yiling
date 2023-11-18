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
 * 协议兑付订单表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_order")
public class AgreementRebateOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 销售主体Eid
     */
    private Long eid;

    /**
     * 采购方eid
     */
    private Long secondEid;

    /**
     * eas账号
     */
    private String easAccount;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     *退货单
     */
    private Long  returnId;

    private Long settlementId;

    private Integer type;

    private Date deliveryTime;
    /**
     * 协议Id
     */
    private Long agreementId;

    /**
     * 兑付条件ID
     */
    private Long agreementConditionId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 购买商品总额
     */
    private BigDecimal goodsAmount;

    /**
     * 购买数量
     */
    private Long goodsQuantity;

    /**
     * 支付方式
     */
    private Integer paymentMethod;

    /**
     * 订单返利金额
     */
    private BigDecimal discountAmount;

    /**
     * 计算时间
     */
    private Date calculateTime;

    private Date comparisonTime;

    /**
     * 兑付时间
     */
    private Date cashTime;

    /**
     * 兑付状态1计算状态2已经兑付
     */
    private Integer cashStatus;

    /**
     * 是否已经满足条件状态1未满足2已经满足
     */
    private Integer conditionStatus;

    private BigDecimal policyValue;

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
