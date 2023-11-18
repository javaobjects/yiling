package com.yiling.dataflow.statistics.service;

import java.util.List;

import com.yiling.dataflow.statistics.entity.AnalyseAvgSaleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author: shuang.zhang
 * @date: 2022/12/29
 */
public interface FlowSaleAvgStatisticsService {

    void saleAvg(List<Long> cEIds, List<Long> specIds);

}
