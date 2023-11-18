package com.yiling.dataflow.report.api;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowGoodsBatchDTO;
import com.yiling.dataflow.report.dto.ReportFlowSaleDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportGoodsBatchRequest;
import com.yiling.dataflow.report.dto.request.QueryReportSaleRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowGoodsBatchRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowSaleRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
public interface ReportFlowGoodsBatchApi {
    /**
     * 删除统计数据
     * @return
     */
    Integer deleteReportFlowGoodsBatch(DeteleTimeRequest deteleTimeRequest);


    List<ReportFlowGoodsBatchDTO> getReportFlowGoodsBatchList(QueryReportGoodsBatchRequest request);
//
    Integer saveReportFlowGoodsBatch(SaveReportFlowGoodsBatchRequest request);

    Integer saveReportFlowGoodsBatchList(List<SaveReportFlowGoodsBatchRequest> request);
//
    Integer updateReportFlowGoodsBatchById(SaveReportFlowGoodsBatchRequest request);

    /**
     * 连花销售数量
     * @param request
     * @return
     */
    List<ReportFlowBO> statisticsFlowGoodsBatchNumber(QueryStatisticsFlowRequest request);

    List<ReportFlowStatisticsBO> statisticsFlowGoodsBatchNumberAndCount(QueryStatisticsFlowRequest request);
}
