package com.yiling.mall.order.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;

/**
 * 流向模块
 * @author: shuang.zhang
 * @date: 2021/9/10
 */
public interface OrderFlowApi {

    /**
     * 以岭流向数据
     * @param request
     * @return
     */
    Page<OrderFlowBO> getPageList(QueryOrderFlowRequest request);
}
