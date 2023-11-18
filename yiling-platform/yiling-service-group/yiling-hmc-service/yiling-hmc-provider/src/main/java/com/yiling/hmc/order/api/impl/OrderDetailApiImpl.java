package com.yiling.hmc.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.OrderDetailApi;
import com.yiling.hmc.order.dto.OrderDetailDTO;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.service.OrderDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderDetailApiImpl implements OrderDetailApi {

    private final OrderDetailService orderDetailService;

    @Override
    public OrderDetailDTO getById(Long id) {
        OrderDetailDO orderDetailDO = orderDetailService.getById(id);
        return PojoUtils.map(orderDetailDO, OrderDetailDTO.class);
    }

    @Override
    public List<OrderDetailDTO> listByOrderId(Long orderId) {
        List<OrderDetailDO> doList = orderDetailService.listByOrderId(orderId);
        return PojoUtils.map(doList, OrderDetailDTO.class);
    }
}
