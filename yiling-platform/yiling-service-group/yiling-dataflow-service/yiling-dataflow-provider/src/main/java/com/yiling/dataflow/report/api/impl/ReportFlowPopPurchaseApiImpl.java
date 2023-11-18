package com.yiling.dataflow.report.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.report.api.ReportFlowPopPurchaseApi;
import com.yiling.dataflow.report.dto.ReportFlowPopPurchaseDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportPopPurchaseRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPopPurchaseRequest;
import com.yiling.dataflow.report.service.ReportFlowPopPurchaseService;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@DubboService
public class ReportFlowPopPurchaseApiImpl implements ReportFlowPopPurchaseApi {

    @Autowired
    private ReportFlowPopPurchaseService reportFlowPopPurchaseService;

    @Override
    public Integer deleteReportFlowPopPurchase(DeteleTimeRequest deteleTimeRequest) {
        return reportFlowPopPurchaseService.deleteReportFlowPopPurchase(deteleTimeRequest);
    }

    @Override
    public List<ReportFlowPopPurchaseDTO> getReportFlowPopPurchaseList(QueryReportPopPurchaseRequest request) {
        return reportFlowPopPurchaseService.getReportFlowPopPurchaseList(request);
    }

    @Override
    public Integer saveReportFlowPopPurchase(SaveReportFlowPopPurchaseRequest request) {
        return reportFlowPopPurchaseService.saveReportFlowPopPurchase(request);
    }

    @Override
    public Integer saveReportFlowPopPurchaseList(List<SaveReportFlowPopPurchaseRequest> requestList) {
        return reportFlowPopPurchaseService.saveReportFlowPopPurchaseList(requestList);
    }

    @Override
    public Integer updateReportFlowPopPurchaseById(SaveReportFlowPopPurchaseRequest request) {
        return reportFlowPopPurchaseService.updateReportFlowPopPurchaseById(request);
    }

    @Override
    public List<ReportFlowPopPurchaseDTO> statisticsPopPurchase(QueryStatisticsFlowRequest request) {
        return reportFlowPopPurchaseService.statisticsPopPurchase(request);
    }


}
