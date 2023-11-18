package com.yiling.dataflow.flowcollect.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowMonthInventoryDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthInventoryRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthPurchaseRequest;

/**
 * 月流向库存数据 API
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
public interface FlowMonthInventoryApi {

    /**
     * 保存月流向库存数据
     *
     * @param request
     * @return
     */
    boolean updateFlowMonthInventoryAndTask(Long opUserId,Long recordId);

    /**
     * 查询月流向库存分页列表
     *
     * @param request
     * @return
     */
    Page<FlowMonthInventoryDTO> queryFlowMonthInventoryPage(QueryFlowMonthPageRequest request);

    boolean saveBatch(List<SaveFlowMonthInventoryRequest> request);
}
