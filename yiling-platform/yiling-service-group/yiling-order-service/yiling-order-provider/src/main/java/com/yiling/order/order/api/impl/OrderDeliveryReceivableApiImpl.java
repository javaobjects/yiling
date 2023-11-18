package com.yiling.order.order.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDeliveryReceivableApi;
import com.yiling.order.order.dto.OrderDeliveryReceivableDTO;
import com.yiling.order.order.entity.OrderDeliveryReceivableDO;
import com.yiling.order.order.service.OrderDeliveryReceivableService;

/**
 * 单出库单和应收单关联关系api实现
 * @author:wei.wang
 * @date:2021/9/23
 */
@DubboService
public class OrderDeliveryReceivableApiImpl implements OrderDeliveryReceivableApi {

    @Autowired
    private OrderDeliveryReceivableService orderDeliveryReceivableService;
    /**
     * 根据订单id查询所有
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderDeliveryReceivableDTO> listByOrderIds(List<Long> orderIds) {
        List<OrderDeliveryReceivableDO> receivableList = orderDeliveryReceivableService.listByOrderIds(orderIds);
        return PojoUtils.map(receivableList,OrderDeliveryReceivableDTO.class);
    }

    /**
     * 根据应收单号获取出库单号
     *
     * @param erpReceivableNo 应收单号
     * @return
     */
    @Override
    public String getDeliveryNo(String erpReceivableNo) {
        List<OrderDeliveryReceivableDO> deliveryList = orderDeliveryReceivableService.getDeliveryNo(erpReceivableNo);
        List<String> collect = deliveryList.stream().map(order -> order.getErpDeliveryNo()).collect(Collectors.toList());
        String deliveryNo = String.join(",", collect);
        return deliveryNo;
    }
}
