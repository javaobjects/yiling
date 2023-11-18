package com.yiling.mall.recommend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.common.request.GoodsRequest;
import com.yiling.mall.recommend.dao.RecommendMapper;
import com.yiling.mall.recommend.dto.request.QueryRecommendPageListRequest;
import com.yiling.mall.recommend.dto.request.SaveRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendStatusRequest;
import com.yiling.mall.recommend.entity.RecommendDO;
import com.yiling.mall.recommend.entity.RecommendGoodsDO;
import com.yiling.mall.recommend.service.RecommendGoodsService;
import com.yiling.mall.recommend.service.RecommendService;

import cn.hutool.core.lang.Assert;

/**
 * <p>
 * 推荐表 服务实现类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RecommendServiceImpl extends BaseServiceImpl<RecommendMapper, RecommendDO> implements RecommendService {

    @Autowired
    private RecommendGoodsService recommendGoodsService;

    @Override
    public Page<RecommendDO> pageList(QueryRecommendPageListRequest request) {
        LambdaQueryWrapper<RecommendDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(request.getTitle())) {
            queryWrapper.like(RecommendDO::getTitle, request.getTitle());
        }
        if (request.getStatus() != null) {
            queryWrapper.eq(RecommendDO::getStatus, request.getStatus());
        }
        return this.baseMapper.selectPage(request.getPage(), queryWrapper);
    }

    @Override
    public RecommendDO get(Long recommendId) {
        Assert.notNull(recommendId, "获取recommend明细：recommendId为空！");
        // 此处不处理商品明细，商品明细单独分页查询接口
        return this.baseMapper.selectById(recommendId);
    }

    @Override
    public Boolean addRecommend(SaveRecommendRequest request) {
        Assert.notEmpty(request.getGoodsList(), "添加推荐位管理：推荐商品信息为空！");

        RecommendDO recommendDO = PojoUtils.map(request, RecommendDO.class);
        recommendDO.setStatus(EnableStatusEnum.ENABLED.getCode());
        this.baseMapper.insert(recommendDO);

        // 保存商品列表信息
        return recommendGoodsService.saveBatch(this.doRecommendGoodsDOList(recommendDO.getId(), request.getGoodsList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRecommend(UpdateRecommendRequest request) {
        RecommendDO recommendDO = PojoUtils.map(request, RecommendDO.class);
        Assert.notNull(recommendDO.getId(), "编辑保存推荐位：recommendId为空！");
        Assert.notEmpty(request.getGoodsList(), "编辑保存推荐位：推荐商品信息为空！");

        this.baseMapper.updateById(recommendDO);
        // 保存商品列表信息。先删除，再保存
        recommendGoodsService.remove(new LambdaQueryWrapper<RecommendGoodsDO>().eq(RecommendGoodsDO::getRecommendId, recommendDO.getId()));
        return recommendGoodsService.saveBatch(this.doRecommendGoodsDOList(recommendDO.getId(), request.getGoodsList()));
    }

    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateRecommendStatus(UpdateRecommendStatusRequest request){
        RecommendDO recommend = PojoUtils.map(request, RecommendDO.class);
        return updateById(recommend);
    }

    /**
     * 处理推荐商品明细DO
     *
     * @param recommendId      推荐位ID
     * @param goodsRequestList 推荐商品列表
     * @return
     */
    private List<RecommendGoodsDO> doRecommendGoodsDOList(Long recommendId, List<GoodsRequest> goodsRequestList) {
        List<GoodsRequest> goodsList = goodsRequestList.stream().distinct().collect(Collectors.toList());

        return goodsList.stream().map(goods ->
                new RecommendGoodsDO().setRecommendId(recommendId).setGoodsId(goods.getGoodsId()).setSort(goods.getSort())).collect(Collectors.toList());
    }

}
