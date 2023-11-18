package com.yiling.order.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.QueryAssistantFirstOrderRequest;
import com.yiling.order.order.entity.OrderDO;

/**
 * 订单ERP对接接口
 *
 * @author:wei.wang
 * @date:2021/8/3
 */
public interface OrderAssistantService {

    /**
     * 全部订单查询接口
     *
     * @param request
     * @return
     */
    Page<OrderDO> getPOPOrderList(OrderB2BPageRequest request);

    /**
     * 查询销售助手第一笔订单
     *
     * @return
     */
    OrderDO getFirstOrder(QueryAssistantFirstOrderRequest request);

}
