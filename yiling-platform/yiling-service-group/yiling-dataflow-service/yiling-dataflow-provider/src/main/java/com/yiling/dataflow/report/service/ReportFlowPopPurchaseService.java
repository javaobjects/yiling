package com.yiling.dataflow.report.service;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.report.dto.ReportFlowPopPurchaseDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportPopPurchaseRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPopPurchaseRequest;
import com.yiling.dataflow.report.entity.ReportFlowPopPurchaseDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
public interface ReportFlowPopPurchaseService extends BaseService<ReportFlowPopPurchaseDO> {
    /**
     * 删除统计数据
     * @return
     */
    Integer deleteReportFlowPopPurchase(DeteleTimeRequest deteleTimeRequest);

    List<ReportFlowPopPurchaseDTO> statisticsPopPurchase(QueryStatisticsFlowRequest request);

    List<ReportFlowPopPurchaseDTO> getReportFlowPopPurchaseList(QueryReportPopPurchaseRequest request);

    Integer saveReportFlowPopPurchase(SaveReportFlowPopPurchaseRequest request);

    Integer saveReportFlowPopPurchaseList(List<SaveReportFlowPopPurchaseRequest> requestList);

    Integer updateReportFlowPopPurchaseById(SaveReportFlowPopPurchaseRequest request);
}
