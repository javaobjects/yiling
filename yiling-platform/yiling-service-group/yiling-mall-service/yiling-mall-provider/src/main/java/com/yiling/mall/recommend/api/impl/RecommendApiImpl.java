package com.yiling.mall.recommend.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.recommend.api.RecommendApi;
import com.yiling.mall.recommend.dto.RecommendDTO;
import com.yiling.mall.recommend.dto.request.QueryRecommendPageListRequest;
import com.yiling.mall.recommend.dto.request.SaveRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendStatusRequest;
import com.yiling.mall.recommend.entity.RecommendDO;
import com.yiling.mall.recommend.service.RecommendService;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/15 0015
 */
@DubboService
public class RecommendApiImpl implements RecommendApi {

    @Autowired
    private RecommendService recommendService;

    @Override
    public Page<RecommendDTO> pageList(QueryRecommendPageListRequest request) {
        Page<RecommendDO> page = recommendService.pageList(request);
        return PojoUtils.map(page, RecommendDTO.class);
    }

    @Override
    public RecommendDTO get(Long recommendId) {
        RecommendDO recommendDO = recommendService.get(recommendId);
        return PojoUtils.map(recommendDO, RecommendDTO.class);
    }

    @Override
    public Boolean createRecommend(SaveRecommendRequest request) {
        return recommendService.addRecommend(request);
    }

    @Override
    public Boolean updateRecommend(UpdateRecommendRequest request) {
        return recommendService.updateRecommend(request);
    }

    @Override
    public Boolean updateRecommendStatus(UpdateRecommendStatusRequest request) {
        return recommendService.updateRecommendStatus(request);
    }

}
