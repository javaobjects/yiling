package com.yiling.order.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderFirstInfoMapper;
import com.yiling.order.order.dao.OrderMapper;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderFirstInfoDO;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.service.OrderFirstInfoService;

import cn.hutool.core.util.NumberUtil;
import io.jsonwebtoken.lang.Assert;

/**
 * <p>
 * 首单信息表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-05-30
 */
@Service
@CacheConfig(cacheNames = "order:first")
public class OrderFirstInfoServiceImpl extends BaseServiceImpl<OrderFirstInfoMapper, OrderFirstInfoDO> implements OrderFirstInfoService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Cacheable(key="#buyerEid  +'+queryOrderFirstInfo' + #orderType",unless= "#result == null")
    public OrderFirstInfoDO queryOrderFirstInfo(Long buyerEid, Integer orderType) {
        Assert.notNull(buyerEid, "参数buyerEid不能为空");
        Assert.notNull(orderType, "参数订单类型不能为空");

        QueryWrapper<OrderFirstInfoDO> orderWrapper = new QueryWrapper<>();
        orderWrapper.lambda().eq(OrderFirstInfoDO::getBuyerEid,buyerEid);
        orderWrapper.lambda().eq(OrderFirstInfoDO::getOrderType,orderType)
         .last("limit 1");

        return  this.getOne(orderWrapper);
    }

    @Override
    public Boolean saveFirstInfo(Long orderId, Long opUserId) {
       OrderDO orderDO = orderMapper.selectById(orderId);
       if (orderDO == null) {
           throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
       }
       OrderFirstInfoDO firstInfoDO = PojoUtils.map(orderDO,OrderFirstInfoDO.class);
       firstInfoDO.setDiscountAmount(NumberUtil.add(orderDO.getCashDiscountAmount(),orderDO.getCouponDiscountAmount(),orderDO.getPlatformCouponDiscountAmount(),orderDO.getTicketDiscountAmount()));
       firstInfoDO.setCreateUser(opUserId);
       firstInfoDO.setOrderId(orderDO.getId());
       firstInfoDO.setOrderTime(orderDO.getCreateTime());
       return this.save(firstInfoDO);
    }
}
