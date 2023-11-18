package com.yiling.mall.banner.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.banner.dto.request.QueryBannerGoodsPageListRequest;
import com.yiling.mall.banner.entity.BannerGoodsDO;

/**
 * <p>
 * banner商品表 服务类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
public interface BannerGoodsService extends BaseService<BannerGoodsDO> {
    /**
     * 获取banner信息
     * @param request
     * @return
     */
    Page<BannerGoodsDO> pageList(QueryBannerGoodsPageListRequest request);
}
