package com.yiling.dataflow.statistics.service;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.statistics.dto.request.SaveFlowErpSyncDateRequest;
import com.yiling.dataflow.statistics.entity.FlowErpSyncDateDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author: houjie.sun
 * @date: 2023/1/3
 */
public interface FlowErpSyncDateService extends BaseService<FlowErpSyncDateDO> {

    boolean insertBatch(List<SaveFlowErpSyncDateRequest> list);

    FlowErpSyncDateDO getOneByEidAndTaskTime(Long eid, String taskTime);

    Integer deleteByEidAndTaskTime(Long eid, String taskTime);

    Integer deleteByEidListAndTaskTime(List<Long> eidList, String taskTime);

    /**
     * 根据企业id获取最新的收集时间
     *
     * @return
     */
    Date getMaxTaskTimeByEid(Long eid);

}
