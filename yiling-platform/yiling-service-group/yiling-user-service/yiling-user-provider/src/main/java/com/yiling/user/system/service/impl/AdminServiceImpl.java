package com.yiling.user.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PasswordUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.dao.AdminMapper;
import com.yiling.user.system.dto.request.QueryAdminPageListRequest;
import com.yiling.user.system.dto.request.SaveAdminRequest;
import com.yiling.user.system.entity.AdminDO;
import com.yiling.user.system.entity.UserAuthsDO;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.enums.UserAuthsIdentityTypeEnum;
import com.yiling.user.system.service.AdminService;
import com.yiling.user.system.service.UserAuthsService;
import com.yiling.user.system.service.UserRoleService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 管理员信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Service
public class AdminServiceImpl extends BaseServiceImpl<AdminMapper, AdminDO> implements AdminService {

    @Value("${user.staff.defaultPwd}")
    private String defaultPwd;

    @Autowired
    UserService userService;
    @Autowired
    UserAuthsService userAuthsService;
    @Autowired
    UserRoleService userRoleService;

    @Override
    public Admin getById(Long id) {
        UserDO userDO = userService.getById(id);
        return this.getAdmin(userDO);
    }

    @Override
    public Admin getByUsername(String username) {
        return this.baseMapper.getByUsername(username);
    }

    @Override
    public Admin getByMobile(String mobile) {
        return this.baseMapper.getByMobile(mobile);
    }

    @Override
    public Admin getByName(String name) {
        List<Admin> adminList = this.baseMapper.getByName(name, 1);
        if (CollUtil.isNotEmpty(adminList)) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public List<Admin> getListByName(String name, Integer limit) {
        return this.baseMapper.getByName(name, limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SaveAdminRequest request) {
        if (request.getId() == null) {
            String username = request.getUsername();
            if (StrUtil.isNotEmpty(username)) {
                Admin admin = this.getByUsername(username);
                if (admin != null) {
                    throw new BusinessException(UserErrorCode.USERNAME_EXISTS);
                }
            }

            String mobile = request.getMobile();
            if (StrUtil.isNotEmpty(mobile)) {
                Admin admin = this.getByMobile(mobile);
                if (admin != null) {
                    throw new BusinessException(UserErrorCode.MOBILE_EXISTS);
                }
            }

            UserDO userDO = new UserDO();
            userDO.setUsername(request.getUsername());
            userDO.setName(request.getName());
            userDO.setGender(request.getGender());
            userDO.setBirthday(request.getBirthday());
            userDO.setMobile(request.getMobile());
            userDO.setEmail(request.getEmail());
            userDO.setNickName(request.getNickName());
            userDO.setStatus(request.getStatus());
            userDO.setOpUserId(request.getOpUserId());
            userService.save(userDO);

            // 创建用户名登录授权
            String password = request.getPassword();
            if (StrUtil.isEmpty(password)) {
                password = defaultPwd;
            }
            userAuthsService.createUsernameIdentifier(userDO.getId(), UserAuthsAppIdEnum.ADMIN.getCode(), request.getUsername(), password);

            AdminDO adminDO = new AdminDO();
            adminDO.setUserId(userDO.getId());
            adminDO.setOpUserId(request.getOpUserId());
            this.save(adminDO);

            //保存用户绑定的角色
            userRoleService.bindUserRoles(PermissionAppEnum.ADMIN, 0L, userDO.getId(), request.getRoleIdList(), request.getOpUserId());
        } else {
            Admin adminInfo = this.getById(request.getId());
            if (adminInfo == null) {
                throw new BusinessException(ResultCode.FAILED, "ID对应的信息不存在");
            } else if(adminInfo.getAdminFlag() == 1){
                AdminDO opUser = this.getByUserId(request.getOpUserId());
                if(opUser == null || opUser.getAdminFlag() != 1){
                    throw new BusinessException(UserErrorCode.AUTH_ILLEGAL);
                }
            }

            String username = request.getUsername();
            if (StrUtil.isNotEmpty(username) && !username.equals(adminInfo.getUsername())) {
                Admin admin = this.getByUsername(username);
                if (admin != null && !admin.getId().equals(request.getId())) {
                    throw new BusinessException(UserErrorCode.USERNAME_EXISTS);
                }

                // 修改用户名登录授权
                userAuthsService.updateUsername(request.getId(), UserAuthsAppIdEnum.ADMIN.getCode(), request.getUsername(), request.getOpUserId());
            }

            String mobile = request.getMobile();
            if (StrUtil.isNotEmpty(mobile) && !mobile.equals(adminInfo.getMobile())) {
                Admin admin = this.getByMobile(mobile);
                if (admin != null && !admin.getId().equals(request.getId())) {
                    throw new BusinessException(UserErrorCode.MOBILE_EXISTS);
                }
            }

            UserDO userDO = new UserDO();
            userDO.setId(request.getId());
            userDO.setUsername(request.getUsername());
            userDO.setName(request.getName());
            userDO.setGender(request.getGender());
            userDO.setBirthday(request.getBirthday());
            userDO.setMobile(request.getMobile());
            userDO.setEmail(request.getEmail());
            userDO.setNickName(request.getNickName());
            userDO.setStatus(request.getStatus());
            userDO.setOpUserId(request.getOpUserId());
            userService.updateById(userDO);

            // 更新记录修改时间字段
            AdminDO adminDO = this.getByUserId(request.getId());
            adminDO.setOpUserId(request.getOpUserId());
            this.updateById(adminDO);

            //更新用户绑定的角色
            userRoleService.updateUserRoles(PermissionAppEnum.ADMIN, 0L, request.getId(), request.getRoleIdList(), request.getOpUserId());
        }

        return true;
    }

    @Override
    public Page<Admin> pageList(QueryAdminPageListRequest request) {
        return this.getBaseMapper().pageList(request.getPage(), request);
    }

    @Override
    public boolean updatePassword(Long id, String password, Long opUserId) {
        return userAuthsService.updatePassword(id, UserAuthsAppIdEnum.ADMIN.getCode(), password, opUserId);
    }

    @Override
    public boolean resetPassword(Long id, Long opUserId) {
        return this.updatePassword(id, defaultPwd, opUserId);
    }

    @Override
    public boolean checkPassword(Long userId, String password) {
        UserAuthsDO userAuthsDO = userAuthsService.getByUser(userId, UserAuthsAppIdEnum.ADMIN.getCode(), UserAuthsIdentityTypeEnum.USERNAME.getCode());
        if (userAuthsDO == null) {
            userAuthsDO = userAuthsService.getByUser(userId, UserAuthsAppIdEnum.ADMIN.getCode(), UserAuthsIdentityTypeEnum.MOBILE.getCode());
        }

        if (userAuthsDO == null) {
            return false;
        }

        return PasswordUtils.encrypt(password, userAuthsDO.getSalt()).equals(userAuthsDO.getCredential());
    }

    private Admin getAdmin(UserDO userDO) {
        if (userDO == null) {
            return null;
        }

        AdminDO adminDO = this.getByUserId(userDO.getId());
        if (adminDO == null) {
            return null;
        }

        Admin admin = new Admin();
        PojoUtils.map(adminDO, admin);
        PojoUtils.map(userDO, admin);
        return admin;
    }

    private AdminDO getByUserId(Long userId) {
        LambdaQueryWrapper<AdminDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminDO::getUserId, userId).last("limit 1");
        return this.getOne(lambdaQueryWrapper);
    }
}
