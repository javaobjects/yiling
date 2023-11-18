package com.yiling.payment.channel.yee.request;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-11-12
 */
@Data
public class YeePayTransferOrder {

	/**
	 * 业务ID
	 */
	private Long businessId;

	/**
	 * 打款单号
	 */
	private String outNo;

	/**
	 * 打款金额（注意小数点后两位）
	 */
	private BigDecimal amount;

	/**
	 * 银行卡号
	 */
	private String payeeAccount;

	/**
	 * 银行卡户名
	 */
	private String payeeName;

	/**
	 * 手续费承担方： 1-平台 2-用户
	 */
	private Integer feeChargeSide;

	/**
	 * 到账类型： 1-实时 2-两小时到账 3-次日到账
	 */
	private Integer receiveType;

	/**
	 * 开户行编号 示例值：ICBC 详见BankDO
	 */
	private String bankCode;

	/**
	 * 交易描述（附言）
	 */
	private String content;
}
