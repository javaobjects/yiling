package com.yiling.settlement.report.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.api.ReportApi;
import com.yiling.settlement.report.dto.RebatedGoodsCountDTO;
import com.yiling.settlement.report.dto.ReportDTO;
import com.yiling.settlement.report.dto.ReportDetailB2bDTO;
import com.yiling.settlement.report.dto.ReportDetailFlowDTO;
import com.yiling.settlement.report.dto.ReportLogDTO;
import com.yiling.settlement.report.dto.ReportPurchaseStockOccupyDTO;
import com.yiling.settlement.report.dto.request.AdjustReportRequest;
import com.yiling.settlement.report.dto.request.ConfirmReportRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageByReportIdRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailFlowPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportGoodsRequest;
import com.yiling.settlement.report.dto.request.QueryReportPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.dto.request.RebateByReportRequest;
import com.yiling.settlement.report.dto.request.RejectReportRequest;
import com.yiling.settlement.report.entity.ReportDO;
import com.yiling.settlement.report.entity.ReportDetailB2bDO;
import com.yiling.settlement.report.entity.ReportDetailFlowDO;
import com.yiling.settlement.report.entity.ReportTaskDO;
import com.yiling.settlement.report.service.B2bOrderSyncService;
import com.yiling.settlement.report.service.LogService;
import com.yiling.settlement.report.service.MemberSyncService;
import com.yiling.settlement.report.service.ReportDetailB2bService;
import com.yiling.settlement.report.service.ReportDetailFlowService;
import com.yiling.settlement.report.service.ReportService;
import com.yiling.settlement.report.service.ReportTaskService;

import cn.hutool.core.collection.CollUtil;

/**
 * @author: dexi.yao
 * @date: 2022-05-20
 */
@DubboService
public class ReportApiImpl implements ReportApi {

    @Autowired
    B2bOrderSyncService b2bOrderSyncService;
    @Autowired
    MemberSyncService memberSyncService;
    @Autowired
    ReportService reportService;
    @Autowired
    ReportDetailB2bService detailB2bService;
    @Autowired
    ReportDetailFlowService reportDetailFlowService;
    @Autowired
    LogService logService;
    @Autowired
    ReportTaskService reportTaskService;


    @Override
    public Page<ReportDTO> queryReportPage(QueryReportPageRequest request) {
        return reportService.queryReportPage(request);
    }

    @Override
    public ReportDTO queryReportById(Long id) {
        ReportDO reportDO = reportService.getById(id);
        return PojoUtils.map(reportDO, ReportDTO.class);
    }

    @Override
    public Boolean queryMemberRefundIsAlert(Long reportId, Long eid) {
        return reportService.queryMemberRefundIsAlert(reportId, eid);
    }

    @Override
    public Boolean rejectReport(RejectReportRequest request) {
        return reportService.rejectReport(request);
    }

    @Override
    public Boolean confirm(ConfirmReportRequest request) {
        return reportService.confirm(request);
    }

    @Override
    public Boolean adjust(AdjustReportRequest request) {
        return reportService.adjust(request);
    }

    @Override
    public List<ReportLogDTO> queryLogList(Long reportId) {
        return logService.queryLogList(reportId);
    }

    @Override
    public Page<ReportDetailB2bDTO> queryReportDetailB2bPage(QueryReportDetailB2bPageRequest request) {
        return detailB2bService.queryReportDetailB2bPage(request);
    }

    @Override
    public ReportDetailB2bDTO queryB2bReportDetailById(Long id) {
        ReportDetailB2bDO detail = detailB2bService.getById(id);
        return PojoUtils.map(detail,ReportDetailB2bDTO.class);
    }

    @Override
    public ReportDetailFlowDTO queryFlowReportDetailById(Long id) {
        ReportDetailFlowDO detail = reportDetailFlowService.getById(id);
        return PojoUtils.map(detail,ReportDetailFlowDTO.class);
    }

    @Override
    public Page<ReportDetailB2bDTO> queryReportDetailB2bPageByReportId(QueryReportDetailB2bPageByReportIdRequest request) {
        return detailB2bService.queryReportDetailPageByReportIdList(request);
    }

    @Override
    public Page<ReportDetailFlowDTO> queryReportDetailFlowPage(QueryReportDetailFlowPageRequest request) {
        return reportDetailFlowService.queryReportDetailFlowPage(request);
    }

    @Override
    public Long queryB2bRebateOrderCount(QueryReportDetailB2bPageRequest request) {
        return detailB2bService.queryB2bRebateOrderCount(request);
    }

    @Override
    public Long queryFlowRebateOrderCount(QueryReportDetailFlowPageRequest request) {
        return reportDetailFlowService.queryFlowRebateOrderCount(request);
    }

    @Override
    public Boolean rebateByReport(RebateByReportRequest request) {
        return reportService.rebateByReport(request);
    }

    @Override
    public List<RebatedGoodsCountDTO> queryRebateCount(List<QueryStockOccupyPageRequest> request) {
        return reportService.queryRebateCount(request);
    }

    @Override
    public Page<ReportPurchaseStockOccupyDTO> queryStockOccupyPage(QueryStockOccupyPageRequest request) {
        return reportService.queryStockOccupyPage(request);
    }

    @Override
    public Boolean isExitReportByGoods(QueryReportGoodsRequest request) {
        return reportService.isExitReportByGoods(request);
    }

    @Override
    public Boolean isInProductionReportTask(Long eid) {
        List<ReportTaskDO> list = reportTaskService.queryInProductionTask(eid);
        return CollUtil.isNotEmpty(list);
    }
}
