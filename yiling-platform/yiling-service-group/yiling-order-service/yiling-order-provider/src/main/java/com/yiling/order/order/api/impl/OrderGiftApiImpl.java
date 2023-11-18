package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderGiftApi;
import com.yiling.order.order.dto.OrderGiftDTO;
import com.yiling.order.order.entity.OrderGiftDO;
import com.yiling.order.order.service.OrderGiftService;

/**
 * 订单赠品Api
 * @author:wei.wang
 * @date:2021/11/8
 */

@DubboService
public class OrderGiftApiImpl implements OrderGiftApi {

    @Autowired
    private OrderGiftService orderGiftService;

    /**
     * 根据订单id查询
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderGiftDTO> listByOrderId(Long orderId) {
        List<OrderGiftDO> orderGiftList = orderGiftService.listByOrderId(orderId);
        return PojoUtils.map(orderGiftList,OrderGiftDTO.class);
    }


    @Override
    public List<OrderGiftDTO> listByOrderId(Long orderId, Long activityId) {
        List<OrderGiftDO> orderGiftList = orderGiftService.listByOrderId(orderId,activityId);
        return PojoUtils.map(orderGiftList,OrderGiftDTO.class);
    }

    @Override
    public List<OrderGiftDTO> listByOrderIds(List<Long> orderIds) {
        List<OrderGiftDO> orderGiftList = orderGiftService.listByOrderIds(orderIds);
        return PojoUtils.map(orderGiftList,OrderGiftDTO.class);
    }
}
