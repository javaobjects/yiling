package com.yiling.basic.version.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.version.dao.AppVersionMapper;
import com.yiling.basic.version.dto.AppVersionPageDTO;
import com.yiling.basic.version.dto.request.VersionAddRequest;
import com.yiling.basic.version.dto.request.VersionPageRequest;
import com.yiling.basic.version.entity.AppChannelDO;
import com.yiling.basic.version.entity.AppInfoDO;
import com.yiling.basic.version.entity.AppVersionDO;
import com.yiling.basic.version.service.AppChannelService;
import com.yiling.basic.version.service.AppInfoService;
import com.yiling.basic.version.service.AppVersionService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 应用版本信息 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppVersionServiceImpl extends BaseServiceImpl<AppVersionMapper, AppVersionDO> implements AppVersionService {

    private final AppInfoService appInfoService;

    private final AppChannelService appChannelService;

    @Override
    public AppVersionDO queryNews(Integer appType, String channelCode) {
        List<AppChannelDO> appChannelDOList = appChannelService.queryByCode(channelCode);
        AppInfoDO appInfoDOSel = null;
        for (AppChannelDO appChannelDO : appChannelDOList) {
            AppInfoDO appInfoDO = appInfoService.getById(appChannelDO.getAppId());
            if (appType.equals(appInfoDO.getAppType())) {
                appInfoDOSel = appInfoDO;
                break;
            }
        }
        if (null == appInfoDOSel) {
            return null;
        }
        QueryWrapper<AppVersionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppVersionDO::getAppId, appInfoDOSel.getId()).eq(AppVersionDO::getChannelCode, channelCode).orderByDesc(AppVersionDO::getCreateTime).last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public boolean saveVersion(VersionAddRequest request) {
        AppVersionDO appVersionDO = PojoUtils.map(request, AppVersionDO.class);
        appVersionDO.setUpdateUser(request.getOpUserId());
        appVersionDO.setUpdateTime(request.getOpTime());
        if (null != request.getPackageSize() && request.getPackageSize() > 0) {
            Long packageSize = appVersionDO.getPackageSize();
            int kb = 1024;
            long size = packageSize / kb;
            appVersionDO.setPackageSize(size);
        }
        if (null == request.getId()) {
            appVersionDO.setCreateUser(request.getOpUserId());
            appVersionDO.setCreateTime(request.getOpTime());
            return this.save(appVersionDO);
        } else {
            return this.getBaseMapper().updateVersionInfo(appVersionDO);
        }
    }

    @Override
    public Page<AppVersionPageDTO> pageList(VersionPageRequest request) {
        Page<AppVersionDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.getBaseMapper().pageByCondition(objectPage, request);
    }
}
