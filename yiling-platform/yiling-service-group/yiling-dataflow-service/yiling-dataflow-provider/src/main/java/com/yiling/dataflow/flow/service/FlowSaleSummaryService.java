package com.yiling.dataflow.flow.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.dto.request.UpdateFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.entity.FlowSaleSummaryDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-10
 */
public interface FlowSaleSummaryService extends BaseService<FlowSaleSummaryDO> {

    /**
     * 通过flowSaleId更新汇总表
     * @param flowSaleId
     */
    void updateFlowSaleSummaryByFlowSaleId(Long flowSaleId);

    /**
     * 通过时间或者eid更新汇总表信息
     * @param request
     */
    void updateFlowSaleSummaryByDateTimeAndEid(UpdateFlowSaleSummaryRequest request);

    /**
     * 更新总的汇总表
     * @param request
     */
    void updateFlowSaleSummaryDay(UpdateFlowSaleSummaryRequest request);

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowSaleSummaryDO> pageList(QueryFlowSaleSummaryRequest request);
}
