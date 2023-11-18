package com.yiling.user.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.system.dao.AppLoginInfoMapper;
import com.yiling.user.system.entity.AppLoginInfoDO;
import com.yiling.user.system.service.AppLoginInfoService;

/**
 * <p>
 * 登录信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-26
 */
@Service
@CacheConfig(cacheNames = "system:appLoginInfo")
public class AppLoginInfoServiceImpl extends BaseServiceImpl<AppLoginInfoMapper, AppLoginInfoDO> implements AppLoginInfoService {

    @Lazy
    @Autowired
    private AppLoginInfoService appLoginInfoService;

    @Override
    @Cacheable(key = "'getLatestLoginInfoByUserId:appId_' + #p0 + ':userId_' + #p1")
    public AppLoginInfoDO getLatestLoginInfoByUserId(Long appId, Long userId) {
        QueryWrapper<AppLoginInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AppLoginInfoDO::getAppId, appId)
                .eq(AppLoginInfoDO::getUserId, userId)
                .orderByDesc(AppLoginInfoDO::getLoginTime, AppLoginInfoDO::getId)
                .last("limit 1");

        AppLoginInfoDO entity = this.getOne(queryWrapper);
        return entity != null ? entity : new AppLoginInfoDO();
    }

    @Override
    @CacheEvict(key = "'getLatestLoginInfoByUserId:appId_' + #p0.appId + ':userId_' + #p0.userId")
    public boolean saveOrUpdate(AppLoginInfoDO entity) {
        AppLoginInfoDO appLoginInfoDO = appLoginInfoService.getLatestLoginInfoByUserId(entity.getAppId(), entity.getUserId());
        if (appLoginInfoDO.getId() != null) {
            entity.setId(appLoginInfoDO.getId());
            return this.updateById(entity);
        } else {
            return this.save(entity);
        }
    }
}
