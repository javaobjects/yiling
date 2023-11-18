package com.yiling.sales.assistant.task.service.strategy.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.sales.assistant.task.constant.TaskConstant;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskOrderDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskGoodsDO;
import com.yiling.sales.assistant.task.entity.UserTaskStepDO;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.sales.assistant.task.enums.TaskTypeEnum;
import com.yiling.sales.assistant.task.enums.UserTaskStatusEnum;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.UserSelectedTerminalService;
import com.yiling.sales.assistant.task.service.UserTaskGoodsService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.sales.assistant.task.service.strategy.AbstractTaskProgressHandler;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gxl
 * @description:按交易量算任务进度
 * @date: Created in 16:18 2020/4/28
 * @modified By:
 */
@Slf4j
@Service("amountProgresStrategy")
public class AmountProgressStrategy extends AbstractTaskProgressHandler {

    @Autowired
    private UserTaskGoodsService userTaskGoodsService;

    @Autowired
    private MarketTaskService marketTaskService;

    @Autowired
    private UserTaskService userTaskService;


    @Autowired
    private RedisDistributedLock redisDistributedLock;


    @Autowired
    private UserSelectedTerminalService userSelectedTerminalService;
    @Override
    public Integer getCurrentType() {
        return FinishTypeEnum.AMOUNT.getCode();
    }

    @Override
    public Object taskHandle(MarketTaskDO taskDO, UserTaskDO userTask, TaskOrderDO taskOrder, List taskOrderGoodsDOList, Object extraParam) {
        Date now  = DateUtil.date();
        AddTaskOrderRequest addTaskOrderRequest = (AddTaskOrderRequest)extraParam;
        log.info("交易量任务执行");

        //计算每个商品的任务进度
        List<AddTaskOrderGoodsRequest> addTaskOrderGoodsRequests = addTaskOrderRequest.getOrderGoodsAddDTOList();
        //计算订单中每个商品的售卖总数量
        HashMap<Long,Integer> orderGoodsAmountMap  = Maps.newHashMapWithExpectedSize(addTaskOrderGoodsRequests.size());
        addTaskOrderGoodsRequests.forEach(goods->{
            orderGoodsAmountMap.put(goods.getGoodsId(),goods.getAmount());
        });
        LambdaQueryWrapper<UserTaskGoodsDO> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(UserTaskGoodsDO::getUserTaskId,userTask.getId());
        List<UserTaskGoodsDO> userTaskGoodsList = userTaskGoodsService.list(lambdaQuery);
        List<Long> orderGoodsIds  = addTaskOrderGoodsRequests.stream().map(AddTaskOrderGoodsRequest::getGoodsId).collect(Collectors.toList());

        userTaskGoodsList.stream().filter(u->orderGoodsIds.contains(u.getGoodsId())).collect(Collectors.toList()) .forEach(userTaskGoods -> {
            userTaskGoods.setFinishValue(userTaskGoods.getFinishValue()+orderGoodsAmountMap.get(userTaskGoods.getGoodsId()));
            int percent = BigDecimal.valueOf(userTaskGoods.getFinishValue()).divide(BigDecimal.valueOf(userTaskGoods.getGoalValue()),2,BigDecimal.ROUND_HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100)).intValue();
            if(percent >= 100){
                percent = 100;
            }
            String p = String.valueOf(percent);
            userTaskGoods.setPercent(p);
            userTaskGoods.setUpdateTime(now);
        });
        userTask.setUpdatedTime(now);
        String stepRuleValue = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());
        boolean isStepTask = false;
        if(stepRuleValue.equals(TaskConstant.ONE)){
            isStepTask = true;
        }
        //平台任务才算佣金
        if(taskDO.getTaskType().equals(TaskTypeEnum.PLATFORM.getCode())){
            // 佣金计算 阶梯任务不走此逻辑-因为任务未结束之前不知道达到哪个阶梯
            if(!isStepTask && !addTaskOrderRequest.isDataFix()){
                this.saveCommission(userTask,taskOrderGoodsDOList,taskOrder,taskDO);
            }
        }

        String lockName = RedisKey.generate("amount:commission", PlatformEnum.SALES_ASSIST.getCode().toString(),"add",userTask.getId().toString());
        String lockId = "";
        try{
            //用户任务进度计算
            lockId = redisDistributedLock.lock2(lockName,100,2, TimeUnit.SECONDS);
            this.computeUserTaskProgress(isStepTask,taskDO,userTask,taskOrder,userTaskGoodsList);
            UserTaskStepDO userTaskStepDO = new UserTaskStepDO();
            //阶梯任务计算完成当前的阶梯
            this.handleStep(taskDO,userTaskStepDO,userTask);

            userTaskService.updateTaskProgress(userTask,userTaskGoodsList,taskOrder,taskOrderGoodsDOList,null,userTaskStepDO);
        }finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return true;
    }

    private void computeUserTaskProgress( boolean isStepTask,MarketTaskDO taskDO,UserTaskDO userTask, TaskOrderDO taskOrder,List<UserTaskGoodsDO> userTaskGoodsList ){
        //计算方式 单品计算还是多品计算
        //String computeRuleValue = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.COMPUTE_MODE.toString());
        //多品累计计算 达到目标值  ComputeTypeEnum.SINGLE.getCode().toString().equals(computeRuleValue)
        if(true){
            Integer orderTotal = taskOrder.getRealAmount();
            //任务总完数量  从数据库重新查
            Integer oldFinishValue = userTaskService.getById(userTask.getId()).getFinishValue();
            Integer finishTotal = oldFinishValue+orderTotal;
            userTask.setFinishValue(finishTotal);
            int percent  = BigDecimal.valueOf(finishTotal).divide(BigDecimal.valueOf(userTask.getGoal()),2,BigDecimal.ROUND_HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100)).intValue();
            if(percent >= 100){
                percent = 100;
            }
            String p = String.valueOf(percent);
            userTask.setPercent(p);
            //阶梯任务 任务未结束之前不更新完成状态
            if(!isStepTask && !UserTaskStatusEnum.FINISHED.getStatus().equals(userTask.getTaskStatus()) && finishTotal.compareTo(userTask.getGoal()) >=0){
                userTask.setTaskStatus(UserTaskStatusEnum.FINISHED.getStatus());
            }
        }
    }
}
