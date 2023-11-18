package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowPurchaseWashDTO;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
public interface FlowPurchaseWashApi {

    /**
     * 查询采购月流向列表
     * @param request
     * @return
     */
    Page<FlowPurchaseWashDTO> listPage(QueryFlowPurchaseWashPageRequest request);

    /**
     * 批量插入采购月流向
     * @param flowSaleWashDTOList
     */
    void batchInsert(List<FlowPurchaseWashDTO> flowPurchaseWashList);
}
