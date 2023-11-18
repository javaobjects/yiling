package com.yiling.dataflow.statistics.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.statistics.api.FlowErpSyncDateApi;
import com.yiling.dataflow.statistics.dto.FlowErpSyncDateDTO;
import com.yiling.dataflow.statistics.dto.request.SaveFlowErpSyncDateRequest;
import com.yiling.dataflow.statistics.service.FlowErpSyncDateService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: houjie.sun
 * @date: 2023/1/3
 */
@DubboService
public class FlowErpSyncDateApiImpl implements FlowErpSyncDateApi {

    @Autowired
    private FlowErpSyncDateService flowErpSyncDateService;

    @Override
    public boolean insertBatch(List<SaveFlowErpSyncDateRequest> list) {
        return flowErpSyncDateService.insertBatch(list);
    }

    @Override
    public FlowErpSyncDateDTO getOneByEidAndTaskTime(Long eid, String taskTime) {
        return PojoUtils.map(flowErpSyncDateService.getOneByEidAndTaskTime(eid, taskTime), FlowErpSyncDateDTO.class);
    }

    @Override
    public Integer deleteByEidAndTaskTime(Long eid, String taskTime) {
        return flowErpSyncDateService.deleteByEidAndTaskTime(eid, taskTime);
    }

    @Override
    public Integer deleteByEidListAndTaskTime(List<Long> eidList, String taskTime) {
        return flowErpSyncDateService.deleteByEidListAndTaskTime(eidList, taskTime);
    }

    @Override
    public Date getMaxTaskTimeByEid(Long eid) {
        return flowErpSyncDateService.getMaxTaskTimeByEid(eid);
    }
}
