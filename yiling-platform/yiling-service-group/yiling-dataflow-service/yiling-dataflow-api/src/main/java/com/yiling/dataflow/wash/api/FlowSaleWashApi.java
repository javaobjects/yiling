package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;

/**
 * @author fucheng.bai
 * @date 2023/3/2
 */
public interface FlowSaleWashApi {

    /**
     * 查询销售月流向列表
     * @param request
     * @return
     */
    Page<FlowSaleWashDTO> listPage(QueryFlowSaleWashPageRequest request);

    /**
     * 批量插入月流向销售数据
     * @param flowSaleWashDTOList
     */
    void batchInsert(List<FlowSaleWashDTO> flowSaleWashDTOList);

    List<FlowSaleWashDTO> getByYearMonth(QueryFlowSaleWashListRequest request);
}
