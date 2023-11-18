package com.yiling.sales.assistant.commissions.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCommissionsDetailPageListRequest extends QueryPageListRequest {

	/**
	 * 佣金记录id
	 */
	@NotNull
	private Long commissionsId;
}
