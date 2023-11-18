package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTempAgreementListRequest extends BaseRequest {


	private static final long serialVersionUID = 1406721544438267341L;
	/**
	 * 主协议id
	 */
	private Long parentId;

	/**
	 * 状态：0-全部 1-进行中 2-未开始 3-已停用 4-已过期 5-进行中&未开始
	 */
	private Integer agreementStatus;
}
