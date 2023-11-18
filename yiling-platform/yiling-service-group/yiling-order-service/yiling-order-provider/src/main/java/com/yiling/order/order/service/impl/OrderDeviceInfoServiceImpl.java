package com.yiling.order.order.service.impl;

import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderDeviceInfoMapper;
import com.yiling.order.order.dto.request.CreateOrderDeviceInfoRequest;
import com.yiling.order.order.entity.OrderDeviceInfoDO;
import com.yiling.order.order.service.OrderDeviceInfoService;

/**
 * <p>
 * 订单-下单设备信息 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-10-20
 */
@Service
public class OrderDeviceInfoServiceImpl extends BaseServiceImpl<OrderDeviceInfoMapper, OrderDeviceInfoDO> implements OrderDeviceInfoService {

    @Override
    public boolean save(CreateOrderDeviceInfoRequest deviceInfoRequest) {

        OrderDeviceInfoDO orderDeviceInfoDO = PojoUtils.map(deviceInfoRequest,OrderDeviceInfoDO.class);

        return super.save(orderDeviceInfoDO);
    }
}
