package com.yiling.b2b.admin.settlement.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBankPageListForm extends QueryPageListForm {

	/**
	 * 银行类型 1-总行 2-支行
	 */
	@ApiModelProperty("银行类型 1-总行 2-支行")
	@Min(value = 1)
	@Max(value = 2)
	private Integer type;

	/**
	 * 名称(模糊）
	 */
	@ApiModelProperty("名称(模糊）")
	private String name;
}
