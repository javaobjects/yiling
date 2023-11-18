package com.yiling.sales.assistant.task.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.task.api.AppTaskApi;
import com.yiling.sales.assistant.task.dto.TaskAccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.TaskMemberPromotiondDTO;
import com.yiling.sales.assistant.task.dto.TaskTerminalDTO;
import com.yiling.sales.assistant.task.dto.app.AccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.app.GoodsProgressDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskProgressDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDistributorDTO;
import com.yiling.sales.assistant.task.dto.app.TaskGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskAccompanyBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.GetTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.InviteTaskMemberRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryAccompanyingBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskTerminalPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.SaveAccompanyingBillRequest;
import com.yiling.sales.assistant.task.dto.request.app.TakeTaskRequest;
import com.yiling.sales.assistant.task.service.AccompanyingBillService;
import com.yiling.sales.assistant.task.service.AppMarketTaskService;
import com.yiling.sales.assistant.task.service.TaskAccompanyingBillService;
import com.yiling.sales.assistant.task.service.TaskDistributorService;
import com.yiling.sales.assistant.task.service.TaskMemberBuyService;
import com.yiling.sales.assistant.task.service.TaskMemberPromotionService;
import com.yiling.sales.assistant.task.service.UserSelectedTerminalService;
import com.yiling.sales.assistant.task.service.UserTaskService;

/**
 * app任务
 * @author: ray
 * @date: 2021/9/27
 */
@DubboService
public class AppTaskApiImpl implements AppTaskApi {

    @Autowired
    private AppMarketTaskService appMarketTaskService;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private UserSelectedTerminalService  userSelectedTerminalService;

    @Autowired
    private TaskMemberPromotionService taskMemberPromotionService;

    @Autowired
    private TaskMemberBuyService taskMemberBuyService;

    @Autowired
    private TaskDistributorService taskDistributorService;

    @Autowired
    private AccompanyingBillService  accompanyingBillService;

    @Autowired
    private TaskAccompanyingBillService taskAccompanyingBillService;

    @Override
    public Page<TaskDTO> listTaskPage(QueryTaskPageRequest queryTaskPageRequest) {

        return appMarketTaskService.listTaskPage(queryTaskPageRequest);
    }

    @Override
    public TaskDetailDTO getTaskDetail(GetTaskDetailRequest getTaskDetailRequest) {
        return appMarketTaskService.getTaskDetail(getTaskDetailRequest);
    }

    @Override
    public List<TaskGoodsDTO> listTaskGoods(QueryTaskGoodsRequest queryTaskGoodsRequest) {
        return appMarketTaskService.listTaskGoodsPage(queryTaskGoodsRequest);
    }

    @Override
    public TaskMemberPromotiondDTO getMemberPromotion(Long taskId) {
        return taskMemberPromotionService.getMemberPromotion(taskId);
    }

    @Override
    public Page<MyTaskDTO> listMyTaskPage(QueryMyTaskRequest queryMyTaskRequest) {
        return userTaskService.listMyTaskPage(queryMyTaskRequest);
    }

    @Override
    public MyTaskDetailDTO getMyTaskDetail(QueryMyTaskDetailRequest queryMyTaskDetailRequest) {
        return userTaskService.getMyTaskDetail(queryMyTaskDetailRequest);
    }

    @Override
    public MyTaskProgressDTO getMyTaskProgress(Long userTaskId) {
        return userTaskService.getMyTaskProgress(userTaskId);
    }

    @Override
    public List<GoodsProgressDTO> getGoodsProgress(Long userTaskId) {
        return userTaskService.getGoodsProgress(userTaskId);
    }

    @Override
    public Page<TaskTerminalDTO> listTaskTerminalPage(QueryTaskTerminalPageRequest queryTaskTerminalPageRequest) {
        return userSelectedTerminalService.listTaskTerminalPage(queryTaskTerminalPageRequest);
    }

    @Override
    public Page<TaskTerminalDTO> listTaskAllTerminalPage(QueryTaskTerminalPageRequest queryTaskTerminalPageRequest) {
        return userSelectedTerminalService.listTaskAllTerminalPage(queryTaskTerminalPageRequest);
    }

    @Override
    public Boolean takeTask(TakeTaskRequest takeTaskRequest) {
        return userTaskService.takeTask(takeTaskRequest);
    }

    @Override
    public String getPromotionPic(InviteTaskMemberRequest inviteTaskMemberRequest) {
        return taskMemberBuyService.getPromotionPic(inviteTaskMemberRequest);
    }

    @Override
    public Page<TaskDistributorDTO> listTaskDistributorPage(QueryTaskDistributorPageRequest queryTaskDistributorPageRequest) {
        return taskDistributorService.listAppTaskDistributorPage(queryTaskDistributorPageRequest);
    }

    @Override
    public void save(SaveAccompanyingBillRequest request) {
        accompanyingBillService.saveV2(request);
    }

    @Override
    public AccompanyingBillDTO getDetailById(Long id) {
        return accompanyingBillService.getDetailById(id);
    }

    @Override
    public Page<AccompanyingBillDTO> queryAccompanyingBillPage(QueryAccompanyingBillPageRequest request) {
        return accompanyingBillService.queryAppPage(request);
    }

    @Override
    public Page<TaskAccompanyingBillDTO> queryTaskAccompanyBillPage(QueryTaskAccompanyBillPageRequest queryTaskAccompanyBillPageRequest) {
        return taskAccompanyingBillService.queryPage(queryTaskAccompanyBillPageRequest);
    }
}