package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderReturnDetailErpApi;
import com.yiling.order.order.dto.OrderReturnDetailErpDTO;
import com.yiling.order.order.entity.OrderReturnDetailErpDO;
import com.yiling.order.order.service.OrderReturnDetailErpService;

/**
 * @author: yong.zhang
 * @date: 2021/9/23
 */
@DubboService
public class OrderReturnDetailErpApiImpl implements OrderReturnDetailErpApi {
    @Autowired
    private OrderReturnDetailErpService orderReturnDetailErpService;

    @Override
    public List<OrderReturnDetailErpDTO> listByReturnIds(List<Long> returnIds) {
        List<OrderReturnDetailErpDO> orderReturnDetailErpDOList = orderReturnDetailErpService.listByReturnIds(returnIds);
        return PojoUtils.map(orderReturnDetailErpDOList, OrderReturnDetailErpDTO.class);
    }
}
