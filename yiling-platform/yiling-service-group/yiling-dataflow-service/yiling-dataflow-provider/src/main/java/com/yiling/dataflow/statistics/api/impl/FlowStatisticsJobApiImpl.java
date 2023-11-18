package com.yiling.dataflow.statistics.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.statistics.api.FlowStatisticsJobApi;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.service.FlowStatisticsJobService;


/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@DubboService
public class FlowStatisticsJobApiImpl implements FlowStatisticsJobApi {

    @Autowired
    private FlowStatisticsJobService flowStatisticsJobService;

    @Override
    public void statisticsFlowJob(List<ErpClientDataDTO> erpClientDataDTOList) {
        flowStatisticsJobService.statisticsFlowJob(erpClientDataDTOList);
    }

}
