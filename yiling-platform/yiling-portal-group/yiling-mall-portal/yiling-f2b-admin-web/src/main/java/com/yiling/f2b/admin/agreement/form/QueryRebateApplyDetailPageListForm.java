package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRebateApplyDetailPageListForm extends QueryPageListForm {

	/**
	 * 返利申请表id
	 */
	@NotNull
	@ApiModelProperty(value = "返利申请表id")
	private Long applyId;

}
