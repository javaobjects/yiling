package com.yiling.order.order.api.impl;


import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.order.api.OrderExportApi;
import com.yiling.order.order.dto.OrderExpectExportDTO;
import com.yiling.order.order.dto.OrderExportReportDTO;
import com.yiling.order.order.dto.OrderExportReportDetailDTO;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.dto.request.QueryOrderExportReportPageRequest;
import com.yiling.order.order.service.OrderService;

/**
 * 订单导出api
 * @author:wei.wang
 * @date:2021/8/3
 */
@DubboService
public class OrderExportApiImpl implements OrderExportApi {
    @Autowired
    private OrderService orderService;

    /**
     * 预订单列表导出
     * @param request
     * @return
     */
    @Override
    public List<OrderExpectExportDTO> orderExpectExport(QueryOrderExpectPageRequest request) {
        List<OrderExpectExportDTO> orderExpectList = orderService.orderExpectExport(request);
        return orderExpectList;
    }

    @Override
    public OrderExportReportDTO orderExportReport(QueryOrderExportReportPageRequest request) {
        OrderExportReportDTO orderExpectList = orderService.orderExportReport(request);
        return orderExpectList;
    }

}
