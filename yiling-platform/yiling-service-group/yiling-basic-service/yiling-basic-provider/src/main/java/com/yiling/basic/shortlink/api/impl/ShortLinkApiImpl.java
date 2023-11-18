package com.yiling.basic.shortlink.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.shortlink.api.ShortLinkApi;
import com.yiling.basic.shortlink.dto.ShortLinkDTO;
import com.yiling.basic.shortlink.dto.request.QueryShortLinkPageRequest;
import com.yiling.basic.shortlink.service.ShortLinkService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * 短链接 API 实现
 *
 * @author: lun.yu
 * @date: 2022/01/13
 */
@DubboService
public class ShortLinkApiImpl implements ShortLinkApi {

    @Autowired
    ShortLinkService shortLinkService;

    @Override
    public String generatorShortLink(String longUrl) {
        return shortLinkService.generatorShortLink(longUrl);
    }

    @Override
    public String getByKey(String key) {
        return shortLinkService.getByKey(key);
    }

    @Override
    public Page<ShortLinkDTO> queryListPage(QueryShortLinkPageRequest request) {
        return PojoUtils.map(shortLinkService.queryListPage(request),ShortLinkDTO.class);
    }
}
