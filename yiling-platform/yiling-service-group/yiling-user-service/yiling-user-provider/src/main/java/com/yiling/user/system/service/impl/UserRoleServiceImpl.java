package com.yiling.user.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.system.dao.UserRoleMapper;
import com.yiling.user.system.dto.request.MoveRoleUsersRequest;
import com.yiling.user.system.entity.UserRoleDO;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.service.AdminService;
import com.yiling.user.system.service.RoleService;
import com.yiling.user.system.service.UserRoleService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleService {

    @Autowired
    RoleService roleService;
    @Autowired
    AdminService adminService;
    @Autowired
    EnterpriseEmployeeService enterpriseEmployeeService;

	@Override
	public List<UserRoleDO> listByRoleId(Long roleId) {
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getRoleId, roleId);
        List<UserRoleDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
	}

	@Override
	public boolean moveRoleUsers(MoveRoleUsersRequest request) {
		//查看角色下是否存在用户
		List<UserRoleDO> list = this.listByRoleId(request.getId());
		if (CollUtil.isEmpty(list)){
			throw new BusinessException(UserErrorCode.ROLE_MOVE);
		}

		QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getRoleId, request.getId());

        UserRoleDO entity = new UserRoleDO();
        entity.setRoleId(request.getNewId());
        entity.setOpUserId(request.getOpUserId());

		return this.update(entity, queryWrapper);
	}

    @Override
    public boolean bindUserRoles(PermissionAppEnum appEnum, Long eid, Long userId, List<Long> roleIds, Long opUserId) {
        if (CollUtil.isEmpty(roleIds)) {
            return true;
        }

        List<UserRoleDO> list = CollUtil.newArrayList();
        roleIds.forEach(e -> {
            UserRoleDO entity = new UserRoleDO();
            entity.setAppId(appEnum.getCode());
            entity.setEid(eid);
            entity.setUserId(userId);
            entity.setRoleId(e);
            entity.setOpUserId(opUserId);
            list.add(entity);
        });
        return this.baseMapper.bindUserRoles(list) > 0;
    }

    @Override
    public boolean unbindUserRoles(PermissionAppEnum appEnum, Long eid, Long userId, List<Long> roleIds, Long opUserId) {
        if (CollUtil.isEmpty(roleIds)) {
            return true;
        }

        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRoleDO::getAppId, appEnum.getCode())
                .eq(UserRoleDO::getEid, eid)
                .eq(UserRoleDO::getUserId, userId)
                .in(UserRoleDO::getRoleId, roleIds);

        UserRoleDO entity = new UserRoleDO();
        entity.setOpUserId(opUserId);

        return this.batchDeleteWithFill(entity, queryWrapper) > 0;
    }

    @Override
    public boolean updateUserRoles(PermissionAppEnum appEnum, Long eid, Long userId, List<Long> roleIds, Long opUserId) {
        List<Long> userRoleIds = this.listRoleIdsByUserId(appEnum, eid, userId);
        if (CollUtil.isEmpty(roleIds)) {
            if (CollUtil.isEmpty(userRoleIds)) {
                return true;
            }

            return this.unbindUserRoles(appEnum, eid, userId, userRoleIds, opUserId);
        }

        if (CollUtil.isEmpty(userRoleIds)) {
            return this.bindUserRoles(appEnum, eid, userId, roleIds, opUserId);
        }

        // 移除
        List<Long> removeMenuIds = userRoleIds.stream().filter(e -> !roleIds.contains(e)).distinct().collect(Collectors.toList());
        this.unbindUserRoles(appEnum, eid, userId, removeMenuIds, opUserId);

        // 新增
        List<Long> addMenuIds = roleIds.stream().filter(e -> !userRoleIds.contains(e)).distinct().collect(Collectors.toList());
        this.bindUserRoles(appEnum, eid, userId, addMenuIds, opUserId);

        return true;
    }

    @Override
    public List<Long> listRoleIdsByUserId(PermissionAppEnum appEnum, Long eid, Long userId) {
        LambdaQueryWrapper<UserRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleDO::getAppId,appEnum.getCode());
        queryWrapper.eq(UserRoleDO::getEid, eid);
        queryWrapper.eq(UserRoleDO::getUserId, userId);
        List<UserRoleDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list.stream().map(UserRoleDO::getRoleId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<UserRoleDO> listByEidUserId(Long eid, Long userId) {
        LambdaQueryWrapper<UserRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleDO::getEid, eid);
        queryWrapper.eq(UserRoleDO::getUserId, userId);

        List<UserRoleDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }
}
