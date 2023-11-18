package com.yiling.order.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderInvoiceLogApi;
import com.yiling.order.order.dto.request.SaveOrderInvoiceLogRequest;
import com.yiling.order.order.entity.OrderInvoiceLogDO;
import com.yiling.order.order.service.OrderInvoiceLogService;

/**
 * 订单发票日志Api
 * @author:wei.wang
 * @date:2021/7/28
 */
@DubboService
public class OrderInvoiceLogApiImpl implements OrderInvoiceLogApi {
    @Autowired
    private OrderInvoiceLogService orderInvoiceLogService;
    /**
     * 保存发票状态日志
     *
     * @param orderInvoiceLog
     * @return
     */
    @Override
    public Boolean save(SaveOrderInvoiceLogRequest orderInvoiceLog) {
        OrderInvoiceLogDO result = PojoUtils.map(orderInvoiceLog, OrderInvoiceLogDO.class);
        return orderInvoiceLogService.save(result);
    }
}
