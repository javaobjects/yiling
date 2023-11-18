package com.yiling.user.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.config.EsbConfig;
import com.yiling.user.esb.entity.EsbEmployeeDO;
import com.yiling.user.esb.service.EsbEmployeeService;
import com.yiling.user.system.bo.SjmsUser;
import com.yiling.user.system.dao.SjmsUserMapper;
import com.yiling.user.system.dto.request.CreateSjmsUserRequest;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.entity.SjmsUserDO;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.service.RoleService;
import com.yiling.user.system.service.SjmsUserService;
import com.yiling.user.system.service.UserRoleService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 神机妙算系统用户 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-23
 */
@Service
public class SjmsUserServiceImpl extends BaseServiceImpl<SjmsUserMapper, SjmsUserDO> implements SjmsUserService {

    @Autowired
    UserService userService;
    @Autowired
    EsbEmployeeService esbEmployeeService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;
    @Autowired
    EsbConfig esbConfig;

    @Override
    public SjmsUser getById(Long id) {
        LambdaQueryWrapper<SjmsUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SjmsUserDO::getUserId, id).last("limit 1");
        SjmsUserDO sjmsUserDO = this.getOne(lambdaQueryWrapper);
        return this.getSjmsUser(sjmsUserDO);
    }

    @Override
    public List<SjmsUser> listByIds(List<Long> ids) {
        LambdaQueryWrapper<SjmsUserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SjmsUserDO::getUserId, ids);
        List<SjmsUserDO> userDOList = this.list(wrapper);
        return userDOList.stream().map(this::getSjmsUser).collect(Collectors.toList());
    }

    @Override
    public SjmsUser getByEmpId(String empId) {
        LambdaQueryWrapper<SjmsUserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SjmsUserDO::getEmpId, empId).last("limit 1");
        SjmsUserDO sjmsUserDO = this.getOne(lambdaQueryWrapper);
        return this.getSjmsUser(sjmsUserDO);
    }

    @Override
    public Long create(CreateSjmsUserRequest request) {
        SjmsUser sjmsUser = this.getByEmpId(request.getEmpId());
        if (sjmsUser != null) {
            if (request.isIgnoreExists()) {
                return sjmsUser.getId();
            } else {
                throw new BusinessException(UserErrorCode.STAFF_EXISTS);
            }
        }

        UserDO userDO = new UserDO();
        userDO.setName(request.getName());
        userDO.setGender(request.getGender());
        userDO.setBirthday(request.getBirthday());
        userDO.setMobile(request.getMobile());
        userDO.setEmail(request.getEmail());
        userDO.setIdNumber(request.getIdNumber());
        userDO.setOpUserId(request.getOpUserId());
        userService.save(userDO);

        SjmsUserDO entity = new SjmsUserDO();
        entity.setUserId(userDO.getId());
        entity.setEmpId(request.getEmpId());
        entity.setOpUserId(request.getOpUserId());
        this.save(entity);

        // 给用户绑定相关角色
        {
            // 神机妙算相关用户
            List<String> bindRoleCodes = CollUtil.newArrayList();
            if (esbConfig.getAdmin().getEmpIds().contains(request.getEmpId())) {
                bindRoleCodes.addAll(esbConfig.getAdmin().getRoleCodes());
            } else if (esbEmployeeService.isProvinceManager(request.getEmpId())) {
                bindRoleCodes.addAll(esbConfig.getProvinceManager().getRoleCodes());
            } else {
                bindRoleCodes.addAll(esbConfig.getCommonUser().getRoleCodes());
            }

            // 是否需货计划人员
            EsbEmployeeDO esbEmployeeDO = esbEmployeeService.getByEmpId(request.getEmpId());
            if (this.isDemandPlanUser(esbEmployeeDO.getFullpath())) {
                bindRoleCodes.addAll(esbConfig.getDemandPlanUser().getRoleCodes());
            }

            List<RoleDO> roleDOList = roleService.getByCodeList(bindRoleCodes);
            List<Long> roleIds = roleDOList.stream().map(RoleDO::getId).distinct().collect(Collectors.toList());
            userRoleService.bindUserRoles(PermissionAppEnum.SJMS, 0L, userDO.getId(), roleIds, 0L);
        }

        return userDO.getId();
    }

    private boolean isDemandPlanUser(String fullPath) {
        List<String> fullPathPrefixList = esbConfig.getDemandPlanUser().getFullPathPrefix();
        return fullPathPrefixList.stream().filter(e -> fullPath.startsWith(e)).findAny().isPresent();
    }

    private SjmsUser getSjmsUser(SjmsUserDO sjmsUserDO) {
        if (sjmsUserDO == null) {
            return null;
        }

        UserDO userDO = userService.getById(sjmsUserDO.getUserId());
        if (userDO == null) {
            return null;
        }

        SjmsUser sjmsUser = new SjmsUser();
        PojoUtils.map(sjmsUserDO, sjmsUser);
        PojoUtils.map(userDO, sjmsUser);
        return sjmsUser;
    }
}
