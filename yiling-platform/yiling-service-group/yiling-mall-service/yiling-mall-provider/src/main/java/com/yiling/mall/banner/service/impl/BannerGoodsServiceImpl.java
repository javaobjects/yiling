package com.yiling.mall.banner.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.mall.banner.dao.BannerGoodsMapper;
import com.yiling.mall.banner.dto.request.QueryBannerGoodsPageListRequest;
import com.yiling.mall.banner.entity.BannerGoodsDO;
import com.yiling.mall.banner.service.BannerGoodsService;

import cn.hutool.core.lang.Assert;

/**
 * <p>
 * banner商品表 服务实现类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Service
public class BannerGoodsServiceImpl extends BaseServiceImpl<BannerGoodsMapper, BannerGoodsDO> implements BannerGoodsService {

    @Override
    public Page<BannerGoodsDO> pageList(QueryBannerGoodsPageListRequest request) {
        Assert.notNull(request.getBannerId(), "查询banner商品明细：bannerId为空！");
        return this.baseMapper.selectPage(request.getPage(), new LambdaQueryWrapper<BannerGoodsDO>()
                        .eq(BannerGoodsDO::getBannerId, request.getBannerId()).orderByDesc(BannerGoodsDO::getSort));
    }
}
