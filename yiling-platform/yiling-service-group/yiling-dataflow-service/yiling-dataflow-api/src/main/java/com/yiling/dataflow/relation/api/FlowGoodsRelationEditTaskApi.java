package com.yiling.dataflow.relation.api;

import java.util.List;

import com.yiling.dataflow.relation.dto.FlowGoodsRelationEditTaskDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsRelationEditTaskRequest;

/**
 * @author: houjie.sun
 * @date: 2022/10/8
 */
public interface FlowGoodsRelationEditTaskApi {

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    FlowGoodsRelationEditTaskDTO getById(Long id);

    /**
     * 根据id列表查询
     *
     * @param idList
     * @return
     */
    List<FlowGoodsRelationEditTaskDTO> getByIdList(List<Long> idList);

    /**
     * 当前企业id是否存在未完成的修改任务（在生成报表中）
     *
     * @return true-存在、false-不存在
     */
    Boolean existFlowGoodsRelationEditTask(Long flowGoodsRelationId,Integer syncStatus);

    /**
     * 根据eid查询列表
     *
     * @param eid
     * @return
     */
    List<FlowGoodsRelationEditTaskDTO> getListByEid(Long eid);

    /**
     * 批量保存修改任务
     *
     * @param requestList
     * @return
     */
    List<Long> saveByRequestList(List<SaveFlowGoodsRelationEditTaskRequest> requestList);

    /**
     * 根据id列表更新状态
     *
     * @param idList
     * @return
     */
    Integer updateSyncStatusByIdList(List<Long> idList, Integer syncStatus);

}
