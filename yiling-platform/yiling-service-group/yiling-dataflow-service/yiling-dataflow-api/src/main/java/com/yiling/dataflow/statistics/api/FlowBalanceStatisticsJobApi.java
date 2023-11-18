package com.yiling.dataflow.statistics.api;

import java.util.List;

import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.dto.request.StatisticsFlowBalanceRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/7/11
 */
public interface FlowBalanceStatisticsJobApi {

    /**
     * 统计每一个商业公司每天的流向平衡数据
     *
     */
    void statisticsFlowBalanceJob(StatisticsFlowBalanceRequest request, List<ErpClientDataDTO> erpClientDataDTOList);
}
