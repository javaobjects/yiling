package com.yiling.sjms.wash.api.impl;

import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;
import com.yiling.sjms.wash.api.SjmsFlowMonthWashControlApi;
import com.yiling.sjms.wash.service.SjmsFlowMonthWashControlService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/5/5
 */
@DubboService
public class SjmsFlowMonthWashControlApiImpl implements SjmsFlowMonthWashControlApi {

    @Autowired
    private SjmsFlowMonthWashControlService sjmsFlowMonthWashControlService;

    @Override
    public boolean gbLockStatus(UpdateStageRequest request) {
        return sjmsFlowMonthWashControlService.gbLockStatus(request);
    }

    @Override
    public boolean unlockStatus(UpdateStageRequest updateStageRequest) {
        return sjmsFlowMonthWashControlService.unlockStatus(updateStageRequest);
    }

    @Override
    public boolean gbUnlockStatus(UpdateStageRequest updateStageRequest) {
        return sjmsFlowMonthWashControlService.gbUnlockStatus(updateStageRequest);
    }
}
