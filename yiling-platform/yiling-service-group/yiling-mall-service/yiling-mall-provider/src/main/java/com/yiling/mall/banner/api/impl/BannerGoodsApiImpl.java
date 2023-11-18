package com.yiling.mall.banner.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.BannerGoodsApi;
import com.yiling.mall.banner.dto.BannerGoodsDTO;
import com.yiling.mall.banner.dto.request.QueryBannerGoodsPageListRequest;
import com.yiling.mall.banner.service.BannerGoodsService;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/15 0015
 */
@DubboService
public class BannerGoodsApiImpl implements BannerGoodsApi {

    @Autowired
    private BannerGoodsService bannerGoodsService;

    @Override
    public Page<BannerGoodsDTO> pageList(QueryBannerGoodsPageListRequest request) {
        return PojoUtils.map(bannerGoodsService.pageList(request), BannerGoodsDTO.class);
    }
}
