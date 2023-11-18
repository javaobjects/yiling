package com.yiling.hmc.order.api.impl;

import com.yiling.hmc.order.api.OrderPrescriptionGoodsApi;
import com.yiling.hmc.order.dto.OrderPrescriptionGoodsDTO;
import com.yiling.hmc.order.service.OrderPrescriptionGoodsService;
import com.yiling.hmc.wechat.dto.request.OrderPrescriptionGoodsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: fan.shen
 * @date: 2022/4/11
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderPrescriptionGoodsImpl implements OrderPrescriptionGoodsApi {

    @Autowired
    OrderPrescriptionGoodsService prescriptionGoodsService;

    @Override
    public List<OrderPrescriptionGoodsDTO> getByOrderId(Long orderId) {
        return prescriptionGoodsService.getByOrderId(orderId);
    }
}
