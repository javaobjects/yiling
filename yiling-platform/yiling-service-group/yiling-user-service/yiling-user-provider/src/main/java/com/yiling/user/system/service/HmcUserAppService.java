package com.yiling.user.system.service;

import com.yiling.user.system.bo.HmcUserApp;
import com.yiling.user.system.dto.request.CreateHmcUserAppRequest;
import com.yiling.user.system.entity.HmcUserAppDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 健康管理中心用户应用表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-09-06
 */
public interface HmcUserAppService extends BaseService<HmcUserAppDO> {

    /**
     * 根据用户id + appId 获取用户信息
     *
     *
     * @param userId 用户id
     * @param appId 微信小程序id
     * @return com.yiling.user.system.bo.HmcUser
     * @author fan.shen
     * @date 2022-09-06
     **/
    HmcUserApp getByUserIdAndAppId(Long userId, String appId);

    /**
     * 创建用户应用
     * @param request
     * @return
     */
    void createHmcUserApp(CreateHmcUserAppRequest request);

    /**
     * 根据openId获取用户信息
     * @param openId
     * @return
     */
    HmcUserApp getByOpenId(String openId);
}
