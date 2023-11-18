package com.yiling.mall.recommend.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.recommend.dto.RecommendDTO;
import com.yiling.mall.recommend.dto.request.QueryRecommendPageListRequest;
import com.yiling.mall.recommend.dto.request.SaveRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendStatusRequest;

/**
 * Recommend Api
 *
 * @author: yuecheng.chen
 * @date: 2021/6/15
 */
public interface RecommendApi {
    /**
     * 查询Recommend分页列表
     *
     * @param request
     * @return
     */
    Page<RecommendDTO> pageList(QueryRecommendPageListRequest request);

    /**
     * 获取Recommend明细
     * @param recommendId  RecommendId
     * @return
     */
    RecommendDTO get(Long recommendId);

    /**
     * 创建Recommend
     *
     * @param request
     * @return
     */
    Boolean createRecommend(SaveRecommendRequest request);

    /**
     * 修改Recommend
     *
     * @param request
     * @return
     */
    Boolean updateRecommend(UpdateRecommendRequest request);

    /**
     * 修改状态
     * @param request
     * @return
     */
    Boolean updateRecommendStatus(UpdateRecommendStatusRequest request);
}
