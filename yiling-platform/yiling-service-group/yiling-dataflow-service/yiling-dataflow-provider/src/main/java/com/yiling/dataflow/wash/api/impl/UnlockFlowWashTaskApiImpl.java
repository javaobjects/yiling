package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.yiling.dataflow.wash.api.UnlockFlowWashTaskApi;
import com.yiling.dataflow.wash.dto.UnlockFlowWashTaskDTO;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerWashTaskRequest;
import com.yiling.dataflow.wash.service.UnlockFlowWashTaskService;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-06
 */
@DubboService
public class UnlockFlowWashTaskApiImpl implements UnlockFlowWashTaskApi {

    @Autowired
    private UnlockFlowWashTaskService unlockFlowWashTaskService;

    @Override
    public Boolean saveBatch(List<SaveOrUpdateUnlockCustomerWashTaskRequest> requestList) {
        return unlockFlowWashTaskService.saveBatch(requestList);
    }

    @Override
    public Long saveUnlockFlowWashTask(SaveOrUpdateUnlockCustomerWashTaskRequest request) {
        return unlockFlowWashTaskService.saveUnlockCustomerWashTask(request);
    }

    @Override
    public List<UnlockFlowWashTaskDTO> getListByFmwcId(Long fmwcId) {
        return PojoUtils.map(unlockFlowWashTaskService.getListByFmwcId(fmwcId), UnlockFlowWashTaskDTO.class);
    }

    @Override
    public UnlockFlowWashTaskDTO getById(Long id) {
        return PojoUtils.map(unlockFlowWashTaskService.getById(id), UnlockFlowWashTaskDTO.class);
    }
}
