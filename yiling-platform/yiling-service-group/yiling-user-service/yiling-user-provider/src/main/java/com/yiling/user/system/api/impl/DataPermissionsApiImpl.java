package com.yiling.user.system.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.service.RoleDataScopeService;

/**
 * 数据权限 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/11/10
 */
@DubboService
public class DataPermissionsApiImpl implements DataPermissionsApi {

    @Autowired
    private RoleDataScopeService roleDataScopeService;


    @Override
    public List<Long> listAuthorizedUserIds(PermissionAppEnum appEnum, Long eid, Long userId) {
        return roleDataScopeService.listAuthorizedUserIds(appEnum, eid, userId);
    }
}
