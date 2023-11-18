package com.yiling.dataflow.flow.service;

import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.entity.FlowSaleSummaryDayDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-10
 */
public interface FlowSaleSummaryDayService extends BaseService<FlowSaleSummaryDayDO> {

    /**
     * 匹配打标
     * @param request
     */
    void updateFlowSaleSummaryDayByDateTimeAndEid(QueryFlowSaleSummaryRequest request);

    /**
     * 最后打自然量和其它商业
     * @param request
     */
    void updateFlowSaleSummaryDayLingShouByTerminalCustomerType(QueryFlowSaleSummaryRequest request);

    /**
     * 最后打自然量和其它商业
     * @param request
     */
    void updateFlowSaleSummaryDayPifaByTerminalCustomerType(QueryFlowSaleSummaryRequest request);
}
