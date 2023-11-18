package com.yiling.mall.recommend.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.recommend.bo.RecommendGoodsBO;
import com.yiling.mall.recommend.dto.RecommendGoodsDTO;
import com.yiling.mall.recommend.dto.request.QueryRecommendGoodsPageListRequest;
import com.yiling.mall.recommend.entity.RecommendGoodsDO;

/**
 * <p>
 * 推荐商品表 服务类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
public interface RecommendGoodsService extends BaseService<RecommendGoodsDO> {
    /**
     * 获取推荐位商品明细信息
     * @param request
     * @return
     */
    Page<RecommendGoodsDO> pageList(QueryRecommendGoodsPageListRequest request);

    /**
     * 查询推荐商品列表
     * @param num   限制个数
     * @return
     */
    List<RecommendGoodsBO> queryRecommendGoodsList(Integer num);

    /**
     * 获取智能推荐获取商品信息
     * @param recommendId
     * @return
     */
    List<RecommendGoodsDTO>  getRecommendGoodsBytRecommendId(Long recommendId);
}
