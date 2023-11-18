package com.yiling.order.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.bo.CombinationBuyNumberBO;
import com.yiling.order.order.dao.OrderPromotionActivityMapper;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.request.SumOrderPromotionActivityRequest;
import com.yiling.order.order.entity.OrderPromotionActivityDO;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.order.order.service.OrderPromotionActivityService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 订单与促销活动关联表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-02-16
 */
@Service
public class OrderPromotionActivityServiceImpl extends BaseServiceImpl<OrderPromotionActivityMapper, OrderPromotionActivityDO> implements OrderPromotionActivityService {
    @Autowired
    private SpringAsyncConfig springAsyncConfig;

    @Override
    public List<OrderPromotionActivityDTO> listByOrderId(Long orderId, PromotionActivityTypeEnum activityTypeEnum) {

        QueryWrapper<OrderPromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderPromotionActivityDO :: getOrderId,orderId);
        wrapper.lambda().eq(OrderPromotionActivityDO :: getActivityType,activityTypeEnum.getCode());

        return PojoUtils.map(this.baseMapper.selectList(wrapper),OrderPromotionActivityDTO.class);
    }

    @Override
    public List<OrderPromotionActivityDTO> listByOrderId(Long orderId) {
        QueryWrapper<OrderPromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderPromotionActivityDO :: getOrderId,orderId);

        return PojoUtils.map(this.baseMapper.selectList(wrapper),OrderPromotionActivityDTO.class);
    }

    @Override
    public List<OrderPromotionActivityDTO> listByActivityType(Integer activityType) {
        QueryWrapper<OrderPromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderPromotionActivityDO :: getActivityType,activityType);
        return PojoUtils.map(list(wrapper),OrderPromotionActivityDTO.class);
    }

    @Override
    public CombinationBuyNumberBO sumCombinationActivityNumber(Long buyerEid, Long activityId) {
        CombinationBuyNumberBO bo = CombinationBuyNumberBO.builder().build();

        SumOrderPromotionActivityRequest request = new SumOrderPromotionActivityRequest();
        request.setActivityId(activityId);
        request.setActivityType(PromotionActivityTypeEnum.COMBINATION.getCode());

        // 查询总的购买数量
        CompletableFuture<Long>  sumQty =
                CompletableFuture
                .supplyAsync(() -> this.baseMapper.sumOrderPromotionActivity(request), springAsyncConfig.getAsyncExecutor())
                .whenComplete((result, throwable) -> bo.setSumQty(result));

        SumOrderPromotionActivityRequest request1 = new SumOrderPromotionActivityRequest();
        request1.setActivityId(activityId);
        request1.setActivityType(PromotionActivityTypeEnum.COMBINATION.getCode());
        request1.setBuyerEid(buyerEid);

        // 查询用户购买数量
        CompletableFuture<Long>  buyerQty =
        CompletableFuture
                .supplyAsync(() -> this.baseMapper.sumOrderPromotionActivity(request1), springAsyncConfig.getAsyncExecutor())
                .whenComplete((result, throwable) -> bo.setBuyerQty(result));

        SumOrderPromotionActivityRequest request2 = new SumOrderPromotionActivityRequest();
        request2.setActivityId(activityId);
        request2.setActivityType(PromotionActivityTypeEnum.COMBINATION.getCode());
        request2.setBuyerEid(buyerEid);
        request2.setStartTime(DateUtil.beginOfDay(new Date()));
        request2.setEndTime(DateUtil.endOfDay(new Date()));

        // 查询用户当天
        CompletableFuture<Long>  buyerDayQty =
        CompletableFuture
                .supplyAsync(() -> this.baseMapper.sumOrderPromotionActivity(request2), springAsyncConfig.getAsyncExecutor())
                .whenComplete((result, throwable) -> bo.setBuyerDayQty(result));

        CompletableFuture.allOf(sumQty, buyerQty,buyerDayQty).join();

        return bo;
    }

    @Override
    public Map<Long, Long> sumBatchCombinationActivityNumber(List<Long> activityIds) {

        QueryWrapper<OrderPromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderPromotionActivityDO :: getActivityId,activityIds);
        wrapper.lambda().in(OrderPromotionActivityDO :: getActivityType,PromotionActivityTypeEnum.COMBINATION.getCode());
        List<OrderPromotionActivityDO> orderPromotionActivityDOS = this.baseMapper.selectList(wrapper);

        if (CollectionUtil.isEmpty(orderPromotionActivityDOS)) {

            return MapUtil.empty();
        }

        return orderPromotionActivityDOS.stream().collect(Collectors.groupingBy(OrderPromotionActivityDO::getActivityId, Collectors.summingLong(OrderPromotionActivityDO::getActivityNum)));
    }
}
