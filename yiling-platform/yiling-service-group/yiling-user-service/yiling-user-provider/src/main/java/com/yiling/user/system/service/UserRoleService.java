package com.yiling.user.system.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.dto.request.MoveRoleUsersRequest;
import com.yiling.user.system.entity.UserRoleDO;
import com.yiling.user.system.enums.PermissionAppEnum;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
public interface UserRoleService extends BaseService<UserRoleDO> {

	/**
	 * 根据角色id查询角色下的所有员工
	 * @param roleId
	 * @return
	 */
	List<UserRoleDO> listByRoleId(Long roleId);

	/**
	 * 转移角色人员
     *
	 * @param request
	 * @return
	 */
	boolean moveRoleUsers(MoveRoleUsersRequest request);

    /**
     * 绑定用户角色
     *
     * @param appEnum 应用类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @param opUserId 操作人ID
     * @return
     */
	boolean bindUserRoles(PermissionAppEnum appEnum, Long eid, Long userId, List<Long> roleIds, Long opUserId);

    /**
     * 解除绑定用户角色
     *
     * @param appEnum 应用类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @param opUserId 操作人ID
     * @return
     */
	boolean unbindUserRoles(PermissionAppEnum appEnum, Long eid, Long userId, List<Long> roleIds, Long opUserId);

    /**
     * 更新角色菜单
     *
     * @param appEnum 应用类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @param opUserId 操作人ID
     * @return
     */
    boolean updateUserRoles(PermissionAppEnum appEnum, Long eid, Long userId, List<Long> roleIds, Long opUserId);

    /**
     * 获取用户在某个应用绑定的角色ID列表
     *
     * @param appEnum 应用类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @return java.util.List<java.lang.Long>
     * @author xuan.zhou
     * @date 2022/12/13
     **/
    List<Long> listRoleIdsByUserId(PermissionAppEnum appEnum, Long eid, Long userId);

    /**
     * 根据企业ID和用户ID获取角色信息列表
     *
     * @param eid 企业ID
     * @param userId 管理员用户ID
     * @return java.util.List<com.yiling.user.system.entity.UserRoleDO>
     * @author xuan.zhou
     * @date 2022/3/8
     **/
    List<UserRoleDO> listByEidUserId(Long eid, Long userId);
}
