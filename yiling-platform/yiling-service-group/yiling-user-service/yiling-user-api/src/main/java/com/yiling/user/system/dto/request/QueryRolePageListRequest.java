package com.yiling.user.system.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.system.enums.PermissionAppEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询角色列表参数
 * @author dexi.yao
 * @date 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRolePageListRequest extends QueryPageListRequest {

	private static final long serialVersionUID = -7945928683271925264L;

	/**
	 * 企业ID
	 */
	@NotNull
	private Long eid;

	/**
	 * 应用类型枚举
	 */
	@NotNull
	private PermissionAppEnum appEnum;

	/**
	 * 角色名
	 */
	private String name;

	/**
	 * 状态：0-全部 1-启用 2-停用
	 */
	private Integer status;

	/**
	 * 查询创建开始时间
	 */
	private Date createTimeStart;

	/**
	 * 查询创建结束时间
	 */
	private Date createTimeEnd;
}
