package com.yiling.user.system.api;

import java.util.List;

import com.yiling.user.system.enums.PermissionAppEnum;

/**
 * 数据权限 API
 *
 * @author: xuan.zhou
 * @date: 2021/11/10
 */
public interface DataPermissionsApi {

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
