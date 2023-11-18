package com.yiling.b2b.admin.settlement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySettlementOrderDetailPageListForm extends BaseForm {

	@ApiModelProperty("订单id")
	@NotNull()
	private Long  orderId;
}
