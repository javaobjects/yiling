package com.yiling.open.web.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRebatePageListForm extends QueryPageListForm {

	@ApiModelProperty(value = "查询天数，默认为1")
	private Integer dayNum=1;

	@ApiModelProperty(value = "票折单号")
	private String ticketDiscountNo;
}
