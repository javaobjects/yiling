package com.yiling.order.order.api;


import java.math.BigDecimal;
import java.util.List;

import com.yiling.order.order.dto.OrderExpectExportDTO;
import com.yiling.order.order.dto.OrderExportReportDTO;
import com.yiling.order.order.dto.OrderExportReportDetailDTO;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.dto.request.QueryOrderExportReportPageRequest;

/**
 * 导出api
 * @author:wei.wang
 * @date:2021/8/3
 */
public interface OrderExportApi {
    /**
     * 预订单列表导出
     * @param request
     * @return
     */
    List<OrderExpectExportDTO> orderExpectExport(QueryOrderExpectPageRequest request);

    /**
     * 导出报表接口
     * @param request
     * @return
     */
   OrderExportReportDTO orderExportReport(QueryOrderExportReportPageRequest request);


}
