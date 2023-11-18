package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 协议兑付订单明细表
 *
 * @author dexi.yao
 * @date 2021-07-15
 */
@Data
@Accessors(chain = true)
public class ExportAgreementRebateOrderBO {



	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 以岭商品Id
	 */
	private Long goodsId;

	/**
	 * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
	 */
	private Integer orderStatus;

	/**
	 * 订单状态
	 */
	private String orderStatusName;

	/**
	 * 支付方式：1-线下支付 2-账期 3-预付款
	 */
	private Integer paymentMethod;

	/**
	 * 支付方式
	 */
	private String paymentMethodName;

	/**
	 * 支付状态：1-待支付 2-已支付
	 */
	private Integer paymentStatus;

	/**
	 * 支付状态
	 */
	private String paymentStatusName;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 商品批准文号
	 */
	private String goodsLicenseNo;

	/**
	 * 商品规格
	 */
	private String goodsSpecification;

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


}
