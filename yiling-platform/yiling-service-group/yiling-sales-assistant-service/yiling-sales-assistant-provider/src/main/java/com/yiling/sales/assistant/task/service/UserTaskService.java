package com.yiling.sales.assistant.task.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskTraceUserDTO;
import com.yiling.sales.assistant.task.dto.UserTypeDTO;
import com.yiling.sales.assistant.task.dto.app.GoodsProgressDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskProgressDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceTaskUserRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateUserTaskMemberRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateUserTaskNoLimitRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskRequest;
import com.yiling.sales.assistant.task.dto.request.app.TakeTaskRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskOrderDO;
import com.yiling.sales.assistant.task.entity.TaskOrderGoodsDO;
import com.yiling.sales.assistant.task.entity.UserSelectedTerminalDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskGoodsDO;
import com.yiling.sales.assistant.task.entity.UserTaskStepDO;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;

/**
 * <p>
 * 我的任务 销售承接的任务 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
public interface UserTaskService extends BaseService<UserTaskDO> {

    /**
     * 任务追踪-承接人
     * @param queryTaskTraceTaskUserRequest
     * @return
     */
    Page<TaskTraceUserDTO> queryTaskUser(QueryTaskTraceTaskUserRequest queryTaskTraceTaskUserRequest);

    /**
     * 任务进度更新
     * @param userTask
     * @param userTaskGoodsList
     * @param taskOrder
     * @param taskOrderGoodsList
     */
    void updateTaskProgress(UserTaskDO userTask, List<UserTaskGoodsDO> userTaskGoodsList, TaskOrderDO taskOrder, List<TaskOrderGoodsDO> taskOrderGoodsList, UserSelectedTerminalDO userSelectedTerminal, UserTaskStepDO userTaskStepDO);

    /**
     * 更新拉新户任务进度（无限制类型：审核通过即可）
     * @param userCustomerDTO
     */
    void updateInviteCostumerProgress(UserCustomerDTO userCustomerDTO);


    /**
     * 更新拉新人任务进度（有限制类型：成功拉入新客户（企业客户））
     * @param updateUserTaskByCostumerRequest

    void updateInviteUserProgress(UpdateUserTaskByCostumerRequest updateUserTaskByCostumerRequest);*/
    /**
     * 拉人任务订单累计或者首单
     * @param addTaskOrderRequest
     */
    void newUserHandler(AddTaskOrderRequest addTaskOrderRequest);

    /**
     *更新拉新人任务进度（无限制类型：注册成功即可）
     * @param updateUserTaskNoLimitRequest
     */
    void updateInviteUserProgress(UpdateUserTaskNoLimitRequest updateUserTaskNoLimitRequest);

    /**
     *
     * 更新购买会员推广任务进度
     */
    void updateMemberBuyProgress(UpdateUserTaskMemberRequest updateUserTaskMemberRequest);

    /**
     * 我的任务列表
     * @param queryMyTaskRequest
     * @return
     */
    Page<MyTaskDTO> listMyTaskPage(QueryMyTaskRequest queryMyTaskRequest);

    /**
     * 佣金
     * @param taskId
     * @return
     */
    String getCommission(Long taskId);

    /**
     * 我的任务详情
     * @param queryMyTaskDetailRequest
     * @return
     */
    MyTaskDetailDTO getMyTaskDetail(QueryMyTaskDetailRequest queryMyTaskDetailRequest);

    /**
     * 我的任务详情-任务进度
     * @param userTaskId
     * @return
     */
    MyTaskProgressDTO getMyTaskProgress(Long userTaskId);

    /**
     * 我的任务详情-多品销售商品进度
     * @param userTaskId
     * @return
     */
    List<GoodsProgressDTO> getGoodsProgress(Long userTaskId);

    /**
     * 企业任务到达t开始时间后自动派发给任务配置的部门或直接员工
     */
    void dispatchEnterpriseTask(List<MarketTaskDO > marketTaskDOS);

    /**
     * 承接任务
     * @param takeTaskRequest
     */
    Boolean takeTask(TakeTaskRequest takeTaskRequest);

    /**
     *
     * @param userTask
     * @param userTaskGoodsList
     * @param userSelectedTerminals
     */
    void saveTask(UserTaskDO userTask,  List<UserTaskGoodsDO> userTaskGoodsList , List<UserSelectedTerminalDO> userSelectedTerminals);

    /**
     * 已完成的非阶梯任务变更佣金状态
     * @param userTasks
     */
    void updateTaskCommission(List<UserTaskDO> userTasks,Long updateUserId);

    /**
     * 阶梯任务计算任务状态和计算佣金并发放
     * @param userTaskDO
     */
    void addStepTaskCommission(UserTaskDO userTaskDO);
    /**
     * 随货同行单阶梯任务计算任务状态和计算佣金并发放
     * @param userTaskDO
     */
    void addStepTaskCommissionV2(UserTaskDO userTaskDO);

    /**
     * 根据用户任务id查询用户类型信息
     * @return
     */
    UserTypeDTO getUserTypeByUserTaskId(Long userTaskId);

    /**
     * 会员退款后回退任务相关
      * @param orderNo
     */
    void rollbackUserTask(String orderNo);

    /**
     * 变更上传随货同行单任务状态和佣金状态定时任务 28号
     */
    void handleTaskCommissionForUpload();
}
