package com.yiling.settlement.b2b.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitPaymentRequest extends BaseRequest {

	/**
	 * 结算单id
	 */
	private Long settlementId;

	/**
	 * 支付流水单号
	 */
	private String payCode;
}
