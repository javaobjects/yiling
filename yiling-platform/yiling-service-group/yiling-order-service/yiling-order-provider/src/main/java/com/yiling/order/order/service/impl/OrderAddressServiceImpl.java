package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderAddressMapper;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.entity.OrderAddressDO;
import com.yiling.order.order.service.OrderAddressService;

/**
 * <p>
 * 订单收货地址 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Service
public class OrderAddressServiceImpl extends BaseServiceImpl<OrderAddressMapper, OrderAddressDO> implements OrderAddressService {

    /**
     * 根据orderId获取地址信息
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderAddressDO> getOrderAddressList(List<Long> orderIds) {
        QueryWrapper<OrderAddressDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderAddressDO ::  getOrderId,orderIds);
        return list(wrapper);
    }

    /**
     * 根据orderId获取地址信息
     *
     * @param orderIds
     * @return
     */
    @Override
    public OrderAddressDTO getOrderAddressInfo(Long orderIds) {
        QueryWrapper<OrderAddressDO> addressWrapper = new QueryWrapper<>();
        addressWrapper.lambda().eq(OrderAddressDO::getOrderId, orderIds);
        OrderAddressDO orderAddress = getOne(addressWrapper);
        OrderAddressDTO orderAddressDto = PojoUtils.map(orderAddress, OrderAddressDTO.class);
        return orderAddressDto;
    }

}
