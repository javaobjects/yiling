package com.yiling.mall.recommend.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.recommend.api.RecommendGoodsApi;
import com.yiling.mall.recommend.dto.RecommendGoodsDTO;
import com.yiling.mall.recommend.dto.request.QueryRecommendGoodsPageListRequest;
import com.yiling.mall.recommend.service.RecommendGoodsService;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/15 0015
 */
@DubboService
public class RecommendGoodsApiImpl implements RecommendGoodsApi {

    @Autowired
    private RecommendGoodsService recommendGoodsService;

    @Override
    public Page<RecommendGoodsDTO> pageList(QueryRecommendGoodsPageListRequest request) {
        return PojoUtils.map(recommendGoodsService.pageList(request), RecommendGoodsDTO.class);
    }

    @Override
    public List<RecommendGoodsDTO> getStaffRecommendGoodsList(Integer num) {
        return PojoUtils.map(recommendGoodsService.queryRecommendGoodsList(num), RecommendGoodsDTO.class);
    }

    /**
     * 获取智能推荐获取商品信息
     *
     * @param recommendId
     * @return
     */
    @Override
    public List<RecommendGoodsDTO> getRecommendGoodsBytRecommendId(Long recommendId) {
        return recommendGoodsService.getRecommendGoodsBytRecommendId(recommendId);
    }
}
