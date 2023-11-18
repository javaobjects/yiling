package com.yiling.dataflow.wash.api;

import java.util.List;

import com.yiling.dataflow.wash.dto.UnlockFlowWashTaskDTO;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerWashTaskRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-06
 */
public interface UnlockFlowWashTaskApi {
    Boolean saveBatch(List<SaveOrUpdateUnlockCustomerWashTaskRequest> requestList);

    Long saveUnlockFlowWashTask(SaveOrUpdateUnlockCustomerWashTaskRequest request);

    List<UnlockFlowWashTaskDTO> getListByFmwcId(Long fmwcId);

    UnlockFlowWashTaskDTO getById(Long id);
}
