package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.entity.OrderAddressDO;

/**
 * <p>
 * 订单收货地址 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
public interface OrderAddressService extends BaseService<OrderAddressDO> {

    /**
     * 根据orderId获取地址信息
     * @param orderIds
     * @return
     */
    List<OrderAddressDO> getOrderAddressList(List<Long> orderIds);

    /**
     * 根据orderId获取地址信息
     * @param orderIds
     * @return
     */
    OrderAddressDTO getOrderAddressInfo(Long orderIds);
}
