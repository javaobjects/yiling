package com.yiling.hmc.order.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.dto.MarketOrderAddressDTO;
import com.yiling.hmc.order.entity.MarketOrderAddressDO;

/**
 * <p>
 * HMC订单地址表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
public interface MarketOrderAddressService extends BaseService<MarketOrderAddressDO> {

    /**
     * 根据订单id获取订单收货地址
     *
     * @param orderId
     * @return
     */
    MarketOrderAddressDTO getAddressByOrderId(Long orderId);
}
