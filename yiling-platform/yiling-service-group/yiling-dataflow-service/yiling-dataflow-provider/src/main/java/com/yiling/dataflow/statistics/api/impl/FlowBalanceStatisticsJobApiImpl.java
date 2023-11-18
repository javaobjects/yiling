package com.yiling.dataflow.statistics.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.statistics.api.FlowBalanceStatisticsJobApi;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.dto.request.StatisticsFlowBalanceRequest;
import com.yiling.dataflow.statistics.service.FlowBalanceStatisticsJobService;

/**
 * @author: shuang.zhang
 * @date: 2022/7/11
 */
@DubboService
public class FlowBalanceStatisticsJobApiImpl implements FlowBalanceStatisticsJobApi {

    @Autowired
    private FlowBalanceStatisticsJobService flowBalanceStatisticsJobService;

    @Override
    public void statisticsFlowBalanceJob(StatisticsFlowBalanceRequest request, List<ErpClientDataDTO> erpClientDataDTOList) {
        flowBalanceStatisticsJobService.statisticsFlowBalanceJob(request, erpClientDataDTOList);
    }
}
