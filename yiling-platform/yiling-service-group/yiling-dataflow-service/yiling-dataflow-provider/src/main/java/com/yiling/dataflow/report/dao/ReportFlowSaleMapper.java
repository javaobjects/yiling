package com.yiling.dataflow.report.dao;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.report.bo.ReportFlowBO;
import com.yiling.dataflow.report.bo.ReportFlowStatisticsBO;
import com.yiling.dataflow.report.dto.request.QueryStatisticsFlowRequest;
import com.yiling.dataflow.report.entity.ReportFlowSaleDO;
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
public interface ReportFlowSaleMapper extends BaseMapper<ReportFlowSaleDO> {
    /**
     * 删除数据
     * @return
     */
    Integer deleteReportFlowSale(@Param("time") Date time);

    List<ReportFlowBO> statisticsLhSaleAmount(@Param("request") QueryStatisticsFlowRequest request);

    List<ReportFlowBO> statisticsLhSaleNumber(@Param("request") QueryStatisticsFlowRequest request);

    List<ReportFlowStatisticsBO> statisticsLhAmountAndNumber(@Param("request") QueryStatisticsFlowRequest request);

}
