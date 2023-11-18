package com.yiling.sales.assistant.commissions.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCommissionsUserPageListRequest extends QueryPageListRequest {

	/**
	 * 用户id
	 */
	private List<Long> userIdList;
}
