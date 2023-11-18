package com.yiling.dataflow.report.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.report.api.ReportFlowPurchaseApi;
import com.yiling.dataflow.report.api.ReportFlowSaleApi;
import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowPurchaseDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportPurchaseRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPurchaseRequest;
import com.yiling.dataflow.report.service.ReportFlowPurchaseService;
import com.yiling.dataflow.report.service.ReportFlowSaleService;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@DubboService
public class ReportFlowPurchaseApiImpl implements ReportFlowPurchaseApi {

    @Autowired
    private ReportFlowPurchaseService reportFlowPurchaseService;

    @Override
    public Integer deleteReportFlowPurchase(DeteleTimeRequest deteleTimeRequest) {
        return reportFlowPurchaseService.deleteReportFlowPurchase(deteleTimeRequest);
    }

    @Override
    public List<ReportFlowPurchaseDTO> getReportFlowPurchaseList(QueryReportPurchaseRequest request) {
        return reportFlowPurchaseService.getReportFlowPurchaseList(request);
    }

    @Override
    public Integer saveReportFlowPurchase(SaveReportFlowPurchaseRequest request) {
        return reportFlowPurchaseService.saveReportFlowPurchase(request);
    }

    @Override
    public Integer saveReportFlowPurchaseList(List<SaveReportFlowPurchaseRequest> request) {
        return reportFlowPurchaseService.saveReportFlowPurchaseList(request);
    }

    @Override
    public Integer updateReportFlowPurchaseById(SaveReportFlowPurchaseRequest request) {
        return reportFlowPurchaseService.updateReportFlowPurchaseById(request);
    }

    @Override
    public List<ReportFlowBO> statisticsLhPurchaseAmount(QueryStatisticsFlowRequest request) {
        return reportFlowPurchaseService.statisticsLhPurchaseAmount(request);
    }

    @Override
    public List<ReportFlowBO> statisticsLhPurchaseNumber(QueryStatisticsFlowRequest request) {
        return reportFlowPurchaseService.statisticsLhPurchaseNumber(request);
    }

    @Override
    public List<ReportFlowStatisticsBO> statisticsLhAmountAndNumber(QueryStatisticsFlowRequest request) {
        return reportFlowPurchaseService.statisticsLhAmountAndNumber(request);
    }


}
