package com.yiling.hmc.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.hmc.order.api.OrderPrescriptionApi;
import com.yiling.hmc.order.dto.OrderPrescriptionDTO;
import com.yiling.hmc.order.service.OrderPrescriptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fan.shen
 * @date: 2022/4/11
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderPrescriptionImpl implements OrderPrescriptionApi {

    private final OrderPrescriptionService prescriptionService;

    @Override
    public OrderPrescriptionDTO getByOrderId(Long orderId) {
        return prescriptionService.getByOrderId(orderId);
    }
}
