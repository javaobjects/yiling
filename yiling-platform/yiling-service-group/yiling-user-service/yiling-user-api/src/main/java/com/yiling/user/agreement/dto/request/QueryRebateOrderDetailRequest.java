package com.yiling.user.agreement.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRebateOrderDetailRequest extends BaseRequest {

	/**
	 * 采购方eid
	 */
	private Long       secondEid;

	/**
	 * 协议id
	 */
	private Long       agreementId;

	/**
	 * easCode
	 */
	private String     easCode;

	/**
	 * 兑付状态1计算状态2已经兑付
	 */
	private Integer cashStatus;

	/**
	 * 兑付状态1计算状态2已经兑付
	 */
	private Integer conditionStatus;

	/**
	 * 订单idList
	 */
	private List<Long> orderIdList;

	/**
	 * 时间范围
	 */
	private List<String> comparisonTimes;
}
