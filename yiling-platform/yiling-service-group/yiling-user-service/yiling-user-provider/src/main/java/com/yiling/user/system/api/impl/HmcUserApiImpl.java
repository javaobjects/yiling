package com.yiling.user.system.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.yiling.user.system.dto.request.QueryActivityDoctorUserCountRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.bo.HmcUserApp;
import com.yiling.user.system.dto.request.CreateHmcUserRequest;
import com.yiling.user.system.dto.request.QueryHmcUserPageListRequest;
import com.yiling.user.system.dto.request.UpdateHmcUserRequest;
import com.yiling.user.system.service.HmcUserAppService;
import com.yiling.user.system.service.HmcUserService;

import cn.hutool.core.lang.Assert;

/**
 * 健康管理中心用户 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/3/24
 */
@DubboService
public class HmcUserApiImpl implements HmcUserApi {

    @Autowired
    HmcUserService hmcUserService;

    @Autowired
    HmcUserAppService hmcUserAppService;

    @Override
    public Page<HmcUser> pageList(QueryHmcUserPageListRequest request) {
        return hmcUserService.pageList(request);
    }

    @Override
    public HmcUser getByIdAndAppId(Long id, String appId) {
        Assert.notNull(id, "参数ID不能为空");
        Assert.notNull(appId, "参数appId不能为空");
        HmcUser hmcUser = hmcUserService.getById(id);

        HmcUserApp hmcUserApp = hmcUserAppService.getByUserIdAndAppId(id, appId);
        if (Objects.nonNull(hmcUserApp)) {
            hmcUser.setAppId(hmcUserApp.getAppId());
            hmcUser.setMiniProgramOpenId(hmcUserApp.getOpenId());
        }

        return hmcUser;
    }

    @Override
    public HmcUser getById(Long id) {
        Assert.notNull(id, "参数ID不能为空");
        return hmcUserService.getById(id);
    }

    @Override
    public List<HmcUser> listByIds(List<Long> ids) {
        Assert.notEmpty(ids, "参数ids不能为空");
        return hmcUserService.listByIds(ids);
    }

    @Override
    public HmcUser getByUnionId(String unionId) {
        Assert.notBlank(unionId, "参数unionId不能为空");
        return hmcUserService.getByUnionId(unionId);
    }

    @Override
    public HmcUser getByOpenId(String openId) {
        Assert.notBlank(openId, "参数openId不能为空");
        return hmcUserService.getByOpenId(openId);
    }

    @Override
    public Long create(CreateHmcUserRequest request) {
        Assert.notNull(request, "参数request不能为空");
        return hmcUserService.create(request);
    }

    @Override
    public Boolean update(UpdateHmcUserRequest request) {
        Assert.notNull(request, "参数request不能为空");
        return hmcUserService.update(request);
    }

    @Override
    public boolean updatePassword(Long id, String password, Long opUserId) {
        return hmcUserService.updatePassword(id, password, opUserId);
    }

    @Override
    public boolean resetPassword(Long id, Long opUserId) {
        return hmcUserService.resetPassword(id, opUserId);
    }

    @Override
    public List<Map<String, Long>> queryActivityDoctorInviteUserCount(QueryActivityDoctorUserCountRequest request) {
        if (CollUtil.isEmpty(request.getDoctorIdList())) {
            return Lists.newArrayList();
        }
        return hmcUserService.queryActivityDoctorInviteUserCount(request);
    }
}
