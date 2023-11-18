package com.yiling.sales.assistant.task.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

/**
 * 任务
 * @author: ray
 * @date: 2021/9/13
 */
public interface TaskApi {

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
     * 移除部门
     * @param request
     * @return
     */
    Boolean deleteDept(DeleteDeptRequest request);

    /**
     * 移除部门药品
     * @param request
     * @return
     */
    Boolean deleteGoods(DeleteGoodsRequest request);

    /**
     * 获取任务状态
     * @param businessId  业务id
     * @param type   1-根据部门查询 2-根据商品查询
     * @return
     */
    Integer getTaskStatusByDeptOrGoods(Long businessId, int type);

    /**
     * 更新任务
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
     *  查询锁定终端列表
     * @param request
     * @return
     */
    Page<LockTerminalListDTO> getLockTerminalList(QueryLockTerminalListRequest request);

    /**
     * 任务分页列表
     * @param queryTaskPageRequest
     * @return
     */
    Page<TaskDTO> queryTaskListPage(QueryTaskPageRequest queryTaskPageRequest);


    /**
     * 任务追踪-头部
     * @param taskId
     * @return
     */
    TaskTraceDTO queryTaskTrace(Long taskId);

    /**
     * 任务追踪-承接人员明细进度
     * @param queryTaskTraceTaskUserRequest
     * @return
     */
    Page<TaskTraceUserDTO> queryTaskUser(QueryTaskTraceTaskUserRequest queryTaskTraceTaskUserRequest);

    /**
     * 任务追踪-任务品完成进度查询
     * @param queryTaskTraceGoodsRequest
     * @return
     */
    List<TaskTraceGoodsDTO> listTaskTraceGoods(QueryTaskTraceGoodsRequest queryTaskTraceGoodsRequest);

    /**
     * 任务追踪-销售记录
     * @param queryTaskTraceOrderRequest
     * @return
     */
    Page<TaskTraceOrderDTO> listTaskTraceOrderPage(QueryTaskTraceOrderRequest queryTaskTraceOrderRequest);

    /**
     * 任务追踪-拉人类
     * @param queryTaskRegisterUserRequest
     * @return
     */
    Page<TaskTraceRegisterUserDTO> listTaskRegisterUserPage(QueryTaskRegisterUserRequest queryTaskRegisterUserRequest);

    /**
     * 任务追踪-拉户类
     * @param queryTaskRegisterTerminalRequest
     * @return
     */
    Page<TaskTraceTerminalDTO> listTaskTraceTerminalPage(QueryTaskRegisterTerminalRequest queryTaskRegisterTerminalRequest);

	/**
	 * 根据marketTask主键id查询任务列表
	 *
	 * @param ids
	 * @return
	 */
	List<TaskDTO> queryMarketTaskListById(List<Long> ids);


	/**
	 *查询任务计算方式
	 *
	 * @param taskId
	 * @param ruleType
	 * @param ruleKey
	 * @return
	 */
	String getRuleValue(Long taskId,Integer ruleType,String ruleKey);

    /**
     * 任务区域反显
     * @param taskId
     * @return
     */
    List<TaskAreaDTO> queryTaskArea(Long taskId);

    /**
     * 创建任务-选择商品
     * @param queryGoodsPageRequest
     * @return
     */
    Page<TaskSelectGoodsDTO> queryGoodsForAdd(QueryGoodsPageRequest queryGoodsPageRequest);


    /**
     * 任务创建-选择供应商
     * @param queryDistributorPageRequest
     * @return
     */
    Page<DistributorDTO> listDistributorPage(QueryDistributorPageRequest queryDistributorPageRequest);

    /**
     * 任务编辑-供应商反显
     * @param queryTaskDistributorPageRequest
     * @return
     */
    Page<DistributorDTO> listTaskDistributorPage(QueryTaskDistributorPageRequest queryTaskDistributorPageRequest);

    /**
     * 任务追踪-推广记录
     * @param queryTaskMemberPageRequest
     * @return
     */
    Page<TaskMemberRecordDTO> listTaskMemberPage(QueryTaskMemberPageRequest queryTaskMemberPageRequest);

    /**
     * 删除配送商
     * @param deleteTaskDistributorRequest
     */
    void deleteTaskDistributorById(DeleteTaskDistributorRequest deleteTaskDistributorRequest) ;

    /**
     * 任务停用
     * @param stopTaskRequest
     */
    void stopTask(StopTaskRequest stopTaskRequest);

    /**
     * 定时任务扫描任务发布
     */
    void publishTask();

    /**
     * 定时任务扫描到达结束时间的任务
     */
    void endTask();

    /**
     * 删除任务
     * @param stopTaskRequest
     */
    void deleteTask(StopTaskRequest stopTaskRequest);

    /**
     * 任务数据补偿
     * @param addTaskOrderRequest
     */
    void handleTaskOrder(AddTaskOrderRequest addTaskOrderRequest);

    /**
     *
     * @param orderNo
     * @return
     */
    AddTaskOrderRequest getOrderByNo(String orderNo);

    /**
     * 根据配送商eid查询用户承接任务配送商eid
     * @param request
     * @return
     */
    List<Long> queryDistributorByEidList(QueryTaskDistributorRequest request);

    /**
     * 查询用承接任务中的以岭品
     * @param request
     * @return
     */
    List<TaskGoodsMatchListDTO> queryTaskGoodsList(List<QueryTaskGoodsMatchRequest> request);

    /**
     * 运营后台随货同行单分页列表
     * @param request
     * @return
     */
    Page<AccompanyingBillDTO> queryAccompanyingBillPage(QueryAccompanyingBillPage request);

    /**
     * 单据详情
     * @param id
     * @return
     */
    AccompanyingBillDTO getAccompanyingBillDetailById(Long id);

    /**
     * 审核随货同行单
     * @param request
     */
    void auditAccompanyingBill(SaveAccompanyingBillRequest request);

    /**
     * 用户任务随货同行单列表
     * @param queryTaskAccompanyBillPageRequest
     * @return
     */
    Page<TaskAccompanyingBillDTO> queryTaskAccompanyBillPage(QueryTaskAccompanyBillPageRequest queryTaskAccompanyBillPageRequest);

    /**
     * 随货同行单匹配流向分页列表
     * @param request
     * @return
     */
    Page<AccompanyingBillMatchDTO> queryMatchBillPage(QueryMatchBillPageRequest request);

    /**
     * 随货同行单匹配流向分页列表详情
     * @param id
     * @return
     */
    AccompanyingBillMatchDTO getMatchBillDetail(Long id);

    /**
     * 匹配流向定时任务
     */
    void billFlowMatchTimer();

    /**
     * 变更上传随货同行单任务状态和佣金状态定时任务 28号
     */
    void handleTaskCommissionForUpload();
}
