package com.yiling.sales.assistant.commissions.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户佣金统计DTO
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommissionsUserStatisticsDTO extends BaseDTO {


	private static final long serialVersionUID = -3203888235091278224L;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 佣金余额
	 */
	private BigDecimal surplusAmount;

	/**
	 * 上一日收益
	 */
	private BigDecimal yesterdayAmount;

	/**
	 * 周收益
	 */
	private BigDecimal weekAmount;

	/**
	 * 月收益
	 */
	private BigDecimal monthAmount;


}
