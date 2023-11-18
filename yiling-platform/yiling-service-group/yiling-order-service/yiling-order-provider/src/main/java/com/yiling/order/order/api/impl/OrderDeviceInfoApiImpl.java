package com.yiling.order.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.order.api.OrderDeviceInfoApi;
import com.yiling.order.order.dto.request.CreateOrderDeviceInfoRequest;
import com.yiling.order.order.service.OrderDeviceInfoService;

/** 用户下单设备信息
 * @author zhigang.guo
 * @date: 2022/10/20
 */
@DubboService
public class OrderDeviceInfoApiImpl implements OrderDeviceInfoApi {

    @Autowired
    private OrderDeviceInfoService orderDeviceInfoService;


    @Override
    public boolean save(CreateOrderDeviceInfoRequest deviceInfoRequest) {


        return orderDeviceInfoService.save(deviceInfoRequest);
    }
}
