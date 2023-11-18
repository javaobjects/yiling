package com.yiling.order.order.api.impl;

import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderAssistantApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.QueryAssistantFirstOrderRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.service.OrderAssistantService;

/**
 * 订单销售助手api实现
 *
 * @author:wei.wang
 * @date:2021/9/22
 */

@DubboService
public class OrderAssistantApiImpl implements OrderAssistantApi {
    @Autowired
    OrderAssistantService orderAssistantService;


    /**
     * 全部订单查询接口
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getPOPOrderList(OrderB2BPageRequest request) {

        return PojoUtils.map(orderAssistantService.getPOPOrderList(request), OrderDTO.class);
    }


    @Override
    public OrderDTO getFirstOrder(QueryAssistantFirstOrderRequest request) {
        OrderDTO orderDTO = new OrderDTO();
        OrderDO firstOrder = orderAssistantService.getFirstOrder(request);
        if(Objects.isNull(firstOrder)){
            return new OrderDTO();
        }
        PojoUtils.map(firstOrder, orderDTO);
        return orderDTO;
    }
}
