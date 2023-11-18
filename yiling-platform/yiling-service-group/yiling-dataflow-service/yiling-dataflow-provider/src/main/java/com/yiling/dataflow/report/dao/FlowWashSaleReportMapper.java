package com.yiling.dataflow.report.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.report.dto.request.QueryFlowWashSaleReportRequest;
import com.yiling.dataflow.report.entity.FlowWashSaleReportDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向销售合并报表 Dao 接口
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-01
 */
@Repository
public interface FlowWashSaleReportMapper extends BaseMapper<FlowWashSaleReportDO> {

    /**
     * 根据年月查询经销商crmId
     * @return
     */
    List<Long> listCrmIdByCondition(@Param("request") QueryFlowWashSaleReportRequest request);

    /**
     * 根据年月日查询机构crmId
     * @param request
     * @return
     */
    List<Long> listCustomerCrmIdByCondition(@Param("request") QueryFlowWashSaleReportRequest request);
}
