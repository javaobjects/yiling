package com.yiling.dataflow.wash.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchSafeDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchSafePageRequest;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
public interface FlowGoodsBatchSafeApi {

    Page<FlowGoodsBatchSafeDTO> listPage(QueryFlowGoodsBatchSafePageRequest request);

}
