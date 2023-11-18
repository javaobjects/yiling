package com.yiling.user.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.system.dao.RoleMapper;
import com.yiling.user.system.dao.RoleMenuMapper;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.entity.RoleMenuDO;
import com.yiling.user.system.service.RoleMenuService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 角色菜单关联表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenuMapper, RoleMenuDO> implements RoleMenuService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public List<RoleMenuDO> listByRoleIds(List<Long> roleIds) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(RoleMenuDO::getRoleId, roleIds);

        List<RoleMenuDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<Long> listMenuIdsByRoleIds(List<Long> roleIds) {
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(RoleMenuDO::getRoleId, roleIds);

        List<RoleMenuDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list.stream().map(RoleMenuDO::getMenuId).distinct().collect(Collectors.toList());
    }

    @Override
    public boolean bindRoleMenus(Long roleId, List<Long> menuIds, Long opUserId) {
        if (CollUtil.isEmpty(menuIds)) {
            return true;
        }

        List<RoleMenuDO> list = CollUtil.newArrayList();
        menuIds.forEach(e -> {
            RoleMenuDO entity = new RoleMenuDO();
            entity.setRoleId(roleId);
            entity.setMenuId(e);
            entity.setOpUserId(Objects.isNull(opUserId) ? 0L : opUserId);
            list.add(entity);
        });

        int n = this.baseMapper.bindRoleMenus(list);
        return n > 0;
    }

    @Override
    public boolean unbindRoleMenus(Long roleId, List<Long> menuIds, Long opUserId) {
        if (CollUtil.isEmpty(menuIds)) {
            return true;
        }

        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RoleMenuDO::getRoleId, roleId)
                .in(RoleMenuDO::getMenuId, menuIds);

        RoleMenuDO entity = new RoleMenuDO();
        entity.setOpUserId(opUserId);

        return this.batchDeleteWithFill(entity, queryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleMenus(Long roleId, List<Long> menuIds, Long opUserId) {
        List<Long> roleMenuIds = this.listMenuIdsByRoleIds(ListUtil.toList(roleId));
        if (CollUtil.isEmpty(menuIds)) {
            if (CollUtil.isEmpty(roleMenuIds)) {
                return true;
            }

            return this.unbindRoleMenus(roleId, roleMenuIds, opUserId);
        }

        if (CollUtil.isEmpty(roleMenuIds)) {
            return this.bindRoleMenus(roleId, menuIds, opUserId);
        }

        // 移除
        List<Long> removeMenuIds = roleMenuIds.stream().filter(e -> !menuIds.contains(e)).distinct().collect(Collectors.toList());
        this.unbindRoleMenus(roleId, removeMenuIds, opUserId);

        // 新增
        List<Long> addMenuIds = menuIds.stream().filter(e -> !roleMenuIds.contains(e)).distinct().collect(Collectors.toList());
        this.bindRoleMenus(roleId, addMenuIds, opUserId);

        //更新对应的角色表数据更新时间字段，方便查看角色管理该条数据修改记录
        RoleDO roleDO = new RoleDO();
        roleDO.setId(roleId);
        roleDO.setOpUserId(opUserId);
        roleDO.setOpTime(new Date());
        roleMapper.updateById(roleDO);

        return true;
    }
}
