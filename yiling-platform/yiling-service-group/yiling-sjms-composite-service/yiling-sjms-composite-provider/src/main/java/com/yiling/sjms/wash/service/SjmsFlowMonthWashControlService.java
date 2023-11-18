package com.yiling.sjms.wash.service;

import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/5/5
 */
public interface SjmsFlowMonthWashControlService {
    boolean gbLockStatus(UpdateStageRequest request);
    boolean unlockStatus(UpdateStageRequest updateStageRequest);
    boolean gbUnlockStatus(UpdateStageRequest updateStageRequest);
}
