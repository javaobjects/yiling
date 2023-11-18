package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchDetailWashDTO;
import com.yiling.dataflow.wash.dto.FlowPurchaseWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
public interface FlowGoodsBatchDetailWashApi {

    /**
     * 查询采购月流向列表
     * @param request
     * @return
     */
    Page<FlowGoodsBatchDetailWashDTO> listPage(QueryFlowGoodsBatchDetailWashPageRequest request);

    /**
     * 批量插入库存月流向
     * @param flowSaleWashDTOList
     */
    void batchInsert(List<FlowGoodsBatchDetailWashDTO> flowGoodsBatchDetailWashDTOList);
}
