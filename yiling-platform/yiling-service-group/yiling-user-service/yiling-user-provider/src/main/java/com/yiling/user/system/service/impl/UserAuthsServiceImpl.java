package com.yiling.user.system.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PasswordUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.dao.UserAuthsMapper;
import com.yiling.user.system.entity.UserAuthsDO;
import com.yiling.user.system.enums.UserAuthsIdentityTypeEnum;
import com.yiling.user.system.service.UserAuthsService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户授权信息表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-09-23
 */
@Slf4j
@Service
public class UserAuthsServiceImpl extends BaseServiceImpl<UserAuthsMapper, UserAuthsDO> implements UserAuthsService {

    @Override
    public UserAuthsDO getByIdentifier(String appId, String identityType, String identifier) {
        QueryWrapper<UserAuthsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserAuthsDO::getAppId, appId)
                .eq(UserAuthsDO::getIdentityType, identityType)
                .eq(UserAuthsDO::getIdentifier, identifier)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public UserAuthsDO getByUser(Long userId, String appId, String identityType) {
        QueryWrapper<UserAuthsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserAuthsDO::getUserId, userId)
                .eq(UserAuthsDO::getAppId, appId)
                .eq(UserAuthsDO::getIdentityType, identityType)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean createUsernameIdentifier(Long userId, String appId, String username, String password) {
        UserAuthsDO entity = new UserAuthsDO();
        entity.setUserId(userId);
        entity.setAppId(appId);
        entity.setIdentityType(UserAuthsIdentityTypeEnum.USERNAME.getCode());
        entity.setIdentifier(username);
        entity.setSalt(PasswordUtils.genPasswordSalt());
        if (StrUtil.isNotEmpty(password)) {
            entity.setCredential(PasswordUtils.encrypt(password, entity.getSalt()));
        }
        entity.setOpUserId(userId);
        return this.save(entity);
    }

    @Override
    public Boolean createMobileIdentifier(Long userId, String appId, String mobile, String password) {
        UserAuthsDO entity = new UserAuthsDO();
        entity.setUserId(userId);
        entity.setAppId(appId);
        entity.setIdentityType(UserAuthsIdentityTypeEnum.MOBILE.getCode());
        entity.setIdentifier(mobile);
        entity.setSalt(PasswordUtils.genPasswordSalt());
        if (StrUtil.isNotEmpty(password)) {
            entity.setCredential(PasswordUtils.encrypt(password, entity.getSalt()));
        }
        entity.setOpUserId(userId);
        return this.save(entity);
    }

    @Override
    public Boolean createWeixinIdentifier(Long userId, String appId, String unionId) {
        UserAuthsDO entity = new UserAuthsDO();
        entity.setUserId(userId);
        entity.setAppId(appId);
        entity.setIdentityType(UserAuthsIdentityTypeEnum.WEIXIN.getCode());
        entity.setIdentifier(unionId);
        entity.setOpUserId(userId);
        return this.save(entity);
    }

    @Override
    public Boolean updatePassword(Long userId, String appId, String password, Long opUserId) {
        QueryWrapper<UserAuthsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserAuthsDO::getUserId, userId)
                .eq(UserAuthsDO::getAppId, appId)
                .in(UserAuthsDO::getIdentityType, ListUtil.toList(UserAuthsIdentityTypeEnum.USERNAME.getCode(), UserAuthsIdentityTypeEnum.MOBILE.getCode(), UserAuthsIdentityTypeEnum.EMAIL.getCode()));

        UserAuthsDO entity = new UserAuthsDO();
        entity.setSalt(PasswordUtils.genPasswordSalt());
        entity.setCredential(PasswordUtils.encrypt(password, entity.getSalt()));
        entity.setOpUserId(opUserId);
        return this.update(entity, queryWrapper);
    }

    @Override
    public Boolean updateUsername(Long userId, String appId, String username, Long opUserId) {
        UserAuthsDO entity = this.getByIdentifier(appId, UserAuthsIdentityTypeEnum.USERNAME.getCode(), username);
        if (entity != null) {
            if (!entity.getUserId().equals(userId)) {
                throw new BusinessException(UserErrorCode.USERNAME_EXISTS);
            } else {
                return true;
            }
        }

        entity = this.getByUser(userId, appId, UserAuthsIdentityTypeEnum.USERNAME.getCode());
        if (entity == null) {
            return true;
        }

        entity.setIdentifier(username);
        entity.setOpUserId(opUserId);
        return this.updateById(entity);
    }

    @Override
    public Boolean updateMobile(Long userId, String appId, String mobile, Long opUserId) {
        UserAuthsDO entity = this.getByIdentifier(appId, UserAuthsIdentityTypeEnum.MOBILE.getCode(), mobile);
        if (entity != null) {
            if (!entity.getUserId().equals(userId)) {
                throw new BusinessException(UserErrorCode.MOBILE_EXISTS);
            } else {
                return true;
            }
        }

        entity = this.getByUser(userId, appId, UserAuthsIdentityTypeEnum.MOBILE.getCode());
        if (entity == null) {
            return true;
        }

        entity.setIdentifier(mobile);
        entity.setOpUserId(opUserId);
        return this.updateById(entity);
    }

    @Override
    public Boolean clear(Long userId, Long opUserId) {
        QueryWrapper<UserAuthsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserAuthsDO::getUserId, userId);

        UserAuthsDO entity = new UserAuthsDO();
        entity.setOpUserId(opUserId);

        this.batchDeleteWithFill(entity, queryWrapper);
        return true;
    }
}
