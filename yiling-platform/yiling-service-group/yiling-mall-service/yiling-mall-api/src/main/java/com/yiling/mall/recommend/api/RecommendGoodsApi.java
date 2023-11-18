package com.yiling.mall.recommend.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.recommend.dto.RecommendGoodsDTO;
import com.yiling.mall.recommend.dto.request.QueryRecommendGoodsPageListRequest;

/**
 * 推荐位商品 Api
 *
 * @author: yuecheng.chen
 * @date: 2021/6/15
 */
public interface RecommendGoodsApi {

    /**
     * 获取Recommend商品明细分页
     * @param request
     * @return
     */
    Page<RecommendGoodsDTO> pageList(QueryRecommendGoodsPageListRequest request);

    /**
     * 根据当前用户判断获取推荐商品列表。
     *
     * @param num       展示商品最大数量
     * @return
     */
    List<RecommendGoodsDTO> getStaffRecommendGoodsList(Integer num);

    /**
     * 获取智能推荐获取商品信息
     * @param recommendId
     * @return
     */
    List<RecommendGoodsDTO>  getRecommendGoodsBytRecommendId(Long recommendId);
}
