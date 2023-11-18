package com.yiling.admin.sales.assistant.commissions.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author dexi.yao
 * @date 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommissionsPageListItemVO extends BaseVO {

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private Long userId;

	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String name;

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

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

}
