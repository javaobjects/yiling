package com.yiling.dataflow.check.service;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.check.dto.request.FlowPurchaseCheckJobRequest;
import com.yiling.dataflow.check.entity.FlowPurchaseCheckJobDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
public interface FlowPurchaseCheckJobService extends BaseService<FlowPurchaseCheckJobDO> {

    /**
     * 根据日期获取列表
     * @param request
     * @return
     */
    List<FlowPurchaseCheckJobDO> listByDate(FlowPurchaseCheckJobRequest request);
}
