package com.yiling.mall.banner.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.banner.dto.BannerGoodsDTO;
import com.yiling.mall.banner.dto.request.QueryBannerGoodsPageListRequest;

/**
 * banner Api
 *
 * @author: yuecheng.chen
 * @date: 2021/6/15
 */
public interface BannerGoodsApi {

    /**
     * 获取banner明细分页
     * @param request
     * @return
     */
    Page<BannerGoodsDTO> pageList(QueryBannerGoodsPageListRequest request);

}
