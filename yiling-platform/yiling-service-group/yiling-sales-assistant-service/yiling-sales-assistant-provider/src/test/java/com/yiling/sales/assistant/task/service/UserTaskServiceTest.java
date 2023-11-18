package com.yiling.sales.assistant.task.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yiling.sales.assistant.BaseTest;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.UserTypeDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskGoodsMatchRequest;
import com.yiling.sales.assistant.task.dto.request.StopTaskRequest;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchDTO;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchListDTO;
import com.yiling.sales.assistant.task.dto.request.UpdateUserTaskMemberRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateUserTaskNoLimitRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskStepDO;
import com.yiling.sales.assistant.task.listener.OrderRecvListener;
import com.yiling.sales.assistant.task.service.strategy.AbstractTaskProgressHandler;
import com.yiling.user.system.enums.UserTypeEnum;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;

import cn.hutool.core.date.DateUtil;

/**
 * @author: ray
 * @date: 2022/2/9
 */
class UserTaskServiceTest extends BaseTest {

    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private OrderRecvListener orderRecvListener;
    @Autowired
    private MarketTaskService marketTaskService;

    @Autowired
    private AppMarketTaskService appMarketTaskService;

    @Autowired
    private TaskApi taskApi;
    @Autowired
    private TaskGoodsRelationService taskGoodsRelationService;

    @Resource(name = "amountProgresStrategy")
    private AbstractTaskProgressHandler abstractTaskProgressHandler;


    @Test
    void updateInviteCostumerProgress() {
        UserCustomerDTO userCustomerDTO = new UserCustomerDTO();
        Date date = DateUtil.parse("2021-11-17 00:00:00");
        userCustomerDTO.setAuditTime(new Date()).setCreateTime(new Date()).setUserId(108L).setCustomerEid(1L);
        userTaskService.updateInviteCostumerProgress(userCustomerDTO);
    }

    @Test
    void endTask(){
        marketTaskService.endTask();
    }

    @Test
    void inviteUserTask(){
        UpdateUserTaskNoLimitRequest updateUserTaskNoLimitRequest = new UpdateUserTaskNoLimitRequest();
        updateUserTaskNoLimitRequest.setInviterUserId(108L).setMobile("15600720124").setName("dev").setRegTime(new Date()).setUserId(1L);
        userTaskService.updateInviteUserProgress(updateUserTaskNoLimitRequest);

    }

    @Test
    void test2(){
        QueryTaskPageRequest queryTaskPageRequest = new QueryTaskPageRequest();
        queryTaskPageRequest.setUserType(UserTypeEnum.XIAOSANYUAN).setUserId(1L).setEid(40L).setTaskType(0);
        appMarketTaskService.listTaskPage(queryTaskPageRequest);
    }

    @Test
    void test3(){
        MarketTaskDO taskDO = new MarketTaskDO();
        taskDO.setId(110L);
        UserTaskStepDO userTaskStepDO = new UserTaskStepDO();
        UserTaskDO userTask = new UserTaskDO();
        userTask.setFinishValue(4000);
        abstractTaskProgressHandler.handleStep(taskDO,userTaskStepDO,userTask);
    }

    @Test
    void test4(){
        StopTaskRequest stopTaskRequest = new StopTaskRequest();
        stopTaskRequest.setId(26L);
        marketTaskService.stopTask(stopTaskRequest);
    }

    @Test
    void test5(){
        UserTypeDTO dto = userTaskService.getUserTypeByUserTaskId(23L);
        System.out.println(dto);
    }
    @Test
    void test6(){
        UpdateUserTaskMemberRequest updateUserTaskMemberRequest = new UpdateUserTaskMemberRequest();
        updateUserTaskMemberRequest.setPromoterUserId(113L).setCreateUser(1L).setMemberId(2L).setMemberStageId(1L).setOrderNo("31241241241242").setTradeTime(new Date())
                .setEid(1L).setEname("以岭药业").setContactorUserId(1L).setContactorPhone("1303221991");
        userTaskService.updateMemberBuyProgress(updateUserTaskMemberRequest);
    }
    @Test
    void test7(){
        userTaskService.rollbackUserTask("31241241241242");
    }
    @Test
    void test8(){
        QueryTaskDistributorRequest request = new QueryTaskDistributorRequest();
        List<Long> eids = Lists.newArrayList();
        eids.add(1L);
        request.setEidList(eids);
        request.setUserId(113L);
        List<Long> longs = taskApi.queryDistributorByEidList(request);
        System.out.println(longs.toString());
    }
    @Test
    void test9(){
        QueryTaskGoodsMatchRequest request = new QueryTaskGoodsMatchRequest();
        request.setUserId(113L);
        request.setGoodsName("小半夏fds");
        request.setEid(36L);
        List<QueryTaskGoodsMatchRequest> list = Lists.newArrayList();
        list.add(request);
        List<TaskGoodsMatchListDTO> taskGoodsMatchListDTOS = taskGoodsRelationService.queryTaskGoodsList(list);
        System.out.println(taskGoodsMatchListDTOS.toString());
    }
    @Test
    void newUserTask(){
        AddTaskOrderRequest order = marketTaskService.getOrderByNo("D20220303192445362177");
        userTaskService.newUserHandler(order);

    }

    @Test
    void  test10(){
        userTaskService.handleTaskCommissionForUpload();
    }
}