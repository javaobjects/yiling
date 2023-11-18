package com.yiling.dataflow.relation.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.relation.api.FlowGoodsRelationEditTaskApi;
import com.yiling.dataflow.relation.dto.FlowGoodsRelationEditTaskDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsRelationEditTaskRequest;
import com.yiling.dataflow.relation.service.FlowGoodsRelationEditTaskService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: houjie.sun
 * @date: 2022/10/8
 */
@DubboService
public class FlowGoodsRelationEditTaskApiImpl implements FlowGoodsRelationEditTaskApi {

    @Autowired
    private FlowGoodsRelationEditTaskService flowGoodsRelationEditTaskService;

    @Override
    public FlowGoodsRelationEditTaskDTO getById(Long id) {
        return PojoUtils.map(flowGoodsRelationEditTaskService.getById(id), FlowGoodsRelationEditTaskDTO.class);
    }

    @Override
    public List<FlowGoodsRelationEditTaskDTO> getByIdList(List<Long> idList) {
        return PojoUtils.map(flowGoodsRelationEditTaskService.getByIdList(idList), FlowGoodsRelationEditTaskDTO.class);
    }

    @Override
    public Boolean existFlowGoodsRelationEditTask(Long flowGoodsRelationId, Integer syncStatus) {
        return flowGoodsRelationEditTaskService.existFlowGoodsRelationEditTask(flowGoodsRelationId, syncStatus);
    }

    @Override
    public List<FlowGoodsRelationEditTaskDTO> getListByEid(Long eid) {
        return PojoUtils.map(flowGoodsRelationEditTaskService.getListByEid(eid), FlowGoodsRelationEditTaskDTO.class);
    }

    @Override
    public List<Long> saveByRequestList(List<SaveFlowGoodsRelationEditTaskRequest> requestList) {
        return flowGoodsRelationEditTaskService.saveByRequestList(requestList);
    }

    @Override
    public Integer updateSyncStatusByIdList(List<Long> idList, Integer syncStatus) {
        return flowGoodsRelationEditTaskService.updateSyncStatusByIdList(idList, syncStatus);
    }
}
