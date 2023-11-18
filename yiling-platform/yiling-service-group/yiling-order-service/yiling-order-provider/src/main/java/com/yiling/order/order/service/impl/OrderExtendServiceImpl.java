package com.yiling.order.order.service.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderExtendMapper;
import com.yiling.order.order.dao.OrderFirstInfoMapper;
import com.yiling.order.order.dao.OrderMapper;
import com.yiling.order.order.dto.OrderB2BCountAmountDTO;
import com.yiling.order.order.dto.request.QueryB2BOrderCountRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderExtendDO;
import com.yiling.order.order.service.OrderExtendService;

/**
 * <p>
 * 订单字段扩展 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-02
 */
@Service
@CacheConfig(cacheNames = "order:extend")
public class OrderExtendServiceImpl extends BaseServiceImpl<OrderExtendMapper, OrderExtendDO> implements OrderExtendService {

    @Override
    @Cacheable(key="'+getOrderExtendInfo' + #orderId",unless= "#result == null")
    public OrderExtendDO getOrderExtendInfo(Long orderId) {

        QueryWrapper<OrderExtendDO> orderWrapper = new QueryWrapper<>();
        orderWrapper.lambda().eq(OrderExtendDO::getOrderId,orderId)
                .last("limit 1");

        return  this.getOne(orderWrapper);
    }
    @Override
    public OrderB2BCountAmountDTO getB2BCountAmount(QueryB2BOrderCountRequest request) {

        return this.baseMapper.getB2BCountAmount(request);
    }

    @Override
    public Integer getB2BCountQuantity(QueryB2BOrderCountRequest request) {
        return this.baseMapper.getB2BCountQuantity(request);
    }
}
