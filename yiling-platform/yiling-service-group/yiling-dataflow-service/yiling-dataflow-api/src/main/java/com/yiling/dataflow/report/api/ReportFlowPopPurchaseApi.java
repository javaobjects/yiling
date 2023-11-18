package com.yiling.dataflow.report.api;

import java.util.List;

import com.yiling.dataflow.report.dto.ReportFlowPopPurchaseDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportPopPurchaseRequest;
import com.yiling.dataflow.report.dto.request.QueryReportSaleRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPopPurchaseRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
public interface ReportFlowPopPurchaseApi {
    /**
     * 删除统计数据
     * @return
     */
    Integer deleteReportFlowPopPurchase(DeteleTimeRequest deteleTimeRequest);


    List<ReportFlowPopPurchaseDTO> getReportFlowPopPurchaseList(QueryReportPopPurchaseRequest request);

    Integer saveReportFlowPopPurchase(SaveReportFlowPopPurchaseRequest request);

    Integer saveReportFlowPopPurchaseList(List<SaveReportFlowPopPurchaseRequest> requestList);

    Integer updateReportFlowPopPurchaseById(SaveReportFlowPopPurchaseRequest request);

    /**
     * 连花销售数量
     * @param request
     * @return
     */
    List<ReportFlowPopPurchaseDTO> statisticsPopPurchase(QueryStatisticsFlowRequest request);

}
