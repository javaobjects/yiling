package com.yiling.mall.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/9/10
 */
public interface OrderFlowService {

    /**
     * 获取以岭的发货单信息
     * @param request
     * @return
     */
    Page<OrderFlowBO> getPageList(QueryOrderFlowRequest request);
}
