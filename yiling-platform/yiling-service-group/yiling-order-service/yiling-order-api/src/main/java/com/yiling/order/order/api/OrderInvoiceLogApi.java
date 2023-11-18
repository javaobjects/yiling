package com.yiling.order.order.api;


import com.yiling.order.order.dto.request.SaveOrderInvoiceLogRequest;

/**
 * 发票状态修改
 * @author:wei.wang
 * @date:2021/7/28
 */
public interface OrderInvoiceLogApi {

    /**
     * 保存发票状态日志
     * @param orderInvoiceLog
     * @return
     */
    Boolean save(SaveOrderInvoiceLogRequest orderInvoiceLog);
}
