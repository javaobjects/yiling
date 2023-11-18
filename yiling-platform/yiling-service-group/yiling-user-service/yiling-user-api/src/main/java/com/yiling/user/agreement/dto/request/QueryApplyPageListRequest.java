package com.yiling.user.agreement.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryApplyPageListRequest extends QueryPageListRequest {


	/**
	 * 企业id
	 */
	private Long eid;

	/**
	 * 企业名称
	 */
	private String name;

	/**
	 * 企业编码
	 */
	private String easCode;

	/**
	 * 申请单号
	 */
	private String code;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 创建人为空时不限制---由于b2b_v1.0新增了数据权限此参数作废
	 *
	 */
	@Deprecated
	private Long createUser;

	/**
	 * 创建人为空时不限制
	 */
	private List<Long> createUserList;

	/**
	 * 所属年度
	 */
	private Integer year;

	/**
	 * 所属月度（1-12）、季度（1-4）、其余该值为 0
	 */
	private Integer month;

	/**
	 * 月度类型 1-月度 2-季度 3-上半年 4-下半年 5-全年
	 */
	private Integer rangeType;

	/**
	 * 申请时间-开始
	 */
	private Date startDate;

	/**
	 * 申请时间-结束
	 */
	private Date endDate;

	/**
	 * 顺序类型 1-正序 2-倒序
	 */
	private Integer sequence=2;
}
