package com.yiling.mall.order.service;

import java.util.List;

import com.yiling.framework.common.pojo.Result;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderConfirmRequest;
import com.yiling.mall.order.dto.request.PopOrderConfirmRequest;

/** 销售助手订单数据
 * @author zhigang.guo
 * @date: 2022/3/3
 */
public interface SaOrderService {

    /**
     * 客户确认订订单
     * @param orderConfirmRequest
     * @return
     */
    OrderSubmitBO b2bConfirmCustomerOrder(OrderConfirmRequest orderConfirmRequest);


    /**
     * POP确认订单
     * @param popOrderConfirmRequest
     * @return
     */
    Boolean popConfirmOrder(PopOrderConfirmRequest popOrderConfirmRequest);


    /**
     * 用户操作取消订单
     * @param orderNoList
     * @return
     */
    Result<Void> userCancelB2BOrder(List<String> orderNoList);
}
