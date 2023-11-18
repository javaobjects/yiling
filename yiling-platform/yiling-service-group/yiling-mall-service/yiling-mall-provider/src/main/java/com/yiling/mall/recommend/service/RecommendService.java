package com.yiling.mall.recommend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.recommend.dto.request.QueryRecommendPageListRequest;
import com.yiling.mall.recommend.dto.request.SaveRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendStatusRequest;
import com.yiling.mall.recommend.entity.RecommendDO;

/**
 * <p>
 * 推荐表 服务类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
public interface RecommendService extends BaseService<RecommendDO> {
    /**
     * 查询推荐位分页列表
     *
     * @param request
     * @return
     */
    Page<RecommendDO> pageList(QueryRecommendPageListRequest request);

    /**
     * 获取推荐位信息
     * @param recommendId  recommendId
     * @return
     */
    RecommendDO get(Long recommendId);

    /**
     * 创建推荐位
     *
     * @param request
     * @return
     */
    Boolean addRecommend(SaveRecommendRequest request);

    /**
     * 修改推荐位
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
