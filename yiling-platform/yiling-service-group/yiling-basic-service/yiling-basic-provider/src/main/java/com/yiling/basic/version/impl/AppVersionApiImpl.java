package com.yiling.basic.version.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.version.api.AppVersionApi;
import com.yiling.basic.version.dto.AppInfoDTO;
import com.yiling.basic.version.dto.AppVersionDTO;
import com.yiling.basic.version.dto.AppVersionPageDTO;
import com.yiling.basic.version.dto.request.VersionAddRequest;
import com.yiling.basic.version.dto.request.VersionPageRequest;
import com.yiling.basic.version.entity.AppInfoDO;
import com.yiling.basic.version.entity.AppVersionDO;
import com.yiling.basic.version.enums.AppChannelEnum;
import com.yiling.basic.version.service.AppInfoService;
import com.yiling.basic.version.service.AppVersionService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.RequiredArgsConstructor;

/**
 * @author: yong.zhang
 * @date: 2021/10/27
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppVersionApiImpl implements AppVersionApi {

    private final AppVersionService appVersionService;

    private final AppInfoService appInfoService;

    @Override
    public AppVersionDTO querySaNews(Integer appType, AppChannelEnum appChannelEnum) {
        AppVersionDO appVersionDO = appVersionService.queryNews(appType, appChannelEnum.getCode());
        return PojoUtils.map(appVersionDO, AppVersionDTO.class);
    }

    @Override
    public boolean saveVersion(VersionAddRequest request) {
        return appVersionService.saveVersion(request);
    }

    @Override
    public Page<AppVersionPageDTO> pageList(VersionPageRequest request) {
        return appVersionService.pageList(request);
    }

    @Override
    public AppVersionDTO queryB2BNews(Integer appType, AppChannelEnum appChannelEnum) {
        AppVersionDO appVersionDO = appVersionService.queryNews(appType, appChannelEnum.getCode());
        return PojoUtils.map(appVersionDO, AppVersionDTO.class);
    }

    @Override
    public List<AppInfoDTO> listAppInfoByChannelCode(AppChannelEnum channelEnum) {
        List<AppInfoDO> appInfoDOS = appInfoService.listAppInfoByChannelCode(channelEnum);
        return PojoUtils.map(appInfoDOS, AppInfoDTO.class);
    }
}
