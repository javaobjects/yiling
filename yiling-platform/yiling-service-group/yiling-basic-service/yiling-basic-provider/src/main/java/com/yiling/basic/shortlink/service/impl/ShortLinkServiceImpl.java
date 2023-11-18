package com.yiling.basic.shortlink.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.shortlink.dao.ShortLinkMapper;
import com.yiling.basic.shortlink.dto.ShortLinkDTO;
import com.yiling.basic.shortlink.dto.request.QueryShortLinkPageRequest;
import com.yiling.basic.shortlink.entity.ShortLinkDO;
import com.yiling.basic.shortlink.service.ShortLinkService;
import com.yiling.basic.shortlink.utils.ShortUrlGeneratorUtil;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 短链接 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-13
 */
@Slf4j
@Service
@RefreshScope
@CacheConfig(cacheNames = "system:shortUrl")
public class ShortLinkServiceImpl extends BaseServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    /**
     * 短地址域名
     */
    @Value("${short.url.domain:https://t.59yi.com}")
    private String domain;

    @Override
    public String generatorShortLink(String longUrl) {
        ShortLinkDO shortLinkDO = new ShortLinkDO();
        shortLinkDO.setLongUrl(longUrl);

        if (StrUtil.isEmpty(longUrl)) {
            return null;
        }
        String key = ShortUrlGeneratorUtil.getShortUrl(longUrl);
        log.info("已生成短链接Key：{}，长链接为：{}",key,longUrl);
        if (StrUtil.isEmpty(key)) {
            return null;
        }

        String longUrlData = this.getByKey(key);
        if (StrUtil.isNotEmpty(longUrlData)) {
            return domain + "/" + key;
        }

        String shortUrl = domain + "/" + key;
        shortLinkDO.setShortUrl(shortUrl);
        shortLinkDO.setUrlKey(key);

        save(shortLinkDO);

        return shortUrl;
    }

    @Override
    @Cacheable(key = "'getLongUrlByKey:key_' + #p0")
    public String getByKey(String key) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShortLinkDO::getUrlKey,key);
        queryWrapper.last("limit 1");

        ShortLinkDO shortLinkDO = Optional.ofNullable(getOne(queryWrapper)).orElse(new ShortLinkDO());

        return shortLinkDO.getLongUrl();
    }

    @Override
    public Page<ShortLinkDTO> queryListPage(QueryShortLinkPageRequest request) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(request.getUrlKey())) {
            queryWrapper.like(ShortLinkDO::getUrlKey,request.getUrlKey());
        }
        if (StrUtil.isNotEmpty(request.getShortUrl())) {
            queryWrapper.like(ShortLinkDO::getShortUrl,request.getShortUrl());
        }
        if (StrUtil.isNotEmpty(request.getLongUrl())) {
            queryWrapper.like(ShortLinkDO::getLongUrl,request.getLongUrl());
        }
        queryWrapper.orderByDesc(ShortLinkDO::getCreateTime);

        return PojoUtils.map(page(request.getPage(),queryWrapper),ShortLinkDTO.class);
    }
}
