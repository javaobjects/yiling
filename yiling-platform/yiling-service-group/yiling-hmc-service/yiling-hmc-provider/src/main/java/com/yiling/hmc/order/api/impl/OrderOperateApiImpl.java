package com.yiling.hmc.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.OrderOperateApi;
import com.yiling.hmc.order.dto.OrderOperateDTO;
import com.yiling.hmc.order.entity.OrderOperateDO;
import com.yiling.hmc.order.service.OrderOperateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/4/21
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderOperateApiImpl implements OrderOperateApi {

    private final OrderOperateService orderOperateService;

    @Override
    public OrderOperateDTO getLastOperate(Long orderId) {
        OrderOperateDO orderOperateDO = orderOperateService.getLastOperate(orderId);
        return PojoUtils.map(orderOperateDO, OrderOperateDTO.class);
    }

    @Override
    public List<OrderOperateDTO> listByOrderIdAndTypeList(Long orderId, List<Integer> operateTypeList) {
        List<OrderOperateDO> doList = orderOperateService.listByOrderIdAndTypeList(orderId, operateTypeList);
        return PojoUtils.map(doList, OrderOperateDTO.class);
    }

}
