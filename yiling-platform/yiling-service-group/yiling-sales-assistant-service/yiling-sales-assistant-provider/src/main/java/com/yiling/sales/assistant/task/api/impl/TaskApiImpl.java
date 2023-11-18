package com.yiling.sales.assistant.task.api.impl;

import java.util.List;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.AccompanyingBillMatchDTO;
import com.yiling.sales.assistant.task.dto.DistributorDTO;
import com.yiling.sales.assistant.task.dto.LockTerminalListDTO;
import com.yiling.sales.assistant.task.dto.TaskAccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.TaskAreaDTO;
import com.yiling.sales.assistant.task.dto.TaskCountDTO;
import com.yiling.sales.assistant.task.dto.TaskDTO;
import com.yiling.sales.assistant.task.dto.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.TaskMemberRecordDTO;
import com.yiling.sales.assistant.task.dto.TaskSelectGoodsDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceGoodsDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceOrderDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceRegisterUserDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceTerminalDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceUserDTO;
import com.yiling.sales.assistant.task.dto.app.AccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskRequest;
import com.yiling.sales.assistant.task.dto.request.DeleteDeptRequest;
import com.yiling.sales.assistant.task.dto.request.DeleteGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.DeleteTaskDistributorRequest;
import com.yiling.sales.assistant.task.dto.request.QueryAccompanyingBillPage;
import com.yiling.sales.assistant.task.dto.request.QueryDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryGoodsPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryLockTerminalListRequest;
import com.yiling.sales.assistant.task.dto.request.QueryMatchBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskAccompanyBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskGoodsMatchRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskMemberPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterTerminalRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterUserRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceOrderRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceTaskUserRequest;
import com.yiling.sales.assistant.task.dto.request.StopTaskRequest;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchListDTO;
import com.yiling.sales.assistant.task.dto.request.UpdateTaskRequest;
import com.yiling.sales.assistant.task.dto.request.app.SaveAccompanyingBillRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskDeptUserDO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;
import com.yiling.sales.assistant.task.entity.UserSelectedTerminalDO;
import com.yiling.sales.assistant.task.enums.AssistantErrorCode;
import com.yiling.sales.assistant.task.service.AccompanyingBillMatchService;
import com.yiling.sales.assistant.task.service.AccompanyingBillService;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.SaTaskRegisterTerminalService;
import com.yiling.sales.assistant.task.service.SaTaskRegisterUserService;
import com.yiling.sales.assistant.task.service.TaskAccompanyingBillService;
import com.yiling.sales.assistant.task.service.TaskDeptUserService;
import com.yiling.sales.assistant.task.service.TaskDistributorService;
import com.yiling.sales.assistant.task.service.TaskGoodsRelationService;
import com.yiling.sales.assistant.task.service.TaskMemberRecordService;
import com.yiling.sales.assistant.task.service.TaskOrderService;
import com.yiling.sales.assistant.task.service.UserSelectedTerminalService;
import com.yiling.sales.assistant.task.service.UserTaskGoodsService;
import com.yiling.sales.assistant.task.service.UserTaskService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 任务
 * @author: ray
 * @date: 2021/9/13
 */
@DubboService
public class TaskApiImpl implements TaskApi {
    @Autowired
    private MarketTaskService marketTaskService;

    @Autowired
    private TaskDeptUserService taskDeptUserService;

    @Autowired
    private TaskGoodsRelationService taskGoodsRelationService;

    @Autowired
    private UserSelectedTerminalService userSelectedTerminalService;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private UserTaskGoodsService userTaskGoodsService;

    @Autowired
    private TaskOrderService taskOrderService;

    @Autowired
    private SaTaskRegisterUserService saTaskRegisterUserService;

    @Autowired
    private SaTaskRegisterTerminalService saTaskRegisterTerminalService;

    @Autowired
    private TaskDistributorService taskDistributorService;

    @Autowired
    private TaskMemberRecordService taskMemberRecordService;

    @Autowired
    private AccompanyingBillService accompanyingBillService;

    @Autowired
    private AccompanyingBillMatchService accompanyingBillMatchService;

    @Autowired
    private TaskAccompanyingBillService taskAccompanyingBillService;

    @Override
    public void addTask(AddTaskRequest addTaskRequest) {
        marketTaskService.addTask(addTaskRequest);
    }

    @Override
    public TaskCountDTO getTaskCount() {
        return marketTaskService.getTaskCount();
    }

    @Override
    public Boolean deleteDept(DeleteDeptRequest request) {
        return taskDeptUserService.deleteDept(request);
    }

    @Override
    public Boolean deleteGoods(DeleteGoodsRequest request) {
        return taskGoodsRelationService.deleteGoods(request);
    }

    @Override
    public Integer getTaskStatusByDeptOrGoods(Long businessId, int type) {
        Long taskId;
        if (type==1){
            TaskDeptUserDO taskDeptUser = taskDeptUserService.getById(businessId);
            if(Objects.isNull(taskDeptUser)){
                throw new BusinessException(AssistantErrorCode.DATA_NOT_EXIST);
            }
            taskId=taskDeptUser.getTaskId();
        } else {
            TaskGoodsRelationDO taskGoodsRelation = taskGoodsRelationService.getById(businessId);
            if(Objects.isNull(taskGoodsRelation)){
                throw new BusinessException(AssistantErrorCode.DATA_NOT_EXIST);
            }
            taskId=taskGoodsRelation.getTaskId();
        }
        return marketTaskService.getTaskStatus(taskId);
    }

    @Override
    public void updateTask(UpdateTaskRequest updateTaskRequest) {
        marketTaskService.updateTask(updateTaskRequest);
    }

    @Override
    public TaskDetailDTO getDetailById(Long id) {
        return marketTaskService.getDetailById(id);

    }

    @Override
    public Page<LockTerminalListDTO> getLockTerminalList(QueryLockTerminalListRequest request) {
        Page<UserSelectedTerminalDO> page = userSelectedTerminalService.getLockTerminalList(request);
        return PojoUtils.map(page,LockTerminalListDTO.class);
    }

    @Override
    public Page<TaskDTO> queryTaskListPage(QueryTaskPageRequest queryTaskPageRequest) {
        return marketTaskService.queryTaskListPage(queryTaskPageRequest);
    }



    @Override
    public TaskTraceDTO queryTaskTrace(Long taskId) {
        return marketTaskService.getTaskTrace(taskId);
    }

    @Override
    public Page<TaskTraceUserDTO> queryTaskUser(QueryTaskTraceTaskUserRequest queryTaskTraceTaskUserRequest) {
        return userTaskService.queryTaskUser(queryTaskTraceTaskUserRequest);
    }

    @Override
    public List<TaskTraceGoodsDTO> listTaskTraceGoods(QueryTaskTraceGoodsRequest queryTaskTraceGoodsRequest) {
        return userTaskGoodsService.listTaskTraceGoods(queryTaskTraceGoodsRequest);
    }

    @Override
    public Page<TaskTraceOrderDTO> listTaskTraceOrderPage(QueryTaskTraceOrderRequest queryTaskTraceOrderRequest) {
        return taskOrderService.listTaskTraceOrderPage(queryTaskTraceOrderRequest);
    }

    @Override
    public Page<TaskTraceRegisterUserDTO> listTaskRegisterUserPage(QueryTaskRegisterUserRequest queryTaskRegisterUserRequest) {
        return saTaskRegisterUserService.listTaskRegisterUserPage(queryTaskRegisterUserRequest);
    }

    @Override
    public Page<TaskTraceTerminalDTO> listTaskTraceTerminalPage(QueryTaskRegisterTerminalRequest queryTaskRegisterTerminalRequest) {
        return saTaskRegisterTerminalService.listTaskTraceTerminalPage(queryTaskRegisterTerminalRequest);
    }

	@Override
	public List<TaskDTO> queryMarketTaskListById(List<Long> ids) {
    	if (CollUtil.isEmpty(ids)){
    		return ListUtil.toList();
		}
		List<MarketTaskDO> marketTaskDOS = marketTaskService.listByIds(ids);
		return PojoUtils.map(marketTaskDOS,TaskDTO.class);
	}

	@Override
	public String getRuleValue(Long taskId, Integer ruleType, String ruleKey) {
		return marketTaskService.getRuleValue(taskId,ruleType,ruleKey);
	}

    @Override
    public List<TaskAreaDTO> queryTaskArea(Long taskId) {
        return marketTaskService.queryTaskArea(taskId);
    }

    @Override
    public Page<TaskSelectGoodsDTO> queryGoodsForAdd(QueryGoodsPageRequest queryGoodsPageRequest) {
        // 创建任务选择商品
        return taskGoodsRelationService.queryGoodsForAdd(queryGoodsPageRequest);
    }

    @Override
    public Page<DistributorDTO> listDistributorPage(QueryDistributorPageRequest queryDistributorPageRequest) {
        return taskDistributorService.listDistributorPage(queryDistributorPageRequest);
    }

    @Override
    public Page<DistributorDTO> listTaskDistributorPage(QueryTaskDistributorPageRequest queryTaskDistributorPageRequest) {
        return taskDistributorService.listTaskDistributorPage(queryTaskDistributorPageRequest);
    }

    @Override
    public Page<TaskMemberRecordDTO> listTaskMemberPage(QueryTaskMemberPageRequest queryTaskMemberPageRequest) {
        return PojoUtils.map(taskMemberRecordService.listTaskMemberPage(queryTaskMemberPageRequest),TaskMemberRecordDTO.class) ;
    }

    @Override
    public void deleteTaskDistributorById(DeleteTaskDistributorRequest deleteTaskDistributorRequest) {
        taskDistributorService.deleteById(deleteTaskDistributorRequest);
    }

    @Override
    public void stopTask(StopTaskRequest stopTaskRequest) {
        marketTaskService.stopTask(stopTaskRequest);
    }

    @Override
    public void publishTask() {
        marketTaskService.publishTask();
    }

    @Override
    public void endTask() {
        marketTaskService.endTask();
    }

    @Override
    public void deleteTask(StopTaskRequest stopTaskRequest) {
        marketTaskService.deleteTask(stopTaskRequest);
    }

    @Override
    public void handleTaskOrder(AddTaskOrderRequest addTaskOrderRequest) {
        marketTaskService.handleTaskOrder(addTaskOrderRequest);
    }

    @Override
    public AddTaskOrderRequest getOrderByNo(String orderNo) {
        return marketTaskService.getOrderByNo(orderNo);
    }

    @Override
    public List<Long> queryDistributorByEidList(QueryTaskDistributorRequest request) {
        return taskDistributorService.queryDistributorByEidList(request);
    }

    @Override
    public List<TaskGoodsMatchListDTO>  queryTaskGoodsList(List<QueryTaskGoodsMatchRequest> request) {
        return taskGoodsRelationService.queryTaskGoodsList(request);
    }

    @Override
    public Page<AccompanyingBillDTO> queryAccompanyingBillPage(QueryAccompanyingBillPage request) {
        return accompanyingBillService.queryPage(request);
    }

    @Override
    public AccompanyingBillDTO getAccompanyingBillDetailById(Long id) {
        return accompanyingBillService.getDetailById(id);
    }

    @Override
    public void auditAccompanyingBill(SaveAccompanyingBillRequest request) {
        accompanyingBillService.auditAccompanyingBill(request);
    }

    @Override
    public Page<TaskAccompanyingBillDTO> queryTaskAccompanyBillPage(QueryTaskAccompanyBillPageRequest queryTaskAccompanyBillPageRequest) {
        return taskAccompanyingBillService.queryPage(queryTaskAccompanyBillPageRequest);
    }

    @Override
    public Page<AccompanyingBillMatchDTO> queryMatchBillPage(QueryMatchBillPageRequest request) {
        return accompanyingBillMatchService.queryPage(request);
    }

    @Override
    public AccompanyingBillMatchDTO getMatchBillDetail(Long id) {
        return accompanyingBillMatchService.getDetail(id);
    }

    @Override
    public void billFlowMatchTimer() {
         accompanyingBillMatchService.billFlowMatchTimer();
    }

    @Override
    public void handleTaskCommissionForUpload() {
        userTaskService.handleTaskCommissionForUpload();
    }
}