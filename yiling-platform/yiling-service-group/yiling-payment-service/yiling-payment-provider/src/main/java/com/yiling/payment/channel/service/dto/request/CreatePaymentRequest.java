package com.yiling.payment.channel.service.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreatePaymentRequest extends BaseRequest {

	/**
	 * 业务ID
	 */
	private Long businessId;

	/**
	 * 企业付款订单号
	 */
	private String payNo;

	/**
	 * 付款金额
	 */
	private BigDecimal amount;

	/**
	 * 手续费承担方： 1-平台 2-用户
	 */
	private Integer feeChargeSide;

	/**
	 * 到账类型： 1-实时 2-两小时到账 3-次日到账
	 */
	private Integer receiveType;

	/**
	 * 收款银行卡号
	 */
	private String account;

	/**
	 * 收款方开户名
	 */
	private String accountName;

	/**
	 * 开户行编号 示例值：ICBC 详见BankDO
	 */
	private String bankCode;

	/**
	 * 交易描述（附言）
	 */
	private String content;

}
