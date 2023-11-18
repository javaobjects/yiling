package com.yiling.user.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PasswordUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dao.StaffMapper;
import com.yiling.user.system.dto.request.CreateStaffByUserIdRequest;
import com.yiling.user.system.dto.request.CreateStaffRequest;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.dto.request.QueryStaffPageListRequest;
import com.yiling.user.system.entity.StaffDO;
import com.yiling.user.system.entity.UserAuthsDO;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.enums.UserAuthsIdentityTypeEnum;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserAuthsService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Service
public class StaffServiceImpl extends BaseServiceImpl<StaffMapper, StaffDO> implements StaffService {

    @Value("${user.staff.defaultPwd}")
    private String defaultPwd;

    @Autowired
    UserService userService;
    @Autowired
    UserAuthsService userAuthsService;

    @Override
    public Staff getById(Long id) {
        UserDO userDO = userService.getById(id);
        return this.getStaff(userDO);
    }

    @Override
    public Staff getByUsername(String username) {
        return this.baseMapper.getByUsername(username);
    }

    @Override
    public Staff getByMobile(String mobile) {
        return this.baseMapper.getByMobile(mobile);
    }

    @Override
    public StaffDO getByUserId(Long userId) {
        LambdaQueryWrapper<StaffDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StaffDO::getUserId, userId).last("limit 1");
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public Page<Staff> pageList(QueryStaffPageListRequest request) {
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public List<Staff> list(QueryStaffListRequest request) {
        List<Staff> list = this.baseMapper.list(request);
        return CollUtil.isNotEmpty(list) ? list : ListUtil.empty();
    }

    @Override
    public Long create(CreateStaffRequest request) {
        String mobile = request.getMobile();
        if (StrUtil.isNotEmpty(mobile)) {
            Staff staff = this.getByMobile(mobile);
            if (staff != null) {
                if (request.isIgnoreExists()) {
                    return staff.getId();
                } else {
                    throw new BusinessException(UserErrorCode.MOBILE_EXISTS);
                }
            }
        }

        UserDO userDO = new UserDO();
        userDO.setName(request.getName());
        userDO.setGender(request.getGender());
        userDO.setBirthday(request.getBirthday());
        userDO.setMobile(request.getMobile());
        userDO.setEmail(request.getEmail());
        userDO.setIdNumber(request.getIdNumber());
        userDO.setNickName(request.getNickName());
        userDO.setAvatarUrl(request.getAvatarUrl());
        userDO.setOpUserId(request.getOpUserId());
        userService.save(userDO);

        // 创建手机号登录授权
        String password = request.getPassword();
        if (StrUtil.isEmpty(password)) {
            password = defaultPwd;
        }
        userAuthsService.createMobileIdentifier(userDO.getId(), UserAuthsAppIdEnum.MALL.getCode(), request.getMobile(), password);

        StaffDO entity = new StaffDO();
        entity.setUserId(userDO.getId());
        entity.setOpUserId(request.getOpUserId());
        this.save(entity);

        return userDO.getId();
    }

    @Override
    public boolean create(CreateStaffByUserIdRequest request) {
        StaffDO staffDO = this.getByUserId(request.getUserId());
        if (staffDO != null) {
            if (request.isIgnoreExists()) {
                return true;
            } else {
                throw new BusinessException(UserErrorCode.STAFF_EXISTS);
            }
        }

        StaffDO entity = PojoUtils.map(request, StaffDO.class);
        return this.save(entity);
    }

    @Override
    public boolean updatePassword(Long id, String password, Long opUserId) {
        return userAuthsService.updatePassword(id, UserAuthsAppIdEnum.MALL.getCode(), password, opUserId);
    }

    @Override
    public boolean resetPassword(Long id, Long opUserId) {
        return this.updatePassword(id, defaultPwd, opUserId);
    }

    @Override
    public boolean checkPassword(Long userId, String password) {
        UserAuthsDO userAuthsDO = userAuthsService.getByUser(userId, UserAuthsAppIdEnum.MALL.getCode(), UserAuthsIdentityTypeEnum.MOBILE.getCode());
        if (userAuthsDO == null) {
            userAuthsDO = userAuthsService.getByUser(userId, UserAuthsAppIdEnum.MALL.getCode(), UserAuthsIdentityTypeEnum.USERNAME.getCode());
        }

        if (userAuthsDO == null) {
            return false;
        }

        return PasswordUtils.encrypt(password, userAuthsDO.getSalt()).equals(userAuthsDO.getCredential());
    }

    private Staff getStaff(UserDO userDO) {
        if (userDO == null) {
            return null;
        }

        StaffDO staffDO = this.getByUserId(userDO.getId());
        if (staffDO == null) {
            return null;
        }

        Staff staff = new Staff();
        PojoUtils.map(staffDO, staff);
        PojoUtils.map(userDO, staff);
        return staff;
    }

}
