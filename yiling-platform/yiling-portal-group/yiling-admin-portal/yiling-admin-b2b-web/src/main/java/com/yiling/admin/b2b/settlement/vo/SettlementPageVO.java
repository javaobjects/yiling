package com.yiling.admin.b2b.settlement.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("结算单page")
public class SettlementPageVO<T> extends Page<T> {

	/**
	 * 今日结算金额
	 */
	@ApiModelProperty(value = "今日结算金额")
	private BigDecimal currentAmount;

	/**
	 * 昨日结算金额
	 */
	@ApiModelProperty(value = "昨日结算金额")
	private BigDecimal yesterdayAmount;

	/**
	 * 总结算金额
	 */
	@ApiModelProperty(value = "总结算金额")
	private BigDecimal totalAmount;

	/**
	 * 平台总计
	 */
	@ApiModelProperty(value = "平台总计")
	private BigDecimal platformTotal;

	public BigDecimal getCurrentAmount() {
		return currentAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getYesterdayAmount() {
		return yesterdayAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getTotalAmount() {
		return totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getPlatformTotal() {
		return platformTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}
