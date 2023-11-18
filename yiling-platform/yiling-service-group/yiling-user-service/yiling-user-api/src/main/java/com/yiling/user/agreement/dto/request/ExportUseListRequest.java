package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExportUseListRequest extends BaseRequest {

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

	/**
	 * eid
	 */
	private Long enterpriseId;

	/**
	 * 当前用户Id
	 */
	private Long currentUserId;
	//由于b2b_v1.0新增了数据权限此参数作废
//    @ApiModelProperty("查询类型（默认商务） 1-商务 2-财务")
//    private Integer queryType;

}
