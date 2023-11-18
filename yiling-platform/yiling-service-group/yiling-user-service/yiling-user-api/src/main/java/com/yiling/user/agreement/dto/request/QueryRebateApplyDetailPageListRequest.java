package com.yiling.user.agreement.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryRebateApplyDetailPageListRequest extends QueryPageListRequest {

	/**
	 * 返利申请表id
	 */
	@NotNull
	private Long applyId;

}
