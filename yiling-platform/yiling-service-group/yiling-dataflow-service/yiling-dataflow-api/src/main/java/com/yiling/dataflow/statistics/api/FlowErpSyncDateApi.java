package com.yiling.dataflow.statistics.api;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.statistics.dto.FlowErpSyncDateDTO;
import com.yiling.dataflow.statistics.dto.request.SaveFlowErpSyncDateRequest;

/**
 * @author: houjie.sun
 * @date: 2023/1/3
 */
public interface FlowErpSyncDateApi {

    boolean insertBatch(List<SaveFlowErpSyncDateRequest> list);

    FlowErpSyncDateDTO getOneByEidAndTaskTime(Long eid, String taskTime);

    Integer deleteByEidAndTaskTime(Long eid, String taskTime);

    Integer deleteByEidListAndTaskTime(List<Long> eidList, String taskTime);

    /**
     * 根据企业id获取最新的收集时间
     *
     * @return
     */
    Date getMaxTaskTimeByEid(Long eid);
}
