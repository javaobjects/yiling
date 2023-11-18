package com.yiling.user.agreement.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryApplyOrderPageListRequest extends QueryPageListRequest {

	/**
	 * 申请单id
	 */
	@NotNull
	private Long applyId;

	/**
	 * 协议id
	 */
	private Long agreementId;
}
