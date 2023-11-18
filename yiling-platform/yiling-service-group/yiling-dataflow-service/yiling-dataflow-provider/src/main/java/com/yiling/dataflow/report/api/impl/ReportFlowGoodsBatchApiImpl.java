package com.yiling.dataflow.report.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.report.api.ReportFlowGoodsBatchApi;
import com.yiling.dataflow.report.api.ReportFlowSaleApi;
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
import com.yiling.dataflow.report.service.ReportFlowGoodsBatchService;
import com.yiling.dataflow.report.service.ReportFlowSaleService;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@DubboService
public class ReportFlowGoodsBatchApiImpl implements ReportFlowGoodsBatchApi {

    @Autowired
    private ReportFlowGoodsBatchService reportFlowGoodsBatchService;


    @Override
    public Integer deleteReportFlowGoodsBatch(DeteleTimeRequest deteleTimeRequest) {
        return reportFlowGoodsBatchService.deleteReportFlowGoodsBatch(deteleTimeRequest);
    }

    @Override
    public List<ReportFlowGoodsBatchDTO> getReportFlowGoodsBatchList(QueryReportGoodsBatchRequest request) {
        return reportFlowGoodsBatchService.getReportFlowGoodsBatchList(request);
    }

    @Override
    public Integer saveReportFlowGoodsBatch(SaveReportFlowGoodsBatchRequest request) {
        return reportFlowGoodsBatchService.saveReportFlowGoodsBatch(request);
    }

    @Override
    public Integer saveReportFlowGoodsBatchList(List<SaveReportFlowGoodsBatchRequest> request) {
        return reportFlowGoodsBatchService.saveReportFlowGoodsBatchList(request);
    }

    @Override
    public Integer updateReportFlowGoodsBatchById(SaveReportFlowGoodsBatchRequest request) {
        return reportFlowGoodsBatchService.updateReportFlowGoodsBatchById(request);
    }

    @Override
    public List<ReportFlowBO> statisticsFlowGoodsBatchNumber(QueryStatisticsFlowRequest request) {
        return reportFlowGoodsBatchService.statisticsFlowGoodsBatchNumber(request);
    }

    @Override
    public List<ReportFlowStatisticsBO> statisticsFlowGoodsBatchNumberAndCount(QueryStatisticsFlowRequest request) {
        return reportFlowGoodsBatchService.statisticsFlowGoodsBatchNumberAndCount(request);
    }
}
