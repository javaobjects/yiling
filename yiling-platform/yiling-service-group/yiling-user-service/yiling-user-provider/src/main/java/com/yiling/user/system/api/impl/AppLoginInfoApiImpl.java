package com.yiling.user.system.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.AppLoginInfoApi;
import com.yiling.user.system.dto.AppLoginInfoDTO;
import com.yiling.user.system.dto.request.SaveAppLoginInfoRequest;
import com.yiling.user.system.entity.AppLoginInfoDO;
import com.yiling.user.system.service.AppLoginInfoService;

import cn.hutool.core.convert.Convert;

/**
 * APP登录信息 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/11/26
 */
@DubboService
public class AppLoginInfoApiImpl implements AppLoginInfoApi {

    @Autowired
    private AppLoginInfoService appLoginInfoService;

    @Override
    public AppLoginInfoDTO getLatestLoginInfoByUserId(Long appId, Long userId) {
        AppLoginInfoDO appLoginInfoDO = appLoginInfoService.getLatestLoginInfoByUserId(appId, userId);
        return PojoUtils.map(appLoginInfoDO, AppLoginInfoDTO.class);
    }

    @Override
    public Boolean saveOrUpdate(SaveAppLoginInfoRequest request) {
        AppLoginInfoDO entity = new AppLoginInfoDO();
        entity.setAppId(request.getAppId());
        entity.setUserId(request.getUserId());
        entity.setGrantType(request.getGrantType());
        entity.setTerminalType(request.getTerminalType());
        entity.setManufacturer(Convert.toStr(request.getManufacturer(), ""));
        entity.setBrand(Convert.toStr(request.getBrand(), ""));
        entity.setModel(Convert.toStr(request.getModel(), ""));
        entity.setOsVersion(Convert.toStr(request.getOsVersion(), ""));
        entity.setSdkVersion(Convert.toStr(request.getSdkVersion(), ""));
        entity.setScreenSize(Convert.toStr(request.getScreenSize(), ""));
        entity.setResolution(Convert.toStr(request.getResolution(), ""));
        entity.setUdid(Convert.toStr(request.getUdid(), ""));
        entity.setAppVersion(Convert.toStr(request.getAppVersion(), ""));
        entity.setChannelCode(Convert.toStr(request.getChannelCode(), ""));
        entity.setLoginTime(request.getLoginTime());
        entity.setOpUserId(request.getOpUserId());
        return appLoginInfoService.saveOrUpdate(entity);
    }
}
