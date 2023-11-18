package com.yiling.dataflow.statistics.service;

import java.util.List;

import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
public interface FlowStatisticsJobService {

    /**
     * 统计报表
     * @param erpClientDataDTOList
     */
    void statisticsFlowJob(List<ErpClientDataDTO> erpClientDataDTOList);



}
