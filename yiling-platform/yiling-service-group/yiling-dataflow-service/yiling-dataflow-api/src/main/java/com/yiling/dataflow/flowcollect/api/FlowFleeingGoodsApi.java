package com.yiling.dataflow.flowcollect.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowFleeingGoodsDTO;
import com.yiling.dataflow.flowcollect.dto.FlowMonthSalesDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowFleeingGoodsRequest;

/**
 * 窜货申报上传数据Api
 *
 * @author: yong.zhang
 * @date: 2023/3/16 0016
 */
public interface FlowFleeingGoodsApi {

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
    Page<FlowFleeingGoodsDTO> pageList(QueryFlowMonthPageRequest request);
}
