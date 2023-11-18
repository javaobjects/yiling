package com.yiling.user.agreement.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.agreement.enums.AgreementCashTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementRebateLogPageListRequest extends QueryPageListRequest {

	/**
	 * 账号列表
	 */
	private List<String> accounts;

	/**
	 * 兑付方式
	 */
	private AgreementCashTypeEnum cashTypeEnum;
}
