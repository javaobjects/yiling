package com.yiling.marketing.promotion.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;
import com.yiling.marketing.promotion.dao.PromotionGoodsGiftLimitMapper;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftUsedDTO;
import com.yiling.marketing.promotion.dto.PromotionReduceStockDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftLimitSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;
import com.yiling.marketing.promotion.dto.request.PromotionReduceRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveRequest;
import com.yiling.marketing.promotion.entity.PromotionActivityDO;
import com.yiling.marketing.promotion.entity.PromotionGoodsGiftLimitDO;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.marketing.promotion.service.PromotionActivityService;
import com.yiling.marketing.promotion.service.PromotionGoodsGiftLimitService;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 促销活动赠品表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class PromotionGoodsGiftLimitServiceImpl extends BaseServiceImpl<PromotionGoodsGiftLimitMapper, PromotionGoodsGiftLimitDO>
                                                implements PromotionGoodsGiftLimitService {

    @Autowired
    private GoodsGiftService         goodsGiftService;

    @Autowired
    private PromotionActivityService promotionActivityService;

    @Override
    public boolean reducePromotion(PromotionReduceRequest request) {

        log.info("[reducePromotion]扣减库存，入参：{}", request);

        List<Long> activityIdList = request.getReduceStockList().stream().map(PromotionReduceStockDTO::getPromotionActivityId)
            .collect(Collectors.toList());

        List<PromotionActivityDO> activityList = promotionActivityService.listByIds(activityIdList);

        Map<Long, PromotionActivityDO> activityMap = activityList.stream()
            .collect(Collectors.toMap(PromotionActivityDO::getId, o -> o, (k1, k2) -> k1));

        Map<Long, Integer> result = new HashMap<>();
        int failCount = 0;
        for (PromotionReduceStockDTO item : request.getReduceStockList()) {
            Integer type = activityMap.get(item.getPromotionActivityId()).getType();
            PromotionTypeEnum promotion = PromotionTypeEnum.getByType(type);
            switch (promotion) {

                case FULL_GIFT:
                    int count = this.getBaseMapper().reducePromotionGoodsGift(item.getPromotionActivityId(), item.getGoodsGiftId(), 1);
                    if (count != 1) {
                        failCount += 1;
                        result.put(item.getPromotionActivityId(), count);
                    }
                    break;

                case SECOND_KILL:

                    break;

                case SPECIAL_PRICE:

                    break;
            }
            if (failCount > 0) {
                log.info("赠品扣减出现错误的数据为:[{}]", result);
            }
        }
        return true;
    }

    @Override
    public List<PromotionGoodsGiftLimitDO> listByActivityIdList(List<Long> promotionActivityIdList) {
        QueryWrapper<PromotionGoodsGiftLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionGoodsGiftLimitDO::getPromotionActivityId, promotionActivityIdList);
        return this.list(wrapper);
    }

    @Override
    public List<PromotionGoodsGiftLimitDO> queryByActivityId(Long promotionActivityId) {
        QueryWrapper<PromotionGoodsGiftLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PromotionGoodsGiftLimitDO::getPromotionActivityId, promotionActivityId);
        return this.list(wrapper);
    }

    @Override
    public List<PromotionGoodsGiftLimitDTO> queryByGiftIdList(List<Long> goodsGiftIdList) {
        QueryWrapper<PromotionGoodsGiftLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionGoodsGiftLimitDO::getGoodsGiftId, goodsGiftIdList);
        List<PromotionGoodsGiftLimitDO> doList = this.list(wrapper);
        return PojoUtils.map(doList, PromotionGoodsGiftLimitDTO.class);
    }

    @Override
    public boolean editGoodsGift(PromotionSaveRequest request) {
        // 运营端
        List<PromotionGoodsGiftLimitSaveRequest> goodsGiftLimit = request.getGoodsGiftLimit();

        List<PromotionGoodsGiftLimitDO> newGoodsGiftList = PojoUtils.map(goodsGiftLimit, PromotionGoodsGiftLimitDO.class);

        List<PromotionGoodsGiftLimitDO> oldGoodsGiftList = this.queryByActivityId(request.getId());

        List<PromotionGoodsGiftLimitDO> insertList = Lists.newArrayList();

        List<PromotionGoodsGiftLimitDO> updateList = Lists.newArrayList();

        List<PromotionGoodsGiftLimitDO> deleteList = Lists.newArrayList();

        if (CollUtil.isEmpty(oldGoodsGiftList)) {
            // 新增
            newGoodsGiftList.stream().forEach(item -> {
                item.setPromotionActivityId(request.getId());

                // 扣减库存
                goodsGiftService.deduct(item.getPromotionStock(), item.getGoodsGiftId());

            });
            insertList.addAll(newGoodsGiftList);
        } else {
            // 修改

            // 1、新 对比 旧 => 更新、新增
            newGoodsGiftList.stream().forEach(item -> {
                Optional<PromotionGoodsGiftLimitDO> first = oldGoodsGiftList.stream().filter(
                    old -> old.getPromotionAmount().compareTo(item.getPromotionAmount()) == 0 && old.getGoodsGiftId().equals(item.getGoodsGiftId())
                ).findFirst();
                Long goodsGiftId = item.getGoodsGiftId();
                // 不包含新的阶梯金额 -> 新增
                if (!first.isPresent()) {
                    goodsGiftService.deduct(item.getPromotionStock(), goodsGiftId);

                    PromotionGoodsGiftLimitDO insert = PojoUtils.map(item, PromotionGoodsGiftLimitDO.class);
                    insert.setPromotionActivityId(request.getId());
                    insertList.add(insert);
                } else {
                    // 包含阶梯金额 -> 修改
                    if (!item.getPromotionStock().equals(first.get().getPromotionStock())) {
                        if (item.getPromotionStock() > first.get().getPromotionStock()) {
                            goodsGiftService.deduct(item.getPromotionStock() - first.get().getPromotionStock(), goodsGiftId);
                        } else {
                            goodsGiftService.increase(first.get().getPromotionStock() - item.getPromotionStock(), item.getGoodsGiftId());
                        }
                    }
                    first.get().setPromotionAmount(item.getPromotionAmount());
                    first.get().setPromotionStock(item.getPromotionStock());
                    first.get().setOpUserId(request.getOpUserId());
                    first.get().setUpdateTime(request.getOpTime());
                    updateList.add(first.get());
                }
            });

            // 2、旧 对比 新 => 删除
            oldGoodsGiftList.stream().forEach(item -> {
                boolean exists = newGoodsGiftList.stream().noneMatch(
                        temp -> temp.getPromotionAmount().compareTo(item.getPromotionAmount()) == 0 && temp.getGoodsGiftId().equals(item.getGoodsGiftId())
                );
                if (exists) {

                    goodsGiftService.increase(item.getPromotionStock(), item.getGoodsGiftId());

                    deleteList.add(item);
                }
            });
        }

        if (CollUtil.isNotEmpty(insertList)) {
            this.saveBatch(insertList);
        }

        if (CollUtil.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }

        if (CollUtil.isNotEmpty(deleteList)) {
            PromotionGoodsGiftLimitDO deleteEntity = PromotionGoodsGiftLimitDO.builder().build();
            deleteEntity.setOpUserId(request.getOpUserId());
            QueryWrapper<PromotionGoodsGiftLimitDO> wrapper = new QueryWrapper<>();
            List<Long> idList = deleteList.stream().map(PromotionGoodsGiftLimitDO::getId).collect(Collectors.toList());
            wrapper.lambda().in(PromotionGoodsGiftLimitDO::getId, idList);
            this.batchDeleteWithFill(deleteEntity, wrapper);
        }

        return true;
    }

    /**
     * 根据促销活动查询
     *
     * @param promotionActivityIdList 促销活动id
     * @return
     */
    @Override
    public List<PromotionGoodsGiftLimitDO> listByActivityId(List<Long> promotionActivityIdList) {
        QueryWrapper<PromotionGoodsGiftLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionGoodsGiftLimitDO::getPromotionActivityId, promotionActivityIdList);
        return list(wrapper);
    }

    @Override
    public boolean copy(PromotionActivityStatusRequest request, Long promotionActivityId) {
        List<PromotionGoodsGiftLimitDO> goodsGiftLimitDO = this.queryByActivityId(request.getId());
        if (CollUtil.isEmpty(goodsGiftLimitDO)) {
            return true;
        }
        List<PromotionGoodsGiftLimitDO> list = goodsGiftLimitDO.stream().map(item -> {
            PromotionGoodsGiftLimitDO giftLimitDO = PromotionGoodsGiftLimitDO.builder().promotionActivityId(promotionActivityId)
                .promotionAmount(item.getPromotionAmount()).promotionStock(item.getPromotionStock()).createUser(request.getOpUserId())
                .createTime(request.getOpTime()).updateUser(request.getOpUserId()).updateTime(request.getOpTime()).build();
            return giftLimitDO;
        }).collect(Collectors.toList());
        return this.saveBatch(list);
    }

    @Override
    public Page<PromotionGoodsGiftUsedDTO> pageGiftOrder(PromotionGoodsGiftUsedRequest request) {
        Page<PromotionGoodsGiftLimitDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.getBaseMapper().pageGiftOrder(page, request);
    }

    @Override
    public boolean reduceActivityQuantity(Long id, Integer quantity, Long opUserId, Date opTime) {
        int count = this.getBaseMapper().reduceActivityGift(id, quantity, opUserId, opTime);
        return count > 0;
    }
}
