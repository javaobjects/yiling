package com.yiling.sales.assistant.app.commissions.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/9/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("我的钱包VO")
@AllArgsConstructor
@NoArgsConstructor
public class CommissionsPageVO<T> extends Page<T> {


	/**
	 * 佣金余额
	 */
	@ApiModelProperty(value = "佣金余额")
	private BigDecimal surplusAmount;

	/**
	 * 上一日收益
	 */
	@ApiModelProperty(value = "上一日收益")
	private BigDecimal yesterdayAmount;

	/**
	 * 周收益
	 */
	@ApiModelProperty(value = "周收益")
	private BigDecimal weekAmount;

	/**
	 * 月收益
	 */
	@ApiModelProperty(value = "月收益")
	private BigDecimal monthAmount;

    public BigDecimal getSurplusAmount() {
        BigDecimal result=surplusAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    public BigDecimal getYesterdayAmount() {
        BigDecimal result=yesterdayAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    public BigDecimal getWeekAmount() {
        BigDecimal result=weekAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    public BigDecimal getMonthAmount() {
        BigDecimal result=monthAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result;
    }
}
