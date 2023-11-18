package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 导入招标挂网价 Form
 *
 * @author: dexi.yao
 * @date: 2021/07/08
 */
@Data
public class ExportAgreementApplyOrderBO {

	/**
	 * 申请单号
	 */
	private String code;

	/**
	 * 协议ID
	 */
	private Long agreementId;

	/**
	 * 协议名称
	 */
	private String name;

	/**
	 * 订单号
	 */
	private String orderCode;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 商品id
	 */
	private Long goodsId;

	/**
	 * erp编码
	 */
	private String erpCode;

	/**
	 * 类型
	 */
	private String orderType;

	/**
	 * 成交数量
	 */
	private Long goodsQuantity;

	/**
	 * 小计
	 */
	private BigDecimal price;

	/**
	 * 返利金额
	 */
	private BigDecimal discountAmount;

}
