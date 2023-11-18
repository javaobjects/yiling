package com.yiling.user.agreement.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/8/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUseListPageRequest extends QueryPageListRequest {

	/**
	 * 企业id为空时查所有
	 */
	private List<Long> eidList;

	/**
	 * 创建人id为空时查所有
	 */
	private List<Long> createUserIdList;

	/**
	 * 申请单编号
	 */
	private String applicantCode;

	/**
	 * 申请企业名称
	 */
	private String name;

	/**
	 * 申请企业easCode
	 */
	private String easCode;

//	/**
//	 * eid
//	 */
//	private Long eid;

}
