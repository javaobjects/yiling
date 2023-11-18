package com.yiling.sales.assistant.task.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.app.TaskDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.TaskGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.app.GetTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;

/**
 * app端 任务
 * @author: ray
 * @date: 2021/9/27
 */
public interface AppMarketTaskService extends BaseService<MarketTaskDO> {
    /**
     * 任务列表
     * @param queryTaskPageRequest
     * @return
     */
    Page<TaskDTO> listTaskPage(QueryTaskPageRequest queryTaskPageRequest);

    /**
     * 任务详情
     * @param getTaskDetailRequest
     * @return
     */
    TaskDetailDTO getTaskDetail(GetTaskDetailRequest getTaskDetailRequest);


    /**
     * 任务详情页-商品
     * @param queryTaskGoodsRequest
     * @return
     */
    List<TaskGoodsDTO> listTaskGoodsPage(QueryTaskGoodsRequest queryTaskGoodsRequest);

    /**
     * 佣金规则
     * @param taskId
     * @return
     */
    String getCommissionRule(Long taskId);

    /**
     * 拼接任务规则
     * @param taskId
     * @param finishRuleValue
     * @return
     */
    String appendRuleStr(Long taskId, String finishRuleValue);

    /**
     *
     * @param taskId
     * @return
     */
    String getCommission(Long taskId);
}
