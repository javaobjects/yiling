package com.yiling.mall.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.order.api.OrderFlowApi;
import com.yiling.mall.order.service.OrderFlowService;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/9/10
 */
@DubboService
public class OrderFlowApiImpl implements OrderFlowApi {

    @Autowired
    OrderFlowService orderFlowService;

    @Override
    public Page<OrderFlowBO> getPageList(QueryOrderFlowRequest request) {
        return orderFlowService.getPageList(request);
    }

}
