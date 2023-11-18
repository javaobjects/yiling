package com.yiling.dataflow.check.api;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.check.dto.FlowPurchaseCheckJobDTO;
import com.yiling.dataflow.check.dto.request.FlowPurchaseCheckJobRequest;
import com.yiling.dataflow.flow.dto.FlowMonthBiJobDTO;

/**
 * @author: houjie.sun
 * @date: 2022/9/6
 */
public interface FlowPurchaseCheckJobApi {

    FlowPurchaseCheckJobDTO save(FlowPurchaseCheckJobDTO flowPurchaseCheckJobDTO);

    /**
     * 根据日期获取列表
     * @param request
     * @return
     */
    List<FlowPurchaseCheckJobDTO> listByDate(FlowPurchaseCheckJobRequest request);

}
