package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowPurchaseWashApi;
import com.yiling.dataflow.wash.dto.FlowPurchaseWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.dataflow.wash.service.FlowPurchaseWashService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@DubboService
public class FlowPurchaseWashApiImpl implements FlowPurchaseWashApi {

    @Autowired
    private FlowPurchaseWashService flowPurchaseWashService;

    @Override
    public Page<FlowPurchaseWashDTO> listPage(QueryFlowPurchaseWashPageRequest request) {
        return PojoUtils.map(flowPurchaseWashService.listPage(request), FlowPurchaseWashDTO.class);
    }

    @Override
    public void batchInsert(List<FlowPurchaseWashDTO> flowPurchaseWashList) {
        List<FlowPurchaseWashDO> flowPurchaseWashDOList = PojoUtils.map(flowPurchaseWashList, FlowPurchaseWashDO.class);
        flowPurchaseWashService.batchInsert(flowPurchaseWashDOList);
    }
}
