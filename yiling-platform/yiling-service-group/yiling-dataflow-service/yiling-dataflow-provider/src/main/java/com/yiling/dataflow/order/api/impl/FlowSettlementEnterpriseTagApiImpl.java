package com.yiling.dataflow.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.order.api.FlowSettlementEnterpriseTagApi;
import com.yiling.dataflow.order.service.FlowSettlementEnterpriseTagService;

/**
 * 流向报表商业标签
 *
 * @author: houjie.sun
 * @date: 2022/6/28
 */
@DubboService
public class FlowSettlementEnterpriseTagApiImpl implements FlowSettlementEnterpriseTagApi {

    @Autowired
    private FlowSettlementEnterpriseTagService flowSettlementEnterpriseTagService;

    @Override
    public List<Long> getFlowSettlementEnterpriseTagNameList() {
        return flowSettlementEnterpriseTagService.getFlowSettlementEnterpriseTagNameList();
    }
}
