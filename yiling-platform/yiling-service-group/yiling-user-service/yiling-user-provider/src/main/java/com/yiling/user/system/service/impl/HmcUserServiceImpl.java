package com.yiling.user.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.yiling.user.system.bo.HmcUserApp;
import com.yiling.user.system.dto.request.*;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.service.HmcUserAppService;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dao.HmcUserMapper;
import com.yiling.user.system.entity.HmcUserDO;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.HmcUserService;
import com.yiling.user.system.service.UserAuthsService;
import com.yiling.user.system.service.UserService;

/**
 * <p>
 * 健康管理中心用户表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-03-24
 */
@Service
public class HmcUserServiceImpl extends BaseServiceImpl<HmcUserMapper, HmcUserDO> implements HmcUserService {

    @Value("${user.staff.defaultPwd}")
    private String defaultPwd;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Autowired
    UserService userService;
    @Autowired
    UserAuthsService userAuthsService;
    @Autowired
    HmcUserAppService hmcUserAppService;

    @Lazy
    @Autowired
    HmcUserServiceImpl _this;

    @Override
    public Page<HmcUser> pageList(QueryHmcUserPageListRequest request) {
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public List<HmcUser> listByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }

        List<HmcUser> list = this.baseMapper.listByIds(ids);
        return CollUtil.isNotEmpty(list) ? list : ListUtil.empty();
    }

    @Override
    public HmcUser getById(Long id) {
        HmcUserDO hmcUserDO = this.getByUserId(id);
        return this.getHmcUser(hmcUserDO);
    }

    @Override
    public HmcUserDO getByUserId(Long userId) {
        QueryWrapper<HmcUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(HmcUserDO::getUserId, userId)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public HmcUser getByUnionId(String unionId) {
        QueryWrapper<HmcUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(HmcUserDO::getUnionId, unionId)
                .last("limit 1");
        HmcUserDO hmcUserDO = this.getOne(queryWrapper);
        return this.getHmcUser(hmcUserDO);
    }

    @Override
    public HmcUser getByOpenId(String openId) {
        // 1、根据openid 获取用户应用信息
        HmcUserApp hmcUserApp = hmcUserAppService.getByOpenId(openId);
        if (Objects.isNull(hmcUserApp)) {
            return null;
        }

        QueryWrapper<HmcUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(HmcUserDO::getUserId, hmcUserApp.getUserId())
                .last("limit 1");
        HmcUserDO hmcUserDO = this.getOne(queryWrapper);
        return this.getHmcUser(hmcUserDO);
    }

    @Override
    public Long create(CreateHmcUserRequest request) {
        HmcUser hmcUser = this.getByUnionId(request.getUnionId());
        if (hmcUser != null) {
            return hmcUser.getId();
        } else {
            MqMessageBO prepare = _this.createUser(request);
            mqMessageSendApi.send(prepare);
            return Long.valueOf(prepare.getBody());
        }
    }

    @GlobalTransactional
    public MqMessageBO createUser(CreateHmcUserRequest request) {
        UserDO userDO = new UserDO();
        userDO.setGender(request.getGender());
        userDO.setMobile(request.getMobile());
        userDO.setNickName(request.getNickName());
        userDO.setAvatarUrl(request.getAvatarUrl());
        userDO.setOpUserId(request.getOpUserId());
        userService.save(userDO);

        HmcUserDO hmcUserDO = new HmcUserDO();
        hmcUserDO.setUserId(userDO.getId());
        hmcUserDO.setUnionId(request.getUnionId());
        hmcUserDO.setRegisterSource(request.getRegisterSource());
        hmcUserDO.setInviteEid(request.getInviteEid());
        hmcUserDO.setInviteUserId(request.getInviteUserId());
        hmcUserDO.setActivityId(request.getActivityId());
        hmcUserDO.setActivitySource(request.getActivitySource());
        hmcUserDO.setOpUserId(request.getOpUserId());
        this.save(hmcUserDO);

        // 创建微信登录授权
        userAuthsService.createWeixinIdentifier(userDO.getId(), UserAuthsAppIdEnum.HMC.getCode(), request.getUnionId());

        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_HMC_REG_MINI_PROGRAM, Constants.TAG_HMC_REG_MINI_PROGRAM, null);
        // 发送小程序注册mq
        mqMessageBO.setBody(String.valueOf(userDO.getId()));

        return mqMessageSendApi.prepare(mqMessageBO);
    }

    @Override
    public Boolean update(UpdateHmcUserRequest request) {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setAppId(UserAuthsAppIdEnum.HMC.getCode());
        updateUserRequest.setId(request.getUserId());
        updateUserRequest.setGender(request.getGender());
        updateUserRequest.setNickName(request.getNickName());
        updateUserRequest.setAvatarUrl(request.getAvatarUrl());
        updateUserRequest.setOpUserId(request.getOpUserId());
        userService.update(updateUserRequest);

        HmcUserDO hmcUserDO = PojoUtils.map(request, HmcUserDO.class);
        return this.updateByUserId(hmcUserDO);
    }

    @Override
    public Boolean updateLoginTime(Long userId) {
        return this.baseMapper.updateLoginTime(userId) > 0;
    }

    @Override
    public boolean updatePassword(Long id, String password, Long opUserId) {
        return userAuthsService.updatePassword(id, UserAuthsAppIdEnum.HMC.getCode(), password, opUserId);
    }

    @Override
    public boolean resetPassword(Long id, Long opUserId) {
        return this.updatePassword(id, defaultPwd, opUserId);
    }

    @Override
    public List<Map<String, Long>> queryActivityDoctorInviteUserCount(QueryActivityDoctorUserCountRequest request) {
        return this.baseMapper.queryActivityDoctorInviteUserCount(request);
    }

    private boolean updateByUserId(HmcUserDO hmcUserDO) {
        QueryWrapper<HmcUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HmcUserDO::getUserId, hmcUserDO.getUserId());
        return this.update(hmcUserDO, queryWrapper);
    }

    private HmcUser getHmcUser(HmcUserDO hmcUserDO) {
        if (hmcUserDO == null) {
            return null;
        }

        UserDO userDO = userService.getById(hmcUserDO.getUserId());
        if (userDO == null) {
            return null;
        }

        HmcUser hmcUser = new HmcUser();
        PojoUtils.map(hmcUserDO, hmcUser);
        PojoUtils.map(userDO, hmcUser);

        return hmcUser;
    }
}
