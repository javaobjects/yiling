package com.yiling.hmc.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.OrderDetailControlApi;
import com.yiling.hmc.order.bo.OrderDetailControlBO;
import com.yiling.hmc.order.dto.OrderDetailControlDTO;
import com.yiling.hmc.order.service.OrderDetailControlService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/5/18
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderDetailControlApiImpl implements OrderDetailControlApi {

    private final OrderDetailControlService orderDetailControlService;

    @Override
    public List<OrderDetailControlDTO> listByOrderIdAndSellSpecificationsId(Long orderId, Long sellSpecificationsId) {
        return PojoUtils.map(orderDetailControlService.listByOrderIdAndSellSpecificationsId(orderId, sellSpecificationsId), OrderDetailControlDTO.class);
    }

    @Override
    public List<OrderDetailControlBO> listByOrderIdAndSellSpecificationsIdList(Long orderId, List<Long> sellSpecificationsIdList) {
        return orderDetailControlService.listByOrderIdAndSellSpecificationsIdList(orderId, sellSpecificationsIdList);
    }
}
