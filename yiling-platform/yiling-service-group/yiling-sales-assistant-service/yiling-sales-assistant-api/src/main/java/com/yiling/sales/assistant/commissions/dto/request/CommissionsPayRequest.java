package com.yiling.sales.assistant.commissions.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommissionsPayRequest extends BaseRequest {

	/**
	 * 佣金明细id
	 */
	private Long id;

	/**
	 * 兑付金额
	 */
	private BigDecimal subAmount;


}
