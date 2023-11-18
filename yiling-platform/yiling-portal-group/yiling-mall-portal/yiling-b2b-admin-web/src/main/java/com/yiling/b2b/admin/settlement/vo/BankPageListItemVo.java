package com.yiling.b2b.admin.settlement.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 银行表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("银行vo")
public class BankPageListItemVo extends BaseVO {


	/**
	 * 总行名称
	 */
	@ApiModelProperty(value = "总行名称")
	private String headName;

	/**
	 * 总行简称
	 */
	@ApiModelProperty(value = "总行简称")
	private String headSimpleName;

	/**
	 * 总行编码
	 */
	@ApiModelProperty(value = "总行编码")
	private String headCode;


	/**
	 * 支行行号
	 */
	@ApiModelProperty(value = "支行编码")
	private String branchName;

	/**
	 * 支行名称
	 */
	@ApiModelProperty(value = "支行名称")
	private String branchNum;

	/**
	 * 银行类型 1-总行 2-支行
	 */
	@ApiModelProperty(value = "银行类型 1-总行 2-支行")
	private Integer type;


}
