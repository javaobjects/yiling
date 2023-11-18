package com.yiling.user.system.api.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.dto.RoleDataScopeDTO;
import com.yiling.user.system.dto.RoleListItemDTO;
import com.yiling.user.system.dto.request.CreateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.CreateRoleRequest;
import com.yiling.user.system.dto.request.MoveRoleUsersRequest;
import com.yiling.user.system.dto.request.QueryRolePageListRequest;
import com.yiling.user.system.dto.request.RemoveRoleRequest;
import com.yiling.user.system.dto.request.RoleMenuRequest;
import com.yiling.user.system.dto.request.UpdateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.UpdateRoleInfoRequest;
import com.yiling.user.system.dto.request.UpdateRoleRequest;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.service.RoleDataScopeService;
import com.yiling.user.system.service.RoleMenuService;
import com.yiling.user.system.service.RoleService;
import com.yiling.user.system.service.UserRoleService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-06-01
 */
@Slf4j
@DubboService
public class RoleApiImpl implements RoleApi {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleDataScopeService roleDataScopeService;

    @Override
    public Page<RoleListItemDTO> queryRolePageList(QueryRolePageListRequest request) {
        return PojoUtils.map(roleService.queryRolePageList(request), RoleListItemDTO.class);
    }

    @Override
    public Page<RoleListItemDTO> queryRoleManagePageList(QueryRolePageListRequest request) {
        return PojoUtils.map(roleService.queryRoleManagePageList(request), RoleListItemDTO.class);
    }

    @Override
    public RoleDTO getById(Long id) {
        QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleDO::getId, id);

        RoleDO roleDO = roleService.getOne(queryWrapper);
        return PojoUtils.map(roleDO, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> listByIds(List<Long> ids) {
        List<RoleDO> roleDO = roleService.listByIds(ids);
        return PojoUtils.map(roleDO, RoleDTO.class);
    }

    @Override
    public boolean createMallCustomRole(CreateMallCustomRoleRequest request) {
        return roleService.createMallCustomRole(request);
    }

    @Override
    public boolean updateMallCustomRole(UpdateMallCustomRoleRequest request) {
        return roleService.updateMallCustomRole(request);
    }

    @Override
    public boolean create(CreateRoleRequest request) {
        return roleService.create(request);
    }

    @Override
    public boolean update(UpdateRoleRequest request) {
        return roleService.update(request);
    }

    @Override
    public boolean updateRole(UpdateRoleInfoRequest request) {
        return roleService.updateRole(request);
    }

    @Override
    public boolean updateStatus(Long id, EnableStatusEnum statusEnum, Long opUserId) {
        return roleService.updateStatus(id, statusEnum, opUserId);
    }

    @Override
    public Map<Long, List<RoleDTO>> listByUserIdEids(PermissionAppEnum appEnum, Long userId, List<Long> eids) {
        Map<Long, List<RoleDO>> map = roleService.listByUserIdEids(appEnum, userId, eids);
        return PojoUtils.map(map, RoleDTO.class);
    }

    @Override
    public Map<Long, List<RoleDTO>> listByEidUserIds(PermissionAppEnum appEnum, Long eid, List<Long> userIds) {
        Map<Long, List<RoleDO>> map = roleService.listByEidUserIds(appEnum, eid, userIds);
        return PojoUtils.map(map, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> listByEidUserId(PermissionAppEnum appEnum, Long eid, Long userId) {
        return PojoUtils.map(roleService.listByEidUserId(appEnum, eid, userId), RoleDTO.class);
    }

    @Override
    public boolean moveRoleUsers(MoveRoleUsersRequest request) {
        return userRoleService.moveRoleUsers(request);
    }

    @Override
    public boolean createRoleMenu(RoleMenuRequest request) {
        return roleMenuService.bindRoleMenus(request.getRoleId(),request.getMenuIdList(),request.getOpUserId());
    }

    @Override
    public boolean updateRoleMenu(RoleMenuRequest request) {
        return roleMenuService.updateRoleMenus(request.getRoleId(),request.getMenuIdList(),request.getOpUserId());
    }

    @Override
    public List<RoleDTO> allRole() {
        LambdaQueryWrapper<RoleDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleDO::getAppId, PermissionAppEnum.ADMIN.getCode());
        lambdaQueryWrapper.eq(RoleDO::getStatus,EnableStatusEnum.ENABLED.getCode());

        return PojoUtils.map(roleService.list(lambdaQueryWrapper),RoleDTO.class);
    }

    @Override
    public boolean batchDelete(RemoveRoleRequest request) {
        return roleService.batchDelete(request);
    }

    @Override
    public Map<Integer, RoleDataScopeDTO> getRoleDataScopeMap(Long roleId) {
        List<RoleDataScopeDTO> list = PojoUtils.map(roleDataScopeService.listByRoleId(roleId), RoleDataScopeDTO.class);
        return list.stream().collect(Collectors.toMap(RoleDataScopeDTO::getAppId, Function.identity()));
    }
}
