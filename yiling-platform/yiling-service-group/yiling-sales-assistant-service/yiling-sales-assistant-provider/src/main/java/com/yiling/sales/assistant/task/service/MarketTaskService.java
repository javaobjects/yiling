package com.yiling.sales.assistant.task.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskAreaDTO;
import com.yiling.sales.assistant.task.dto.TaskCountDTO;
import com.yiling.sales.assistant.task.dto.TaskDTO;
import com.yiling.sales.assistant.task.dto.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.dto.request.StopTaskRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateTaskRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.enums.UserTaskStatusEnum;

/**
 * <p>
 * 任务基本信息表  服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
public interface MarketTaskService extends BaseService<MarketTaskDO> {
    /**
     * 创建任务
     * @param addTaskRequest
     */
    void addTask(AddTaskRequest addTaskRequest);
    /**
     * 获取任务数量
     * @return
     */
    TaskCountDTO getTaskCount();

    /**
     * 获取任务状态
     * @param taskId
     * @return
     */
    Integer getTaskStatus(Long taskId);

    /**
     * 更新任务、
     * @param updateTaskRequest
     */
    void updateTask(UpdateTaskRequest updateTaskRequest);

    /**
     * 任务详情
     * @param id
     * @return
     */
    TaskDetailDTO getDetailById(Long id);

    /**
     * 任务列表
     * @return
     */
    Page<TaskDTO> queryTaskListPage(QueryTaskPageRequest queryTaskPageRequest);



    /**
     * 任务追踪-头部
     * @param taskId
     * @return
     */
    TaskTraceDTO getTaskTrace(Long taskId);

    /**
     *
     * @param taskId
     * @param ruleType
     * @param ruleKey
     * @return
     */
    String getRuleValue(Long taskId,Integer ruleType,String ruleKey);

    /**
     * 任务处理
     * @param addTaskOrderRequest
     */
    void handleTaskOrder(AddTaskOrderRequest addTaskOrderRequest);

    /**
     * 任务区域反显
     * @param taskId
     * @return
     */
    List<TaskAreaDTO> queryTaskArea(Long taskId);


    /**
     * 定时任务扫描任务发布
     */
    void publishTask();

    /**
     * 定时任务扫描到达结束时间的任务
     */
    void endTask();

    /**
     * 停止任务
     * @param stopTaskRequest
     */
    void stopTask(StopTaskRequest stopTaskRequest);

    void updateUserTask(List<UserTaskDO> userTasks, List<UserTaskDO> updateUserTasks, UserTaskStatusEnum userTaskStatusEnum);
    /**
     * 删除任务
     * @param stopTaskRequest
     */
    void deleteTask(StopTaskRequest stopTaskRequest);

    /**
     * 拉户任务订单累计或者首单
     * @param addTaskOrderRequest
     */
    void newCustomerHandler(AddTaskOrderRequest addTaskOrderRequest);


    /**
     *
     * @param orderNo
     * @return
     */
    AddTaskOrderRequest getOrderByNo(String orderNo);

    void finishedTask(Long taskId, Long optUserId);
}
