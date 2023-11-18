package com.yiling.hmc.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.order.bo.OrderDetailControlBO;
import com.yiling.hmc.order.dao.OrderDetailControlMapper;
import com.yiling.hmc.order.entity.OrderDetailControlDO;
import com.yiling.hmc.order.service.OrderDetailControlService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 订单明细管控表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-18
 */
@Service
public class OrderDetailControlServiceImpl extends BaseServiceImpl<OrderDetailControlMapper, OrderDetailControlDO> implements OrderDetailControlService {

    @Override
    public List<OrderDetailControlDO> listByOrderIdAndSellSpecificationsId(Long orderId, Long sellSpecificationsId) {
        QueryWrapper<OrderDetailControlDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDetailControlDO::getOrderId, orderId);
        wrapper.lambda().eq(OrderDetailControlDO::getSellSpecificationsId, sellSpecificationsId);
        return this.list(wrapper);
    }

    @Override
    public List<OrderDetailControlBO> listByOrderIdAndSellSpecificationsIdList(Long orderId, List<Long> sellSpecificationsIdList) {
        List<OrderDetailControlBO> controlBOList = new ArrayList<>();
        QueryWrapper<OrderDetailControlDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDetailControlDO::getOrderId, orderId);
        if (CollUtil.isNotEmpty(sellSpecificationsIdList)) {
            wrapper.lambda().in(OrderDetailControlDO::getSellSpecificationsId, sellSpecificationsIdList);
        }
        List<OrderDetailControlDO> orderDetailControlDOList = this.list(wrapper);
        if (CollUtil.isEmpty(orderDetailControlDOList)) {
            return controlBOList;
        }

        Map<Long, List<OrderDetailControlDO>> longListMap = orderDetailControlDOList.stream().collect(Collectors.groupingBy(OrderDetailControlDO::getSellSpecificationsId));
        longListMap.forEach((sellSpecificationsId, doList) -> {
            if (CollUtil.isNotEmpty(doList)) {
                OrderDetailControlBO controlBO = new OrderDetailControlBO();
                List<Long> eidList = doList.stream().map(OrderDetailControlDO::getEid).collect(Collectors.toList());
                controlBO.setOrderId(orderId);
                controlBO.setSellSpecificationsId(sellSpecificationsId);
                controlBO.setEidList(eidList);
                controlBOList.add(controlBO);
            }
        });
        return controlBOList;
    }
}
