package com.yiling.user.system.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建角色 Request
 *
 * @author: lun.yu
 * @date: 2021/7/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleMenuRequest extends BaseRequest {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID集合
     */
    private List<Long> menuIdList;

}
