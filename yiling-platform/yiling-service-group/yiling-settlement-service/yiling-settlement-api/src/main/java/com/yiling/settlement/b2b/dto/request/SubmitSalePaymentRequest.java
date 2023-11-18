package com.yiling.settlement.b2b.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitSalePaymentRequest extends BaseRequest {

	/**
	 * 结算单id
	 */
	@NotNull
	private List<Long> settlementIds;

	/**
	 * 备注
	 */
	@NotNull
	private String settlementRemark;
}
