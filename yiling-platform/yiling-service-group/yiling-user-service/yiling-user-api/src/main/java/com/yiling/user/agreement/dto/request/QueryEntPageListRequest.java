package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEntPageListRequest extends QueryPageListRequest {

	private static final long serialVersionUID = -3943375351689651449L;
	/**
	 * 查询名称
	 */
	private String queryStr;
}
