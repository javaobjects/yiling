package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryThirdAgreementEntListRequest extends QueryPageListRequest {

	/**
	 * 查询数据的企业id
	 */
	private  Long eid;

}
