package com.yiling.user.agreement.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditRebateAmountRequest extends BaseRequest {

	/**
	 * account
	 */
	@NotNull
	private String account;

	/**
	 * reason
	 */
	@NotNull
	private String logName;

	/**
	 * 金额
	 */
	@NotNull
	private BigDecimal discountAmount;

	/**
	 * 兑付以前的金额
	 */
	@NotNull
	private BigDecimal beforeTotalAmount;
}
