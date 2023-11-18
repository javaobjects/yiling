package com.yiling.order.order.api;

import com.yiling.order.order.dto.OrderB2BCountAmountDTO;
import com.yiling.order.order.dto.request.QueryB2BOrderCountRequest;
import com.yiling.order.order.dto.OrderExtendDTO;
import com.yiling.order.order.dto.request.CreateOrderExtendRequest;

/**
 * 订单扩展字段API
 * @author:wei.wang
 * @date:2021/8/3
 */
public interface OrderExtendApi {
    /**
     * 获取B2B订单报表金额信息
     * @param request
     * @return
     */
    OrderB2BCountAmountDTO getB2BCountAmount(QueryB2BOrderCountRequest request);

    /**
     * 获取B2B订单报表数量信息
     * @param request
     * @return
     */
    Integer getB2BCountQuantity( QueryB2BOrderCountRequest request);

    /**
     * 创建订单扩展属性
     * @param createOrderExtendRequest
     * @return
     */
    boolean save(CreateOrderExtendRequest createOrderExtendRequest);

    /**
     * 获取订单属性
     * @param orderId
     * @return
     */
    OrderExtendDTO getOrderExtendInfo(Long orderId);


}
