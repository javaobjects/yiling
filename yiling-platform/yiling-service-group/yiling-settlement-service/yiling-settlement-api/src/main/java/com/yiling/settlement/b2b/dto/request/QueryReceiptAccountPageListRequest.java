package com.yiling.settlement.b2b.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReceiptAccountPageListRequest extends QueryPageListRequest {

	/**
	 * eidList
	 */
	private List<Long> eidList;

	/**
	 * 账户状态 1-待审核 2-审核成功 3-审核失败
	 */
	private Integer status;

	/**
	 * 最小提交时间
	 */
	private Date minDate;

	/**
	 * 最大提交时间
	 */
	private Date maxDate;

}
