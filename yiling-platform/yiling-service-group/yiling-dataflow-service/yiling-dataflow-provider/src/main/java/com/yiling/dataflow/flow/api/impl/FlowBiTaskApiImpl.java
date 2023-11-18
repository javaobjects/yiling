package com.yiling.dataflow.flow.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.flow.api.FlowBiTaskApi;
import com.yiling.dataflow.flow.dto.FlowBiTaskDTO;
import com.yiling.dataflow.flow.entity.FlowBiTaskDO;
import com.yiling.dataflow.flow.service.FlowBiTaskService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/8/5
 */
@DubboService
@Slf4j
public class FlowBiTaskApiImpl implements FlowBiTaskApi {

    @Autowired
    private FlowBiTaskService flowBiTaskService;


    @Override
    public void saveBatch(List<FlowBiTaskDTO> flowBiTaskDTOList) {
        List<FlowBiTaskDO> flowBiTaskDOList = PojoUtils.map(flowBiTaskDTOList, FlowBiTaskDO.class);
        flowBiTaskDOList.forEach(f -> {
            Date opTime = f.getOpTime() != null ?  f.getOpTime() : new Date();
            f.setOpTime(opTime);
        });
        flowBiTaskService.saveBatch(flowBiTaskDOList);
    }

    @Override
    public Integer deleteByTaskTime(String taskTimeEnd, Long eid) {
        return flowBiTaskService.deleteByTaskTime(taskTimeEnd, eid);
    }
}
