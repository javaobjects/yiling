package com.yiling.dataflow.report.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.report.api.ReportFlowSaleApi;
import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowSaleDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportSaleRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowSaleRequest;
import com.yiling.dataflow.report.service.ReportFlowSaleService;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@DubboService
public class ReportFlowSaleApiImpl implements ReportFlowSaleApi {

    @Autowired
    private ReportFlowSaleService reportFlowSaleService;

    @Override
    public Integer deleteReportFlowSale(DeteleTimeRequest deteleTimeRequest) {
        return reportFlowSaleService.deleteReportFlowSale(deteleTimeRequest);
    }

    @Override
    public List<ReportFlowSaleDTO> getReportFlowSaleList(QueryReportSaleRequest request) {
        return reportFlowSaleService.getReportFlowSaleList(request);
    }

    @Override
    public Integer saveReportFlowSale(SaveReportFlowSaleRequest request) {
        return reportFlowSaleService.saveReportFlowSale(request);
    }

    @Override
    public Integer saveReportFlowSaleList(List<SaveReportFlowSaleRequest> requestList) {
        return reportFlowSaleService.saveReportFlowSaleList(requestList);
    }

    @Override
    public Integer updateReportFlowSaleById(SaveReportFlowSaleRequest request) {
        return reportFlowSaleService.updateReportFlowSaleById(request);
    }

    @Override
    public List<ReportFlowBO> statisticsLhSaleAmount(QueryStatisticsFlowRequest request) {
        return reportFlowSaleService.statisticsLhSaleAmount(request);
    }

    @Override
    public List<ReportFlowBO> statisticsLhSaleNumber(QueryStatisticsFlowRequest request) {
        return reportFlowSaleService.statisticsLhSaleNumber(request);
    }

    @Override
    public List<ReportFlowStatisticsBO> statisticsLhAmountAndNumber(QueryStatisticsFlowRequest request) {
        return reportFlowSaleService.statisticsLhAmountAndNumber(request);
    }


}
