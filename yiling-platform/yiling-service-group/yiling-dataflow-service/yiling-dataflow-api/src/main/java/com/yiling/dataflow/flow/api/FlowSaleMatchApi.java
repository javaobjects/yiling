package com.yiling.dataflow.flow.api;

import com.yiling.dataflow.flow.dto.FlowSaleMatchResultDTO;
import com.yiling.dataflow.flow.dto.request.FlowSaleMatchRequest;

/**
 * @author fucheng.bai
 * @date 2023/1/30
 */
public interface FlowSaleMatchApi {

    FlowSaleMatchResultDTO match(FlowSaleMatchRequest request);
}
