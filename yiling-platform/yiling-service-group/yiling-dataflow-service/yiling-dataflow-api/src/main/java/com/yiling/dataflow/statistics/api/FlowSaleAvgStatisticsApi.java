package com.yiling.dataflow.statistics.api;

import java.util.List;

/**
 * @author: shuang.zhang
 * @date: 2022/12/29
 */
public interface FlowSaleAvgStatisticsApi {

    /**
     * 需要处理的商业公司和商品规格
     */
    void saleAvg(List<Long> cEIds);
}
