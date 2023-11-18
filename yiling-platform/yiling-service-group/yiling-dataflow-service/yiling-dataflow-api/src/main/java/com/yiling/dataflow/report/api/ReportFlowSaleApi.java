package com.yiling.dataflow.report.api;

import java.util.List;

import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowSaleDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportSaleRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowSaleRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
public interface ReportFlowSaleApi {
    /**
     * 删除统计数据
     * @return
     */
    Integer deleteReportFlowSale(DeteleTimeRequest deteleTimeRequest);


    List<ReportFlowSaleDTO> getReportFlowSaleList(QueryReportSaleRequest request);

    Integer saveReportFlowSale(SaveReportFlowSaleRequest request);

    Integer saveReportFlowSaleList(List<SaveReportFlowSaleRequest> requestList);

    Integer updateReportFlowSaleById(SaveReportFlowSaleRequest request);

    /**
     * 连花销售额
     * @param request
     * @return
     */
    List<ReportFlowBO> statisticsLhSaleAmount(QueryStatisticsFlowRequest request);
    /**
     * 连花销售数量
     * @param request
     * @return
     */
    List<ReportFlowBO> statisticsLhSaleNumber(QueryStatisticsFlowRequest request);

    List<ReportFlowStatisticsBO> statisticsLhAmountAndNumber(QueryStatisticsFlowRequest request);

}
