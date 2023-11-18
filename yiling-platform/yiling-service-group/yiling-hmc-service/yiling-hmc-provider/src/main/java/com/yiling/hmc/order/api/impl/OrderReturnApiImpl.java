package com.yiling.hmc.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.OrderReturnApi;
import com.yiling.hmc.order.dto.OrderReturnDTO;
import com.yiling.hmc.order.dto.request.OrderReturnApplyRequest;
import com.yiling.hmc.order.dto.request.OrderReturnPageRequest;
import com.yiling.hmc.order.entity.OrderReturnDO;
import com.yiling.hmc.order.service.OrderReturnService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderReturnApiImpl implements OrderReturnApi {

    private final OrderReturnService orderReturnService;

    @Override
    public OrderReturnDTO queryById(Long returnId) {
        OrderReturnDO orderReturnDO = orderReturnService.getById(returnId);
        return PojoUtils.map(orderReturnDO, OrderReturnDTO.class);
    }

    @Override
    public OrderReturnDTO queryByOrderId(Long orderId) {
        OrderReturnDO orderReturnDO = orderReturnService.queryByOrderId(orderId);
        return PojoUtils.map(orderReturnDO, OrderReturnDTO.class);
    }

    @Override
    public Result apply(OrderReturnApplyRequest request) {
        return orderReturnService.apply(request);
    }

    @Override
    public Page<OrderReturnDTO> pageList(OrderReturnPageRequest request) {
        Page<OrderReturnDO> doPage = orderReturnService.pageList(request);
        return PojoUtils.map(doPage, OrderReturnDTO.class);
    }
}
