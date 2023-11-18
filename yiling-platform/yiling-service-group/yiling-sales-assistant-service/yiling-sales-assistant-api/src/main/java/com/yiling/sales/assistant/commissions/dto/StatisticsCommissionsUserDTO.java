package com.yiling.sales.assistant.commissions.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售助手用户佣金DTO
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StatisticsCommissionsUserDTO extends BaseDTO {


	private static final long serialVersionUID = -3764013600989628562L;


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



}
