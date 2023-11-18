package com.yiling.dataflow.flow.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.flow.api.FlowMonthBiTaskApi;
import com.yiling.dataflow.flow.dto.FlowCrmEnterpriseDTO;
import com.yiling.dataflow.flow.dto.FlowMonthBiTaskDTO;
import com.yiling.dataflow.flow.dto.request.FlowMonthBiTaskRequest;
import com.yiling.dataflow.flow.entity.FlowBiTaskDO;
import com.yiling.dataflow.flow.entity.FlowMonthBiTaskDO;
import com.yiling.dataflow.flow.service.FlowMonthBiTaskService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowMonthBiTaskApiImpl
 * @描述
 * @创建时间 2022/7/11
 * @修改人 shichen
 * @修改时间 2022/7/11
 **/
@DubboService
@Slf4j
public class FlowMonthBiTaskApiImpl implements FlowMonthBiTaskApi {

    @Autowired
    private FlowMonthBiTaskService flowMonthBiTaskService;

    @Override
    public HashMap<String, List> getAllFlowMonthBiData(FlowMonthBiTaskRequest request) {
        return flowMonthBiTaskService.getAllFlowMonthBiData(request);
    }

    @Override
    public FlowCrmEnterpriseDTO getCrmEnterpriseInfo(FlowMonthBiTaskRequest request) {
        return flowMonthBiTaskService.getCrmEnterpriseInfo(request);
    }

    @Override
    public void saveBatch(List<FlowMonthBiTaskDTO> flowMonthBiTaskDTOList) {
        List<FlowMonthBiTaskDO> flowMonthBiTaskDOList = PojoUtils.map(flowMonthBiTaskDTOList, FlowMonthBiTaskDO.class);
        flowMonthBiTaskDOList.forEach(f -> {
            Date opTime = f.getOpTime() != null ?  f.getOpTime() : new Date();
            f.setOpTime(opTime);
        });
        flowMonthBiTaskService.saveBatch(flowMonthBiTaskDOList);
    }
}
