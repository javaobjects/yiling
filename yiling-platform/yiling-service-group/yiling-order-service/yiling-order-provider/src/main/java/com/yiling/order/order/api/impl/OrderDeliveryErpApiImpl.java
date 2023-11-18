package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDeliveryErpApi;
import com.yiling.order.order.dto.OrderDeliveryErpDTO;
import com.yiling.order.order.entity.OrderDeliveryErpDO;
import com.yiling.order.order.service.OrderDeliveryErpService;

/**
 * 订单ERP出库单信息Api实现
 * @author:wei.wang
 * @date:2021/9/23
 */
@DubboService
public class OrderDeliveryErpApiImpl implements OrderDeliveryErpApi {

    @Autowired
    private OrderDeliveryErpService orderDeliveryErpService;
    /**
     * 根据订单id查询所有
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderDeliveryErpDTO> listByOrderIds(List<Long> orderIds) {
        List<OrderDeliveryErpDO> orderDeliveryErpList = orderDeliveryErpService.listByOrderIds(orderIds);
        return PojoUtils.map(orderDeliveryErpList,OrderDeliveryErpDTO.class);
    }

    /**
     * 根据订单查询
     *
     * @param detailId
     * @param erpDeliveryNo
     * @return
     */
    @Override
    public List<OrderDeliveryErpDTO> listByDetailIdAndNo(Long detailId, String erpDeliveryNo) {
        List<OrderDeliveryErpDO> orderDeliveryErpList = orderDeliveryErpService.listByDetailIdAndNo(detailId, erpDeliveryNo);
        return PojoUtils.map(orderDeliveryErpList,OrderDeliveryErpDTO.class);
    }
}
