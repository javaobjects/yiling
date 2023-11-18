package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dao.EnterpriseEmployeeMapper;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.CreateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryEmployeePageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeStatusRequest;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDepartmentDO;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;
import com.yiling.user.enterprise.service.EnterpriseEmployeeDepartmentService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.request.CreateStaffRequest;
import com.yiling.user.system.dto.request.QueryMrPageListRequest;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.entity.StaffDO;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.service.RoleService;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserRoleService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业员工信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-26
 */
@Service
public class EnterpriseEmployeeServiceImpl extends BaseServiceImpl<EnterpriseEmployeeMapper, EnterpriseEmployeeDO> implements EnterpriseEmployeeService {

    @Value("${user.staff.defaultPwd}")
    private String defaultPwd;

    @Autowired
    private UserService userService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EnterpriseEmployeeDepartmentService enterpriseEmployeeDepartmentService;

    @Override
    public Page<EnterpriseEmployeeDTO> pageList(QueryEmployeePageListRequest request) {
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public Page<MrBO> mrPageList(QueryMrPageListRequest request) {
        return this.baseMapper.mrPageList(request.getPage(), request);
    }

    @Override
    public List<MrBO> mrListByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }

        List<EnterpriseEmployeeDO> list = this.listByIds(ids);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        list = list.stream().filter(e -> EmployeeTypeEnum.MR.getCode().equals(e.getType())).collect(Collectors.toList());
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        List<Long> userIds = list.stream().map(EnterpriseEmployeeDO::getUserId).collect(Collectors.toList());
        List<UserDO> userDOList = userService.listByIds(userIds);
        Map<Long, UserDO> userDOMap = userDOList.stream().collect(Collectors.toMap(UserDO::getId, Function.identity()));

        return list.stream().map(e -> this.buildMrBO(userDOMap, e)).collect(Collectors.toList());
    }

    private MrBO buildMrBO(Map<Long, UserDO> userDOMap, EnterpriseEmployeeDO enterpriseEmployeeDO) {
        UserDO userDO = userDOMap.get(enterpriseEmployeeDO.getUserId());
        if (userDO == null) {
            return null;
        }

        MrBO mrBO = new MrBO();
        mrBO.setUserId(userDO.getId());
        mrBO.setName(userDO.getName());
        mrBO.setMobile(userDO.getMobile());
        mrBO.setEid(enterpriseEmployeeDO.getEid());
        mrBO.setId(enterpriseEmployeeDO.getId());
        mrBO.setCode(enterpriseEmployeeDO.getCode());
        mrBO.setStatus(enterpriseEmployeeDO.getStatus());
        return mrBO;
    }

    @Override
    public MrBO getMrById(Long id) {
        List<MrBO> list = this.mrListByIds(ListUtil.toList(id));
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(CreateEmployeeRequest request) {
        CreateStaffRequest createStaffRequest = new CreateStaffRequest();
        createStaffRequest.setName(request.getName());
        createStaffRequest.setMobile(request.getMobile());
        createStaffRequest.setPassword(request.getPassword());
        createStaffRequest.setIgnoreExists(true);
        createStaffRequest.setOpUserId(request.getOpUserId());
        Long userId = staffService.create(createStaffRequest);

        EnterpriseEmployeeDO employeeDO = this.getByEidUserId(request.getEid(), userId);
        if (employeeDO != null) {
            throw new BusinessException(UserErrorCode.EMPLOYEE_EXISTS);
        }

        //校验员工工号唯一性
        EnterpriseEmployeeDO enterpriseEmployeeDO = this.getByCode(request.getEid(), request.getCode());
        if(enterpriseEmployeeDO != null){
            throw new BusinessException(UserErrorCode.EMPLOYEE_CODE_EXISTS);
        }

        // 创建员工信息
        employeeDO = PojoUtils.map(request, EnterpriseEmployeeDO.class);
        employeeDO.setUserId(userId);
        this.save(employeeDO);

        // 简历员工与部门的关系
        List<Long> departmentIds = request.getDepartmentIds();
        if (CollUtil.isNotEmpty(departmentIds)) {
            for (Long departmentId : departmentIds) {
                EnterpriseEmployeeDepartmentDO enterpriseEmployeeDepartmentDO = new EnterpriseEmployeeDepartmentDO();
                enterpriseEmployeeDepartmentDO.setEmployeeId(employeeDO.getId());
                enterpriseEmployeeDepartmentDO.setEid(request.getEid());
                enterpriseEmployeeDepartmentDO.setUserId(userId);
                enterpriseEmployeeDepartmentDO.setDepartmentId(departmentId);
                enterpriseEmployeeDepartmentDO.setOpUserId(request.getOpUserId());
                enterpriseEmployeeDepartmentService.save(enterpriseEmployeeDepartmentDO);
            }
        }

        // 建立用户与角色的关系
        List<Long> roleIds = request.getRoleIds();
        if (CollUtil.isNotEmpty(roleIds)) {
            roleIds.forEach(roleId -> {
                RoleDO roleDO = roleService.getById(roleId);
                userRoleService.bindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), request.getEid(), userId, ListUtil.toList(roleId), request.getOpUserId());
            });
        }

        return userId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UpdateEmployeeRequest request) {
        EnterpriseEmployeeDO enterpriseEmployeeDO = this.getByEidUserId(request.getEid(), request.getUserId());

        //校验员工工号唯一性：以岭员工才有工号，非以岭的员工可能没有工号
        if (StrUtil.isNotEmpty(request.getCode())) {
            EnterpriseEmployeeDO employeeDo = this.getByCode(request.getEid(), request.getCode());
            if (employeeDo != null && !employeeDo.getUserId().equals(request.getUserId())) {
                throw new BusinessException(UserErrorCode.EMPLOYEE_CODE_EXISTS);
            }
        }

        UserDO userDO = userService.getById(request.getUserId());
        if (StrUtil.isNotEmpty(request.getName()) && !request.getName().equals(userDO.getName())) {
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setAppId(UserAuthsAppIdEnum.MALL.getCode());
            updateUserRequest.setId(request.getUserId());
            updateUserRequest.setName(request.getName());
            userService.update(updateUserRequest);
        }

        String code = request.getCode();
        Long positionId = request.getPositionId();
        Long parentId = request.getParentId();
        boolean updateFlag = code != null || positionId != null || parentId != null;
        if (updateFlag) {
            EnterpriseEmployeeDO entity = this.getByEidUserId(request.getEid(), request.getUserId());
            entity.setCode(code);
            entity.setType(request.getType());
            entity.setPositionId(positionId);
            entity.setParentId(request.getParentId());
            entity.setOpUserId(request.getOpUserId());
            this.updateById(entity);
        }

        List<Long> departmentIds = request.getDepartmentIds();
        if (departmentIds != null) {
            enterpriseEmployeeDepartmentService.saveEmployeeDepartments(enterpriseEmployeeDO.getId(), departmentIds, request.getOpUserId());
        }

        Long roleId = request.getRoleId();
        if (roleId != null && roleId != 0L) {
            RoleDO newRole = roleService.getById(roleId);
            PermissionAppEnum appEnum = PermissionAppEnum.getByCode(newRole.getAppId());

            List<RoleDO> roleDOList = roleService.listByEidUserId(appEnum, request.getEid(), request.getUserId());
            if (CollUtil.isNotEmpty(roleDOList)) {
                RoleDO originalRole = roleDOList.get(0);

                if (!newRole.getId().equals(originalRole.getId())) {
                    userRoleService.updateUserRoles(appEnum, request.getEid(), request.getUserId(), ListUtil.toList(newRole.getId()), request.getOpUserId());
                }
            } else {
                userRoleService.bindUserRoles(appEnum, request.getEid(), request.getUserId(), ListUtil.toList(newRole.getId()), request.getOpUserId());
            }
        }

        return true;
    }

    @Override
    public boolean updateStatus(UpdateEmployeeStatusRequest request) {
        EnterpriseEmployeeDO enterpriseEmployeeDO = this.getByEidUserId(request.getEid(), request.getUserId());
        if (enterpriseEmployeeDO == null) {
            throw new BusinessException(UserErrorCode.EMPLOYEE_NOT_EXISTS);
        }

        EnterpriseEmployeeDO entity = new EnterpriseEmployeeDO();
        entity.setId(enterpriseEmployeeDO.getId());
        entity.setStatus(request.getStatus());
        entity.setOpUserId(request.getOpUserId());

        //账号信息页面展示更新维护人和时间，获取的是员工的信息，所以更新用户时更新员工的时间和人
        StaffDO staffDO = new StaffDO();
        staffDO.setOpUserId(request.getOpUserId());
        staffService.update(staffDO,new LambdaQueryWrapper<StaffDO>().eq(StaffDO::getUserId, request.getUserId()));

        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long eid, Long userId, Long opUserId) {
        EnterpriseEmployeeDO entity = this.getByEidUserId(eid, userId);
        entity.setOpUserId(opUserId);

        // 判断员工是否为企业管理员
        boolean isAdmin = this.isAdmin(eid, userId);
        if (isAdmin) {
            throw new BusinessException(UserErrorCode.EMPLOYEE_IS_ADMIN_CANNOT_REMOVE);
        }

        // 删除员工与部门的关系
        enterpriseEmployeeDepartmentService.saveEmployeeDepartments(entity.getId(), ListUtil.empty(), opUserId);

        // 删除员工与角色的关系
        List<Long> userRoleIds = userRoleService.listRoleIdsByUserId(PermissionAppEnum.MALL_ADMIN, eid, userId);
        if (CollUtil.isNotEmpty(userRoleIds)) {
            userRoleService.unbindUserRoles(PermissionAppEnum.MALL_ADMIN, eid, userId, userRoleIds, opUserId);
        }

        return this.deleteByIdWithFill(entity) > 0;
    }

    @Override
    public List<EnterpriseEmployeeDO> listByUserId(Long userId, EnableStatusEnum statusEnum) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseEmployeeDO::getUserId, userId);

        if (statusEnum != EnableStatusEnum.ALL) {
            queryWrapper.lambda().eq(EnterpriseEmployeeDO::getStatus, statusEnum.getCode());
        }

        List<EnterpriseEmployeeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<EnterpriseEmployeeDO> listByUserIdEids(Long userId, List<Long> eids, EnableStatusEnum statusEnum) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseEmployeeDO::getUserId, userId)
                .in(EnterpriseEmployeeDO::getEid, eids);

        if (statusEnum != EnableStatusEnum.ALL) {
            queryWrapper.lambda().eq(EnterpriseEmployeeDO::getStatus, statusEnum.getCode());
        }

        List<EnterpriseEmployeeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<EnterpriseEmployeeDO> listByEid(Long eid, EnableStatusEnum statusEnum) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseEmployeeDO::getEid, eid);

        if (statusEnum != EnableStatusEnum.ALL) {
            queryWrapper.lambda().eq(EnterpriseEmployeeDO::getStatus, statusEnum.getCode());
        }

        List<EnterpriseEmployeeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<EnterpriseEmployeeDO> listByUserIds(List<Long> userIds) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseEmployeeDO::getUserId, userIds);
        return this.list(queryWrapper);
    }

    @Override
    public List<EnterpriseEmployeeDO> listByEidUserIds(Long eid, List<Long> userIds) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseEmployeeDO::getEid, eid)
                .in(EnterpriseEmployeeDO::getUserId, userIds);
        return this.list(queryWrapper);
    }

    @Override
    public boolean exists(Long eid, Long userId) {
        return this.getByEidUserId(eid, userId) != null;
    }

    @Override
    public List<EnterpriseEmployeeDO> listAdminsByEid(Long eid) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseEmployeeDO::getEid, eid)
                .eq(EnterpriseEmployeeDO::getAdminFlag, 1)
                .orderByAsc(EnterpriseEmployeeDO::getId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean isAdmin(Long eid, Long userId) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseEmployeeDO::getEid, eid)
                .eq(EnterpriseEmployeeDO::getUserId, userId)
                .last("limit 1");

        EnterpriseEmployeeDO entity = this.getOne(queryWrapper);
        if (entity == null || entity.getAdminFlag() == 0) {
            return false;
        }

        return true;
    }

    @Override
    public Map<Long, Long> countByEids(List<Long> eids) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseEmployeeDO::getEid, eids);
        List<EnterpriseEmployeeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        return list.stream().collect(Collectors.groupingBy(EnterpriseEmployeeDO::getEid, Collectors.counting()));
    }

    @Override
    public Integer countByDepartmentId(Long departmentId) {
        QueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseEmployeeDepartmentDO::getDepartmentId, departmentId);
        return enterpriseEmployeeDepartmentService.count(queryWrapper);
    }

    @Override
    public Map<Long, Long> countByDepartmentIds(List<Long> departmentIds) {
        QueryWrapper<EnterpriseEmployeeDepartmentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseEmployeeDepartmentDO::getDepartmentId, departmentIds);
        List<EnterpriseEmployeeDepartmentDO> list = enterpriseEmployeeDepartmentService.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        return list.stream().collect(Collectors.groupingBy(EnterpriseEmployeeDepartmentDO::getDepartmentId, Collectors.counting()));
    }

    @Override
    public Integer countByPositionId(Long positionId) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseEmployeeDO::getPositionId, positionId);
        return this.count(queryWrapper);
    }

    @Override
    public Map<Long, Long> countByPositionIds(List<Long> positionIds) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseEmployeeDO::getPositionId, positionIds);
        List<EnterpriseEmployeeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        return list.stream().collect(Collectors.groupingBy(EnterpriseEmployeeDO::getPositionId, Collectors.counting()));
    }

    @Override
    public EnterpriseEmployeeDO getByEidUserId(Long eid, Long userId) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseEmployeeDO::getEid, eid)
                .eq(EnterpriseEmployeeDO::getUserId, userId)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public EnterpriseEmployeeDO getByCode(Long eid, String code) {
        QueryWrapper<EnterpriseEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseEmployeeDO::getEid, eid)
                .eq(EnterpriseEmployeeDO::getCode, code)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public EnterpriseEmployeeDO getParentInfo(Long eid, Long userId) {
        EnterpriseEmployeeDO userEmployeeDO = this.getByEidUserId(eid, userId);
        if (userEmployeeDO == null) {
            return null;
        }

        Long parentId = userEmployeeDO.getParentId();
        if (parentId == null || parentId == 0L) {
            return null;
        }

        EnterpriseEmployeeDO parentEmployeeDO = this.getByEidUserId(eid, parentId);
        return parentEmployeeDO;
    }

}
