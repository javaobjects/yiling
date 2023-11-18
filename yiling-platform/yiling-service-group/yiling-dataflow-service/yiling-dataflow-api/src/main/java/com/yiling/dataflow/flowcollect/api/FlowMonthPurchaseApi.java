package com.yiling.dataflow.flowcollect.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowMonthPurchaseDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthPurchaseRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthSalesRequest;

/**
 * 月流向库存数据 API
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
public interface FlowMonthPurchaseApi {

    /**
     * 保存月流向采购数据
     *
     * @param recordId
     * @return
     */
    boolean  updateFlowMonthPurchaseAndTask(Long opUserId,Long recordId);

    /**
     * 查询月流向采购分页列表
     *
     * @param request
     * @return
     */
    Page<FlowMonthPurchaseDTO> queryFlowMonthPurchasePage(QueryFlowMonthPageRequest request);

    boolean saveBatch(List<SaveFlowMonthPurchaseRequest> request);

}
