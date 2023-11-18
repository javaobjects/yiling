package com.yiling.user.system.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除菜单 Request
 *
 * @author lun.yu
 * @date 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveMenuRequest extends BaseRequest {

	/**
	 * 菜单ID集合
	 */
	private List<Long> menuIdList;

}
