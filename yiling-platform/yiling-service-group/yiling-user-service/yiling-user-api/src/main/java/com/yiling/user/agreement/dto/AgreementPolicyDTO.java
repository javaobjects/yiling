package com.yiling.user.agreement.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 协议政策DTO
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementPolicyDTO extends BaseDTO {

	/**
	 * 条件序号可为月,季度,梯度
	 */
	private Integer rangeNo;

	/**
	 * 协议政策
	 */
	private BigDecimal policyValue;

	/**
	 * 政策类型 1-购进额 2-回款额
	 */
	private Integer policyType;

	/**
	 * 梯度起始值
	 */
	private BigDecimal mixValue;

	/**
	 * 梯度最大值
	 */
	private BigDecimal maxValue;
}
