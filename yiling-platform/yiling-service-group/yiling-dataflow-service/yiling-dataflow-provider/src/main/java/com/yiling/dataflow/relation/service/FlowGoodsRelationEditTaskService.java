package com.yiling.dataflow.relation.service;

import java.util.List;

import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsRelationEditTaskRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationEditTaskDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author: houjie.sun
 * @date: 2022/10/8
 */
public interface FlowGoodsRelationEditTaskService extends BaseService<FlowGoodsRelationEditTaskDO> {

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    FlowGoodsRelationEditTaskDO getById(Long id);

    /**
     * 根据id列表查询
     *
     * @param idList
     * @return
     */
    List<FlowGoodsRelationEditTaskDO> getByIdList(List<Long> idList);

    /**
     * 保存修改任务
     *
     * @param request
     * @return
     */
    Long saveByRequest(SaveFlowGoodsRelationEditTaskRequest request);

    /**
     * 批量保存修改任务
     *
     * @param requestList
     * @return
     */
    List<Long> saveByRequestList(List<SaveFlowGoodsRelationEditTaskRequest> requestList);

    /**
     * 当前企业id是否存在未完成的修改任务（在生成报表中）
     *
     * @return true-存在、false-不存在
     */
    Boolean existFlowGoodsRelationEditTask(Long flowGoodsRelationId, Integer syncStatus);

    /**
     * 根据eid查询列表
     *
     * @param eid
     * @return
     */
    List<FlowGoodsRelationEditTaskDO> getListByEid(Long eid);

    /**
     * 根据id列表更新状态
     *
     * @param idList
     * @return
     */
    Integer updateSyncStatusByIdList(List<Long> idList, Integer syncStatus);
}
