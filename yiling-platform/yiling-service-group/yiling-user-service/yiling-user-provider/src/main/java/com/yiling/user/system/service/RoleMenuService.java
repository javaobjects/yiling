package com.yiling.user.system.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.entity.RoleMenuDO;

/**
 * <p>
 * 角色菜单关联表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
public interface RoleMenuService extends BaseService<RoleMenuDO> {

    /**
     * 获取角色列表对应的角色菜单绑定信息列表
     *
     * @param roleIds 角色ID列表
     * @return
     */
    List<RoleMenuDO> listByRoleIds(List<Long> roleIds);

    /**
     * 获取角色绑定的菜单ID列表
     *
     * @param roleIds 角色ID列表
     * @return java.util.List<java.lang.Long>
     * @author xuan.zhou
     * @date 2022/12/8
     **/
    List<Long> listMenuIdsByRoleIds(List<Long> roleIds);

    /**
     * 绑定角色菜单
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID集合
     * @param opUserId 操作人ID
     * @return
     */
    boolean bindRoleMenus(Long roleId, List<Long> menuIds, Long opUserId);

    /**
     * 解除绑定角色菜单
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID集合
     * @param opUserId 操作人ID
     * @return
     */
    boolean unbindRoleMenus(Long roleId, List<Long> menuIds, Long opUserId);

    /**
     * 更新角色菜单
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID集合
     * @param opUserId 操作人ID
     * @return
     */
    boolean updateRoleMenus(Long roleId, List<Long> menuIds, Long opUserId);
}
