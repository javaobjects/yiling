package com.yiling.admin.b2b.settlement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderDetailPageListForm extends QueryPageListForm {

	@ApiModelProperty("订单id")
	@NotNull()
	private Long  orderId;

	@ApiModelProperty("结算单id")
	@NotNull()
	private Long  settlementId;
}
