package com.yiling.admin.b2b.settlement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySettlementDetailPageListForm extends QueryPageListForm {

	@ApiModelProperty("结算单id")
	@NotNull()
	private Long  settlementId;
}
