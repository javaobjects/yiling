package com.yiling.sales.assistant.task.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yiling.sales.assistant.BaseTest;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskGoodsDO;
import com.yiling.sales.assistant.task.enums.UserTaskStatusEnum;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.UserTaskService;

/**
 * @author: ray
 * @date: 2021/11/11
 */
class MarketTaskServiceImplTest extends BaseTest {

    @Autowired
    MarketTaskService marketTaskService;

    @Autowired
    UserTaskService userTaskService;
    @Test
    void handleTaskOrder() {
        List<AddTaskOrderGoodsRequest> orderGoodsAddDTOList = Lists.newArrayList();
        AddTaskOrderGoodsRequest add1 = new AddTaskOrderGoodsRequest();
        AddTaskOrderGoodsRequest add2 = new AddTaskOrderGoodsRequest();
        orderGoodsAddDTOList.add(add1);
        orderGoodsAddDTOList.add(add2);
        add1.setAmount(335).setGoodName("测试商品1").setGoodsId(1L).setPrice(new BigDecimal(3.33)).setSpecifications("测试规格1");
        add2.setAmount(2).setGoodName("测试商品2").setGoodsId(2L).setPrice(new BigDecimal(10)).setSpecifications("测试规格2");

        AddTaskOrderRequest addTaskOrderRequest = new AddTaskOrderRequest();
       // addTaskOrderRequest.setIsFirstOrder(false);
        addTaskOrderRequest.setOrderId(2L).setOrderNo("110").setOrderTime(new Date()).setTerminalId(1L).setTerminalName("测试终端").setTotalAmount(new BigDecimal(30)).setUserId(108L)
                .setUserName("测试用户名 ").setOrderGoodsAddDTOList(orderGoodsAddDTOList);
        marketTaskService.handleTaskOrder(addTaskOrderRequest);
    }

    @Test
    void handleTaskOrderwithstep() {
        List<AddTaskOrderGoodsRequest> orderGoodsAddDTOList = Lists.newArrayList();
        AddTaskOrderGoodsRequest add1 = new AddTaskOrderGoodsRequest();
        orderGoodsAddDTOList.add(add1);
        add1.setAmount(1000).setGoodName("测试商品1").setGoodsId(1L).setPrice(new BigDecimal(500)).setSpecifications("测试规格1");

        AddTaskOrderRequest addTaskOrderRequest = new AddTaskOrderRequest();
       // addTaskOrderRequest.setIsFirstOrder(false);
        addTaskOrderRequest.setOrderId(1L).setOrderNo("11000101011111").setOrderTime(new Date()).setTerminalId(1L).setTerminalName("测试终端").setTotalAmount(new BigDecimal(2)).setUserId(108L)
                .setUserName("测试用户名 ").setOrderGoodsAddDTOList(orderGoodsAddDTOList);
        marketTaskService.handleTaskOrder(addTaskOrderRequest);
    }

    @Test
    void takeEnterpriseTask(){
        UserTaskDO userTaskDO = new UserTaskDO();

        userTaskDO.setFinishValue(0).setTaskStatus(UserTaskStatusEnum.IN_PROGRESS.getStatus()).setPercent("0").setFinishGoods(0).setTaskId(23L).setTaskType(1).setFinishType(1)
                .setEid(0L).setGoal(1000).setTaskGoodsTotal(1).setUserId(7L).setMobile("15600710212").setUserName("企业员工名称");
        UserTaskGoodsDO userTaskGoodsDO = new UserTaskGoodsDO();
        userTaskGoodsDO.setFinishValue(0).setPercent("0").setGoalValue(0).setGoodsId(1l).setValueType(1).setUserId(7L).setGoalValue(1000).setTaskId(23L).setGoodsName("c测试企业商品");
        List<UserTaskGoodsDO> list = Lists.newArrayList();
        list.add(userTaskGoodsDO);
        userTaskService.saveTask(userTaskDO,list,null);
    }

    @Test
    void updateMemberBuyProgress(){
/*        UpdateUserTaskMemberRequest updateUserTaskMemberRequest = new UpdateUserTaskMemberRequest();
        updateUserTaskMemberRequest.setContactorPhone("13032219922").setContactorUserId(1L).setEid(1L).setEname("测试企业").setFinishTypeEnum(FinishTypeEnum.MEMBER_BUY)
                .setTradeTime(new Date()).setMemberId(1L).setMemberStageId(2L).setPromoterId(4L);
        userTaskService.updateMemberBuyProgress(updateUserTaskMemberRequest);*/
    }
}