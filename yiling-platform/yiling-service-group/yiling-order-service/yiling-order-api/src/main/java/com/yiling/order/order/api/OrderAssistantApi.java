package com.yiling.order.order.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.QueryAssistantFirstOrderRequest;

/**
 * 丁单销售助手接口
 *
 * @author:wei.wang
 * @date:2021/8/3
 */
public interface OrderAssistantApi {

    /**
     * 全部订单查询接口
     *
     * @param request
     * @return
     */
    Page<OrderDTO> getPOPOrderList(OrderB2BPageRequest request);

    /**
     * 查询销售助手第一笔订单
     *
     * @return
     */
    OrderDTO getFirstOrder(QueryAssistantFirstOrderRequest request);
}
