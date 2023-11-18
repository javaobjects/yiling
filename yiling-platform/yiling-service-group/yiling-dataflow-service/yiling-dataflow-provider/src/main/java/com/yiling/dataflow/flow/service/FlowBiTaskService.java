package com.yiling.dataflow.flow.service;

import com.yiling.dataflow.flow.entity.FlowBiTaskDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-04-06
 */
public interface FlowBiTaskService extends BaseService<FlowBiTaskDO> {
    /**
     * 执行流向导入到bi系统任务
     */
    void excelFlowBiTask();

    Integer deleteByTaskTime(String taskTimeEnd, Long eid);
}
