package com.yiling.order.order.api.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.bo.CombinationBuyNumberBO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.entity.OrderPromotionActivityDO;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.order.order.service.OrderPromotionActivityService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;

@DubboService
public class OrderPromotionActivityApiImpl implements OrderPromotionActivityApi {

    @Autowired
    OrderPromotionActivityService orderPromotionActivityService;

    @Override
    public List<OrderPromotionActivityDTO> listByOrderIds(List<Long> orderIds) {
        QueryWrapper<OrderPromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderPromotionActivityDO :: getOrderId,orderIds);
        return PojoUtils.map(orderPromotionActivityService.list(wrapper),OrderPromotionActivityDTO.class);
    }

    @Override
    public OrderPromotionActivityDTO getOneByOrderIds(Long orderId) {
        QueryWrapper<OrderPromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderPromotionActivityDO :: getOrderId,orderId).last(" limit 1");
        return PojoUtils.map(orderPromotionActivityService.getOne(wrapper),OrderPromotionActivityDTO.class);
    }


    @Override
    public List<OrderPromotionActivityDTO> listByActivityIds(List<Long> activityIds, PromotionActivityTypeEnum... promotionActivityTypeEnums) {

        Assert.notEmpty(activityIds,"活动Id不能为空");
        Assert.notEmpty(promotionActivityTypeEnums,"活动促销类型不能为空!");


        QueryWrapper<OrderPromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderPromotionActivityDO :: getActivityId,activityIds);
        wrapper.lambda().in(OrderPromotionActivityDO :: getActivityType, ListUtil.toList(promotionActivityTypeEnums).stream().map(t -> t.getCode()).collect(Collectors.toList()));

        return PojoUtils.map(orderPromotionActivityService.list(wrapper),OrderPromotionActivityDTO.class);
    }


    @Override
    public CombinationBuyNumberBO sumCombinationActivityNumber(Long buyerEid, Long activityId) {

        Assert.notNull(activityId,"活动Id不能为空");
        Assert.notNull(buyerEid,"登录人信息不能为空!");

        return orderPromotionActivityService.sumCombinationActivityNumber(buyerEid,activityId);
    }

    @Override
    public Map<Long,Long> sumBatchCombinationActivityNumber(List<Long> activityIds) {

        Assert.notEmpty(activityIds,"活动Id不能为空");

       return orderPromotionActivityService.sumBatchCombinationActivityNumber(activityIds);
    }
}
