package com.yiling.admin.b2b.settlement.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentForm extends BaseForm {

	/**
	 * 结算单id
	 */
	@ApiModelProperty("结算单id")
	@NotNull
	private List<SettlementId> settlementIds;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String settlementRemark;

	@Data
	@Accessors(chain = true)
	public  static  class SettlementId{

		/**
		 * 结算单id
		 */
		@ApiModelProperty("结算单id")
		@NotNull
		private Long settlementId;

	}


}
