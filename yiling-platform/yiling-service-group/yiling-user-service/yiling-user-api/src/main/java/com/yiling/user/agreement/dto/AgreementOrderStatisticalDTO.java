package com.yiling.user.agreement.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 详情信息
 *
 * @author:wei.wang
 * @date:2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AgreementOrderStatisticalDTO extends BaseDTO {

	/**
	 * 可对账订单数量
	 */
	private Integer passOrderCount;

	/**
	 * 不可对账订单数量
	 */
	private Integer unPassOrderCount;

	/**
	 * 可对账订单退款数量
	 */
	private Integer passOrderRefundCount;

	/**
	 * 不可对账订单退款数量
	 */
	private Integer unPassOrderRefundCount;


}
