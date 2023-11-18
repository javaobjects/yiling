package com.yiling.user.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.RoleListItemBO;
import com.yiling.user.system.dao.RoleMapper;
import com.yiling.user.system.dto.request.CreateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.CreateRoleRequest;
import com.yiling.user.system.dto.request.QueryRolePageListRequest;
import com.yiling.user.system.dto.request.RemoveRoleRequest;
import com.yiling.user.system.dto.request.UpdateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.UpdateRoleInfoRequest;
import com.yiling.user.system.dto.request.UpdateRoleRequest;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.entity.RoleDataScopeDO;
import com.yiling.user.system.entity.RoleMenuDO;
import com.yiling.user.system.entity.UserRoleDO;
import com.yiling.user.system.enums.DataScopeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.enums.RoleTypeEnum;
import com.yiling.user.system.service.AdminService;
import com.yiling.user.system.service.MenuService;
import com.yiling.user.system.service.RoleDataScopeService;
import com.yiling.user.system.service.RoleMenuService;
import com.yiling.user.system.service.RoleService;
import com.yiling.user.system.service.UserRoleService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
@Service
@Slf4j
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, RoleDO> implements RoleService {

	@Autowired
	private RoleMenuService roleMenuService;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
    private MenuService menuService;
	@Autowired
    private AdminService adminService;
    @Autowired
    private RoleDataScopeService roleDataScopeService;

    @Override
    public RoleDO getByCode(String code) {
        QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RoleDO::getCode, code)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<RoleDO> getByCodeList(List<String> codeList) {
        if (CollUtil.isEmpty(codeList)) {
            return ListUtil.empty();
        }

        QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(RoleDO::getCode, codeList);

        List<RoleDO> list = this.list(queryWrapper);
        return CollUtil.isNotEmpty(list) ? list : ListUtil.empty();
    }

    @Override
	public Page<RoleListItemBO> queryRolePageList(QueryRolePageListRequest request) {
		return this.baseMapper.queryRolePageList(new Page<>(request.getCurrent(), request.getSize()), request);
	}

	@Override
    public Page<RoleListItemBO> queryRoleManagePageList(QueryRolePageListRequest request) {
        return this.baseMapper.queryRoleManagePageList(new Page<>(request.getCurrent(), request.getSize()), request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createMallCustomRole(CreateMallCustomRoleRequest request) {
        // 检查角色名称是否重复
        this.checkMallCustomRoleName(request.getEid(), request.getName(), null);

        RoleDO entity = new RoleDO();
        entity.setAppId(PermissionAppEnum.MALL_ADMIN.getCode());
        entity.setEid(request.getEid());
        entity.setName(request.getName());
        entity.setType(RoleTypeEnum.CUSTOM.getCode());
        entity.setStatus(request.getStatus());
        entity.setRemark(request.getRemark());
        entity.setOpUserId(request.getOpUserId());
        this.save(entity);

        // 保存数据权限
        List<CreateMallCustomRoleRequest.RolePermissionRequest> rolePermissionList =  request.getRolePermissionList();
        if (CollUtil.isNotEmpty(rolePermissionList)) {
            for (CreateMallCustomRoleRequest.RolePermissionRequest rolePermissionRequest : rolePermissionList) {
                // 保存操作权限
                roleMenuService.bindRoleMenus(entity.getId(), rolePermissionRequest.getMenuIds(), request.getOpUserId());

                // 保存数据权限
                Integer dataScope = rolePermissionRequest.getDataScope();
                if (dataScope != null && dataScope != 0) {
                    RoleDataScopeDO roleDataScopeDO = new RoleDataScopeDO();
                    roleDataScopeDO.setAppId(rolePermissionRequest.getAppId());
                    roleDataScopeDO.setEid(request.getEid());
                    roleDataScopeDO.setRoleId(entity.getId());
                    roleDataScopeDO.setDataScope(dataScope);
                    roleDataScopeDO.setOpUserId(request.getOpUserId());
                    roleDataScopeService.save(roleDataScopeDO);
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMallCustomRole(UpdateMallCustomRoleRequest request) {
        RoleDO entity = this.getById(request.getId());

        // 检查角色名称是否重复
        this.checkMallCustomRoleName(entity.getEid(), request.getName(), request.getId());

        entity.setName(request.getName());
        entity.setRemark(request.getRemark());
        entity.setStatus(request.getStatus());
        entity.setOpUserId(request.getOpUserId());
        this.updateById(entity);

        // 保存数据权限
        List<UpdateMallCustomRoleRequest.RolePermissionRequest> rolePermissionList =  request.getRolePermissionList();
        if (CollUtil.isNotEmpty(rolePermissionList)) {
            List<Long> menuIds = CollUtil.newArrayList();
            for (UpdateMallCustomRoleRequest.RolePermissionRequest rolePermissionRequest : rolePermissionList) {
                menuIds.addAll(rolePermissionRequest.getMenuIds());

                // 保存数据权限
                Integer dataScope = rolePermissionRequest.getDataScope();
                if (dataScope != null && dataScope != 0) {
                    roleDataScopeService.saveRoleDataScope(PermissionAppEnum.getByCode(rolePermissionRequest.getAppId()), entity.getEid(),
                        request.getId(), DataScopeEnum.getByCode(rolePermissionRequest.getDataScope()), request.getOpUserId());
                }
            }

            // 保存操作权限
            roleMenuService.updateRoleMenus(request.getId(), menuIds, request.getOpUserId());
        }

        return true;
    }

    /**
     * 检查商城自定义角色名称是否重复
     *
     * @param eid 所属企业ID
     * @param name 角色名称
     * @param id 角色ID
     */
    private void checkMallCustomRoleName(Long eid, String name, Long id) {
        LambdaQueryWrapper<RoleDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(RoleDO::getEid, eid)
                .eq(RoleDO::getName, name)
                .last("limit 1");
        RoleDO roleDO = this.getOne(queryWrapper);

        if (id == null) {
            if (roleDO != null) {
                throw new BusinessException(UserErrorCode.ROLE_EXIST);
            }
        } else {
            if (roleDO != null && !roleDO.getId().equals(id)) {
                throw new BusinessException(UserErrorCode.ROLE_EXIST);
            }
        }
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
	public boolean create(CreateRoleRequest request) {
	    RoleDO entity = PojoUtils.map(request, RoleDO.class);

	    LambdaQueryWrapper<RoleDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
	    lambdaQueryWrapper.eq(RoleDO::getName,request.getName());
	    lambdaQueryWrapper.eq(RoleDO::getAppId,request.getAppId());
	    if(Objects.nonNull(request.getEid()) && request.getEid() != 0){
            lambdaQueryWrapper.eq(RoleDO::getEid,request.getEid());
        }

        int count = this.count(lambdaQueryWrapper);
        if(count > 0){
            throw new BusinessException(UserErrorCode.ROLE_EXIST);
        }
        this.save(entity);

	    return roleMenuService.bindRoleMenus(entity.getId(), request.getMenuIds(), request.getOpUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UpdateRoleRequest request) {
        RoleDO entity = PojoUtils.map(request, RoleDO.class);
        checkName(request.getId(),request.getName());
        this.updateById(entity);

        return roleMenuService.updateRoleMenus(request.getId(), request.getMenuIds(), request.getOpUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(UpdateRoleInfoRequest request) {
        RoleDO entity = PojoUtils.map(request, RoleDO.class);
        checkName(request.getId(),request.getName());

        //系统角色只有admin超级管理员账号可以操作
        if(RoleTypeEnum.SYSTEM.getCode().equals(request.getType())){
            Admin admin = adminService.getById(request.getOpUserId());
            if(Objects.nonNull(admin) && !admin.isAdmin()){
                throw new BusinessException(UserErrorCode.AUTH_ILLEGAL);
            }
        }

        return this.updateById(entity);
    }

    /**
     * 第一次查询为了得到所属应用，刚好顺便判断名称是否发生改变；
     * 第二次根据应用、名称判断是否存在
     * @param id 角色ID
     * @param name 角色名称
     */
    private void checkName(Long id , String name) {
        RoleDO roleDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.ROLE_NOT_FOUND));
        if (!StrUtil.equals(roleDO.getName(),name)) {
            LambdaQueryWrapper<RoleDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.notIn(RoleDO::getId,id);
            lambdaQueryWrapper.eq(RoleDO::getName,name);
            lambdaQueryWrapper.eq(RoleDO::getAppId,roleDO.getAppId());
            int count = this.count(lambdaQueryWrapper);
            if(count > 0){
                throw new BusinessException(UserErrorCode.ROLE_EXIST);
            }
        }
    }

    @Override
    public boolean updateStatus(Long id, EnableStatusEnum statusEnum, Long opUserId) {
	    if (statusEnum == EnableStatusEnum.DISABLED) {
            List<UserRoleDO> userRoleDOList = userRoleService.listByRoleId(id);
            if (CollUtil.isNotEmpty(userRoleDOList)) {
                throw new BusinessException(UserErrorCode.ROLE_DEACTIVATE_ILLEGAL);
            }
        }

	    RoleDO entity = new RoleDO();
	    entity.setId(id);
	    entity.setStatus(statusEnum.getCode());
	    entity.setOpUserId(opUserId);
        return this.updateById(entity);
    }

    @Override
	public Map<Long, List<RoleDO>> listByUserIdEids(PermissionAppEnum appEnum, Long userId, List<Long> eids) {
		QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
                .eq(UserRoleDO::getAppId, appEnum.getCode())
				.eq(UserRoleDO::getUserId, userId)
				.in(UserRoleDO::getEid, eids);

		List<UserRoleDO> userRoleDOList = userRoleService.list(queryWrapper);
		if (CollUtil.isEmpty(userRoleDOList)) {
			return MapUtil.empty();
		}

		List<Long> roleIds = userRoleDOList.stream().map(UserRoleDO::getRoleId).distinct().collect(Collectors.toList());
		List<RoleDO> roleDOList = this.listByIds(roleIds);
		if (CollUtil.isEmpty(roleDOList)) {
			return MapUtil.empty();
		}

        roleDOList = roleDOList.stream().filter(e -> e.getStatus().equals(EnableStatusEnum.ENABLED.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(roleDOList)) {
            return MapUtil.empty();
        }

		Map<Long, List<UserRoleDO>> userRoleDOMap = userRoleDOList.stream().collect(Collectors.groupingBy(UserRoleDO::getEid));

		Map<Long, List<RoleDO>> map = MapUtil.newHashMap();
		for (Long eid : userRoleDOMap.keySet()) {
			List<Long> userRoleIds = userRoleDOMap.get(eid).stream().map(UserRoleDO::getRoleId).distinct().collect(Collectors.toList());
			map.put(eid, roleDOList.stream().filter(e -> userRoleIds.contains(e.getId())).collect(Collectors.toList()));
		}

		return map;
	}

    @Override
    public Map<Long, List<RoleDO>> listByEidUserIds(PermissionAppEnum appEnum, Long eid, List<Long> userIds) {
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRoleDO::getAppId, appEnum.getCode())
                .eq(UserRoleDO::getEid, eid)
                .in(UserRoleDO::getUserId, userIds);

        List<UserRoleDO> userRoleDOList = userRoleService.list(queryWrapper);
        if (CollUtil.isEmpty(userRoleDOList)) {
            return MapUtil.empty();
        }

        // 按用户ID分组
        Map<Long, List<UserRoleDO>> userRoleDOMap = userRoleDOList.stream().collect(Collectors.groupingBy(UserRoleDO::getUserId));

        // 所有的角色ID集合
        List<Long> roleIds = userRoleDOList.stream().map(UserRoleDO::getRoleId).distinct().collect(Collectors.toList());
        List<RoleDO> roleDOList = this.listByIds(roleIds);
        if (CollUtil.isEmpty(roleDOList)) {
            return MapUtil.empty();
        }

        roleDOList = roleDOList.stream().filter(e -> e.getStatus().equals(EnableStatusEnum.ENABLED.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(roleDOList)) {
            return MapUtil.empty();
        }

        Map<Long, List<RoleDO>> userRoleMap = MapUtil.newHashMap();
        for (Long userId : userRoleDOMap.keySet()) {
            List<Long> userRoleIds = userRoleDOMap.get(userId).stream().map(UserRoleDO::getRoleId).distinct().collect(Collectors.toList());
            List<RoleDO> roleList = roleDOList.stream().filter(e -> userRoleIds.contains(e.getId())).collect(Collectors.toList());
            userRoleMap.put(userId, roleList);
        }

        return userRoleMap;
    }

    @Override
    public List<RoleDO> listByEidUserId(PermissionAppEnum appEnum, Long eid, Long userId) {
        return this.listByEidUserId(ListUtil.toList(appEnum), eid, userId);
    }

    @Override
    public List<RoleDO> listByEidUserId(List<PermissionAppEnum> appEnums, Long eid, Long userId) {
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(UserRoleDO::getAppId, appEnums.stream().map(PermissionAppEnum::getCode).collect(Collectors.toList()))
                .eq(UserRoleDO::getEid, eid)
                .eq(UserRoleDO::getUserId, userId);

        List<UserRoleDO> list = userRoleService.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        List<Long> roleIds = list.stream().map(UserRoleDO::getRoleId).distinct().collect(Collectors.toList());
        List<RoleDO> roleList = this.listByIds(roleIds);
        if (CollUtil.isEmpty(roleList)) {
            return ListUtil.empty();
        }

        roleList = roleList.stream().filter(e -> e.getStatus().equals(EnableStatusEnum.ENABLED.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(roleList)) {
            return ListUtil.empty();
        }
        return roleList;
    }

    @Override
    public List<RoleDO> listByEid(PermissionAppEnum appEnum, Long eid) {
        QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RoleDO::getAppId, appEnum.getCode())
                .eq(RoleDO::getEid, eid);

        List<RoleDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchDelete(RemoveRoleRequest request) {
        if(CollUtil.isEmpty(request.getRoleIdList())){
            return true;
        }

        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleDO::getId,request.getRoleIdList());

        //只能删除类型为自定义的角色
        List<RoleDO> roleDOList = this.list(queryWrapper);
        if(CollUtil.isNotEmpty(roleDOList)){
            for(RoleDO roleDO : roleDOList){
                if(!roleDO.getType().equals(RoleTypeEnum.CUSTOM.getCode())){
                    throw new BusinessException(UserErrorCode.ROLE_TYPE_ERROR);
                }
            }
        }

        RoleDO roleDO = new RoleDO();
        roleDO.setOpUserId(request.getOpUserId());
        int result = this.batchDeleteWithFill(roleDO,queryWrapper);

        if(result > 0){
            //删除角色所绑定的菜单
            LambdaQueryWrapper<RoleMenuDO> roleMenuWrapper = new LambdaQueryWrapper<>();
            roleMenuWrapper.in(RoleMenuDO::getRoleId,request.getRoleIdList());
            RoleMenuDO roleMenuDO = new RoleMenuDO();
            roleMenuDO.setOpUserId(request.getOpUserId());
            roleMenuService.batchDeleteWithFill(roleMenuDO,roleMenuWrapper);

            //删除角色所绑定的用户
            LambdaQueryWrapper<UserRoleDO> userRoleWrapper = new LambdaQueryWrapper<>();
            userRoleWrapper.in(UserRoleDO::getRoleId,request.getRoleIdList());
            UserRoleDO userRoleDo = new UserRoleDO();
            userRoleDo.setOpUserId(request.getOpUserId());
            userRoleService.batchDeleteWithFill(userRoleDo,userRoleWrapper);
        }

        return true;

    }

}
