package com.yiling.admin.sales.assistant.commissions.vo;

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
@ApiModel("用户佣金列表VO")
@AllArgsConstructor
@NoArgsConstructor
public class CommissionsPageVO<T> extends Page<T> {

	/**
	 * 累计佣金金额
	 */
	@ApiModelProperty(value = "累计佣金金额")
	private BigDecimal totalAmount;

	/**
	 * 以结算金额
	 */
	@ApiModelProperty(value = "以结算金额")
	private BigDecimal paidAmount;

	/**
	 * 待结算金额
	 */
	@ApiModelProperty(value = "待结算金额")
	private BigDecimal surplusAmount;

}
