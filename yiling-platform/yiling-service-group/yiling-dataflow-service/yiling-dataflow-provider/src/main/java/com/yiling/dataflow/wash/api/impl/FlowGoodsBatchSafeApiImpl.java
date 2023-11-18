package com.yiling.dataflow.wash.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowGoodsBatchSafeApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchSafeDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchSafePageRequest;
import com.yiling.dataflow.wash.service.FlowGoodsBatchSafeService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
@DubboService
public class FlowGoodsBatchSafeApiImpl implements FlowGoodsBatchSafeApi {

    @Autowired
    private FlowGoodsBatchSafeService flowGoodsBatchSafeService;

    @Override
    public Page<FlowGoodsBatchSafeDTO> listPage(QueryFlowGoodsBatchSafePageRequest request) {
        return PojoUtils.map(flowGoodsBatchSafeService.listPage(request), FlowGoodsBatchSafeDTO.class);
    }
}
