package com.yiling.user.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.entity.EnterpriseDepartmentDO;
import com.yiling.user.enterprise.service.EnterpriseDepartmentService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeDepartmentService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.system.dao.RoleDataScopeMapper;
import com.yiling.user.system.entity.RoleDataScopeDO;
import com.yiling.user.system.enums.DataScopeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.service.RoleDataScopeService;
import com.yiling.user.system.service.UserRoleService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 系统角色对应的数据权限表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-01
 */
@Service
public class RoleDataScopeServiceImpl extends BaseServiceImpl<RoleDataScopeMapper, RoleDataScopeDO> implements RoleDataScopeService {

    @Autowired
    private EnterpriseDepartmentService enterpriseDepartmentService;
    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    private EnterpriseEmployeeDepartmentService enterpriseEmployeeDepartmentService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public boolean saveRoleDataScope(PermissionAppEnum appEnum, Long eid, Long roleId, DataScopeEnum dataScopeEnum, Long opUserId) {
        QueryWrapper<RoleDataScopeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RoleDataScopeDO::getRoleId, roleId)
                .eq(RoleDataScopeDO::getAppId, appEnum.getCode())
                .last("limit 1");

        RoleDataScopeDO entity = this.getOne(queryWrapper);
        if (entity != null) {
            entity.setDataScope(dataScopeEnum.getCode());
            entity.setOpUserId(opUserId);
            this.updateById(entity);
        } else {
            entity = new RoleDataScopeDO();
            entity.setAppId(appEnum.getCode());
            entity.setEid(eid);
            entity.setRoleId(roleId);
            entity.setDataScope(dataScopeEnum.getCode());
            entity.setOpUserId(opUserId);
            this.save(entity);
        }

        return true ;
    }

    @Override
    public List<RoleDataScopeDO> listByRoleId(Long roleId) {
        QueryWrapper<RoleDataScopeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleDataScopeDO::getRoleId, roleId);

        List<RoleDataScopeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    @Override
    public List<Long> listAuthorizedUserIds(PermissionAppEnum appEnum, Long eid, Long userId) {
        DataScopeEnum dataScopeEnum = this.getUserDataScope(appEnum, eid, userId);

        // 未定义
        if (dataScopeEnum == DataScopeEnum.UNDEFINED) {
            return ListUtil.toList(0L);
        }

        // 本人
        if (dataScopeEnum == DataScopeEnum.MINE) {
            return ListUtil.toList(userId);
        }

        // 本部门
        if (dataScopeEnum == DataScopeEnum.DEPARTMENT) {
            // 获取用户当前所在部门ID列表
            List<Long> departmentIds = enterpriseEmployeeDepartmentService.listByUser(eid, userId);
            if (CollUtil.isEmpty(departmentIds)) {
                return ListUtil.toList(userId);
            }

            // 获取用户所在部门的所有用户ID列表
            List<Long> userIds = enterpriseEmployeeDepartmentService.listUserIdsByDepartmentIds(departmentIds);
            return userIds;
        }

        // 本部门及下级部门
        if (dataScopeEnum == DataScopeEnum.DEPARTMENT_LIST) {
            List<Long> allDepartmentIds = CollUtil.newArrayList();

            // 获取用户当前所在部门ID列表
            List<Long> departmentIds = enterpriseEmployeeDepartmentService.listByUser(eid, userId);
            if (CollUtil.isEmpty(departmentIds)) {
                return ListUtil.toList(userId);
            }

            // 添加所在部门
            allDepartmentIds.addAll(departmentIds);
            // 添加所在部门的下级部门
            departmentIds.forEach(e-> {
                List<EnterpriseDepartmentDO> departmentDOList = enterpriseDepartmentService.sublistById(e);
                allDepartmentIds.addAll(departmentDOList.stream().map(EnterpriseDepartmentDO::getId).distinct().collect(Collectors.toList()));
            });

            // 获取用户所在部门及下级部门的所有用户ID列表
            List<Long> userIds = enterpriseEmployeeDepartmentService.listUserIdsByDepartmentIds(allDepartmentIds);
            return userIds;
        }

        // 全部数据
        return ListUtil.empty();
    }

    public DataScopeEnum getUserDataScope(PermissionAppEnum appEnum, Long eid, Long userId) {
        boolean isAdmin = enterpriseEmployeeService.isAdmin(eid, userId);
        if (isAdmin) {
            return DataScopeEnum.ALL;
        }

        List<Long> userRoleIds = userRoleService.listRoleIdsByUserId(PermissionAppEnum.MALL_ADMIN, eid, userId);
        if (CollUtil.isEmpty(userRoleIds)) {
            return DataScopeEnum.UNDEFINED;
        }

        QueryWrapper<RoleDataScopeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(RoleDataScopeDO::getAppId, appEnum.getCode())
                .eq(RoleDataScopeDO::getEid, eid)
                .eq(RoleDataScopeDO::getRoleId, userRoleIds.get(0))
                .last("limit 1");

        RoleDataScopeDO roleDataScopeDO = this.getOne(queryWrapper);
        if (roleDataScopeDO == null) {
            return DataScopeEnum.UNDEFINED;
        }

        return DataScopeEnum.getByCode(roleDataScopeDO.getDataScope());
    }
}
