package com.yiling.dataflow.report.dao;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.entity.ReportFlowGoodsBatchDO;
import com.yiling.dataflow.report.entity.ReportFlowPopPurchaseDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
@Repository
public interface ReportFlowGoodsBatchMapper extends BaseMapper<ReportFlowGoodsBatchDO> {

    Integer deleteReportFlowGoodsBatch(@Param("time")Date time);

    List<ReportFlowBO> statisticsFlowGoodsBatchNumber(@Param("request") QueryStatisticsFlowRequest request);

    List<ReportFlowStatisticsBO> statisticsFlowGoodsBatchNumberAndCount(@Param("request") QueryStatisticsFlowRequest request);
}
