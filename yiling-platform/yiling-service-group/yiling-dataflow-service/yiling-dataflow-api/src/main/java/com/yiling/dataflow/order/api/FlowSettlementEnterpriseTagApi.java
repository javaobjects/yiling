package com.yiling.dataflow.order.api;

import java.util.List;

/**
 * 流向报表商业标签接口
 *
 * @author: houjie.sun
 * @date: 2022/6/28
 */
public interface FlowSettlementEnterpriseTagApi {

    /**
     * 获取流向报表商业标签名称列表
     *
     * @return
     */
    List<Long> getFlowSettlementEnterpriseTagNameList();

}
