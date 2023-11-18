package com.yiling.dataflow.order.service;

import java.util.List;

/**
 * 获取流向报表商业标签服务类
 *
 * @author: houjie.sun
 * @date: 2022/6/28
 */
public interface FlowSettlementEnterpriseTagService {

    /**
     * 获取流向报表商业标签名称列表
     *
     * @return
     */
    List<Long> getFlowSettlementEnterpriseTagNameList();

}
