package com.yiling.mall.order.service;

import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-24
 */
public interface OrderSubmitService {

    /**
     * 创建订单
     *
     * @param request
     * @return
     */
    OrderSubmitBO submit(OrderSubmitRequest request);


    /**
     * 匹配订单类型
     * @param orderTypeEnum 订单类型
     * @param orderSourceEnum 订单来源
     * @return
     */
    boolean matchOrder(OrderTypeEnum orderTypeEnum, OrderSourceEnum orderSourceEnum);

}
