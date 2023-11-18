package com.yiling.sales.assistant.app.rebate.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRebateOrderPageListForm extends QueryPageListForm {

	/**
	 * 返利申请表id
	 */
	@NotNull
	@ApiModelProperty(value = "返利申请表明细id")
	private Long applyDetailId;

}
