package com.yiling.user.agreement.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExportRebateOrderPageListRequest extends QueryPageListRequest {

	/**
	 * 协议id
	 */
	@NotNull
	private Long   agreementId;

	/**
	 * 是否满足条件
	 */
	@NotNull
	private AgreementRebateOrderConditionStatusEnum conditionStatusEnum;
}
