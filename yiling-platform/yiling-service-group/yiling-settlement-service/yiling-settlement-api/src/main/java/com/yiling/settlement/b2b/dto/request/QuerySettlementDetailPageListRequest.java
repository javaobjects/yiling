package com.yiling.settlement.b2b.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QuerySettlementDetailPageListRequest extends QueryPageListRequest {

	/**
	 * 结算单id
	 */
	@NotNull
	private Long  settlementId;


}
