package com.yiling.sjms.wash.api;

import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
public interface SjmsFlowMonthWashControlApi {

    /**
     * 团购处理(锁定部分、非客户分类)
     *
     * @param updateStageRequest
     * @return
     */
    boolean gbLockStatus(UpdateStageRequest updateStageRequest);

    /**
     * 非锁销量分配
     *
     * @param updateStageRequest
     * @return
     */
    boolean unlockStatus(UpdateStageRequest updateStageRequest);


    /**
     * 团购处理(非锁部分)
     *
     * @param updateStageRequest
     * @return
     */
    boolean gbUnlockStatus(UpdateStageRequest updateStageRequest);
}
