package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebateOrderPageListDTO extends BaseDTO {

	/**
	 * 销售主体Eid
	 */
	private Long eid;

	/**
	 * 采购方eid
	 */
	private Long secondEid;

	/**
	 * 订单id
	 */
	private Long orderId;

	/**
	 *退货单
	 */
	private Long  returnId;

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
}
