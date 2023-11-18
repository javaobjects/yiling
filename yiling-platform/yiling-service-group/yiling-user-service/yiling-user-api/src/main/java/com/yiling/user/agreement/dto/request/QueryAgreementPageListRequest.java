package com.yiling.user.agreement.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementPageListRequest extends QueryPageListRequest {

	/**
	 * 当前查询的企业Id
	 */
	private Long   queryEid;

	/**
	 * 年度协议Id集合,查询年度协议时传空
	 */
	private List<Long> parentAgreementIds;

	/**
	 * 协议状态：1-进行中 2-未开始 3-已停用 4-已过期 5-进行中&未开始
	 */
	private Integer   agreementStatus;

	/**
	 * 协议id
	 */
	private Long id;

	/**
	 * 协议主体id
	 */
	private Long eid;

	/**
	 * 履约开始时间
	 */
	private Date startTime;

	/**
	 * 履约结束时间
	 */
	private Date endTime;
}
