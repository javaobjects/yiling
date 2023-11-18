package com.yiling.user.agreement.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 协议兑付订单明细表
 *
 * @author dexi.yao
 * @date 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateOrderDetailDTO extends BaseDTO {

	private static final long serialVersionUID = -2015197498552752876L;

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
	 * 订单明细Id
	 */
	private Long orderDetailId;

	/**
	 * 单据类型 1- 订单 2-退货单 3-结算单
	 */
    private Integer type;

	/**
	 * eas账号
	 */
	private String easAccount;

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

    /**
     * 备注
     */
    private String remark;


}
