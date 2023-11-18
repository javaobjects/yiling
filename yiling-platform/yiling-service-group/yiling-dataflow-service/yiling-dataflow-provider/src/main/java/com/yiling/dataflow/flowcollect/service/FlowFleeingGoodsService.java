package com.yiling.dataflow.flowcollect.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowFleeingGoodsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowFleeingGoodsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 窜货申报上传数据表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-16
 */
public interface FlowFleeingGoodsService extends BaseService<FlowFleeingGoodsDO> {

    /**
     * 窜货申报上传数据新增
     *
     * @param requestList 新增内容
     * @return 成功/失败
     */
    boolean saveFlowFleeingGoodsAndTask(List<SaveFlowFleeingGoodsRequest> requestList);

    /**
     * @param recordId
     * @param taskId
     * @param opUserId
     * @return
     */
    boolean updateTaskIdByRecordId(Long recordId, Long taskId, Long opUserId);

    /**
     * 窜货申报上传数据分页查询
     *
     * @param request 查询条件
     * @return 窜货申报上传数据
     */
    Page<FlowFleeingGoodsDO> pageList(QueryFlowMonthPageRequest request);
}
