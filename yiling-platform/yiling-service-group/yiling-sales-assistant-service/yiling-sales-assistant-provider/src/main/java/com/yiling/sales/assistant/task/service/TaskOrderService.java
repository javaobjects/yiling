package com.yiling.sales.assistant.task.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskTraceOrderDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceOrderRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskOrderDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;

/**
 * <p>
 * 任务订单 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
public interface TaskOrderService extends BaseService<TaskOrderDO> {

    /**
     * 任务追踪-销售记录
      * @param queryTaskTraceOrderRequest
     * @return
     */
    Page<TaskTraceOrderDTO> listTaskTraceOrderPage(QueryTaskTraceOrderRequest queryTaskTraceOrderRequest);

    /**
     * 计算订单和商品信息并保存
     * @param taskGoodsIds
     * @param addTaskOrderRequest
     * @param taskDO
     * @param userTaskDO
     */
    void computeTaskOrder(List<Long> taskGoodsIds, AddTaskOrderRequest addTaskOrderRequest, MarketTaskDO taskDO, UserTaskDO userTaskDO);
}
