package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * 导出报表DTO
 */
@Data
public class OrderExportReportDTO implements java.io.Serializable {

    /**
     * 各个省份付款的数据
     */
    private List<OrderExportReportDetailDTO> orderPaymentReportList;

    /**
     * 各个省份商品发货数量-退货数量的数据
     */
    private List<OrderExportReportDetailDTO> orderQuantityReportList;



}
