package com.yiling.user.system.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.system.api.HmcUserAppApi;
import com.yiling.user.system.bo.HmcUserApp;
import com.yiling.user.system.dto.request.CreateHmcUserAppRequest;
import com.yiling.user.system.service.HmcUserAppService;
import com.yiling.user.system.service.HmcUserService;

import cn.hutool.core.lang.Assert;

/**
 * 健康管理中心用户 API 实现
 *
 * @author: fan.shen
 * @date: 2022-09-06
 */
@DubboService
public class HmcUserAppApiImpl implements HmcUserAppApi {

    @Autowired
    HmcUserService hmcUserService;

    @Autowired
    HmcUserAppService hmcUserAppService;

    @Override
    public HmcUserApp getByUserIdAndAppId(Long userId, String appId) {
        return hmcUserAppService.getByUserIdAndAppId(userId, appId);
    }

    @Override
    public void createHmcUserApp(CreateHmcUserAppRequest request) {
        Assert.notNull(request, "参数request不能为空");
        hmcUserAppService.createHmcUserApp(request);
    }

    @Override
    public HmcUserApp getByOpenId(String openId) {
        Assert.notNull(openId, "参数openId不能为空");
        return hmcUserAppService.getByOpenId(openId);
    }

//    @Override
//    public Page<HmcUser> pageList(QueryHmcUserPageListRequest request) {
//        return hmcUserService.pageList(request);
//    }
//
//    @Override
//    public HmcUser getById(Long id) {
//        Assert.notNull(id, "参数ID不能为空");
//        return hmcUserService.getById(id);
//    }
//
//    @Override
//    public List<HmcUserDTO> listByIds(List<Long> ids) {
//        Assert.notEmpty(ids, "参数ids不能为空");
//        return PojoUtils.map(hmcUserService.listByIds(ids), HmcUserDTO.class);
//    }
//
//    @Override
//    public HmcUser getByAppIdAndUnionId(String appId, String unionId) {
//        Assert.notBlank(appId, "参数appId不能为空");
//        Assert.notBlank(unionId, "参数unionId不能为空");
//        return hmcUserService.getByUnionId(unionId);
//    }
//
//    @Override
//    public Long create(CreateHmcUserRequest request) {
//        Assert.notNull(request, "参数request不能为空");
//        return hmcUserService.create(request);
//    }
//
//    @Override
//    public Boolean update(UpdateHmcUserRequest request) {
//        Assert.notNull(request, "参数request不能为空");
//        return hmcUserService.update(request);
//    }

}
