package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FinancialRebateEntPageListItemVO extends BaseVO {

	/**
	 * 企业id
	 */
	@ApiModelProperty(value = "企业id", hidden = true)
	@JsonIgnore
	private Long customerEid;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String name;

	/**
	 * 渠道ID
	 */
	@ApiModelProperty(value = "渠道ID")
	private Long channelId;

	/**
	 * 企业账号
	 */
	@ApiModelProperty(value = "企业账号")
	private String easCode;

	/**
	 * 已兑付金额
	 */
	@ApiModelProperty(value = "已兑付金额")
	private BigDecimal discountAmount;

	/**
	 * 已使用金额
	 */
	@ApiModelProperty(value = "已使用金额")
	private BigDecimal usedAmount;

	/**
	 * 已兑付次数
	 */
	@ApiModelProperty(value = "已兑付次数")
	private Integer discountCount;

	/**
	 * 已使用次数
	 */
	@ApiModelProperty(value = "已使用次数")
	private Integer usedCount;

	/**
	 * 执业许可证号/社会信用统一代码
	 */
	@ApiModelProperty(value = "执业许可证号/社会信用统一代码")
	private String licenseNumber;
}
