package com.yiling.user.system.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转移角色人员 Request
 * 
 * @author xuan.zhou
 * @date 2021/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MoveRoleUsersRequest extends BaseRequest {

	/**
	 * 角色ID
	 */
	@NotNull
	private Long id;

	/**
	 * 目标角色ID
	 */
    @NotNull
	private Long newId;
}
