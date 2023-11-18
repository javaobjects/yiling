package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询协议列表参数
 * @author dexi.yao
 * @date 2021-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryThirdAgreementPageListRequest extends QueryPageListRequest {

	private static final long serialVersionUID = -3138106174115412915L;

	/**
	 * 当前查询者eid
	 */
	private Long   queryEid;

	/**
	 * 协议参与方eid
	 */
	private Long  eid;

	/**
	 * 协议状态：1-进行中 2-未开始 3-已停用 4-已过期
	 */
	private Integer   agreementStatus;
}
