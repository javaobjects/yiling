package com.yiling.settlement.b2b.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SettlementAmountInfoDTO extends BaseDTO {

	/**
	 * 今日结算金额
	 */
	private BigDecimal currentAmount;

	/**
	 * 昨日结算金额
	 */
	private BigDecimal yesterdayAmount;

	/**
	 * 总结算金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 平台总计
	 */
	private BigDecimal platformTotal;
}
