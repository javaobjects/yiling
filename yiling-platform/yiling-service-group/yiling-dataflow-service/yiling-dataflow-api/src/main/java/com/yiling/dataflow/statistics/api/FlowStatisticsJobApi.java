package com.yiling.dataflow.statistics.api;

import java.util.List;

import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
public interface FlowStatisticsJobApi {
    /**
     * 每天生成最3个月的报表数据
     */
    void statisticsFlowJob(List<ErpClientDataDTO> erpClientDataDTOList);


}
