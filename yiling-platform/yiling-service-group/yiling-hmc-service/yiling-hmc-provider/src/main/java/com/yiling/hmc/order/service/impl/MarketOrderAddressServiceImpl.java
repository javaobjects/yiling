package com.yiling.hmc.order.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.dao.MarketOrderAddressMapper;
import com.yiling.hmc.order.dto.MarketOrderAddressDTO;
import com.yiling.hmc.order.entity.MarketOrderAddressDO;
import com.yiling.hmc.order.service.MarketOrderAddressService;

/**
 * <p>
 * HMC订单地址表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
@Service
public class MarketOrderAddressServiceImpl extends BaseServiceImpl<MarketOrderAddressMapper, MarketOrderAddressDO> implements MarketOrderAddressService {

    @Override
    public MarketOrderAddressDTO getAddressByOrderId(Long orderId) {
        LambdaQueryWrapper<MarketOrderAddressDO> wrapper = new LambdaQueryWrapper();
        wrapper.eq(MarketOrderAddressDO::getOrderId, orderId);
        return PojoUtils.map(this.getOne(wrapper), MarketOrderAddressDTO.class);
    }
}
