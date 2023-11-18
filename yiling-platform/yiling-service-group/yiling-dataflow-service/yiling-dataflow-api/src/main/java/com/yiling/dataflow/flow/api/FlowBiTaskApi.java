package com.yiling.dataflow.flow.api;

import java.util.List;

import com.yiling.dataflow.flow.dto.FlowBiTaskDTO;

/**
 * @author fucheng.bai
 * @date 2022/8/5
 */
public interface FlowBiTaskApi {

    void saveBatch(List<FlowBiTaskDTO> flowBiTaskDTOList);

    Integer deleteByTaskTime(String taskTimeEnd, Long eid);
}
