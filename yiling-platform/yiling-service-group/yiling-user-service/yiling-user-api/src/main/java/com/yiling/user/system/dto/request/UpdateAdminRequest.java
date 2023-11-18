package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 启用/停用 Request
 *
 * @author lun.yu
 * @date 2021/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateAdminRequest extends BaseRequest {

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 状态：1-启用 2-停用
	 */
	private Integer status;

}
