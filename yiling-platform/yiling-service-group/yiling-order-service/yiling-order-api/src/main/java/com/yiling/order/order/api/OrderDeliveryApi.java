package com.yiling.order.order.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;

/**
 * 订单发货api查询
 * @author:wei.wang
 * @date:2021/6/22
 */
public interface OrderDeliveryApi {
    /**
     * 获取发货批次信息批量
     * @param orderId
     * @return
     */
    List<OrderDeliveryDTO> getOrderDeliveryList(Long orderId);

    /**
     * 条件获取发货批次信息批量
     * @param orderId
     * @param detailId
     * @return
     */
    List<OrderDeliveryDTO> getOrderDeliveryList(Long orderId,Long detailId);

    /**
     * 获取发货信息
     * @param orderIds
     * @return
     */
    List<OrderDeliveryDTO> listByOrderIds(List<Long> orderIds);

    /**
     * pop流向接口
     *
     * @param request
     * @return
     */
    Page<OrderFlowBO> getPageList(QueryOrderFlowRequest request);
}
