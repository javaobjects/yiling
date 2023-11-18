package com.yiling.user.system.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.entity.RoleDataScopeDO;
import com.yiling.user.system.enums.DataScopeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;

/**
 * <p>
 * 系统角色对应的数据权限表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-01
 */
public interface RoleDataScopeService extends BaseService<RoleDataScopeDO> {

    /**
     * 修改角色对应某个子系统的数据权限
     *
     * @param appEnum 系统枚举
     * @param eid 企业ID
     * @param roleId 角色ID
     * @param dataScopeEnum 数据权限
     * @param opUserId 操作人ID
     * @return
     */
    boolean saveRoleDataScope(PermissionAppEnum appEnum, Long eid, Long roleId, DataScopeEnum dataScopeEnum, Long opUserId);

    /**
     * 获取角色对应的数据权限列表
     *
     * @param roleId 角色ID
     * @return
     */
    List<RoleDataScopeDO> listByRoleId(Long roleId);

    /**
     * 获取用户数据权限对应的用户ID列表
     *
     * @param appEnum 系统枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @return 用户ID列表。如果返回集合为空，则表示所有数据权限。
     */
    List<Long> listAuthorizedUserIds(PermissionAppEnum appEnum, Long eid, Long userId);
}
