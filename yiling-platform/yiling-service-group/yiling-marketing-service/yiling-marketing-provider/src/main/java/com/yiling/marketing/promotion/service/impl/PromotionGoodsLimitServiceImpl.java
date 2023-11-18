package com.yiling.marketing.promotion.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.promotion.dao.PromotionGoodsLimitMapper;
import com.yiling.marketing.promotion.dto.request.ActivityGoodsPageRequest;
import com.yiling.marketing.promotion.entity.PromotionGoodsLimitDO;
import com.yiling.marketing.promotion.service.PromotionGoodsLimitService;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 促销活动商品表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Service
public class PromotionGoodsLimitServiceImpl extends BaseServiceImpl<PromotionGoodsLimitMapper, PromotionGoodsLimitDO> implements PromotionGoodsLimitService {

    @Override
    public List<PromotionGoodsLimitDO> queryByActivityId(Long promotionActivityId) {
        QueryWrapper<PromotionGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PromotionGoodsLimitDO::getPromotionActivityId, promotionActivityId);
        return this.list(wrapper);
    }

    @Override
    public List<PromotionGoodsLimitDO> queryByActivityIdList(List<Long> promotionActivityIdList) {
        QueryWrapper<PromotionGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionGoodsLimitDO::getPromotionActivityId, promotionActivityIdList);
        return this.list(wrapper);
    }

    @Override
    public Page<PromotionGoodsLimitDO> pageGoodsByActivityId(ActivityGoodsPageRequest request) {
        QueryWrapper<PromotionGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PromotionGoodsLimitDO::getPromotionActivityId, request.getId());
        wrapper.lambda().eq(PromotionGoodsLimitDO::getEid, request.getShopEid());

        if (StrUtil.isNotBlank(request.getGoodsName())) {
            wrapper.lambda().like(PromotionGoodsLimitDO::getGoodsName, request.getGoodsName());
        }

        Page<PromotionGoodsLimitDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.page(objectPage, wrapper);
    }

    @Override
    public List<PromotionGoodsLimitDO> queryByGoodsIdList(List<Long> goodsIdList) {
        QueryWrapper<PromotionGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionGoodsLimitDO::getGoodsId, goodsIdList);
        return this.list(wrapper);
    }

    @Override
    public boolean editGoods(List<PromotionGoodsLimitDO> goodsLimitDOS, Long promotionActivityId, Long opUserId, Date opTime) {
        List<PromotionGoodsLimitDO> promotionGoodsLimitDOS = this.queryByActivityId(promotionActivityId);
        if (null == promotionActivityId && promotionGoodsLimitDOS.size() < 1) {
            // 新增营销活动
            goodsLimitDOS.forEach(e -> e.setPromotionActivityId(promotionActivityId).setUpdateTime(opTime).setUpdateUser(opUserId).setCreateTime(opTime).setCreateUser(opUserId));
            return this.saveBatch(goodsLimitDOS);
        }
        List<Long> newLongList = goodsLimitDOS.stream().map(PromotionGoodsLimitDO::getGoodsId).collect(Collectors.toList());
        List<Long> oldLongList = promotionGoodsLimitDOS.stream().map(PromotionGoodsLimitDO::getGoodsId).collect(Collectors.toList());

        List<Long> addLongList = newLongList.stream().filter(item -> !oldLongList.contains(item)).collect(Collectors.toList());
        List<Long> deleteLongList = oldLongList.stream().filter(item -> !newLongList.contains(item)).collect(Collectors.toList());
        List<Long> editLongList = newLongList.stream().filter(item -> oldLongList.contains(item)).collect(Collectors.toList());

        Map<Long, PromotionGoodsLimitDO> newLimitDOMap = goodsLimitDOS.stream().collect(Collectors.toMap(PromotionGoodsLimitDO::getGoodsId, o -> o, (k1, k2) -> k1));
        Map<Long, PromotionGoodsLimitDO> oldLimitDOMap = promotionGoodsLimitDOS.stream().collect(Collectors.toMap(PromotionGoodsLimitDO::getGoodsId, o -> o, (k1, k2) -> k1));

        for (Long goodsId : addLongList) {
            // 修改营销活动，新增产品
            PromotionGoodsLimitDO goodsLimitDO = newLimitDOMap.get(goodsId);
            goodsLimitDO.setPackageTotalPrice(NumberUtil.round(NumberUtil.mul(goodsLimitDO.getPromotionPrice(),goodsLimitDO.getAllowBuyCount()),2));
            goodsLimitDO.setPromotionActivityId(promotionActivityId)
                    .setCreateUser(opUserId)
                    .setCreateTime(opTime)
                    .setUpdateUser(opUserId)
                    .setUpdateTime(opTime);
            this.save(goodsLimitDO);
        }
        for (Long goodsId : deleteLongList) {
            // 修改营销活动，删除其中产品
            PromotionGoodsLimitDO goodsLimitDO = oldLimitDOMap.get(goodsId);
            goodsLimitDO.setUpdateUser(opUserId)
                    .setUpdateTime(opTime);
            this.deleteByIdWithFill(goodsLimitDO);
        }
        for (Long goodsId : editLongList) {
            // 修改营销活动，编辑其中产品
            PromotionGoodsLimitDO goodsLimitDONew = newLimitDOMap.get(goodsId);
            PromotionGoodsLimitDO limitDO = new PromotionGoodsLimitDO();
            limitDO.setId(oldLimitDOMap.get(goodsId).getId());
            limitDO.setPrice(goodsLimitDONew.getPrice());
            limitDO.setPromotionPrice(goodsLimitDONew.getPromotionPrice());
            limitDO.setPromotionStock(goodsLimitDONew.getPromotionStock());
            limitDO.setAllowBuyCount(goodsLimitDONew.getAllowBuyCount());
            limitDO.setPackageTotalPrice(goodsLimitDONew.getPackageTotalPrice());
            limitDO.setGoodsSkuId(goodsLimitDONew.getGoodsSkuId());
            limitDO.setUpdateUser(opUserId);
            limitDO.setUpdateTime(opTime);
            limitDO.setPackageTotalPrice(NumberUtil.round(NumberUtil.mul(limitDO.getPromotionPrice(),limitDO.getAllowBuyCount()),2));
            this.updateById(limitDO);
        }
        return true;
    }

}
