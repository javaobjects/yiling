package com.yiling.dataflow.report.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.ReportFlowGoodsBatchDTO;
import com.yiling.dataflow.report.dto.request.DeteleTimeRequest;
import com.yiling.dataflow.report.dto.request.QueryReportGoodsBatchRequest;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.dto.request.SaveReportFlowGoodsBatchRequest;
import com.yiling.dataflow.report.entity.ReportFlowGoodsBatchDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
public interface ReportFlowGoodsBatchService extends BaseService<ReportFlowGoodsBatchDO> {

    Integer deleteReportFlowGoodsBatch(DeteleTimeRequest deteleTimeRequest);

    List<ReportFlowBO> statisticsFlowGoodsBatchNumber(QueryStatisticsFlowRequest request);

    List<ReportFlowStatisticsBO> statisticsFlowGoodsBatchNumberAndCount(QueryStatisticsFlowRequest request);

    List<ReportFlowGoodsBatchDTO> getReportFlowGoodsBatchList(QueryReportGoodsBatchRequest request);

    //
    Integer saveReportFlowGoodsBatch(SaveReportFlowGoodsBatchRequest request);

    Integer saveReportFlowGoodsBatchList(List<SaveReportFlowGoodsBatchRequest> request);
    //
    Integer updateReportFlowGoodsBatchById(SaveReportFlowGoodsBatchRequest request);
}
