package com.yiling.mall.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.pojo.Result;
import com.yiling.mall.order.api.SaOrderApi;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderConfirmRequest;
import com.yiling.mall.order.dto.request.PopOrderConfirmRequest;
import com.yiling.mall.order.service.SaOrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手订单业务
 * @author zhigang.guo
 * @date: 2022/3/3
 */
@DubboService
@Slf4j
public class SaOrderApiImpl implements SaOrderApi {

    @Autowired
    private SaOrderService saOrderService;


    /**
     * 客户确认订订单
     * @param orderConfirmRequest
     * @return
     */
    @Override
    public OrderSubmitBO b2bConfirmCustomerOrder(OrderConfirmRequest orderConfirmRequest) {

        return saOrderService.b2bConfirmCustomerOrder(orderConfirmRequest);
    }

    /**
     * 销售助手 pop订单确认
     * @param popOrderConfirmRequest
     * @return
     */
    @Override
    public Boolean popConfirmOrder(PopOrderConfirmRequest popOrderConfirmRequest) {

        return saOrderService.popConfirmOrder(popOrderConfirmRequest);
    }

    /**
     * 用户操作取消
     * @param orderNoList
     * @return
     */
    @Override
    public Result<Void> userCancelB2BOrder(List<String> orderNoList) {

        return saOrderService.userCancelB2BOrder(orderNoList);
    }
}
