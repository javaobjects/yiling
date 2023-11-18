package com.yiling.dataflow.wash.service;

import java.util.List;

import com.yiling.dataflow.wash.entity.UnlockCustomerMatchingTaskDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/4/27
 */
public interface UnlockCustomerMatchingTaskService  extends BaseService<UnlockCustomerMatchingTaskDO> {

    UnlockCustomerMatchingTaskDO getByCustomerName(String name);

    List<UnlockCustomerMatchingTaskDO> getByCustomerNameList(List<String> nameList);
}
