package com.yiling.dataflow.report.dao;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
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
public interface ReportFlowPopPurchaseMapper extends BaseMapper<ReportFlowPopPurchaseDO> {

    Integer deleteReportFlowPopPurchase(@Param("time") Date time);

    List<ReportFlowPopPurchaseDO> statisticsPopPurchase(@Param("request") QueryStatisticsFlowRequest request);

}
