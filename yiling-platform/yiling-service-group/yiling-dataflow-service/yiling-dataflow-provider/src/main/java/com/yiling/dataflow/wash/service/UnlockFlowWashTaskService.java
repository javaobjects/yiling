package com.yiling.dataflow.wash.service;

import java.util.List;

import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerWashTaskRequest;
import com.yiling.dataflow.wash.entity.UnlockFlowWashTaskDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-06
 */
public interface UnlockFlowWashTaskService extends BaseService<UnlockFlowWashTaskDO> {
    Boolean saveBatch(List<SaveOrUpdateUnlockCustomerWashTaskRequest> requestList);
    Long saveUnlockCustomerWashTask(SaveOrUpdateUnlockCustomerWashTaskRequest request);
    List<UnlockFlowWashTaskDO> getListByFmwcId(Long fmwcId);
}
