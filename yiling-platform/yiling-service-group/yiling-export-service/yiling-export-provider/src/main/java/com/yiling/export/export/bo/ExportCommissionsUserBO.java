package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-09-26
 */
@Data
public class ExportCommissionsUserBO {

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 用户名子
	 */
	private String userName;

	/**
	 * 累计佣金金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 以结算金额
	 */
	private BigDecimal paidAmount;

	/**
	 * 待结算金额
	 */
	private BigDecimal surplusAmount;

	/**
	 * 手机号
	 */
	private String mobile;


}
