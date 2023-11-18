package com.yiling.hmc.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.order.dao.OrderOperateMapper;
import com.yiling.hmc.order.entity.OrderOperateDO;
import com.yiling.hmc.order.enums.HmcOrderOperateTypeEnum;
import com.yiling.hmc.order.service.OrderOperateService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 订单操作表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-04-21
 */
@Service
public class OrderOperateServiceImpl extends BaseServiceImpl<OrderOperateMapper, OrderOperateDO> implements OrderOperateService {

    @Override
    public OrderOperateDO getLastOperate(Long orderId) {
        QueryWrapper<OrderOperateDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderOperateDO::getOrderId, orderId);
        wrapper.lambda().orderByDesc(OrderOperateDO::getCreateTime);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<OrderOperateDO> listByOrderIdAndTypeList(Long orderId, List<Integer> operateTypeList) {
        QueryWrapper<OrderOperateDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderOperateDO::getOrderId, orderId);
        if (CollUtil.isNotEmpty(operateTypeList)) {
            wrapper.lambda().in(OrderOperateDO::getOperateType, operateTypeList);
        }
        wrapper.lambda().orderByDesc(OrderOperateDO::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public boolean saveOrderOperate(Long orderId, HmcOrderOperateTypeEnum hmcOrderOperateTypeEnum, String content, Long opUserId, Date opTime) {
        OrderOperateDO orderOperateDO = new OrderOperateDO();
        orderOperateDO.setOrderId(orderId);
        orderOperateDO.setOperateUserId(opUserId);
        orderOperateDO.setOperateTime(opTime);
        orderOperateDO.setOperateType(hmcOrderOperateTypeEnum.getCode());
        orderOperateDO.setContent(content);
        orderOperateDO.setOpUserId(opUserId);
        orderOperateDO.setOpTime(opTime);
        return this.save(orderOperateDO);
    }
}
