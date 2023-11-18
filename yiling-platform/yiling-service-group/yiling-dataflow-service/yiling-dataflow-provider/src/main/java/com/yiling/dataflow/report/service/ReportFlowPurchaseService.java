package com.yiling.dataflow.report.service;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowPurchaseDTO;
import com.yiling.dataflow.report.dto.ReportFlowSaleDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportPurchaseRequest;
import com.yiling.dataflow.report.dto.request.QueryReportSaleRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowPurchaseRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowSaleRequest;
import com.yiling.dataflow.report.entity.ReportFlowPurchaseDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
public interface ReportFlowPurchaseService extends BaseService<ReportFlowPurchaseDO> {

    /**
     * 删除统计数据
     * @return
     */
    Integer deleteReportFlowPurchase(DeteleTimeRequest deteleTimeRequest);

    List<ReportFlowPurchaseDTO> getReportFlowPurchaseList(QueryReportPurchaseRequest request);

    Integer saveReportFlowPurchase(SaveReportFlowPurchaseRequest request);

    Integer saveReportFlowPurchaseList(List<SaveReportFlowPurchaseRequest> request);

    Integer updateReportFlowPurchaseById(SaveReportFlowPurchaseRequest request);

    /**
     * 连花销售额
     * @param request
     * @return
     */
    List<ReportFlowBO> statisticsLhPurchaseAmount(QueryStatisticsFlowRequest request);

    /**
     * 连花销售数量
     * @param request
     * @return
     */
    List<ReportFlowBO> statisticsLhPurchaseNumber(QueryStatisticsFlowRequest request);

    List<ReportFlowStatisticsBO> statisticsLhAmountAndNumber(QueryStatisticsFlowRequest request);

}
