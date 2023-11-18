package com.yiling.sales.assistant.app.task.controller;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.sales.assistant.app.task.form.InviteTaskMemberForm;
import com.yiling.sales.assistant.app.task.form.QueryMyTaskDetailForm;
import com.yiling.sales.assistant.app.task.form.QueryMyTaskForm;
import com.yiling.sales.assistant.app.task.form.QueryMyTaskOrderForm;
import com.yiling.sales.assistant.app.task.form.QueryTaskDistributorPageForm;
import com.yiling.sales.assistant.app.task.form.QueryTaskGoodsForm;
import com.yiling.sales.assistant.app.task.form.QueryTaskMemberPageForm;
import com.yiling.sales.assistant.app.task.form.QueryTaskPageForm;
import com.yiling.sales.assistant.app.task.form.QueryTaskRegisterTerminalForm;
import com.yiling.sales.assistant.app.task.form.QueryTaskRegisterUserForm;
import com.yiling.sales.assistant.app.task.form.QueryTaskTerminalPageForm;
import com.yiling.sales.assistant.app.task.form.ReadModifyNoticeForm;
import com.yiling.sales.assistant.app.task.form.TakeTaskForm;
import com.yiling.sales.assistant.app.task.vo.MyTaskDetailVO;
import com.yiling.sales.assistant.app.task.vo.MyTaskOrderVO;
import com.yiling.sales.assistant.app.task.vo.MyTaskProgressVO;
import com.yiling.sales.assistant.app.task.vo.MyTaskVO;
import com.yiling.sales.assistant.app.task.vo.TaskDetailVO;
import com.yiling.sales.assistant.app.task.vo.TaskDistributorVO;
import com.yiling.sales.assistant.app.task.vo.TaskGoodsVO;
import com.yiling.sales.assistant.app.task.vo.TaskMemberRecordVO;
import com.yiling.sales.assistant.app.task.vo.TaskTerminalVO;
import com.yiling.sales.assistant.app.task.vo.TaskTraceRegisterUserVO;
import com.yiling.sales.assistant.app.task.vo.TaskTraceTerminalVO;
import com.yiling.sales.assistant.app.task.vo.TaskVO;
import com.yiling.sales.assistant.task.api.AppTaskApi;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.constant.TaskConstant;
import com.yiling.sales.assistant.task.dto.TaskMemberRecordDTO;
import com.yiling.sales.assistant.task.dto.TaskTerminalDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceOrderDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceRegisterUserDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceTerminalDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskProgressDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDistributorDTO;
import com.yiling.sales.assistant.task.dto.app.TaskGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskMemberPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterTerminalRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskRegisterUserRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceOrderRequest;
import com.yiling.sales.assistant.task.dto.request.app.GetTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.InviteTaskMemberRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskTerminalPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.TakeTaskRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 任务
 * @author: ray
 * @date: 2021/9/27
 */
@Api(tags = "任务")
@RestController
@Validated
@RequestMapping("task")
public class TaskController extends BaseController {

    @DubboReference
    AppTaskApi appTaskApi;

    @DubboReference
    TaskApi taskApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Autowired
    private RedisService redisService;

    /**
     *
     * @param staffInfo
     * @param queryTaskPageForm
     * @return
     */
    @ApiOperation(value = "任务列表")
    @GetMapping("listTaskPage")
    public Result<Page<TaskVO>> listTaskPage(@CurrentUser CurrentStaffInfo staffInfo, QueryTaskPageForm queryTaskPageForm){
        QueryTaskPageRequest queryTaskPageRequest = new QueryTaskPageRequest();
        PojoUtils.map(queryTaskPageForm,queryTaskPageRequest);
        queryTaskPageRequest.setUserId(staffInfo.getCurrentUserId()).setUserType(staffInfo.getUserType());
        queryTaskPageRequest.setEid(staffInfo.getCurrentEid());
        Page<TaskDTO> taskDTOPage = appTaskApi.listTaskPage(queryTaskPageRequest);
        Page<TaskVO> taskVOPage = PojoUtils.map(taskDTOPage,TaskVO.class);
        return Result.success(taskVOPage);
    }

    @ApiOperation(value = "任务详情")
    @GetMapping("getTaskDetail")
    public Result<TaskDetailVO> getTaskDetail(@CurrentUser CurrentStaffInfo staffInfo, Long taskId){
        GetTaskDetailRequest getTaskDetailRequest = new GetTaskDetailRequest();
        getTaskDetailRequest.setTaskId(taskId);
        getTaskDetailRequest.setUserId(staffInfo.getCurrentUserId());
        TaskDetailDTO taskDetailDTO = appTaskApi.getTaskDetail(getTaskDetailRequest);
        TaskDetailVO taskVO = PojoUtils.map(taskDetailDTO,TaskDetailVO.class);
        taskVO.setId(taskId);
        return Result.success(taskVO);
    }

    @ApiOperation("查询任务详情-商品列表")
    @GetMapping("listTaskGoods")
    public Result<CollectionObject<List<TaskGoodsVO>>> listTaskGoods(QueryTaskGoodsForm queryTaskGoodsForm){
        QueryTaskGoodsRequest queryTaskGoodsRequest = new QueryTaskGoodsRequest();
        PojoUtils.map(queryTaskGoodsForm,queryTaskGoodsRequest);
        List<TaskGoodsDTO> taskGoodsDTOPage = appTaskApi.listTaskGoods(queryTaskGoodsRequest);
        List<TaskGoodsVO> taskGoodsVOPage = PojoUtils.map(taskGoodsDTOPage,TaskGoodsVO.class);
        CollectionObject<List<TaskGoodsVO>> result = new CollectionObject(taskGoodsVOPage);

        return Result.success(result);
    }
/*    @ApiOperation("查询任务详情-满赠会员")
    @GetMapping("getMemberPromotion")
    public Result<TaskMemberPromotiondVO> getMemberPromotion(Long taskId){
        TaskMemberPromotiondDTO memberPromotion = appTaskApi.getMemberPromotion(taskId);
        TaskMemberPromotiondVO taskMemberPromotiondVO = new TaskMemberPromotiondVO();
        PojoUtils.map(memberPromotion,taskMemberPromotiondVO);
        return Result.success(taskMemberPromotiondVO);
    }*/

    @ApiOperation(value = "我的任务列表")
    @GetMapping("listMyTaskPage")
    public Result<Page<MyTaskVO>> listMyTaskPage(@CurrentUser CurrentStaffInfo staffInfo,QueryMyTaskForm queryMyTaskForm){
        QueryMyTaskRequest queryMyTaskRequest = new QueryMyTaskRequest();
        PojoUtils.map(queryMyTaskForm,queryMyTaskRequest);
        queryMyTaskRequest.setOpUserId(staffInfo.getCurrentUserId());
        Page<MyTaskDTO> myTaskDTOPage = appTaskApi.listMyTaskPage(queryMyTaskRequest);
        Page<MyTaskVO> myTaskVOPage = PojoUtils.map(myTaskDTOPage,MyTaskVO.class);
        return Result.success(myTaskVOPage);
    }

    @ApiOperation(value = "我的任务详情")
    @GetMapping("getMyTaskDetail")
    public Result<MyTaskDetailVO> getMyTaskDetail(@CurrentUser CurrentStaffInfo staffInfo,QueryMyTaskDetailForm queryMyTaskDetailForm){
        QueryMyTaskDetailRequest queryMyTaskRequest = new QueryMyTaskDetailRequest();
        PojoUtils.map(queryMyTaskDetailForm,queryMyTaskRequest);
        queryMyTaskRequest.setUserId(staffInfo.getCurrentUserId()).setEid(staffInfo.getCurrentEid());
        MyTaskDetailDTO myTaskDetailDTO = appTaskApi.getMyTaskDetail(queryMyTaskRequest);
        MyTaskDetailVO myTaskDetailVO = new MyTaskDetailVO();
        PojoUtils.map(myTaskDetailDTO,myTaskDetailVO);
        return Result.success(myTaskDetailVO);
    }
    @ApiOperation(value = "商品或配送商弹窗已读")
    @PostMapping("readNotify")
    public Result<Boolean> readNotify(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody ReadModifyNoticeForm form){
        String key = String.format(TaskConstant.ADD_GOODS_OR_DISTRIBUTOR_NOTIFY, form.getUserTaskId());
        Boolean del = redisService.del(key);
        return Result.success(del);
    }
    @ApiOperation(value = "我的任务详情-任务进度")
    @GetMapping("getMyTaskProgress")
    public Result<MyTaskProgressVO> getMyTaskProgress(Long userTaskId){
        MyTaskProgressDTO myTaskProgress = appTaskApi.getMyTaskProgress(userTaskId);
        MyTaskProgressVO myTaskProgressVO = new MyTaskProgressVO();
        PojoUtils.map(myTaskProgress,myTaskProgressVO);
        return Result.success(myTaskProgressVO);
    }
/*    @ApiOperation(value = "我的任务详情-多品销售每个商品的进度")
    @GetMapping("getGoodsProgress")
    public Result<CollectionObject<List<GoodsProgressVO>>> getGoodsProgress(Long userTaskId){
        List<GoodsProgressDTO> goodsProgress = appTaskApi.getGoodsProgress(userTaskId);
        List<GoodsProgressVO> goodsProgressVOS = PojoUtils.map(goodsProgress,GoodsProgressVO.class);
        CollectionObject<List<GoodsProgressVO>> collectionObject = new CollectionObject(goodsProgressVOS);
        return Result.success(collectionObject);
    }*/
   /* @ApiOperation(value = "我的任务详情-已锁定终端")
    @GetMapping("listMyTerminalPage")
    public Result<Page<MyTerminalVO>> listMyTerminalPage(QueryLockTerminalListForm queryLockTerminalListForm){
        QueryLockTerminalListRequest request = new QueryLockTerminalListRequest();
                PojoUtils.map(queryLockTerminalListForm,request);
        Page<LockTerminalListDTO> pageList = taskApi.getLockTerminalList(request);
        List<LockTerminalListDTO> records = pageList.getRecords();
        if (CollUtil.isEmpty(records)){
            return Result.success(request.getPage());
        }
        //终端主键（enterprise_id ）
        List<Long> terminalIdList = records.stream().map(LockTerminalListDTO::getTerminalId).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(terminalIdList);
        Map<Long, EnterpriseDTO> dtoMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        List<MyTerminalVO> list = Lists.newArrayList();
        records.forEach(e -> {
            MyTerminalVO listVO = PojoUtils.map(e, MyTerminalVO.class);
            EnterpriseDTO enterpriseDTO = dtoMap.get(e.getTerminalId());
            listVO.setContactor(enterpriseDTO.getContactor());
            listVO.setContactorPhone(enterpriseDTO.getContactorPhone());
            list.add(listVO);
        });
        Page<MyTerminalVO> page = PojoUtils.map(pageList, MyTerminalVO.class);
        page.setRecords(list);
        return Result.success(page);
    }*/
    @ApiOperation(value = "我的任务详情-记录查看-销售类")
    @GetMapping("listTaskTraceOrderPage")
    public Result<Page<MyTaskOrderVO>> listTaskTraceOrderPage(QueryMyTaskOrderForm queryTaskTraceOrderForm){
        QueryTaskTraceOrderRequest queryTaskTraceOrderRequest = new QueryTaskTraceOrderRequest();
        PojoUtils.map(queryTaskTraceOrderForm,queryTaskTraceOrderRequest);
        Page<TaskTraceOrderDTO> taskTraceOrderDTOPage = taskApi.listTaskTraceOrderPage(queryTaskTraceOrderRequest);
        Page<MyTaskOrderVO> taskTraceOrderVOPage = PojoUtils.map(taskTraceOrderDTOPage,MyTaskOrderVO.class);
        return Result.success(taskTraceOrderVOPage);
    }

    @ApiOperation("我的任务详情-记录查看-拉人类")
    @GetMapping("listTaskRegisterUserPage")
    public Result<Page<TaskTraceRegisterUserVO>> listTaskRegisterUserPage(QueryTaskRegisterUserForm queryTaskRegisterUserForm){
        QueryTaskRegisterUserRequest queryTaskRegisterUserRequest = new QueryTaskRegisterUserRequest();
        PojoUtils.map(queryTaskRegisterUserForm,queryTaskRegisterUserRequest);
        Page<TaskTraceRegisterUserDTO> taskTraceRegisterUserDTOPage = taskApi.listTaskRegisterUserPage(queryTaskRegisterUserRequest);
        Page<TaskTraceRegisterUserVO> taskTraceRegisterUserVOPage = PojoUtils.map(taskTraceRegisterUserDTOPage,TaskTraceRegisterUserVO.class);
        return Result.success(taskTraceRegisterUserVOPage);
    }
    @ApiOperation("我的任务详情-记录查看-拉户类")
    @GetMapping("listTaskTraceTerminalPage")
    public Result<Page<TaskTraceTerminalVO>> listTaskTraceTerminalPage(QueryTaskRegisterTerminalForm queryTaskRegisterTerminalForm){
        QueryTaskRegisterTerminalRequest queryTaskRegisterTerminalRequest = new QueryTaskRegisterTerminalRequest();
        PojoUtils.map(queryTaskRegisterTerminalForm,queryTaskRegisterTerminalRequest);
        Page<TaskTraceTerminalDTO> taskTraceTerminalDTOPage = taskApi.listTaskTraceTerminalPage(queryTaskRegisterTerminalRequest);
        Page<TaskTraceTerminalVO> taskTraceTerminalVOPage = PojoUtils.map(taskTraceTerminalDTOPage,TaskTraceTerminalVO.class);
        return Result.success(taskTraceTerminalVOPage);
    }

    @ApiOperation("我的终端")
    @GetMapping("listTaskTerminalPage")
    public Result<Page<TaskTerminalVO>> listTaskTerminalPage(@CurrentUser CurrentStaffInfo staffInfo,QueryTaskTerminalPageForm queryTaskTerminalPageForm){
        QueryTaskTerminalPageRequest queryTaskTerminalPageRequest = new QueryTaskTerminalPageRequest();
        queryTaskTerminalPageRequest.setUserId(staffInfo.getCurrentUserId());
        PojoUtils.map(queryTaskTerminalPageForm,queryTaskTerminalPageRequest);
        Page<TaskTerminalDTO> taskTerminalDTOPage = appTaskApi.listTaskTerminalPage(queryTaskTerminalPageRequest);
        Page<TaskTerminalVO> terminalVOPage = PojoUtils.map(taskTerminalDTOPage,TaskTerminalVO.class);
        return Result.success(terminalVOPage);
    }
    @ApiOperation("可选终端")
    @GetMapping("listTaskAllTerminalPage")
    public Result<Page<TaskTerminalVO>>  listTaskAllTerminalPage(@CurrentUser CurrentStaffInfo staffInfo,QueryTaskTerminalPageForm queryTaskTerminalPageForm){
        QueryTaskTerminalPageRequest queryTaskTerminalPageRequest = new QueryTaskTerminalPageRequest();
        queryTaskTerminalPageRequest.setUserId(staffInfo.getCurrentUserId());
        PojoUtils.map(queryTaskTerminalPageForm,queryTaskTerminalPageRequest);
        Page<TaskTerminalDTO> taskTerminalDTOPage = appTaskApi.listTaskAllTerminalPage(queryTaskTerminalPageRequest);
        Page<TaskTerminalVO> terminalVOPage = PojoUtils.map(taskTerminalDTOPage,TaskTerminalVO.class);
        return Result.success(terminalVOPage);
    }
    @ApiOperation("承接任务")
    @PostMapping("takeTask")
    public Result<Boolean> takeTask(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody TakeTaskForm takeTaskForm){
        TakeTaskRequest takeTaskRequest = new TakeTaskRequest();
        PojoUtils.map(takeTaskForm,takeTaskRequest);
        takeTaskRequest.setUserId(staffInfo.getCurrentUserId());
        takeTaskRequest.setCurrentEid(staffInfo.getCurrentEid());
        takeTaskRequest.setUserType(staffInfo.getUserType());
        Boolean result = appTaskApi.takeTask(takeTaskRequest);
        return Result.success(result);
    }

    @ApiOperation("任务追踪-推广类")
    @GetMapping("listTaskMemberPage")
    public Result<Page<TaskMemberRecordVO>> listTaskMemberPage(QueryTaskMemberPageForm queryTaskMemberPageForm){
        QueryTaskMemberPageRequest queryTaskMemberPageRequest = new QueryTaskMemberPageRequest();
        PojoUtils.map(queryTaskMemberPageForm,queryTaskMemberPageRequest);
        Page<TaskMemberRecordDTO> recordDTOPage = taskApi.listTaskMemberPage(queryTaskMemberPageRequest);
        Page<TaskMemberRecordVO> taskTraceTerminalVOPage = PojoUtils.map(recordDTOPage,TaskMemberRecordVO.class);
        return Result.success(taskTraceTerminalVOPage);
    }
    @ApiOperation("会员购买任务-分享")
    @GetMapping("getPromotionPic")
    public Result<String> getPromotionPic(@CurrentUser CurrentStaffInfo staffInfo,InviteTaskMemberForm inviteTaskMemberForm){
        InviteTaskMemberRequest inviteTaskMemberRequest = new InviteTaskMemberRequest();
        inviteTaskMemberRequest.setUserTaskId(inviteTaskMemberForm.getUserTaskId());
        inviteTaskMemberRequest.setTaskId(inviteTaskMemberForm.getTaskId());
        inviteTaskMemberRequest.setOpUserId(staffInfo.getCurrentUserId());
        inviteTaskMemberRequest.setEid(staffInfo.getCurrentEid());
        String result = appTaskApi.getPromotionPic(inviteTaskMemberRequest);
        return Result.success(result);
    }
    @ApiOperation("任务详情-参与配送商")
    @GetMapping("listTaskDistributorPage")
    public Result<Page<TaskDistributorVO>> listTaskDistributorPage(QueryTaskDistributorPageForm queryTaskDistributorPageForm){
        QueryTaskDistributorPageRequest queryTaskDistributorPageRequest = new  QueryTaskDistributorPageRequest();
        PojoUtils.map(queryTaskDistributorPageForm,queryTaskDistributorPageRequest);
        Page<TaskDistributorDTO> taskDistributorDTOPage = appTaskApi.listTaskDistributorPage(queryTaskDistributorPageRequest);
        Page<TaskDistributorVO> taskDistributorVOPage = PojoUtils.map(taskDistributorDTOPage,TaskDistributorVO.class);
        return Result.success(taskDistributorVOPage);
    }


}