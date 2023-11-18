package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateOrderDTO extends BaseDTO {


	private static final long serialVersionUID = 2560787671640832456L;

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
     * 订单id
     */
    private Long returnId;

    private Long settlementId;

    private Date deliveryTime;

    private Integer type;

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
     * 购买商品总额
     */
    private BigDecimal backAmount;

    /**
     * 购买数量
     */
    private Long goodsQuantity;

    /**
     * 支付方式
     */
    private Integer paymentMethod;

    /**
     *
     */
    private Integer backAmountType;

    /**
     * 订单返利金额
     */
    private BigDecimal discountAmount;

    /**
     * 计算时间
     */
    private Date calculateTime;

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

    private Date comparisonTime;
}
