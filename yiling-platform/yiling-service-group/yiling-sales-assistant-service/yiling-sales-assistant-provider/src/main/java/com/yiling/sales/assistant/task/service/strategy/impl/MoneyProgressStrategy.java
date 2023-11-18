package com.yiling.sales.assistant.task.service.strategy.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;
import com.yiling.sales.assistant.task.entity.TaskOrderDO;
import com.yiling.sales.assistant.task.entity.UserSelectedTerminalDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskGoodsDO;
import com.yiling.sales.assistant.task.entity.UserTaskStepDO;
import com.yiling.sales.assistant.task.enums.ComputeTypeEnum;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.sales.assistant.task.enums.TaskTypeEnum;
import com.yiling.sales.assistant.task.enums.UserTaskStatusEnum;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.TaskGoodsRelationService;
import com.yiling.sales.assistant.task.service.UserSelectedTerminalService;
import com.yiling.sales.assistant.task.service.UserTaskGoodsService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.sales.assistant.task.service.strategy.AbstractTaskProgressHandler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 按交易额计算
 * @author gxl
 */
@Slf4j
@Service("moneyProgresStrategy")
public class MoneyProgressStrategy extends AbstractTaskProgressHandler {

    @Autowired
    private UserTaskGoodsService userTaskGoodsService;

    @Autowired
    private MarketTaskService marketTaskService;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private UserSelectedTerminalService userSelectedTerminalService;

    @Autowired
    private TaskGoodsRelationService taskGoodsRelationService;

    @Override
    public Integer getCurrentType() {
        return FinishTypeEnum.MONEY.getCode();
    }


    @Override
    public Object taskHandle(MarketTaskDO taskDO, UserTaskDO userTask, TaskOrderDO taskOrder, List taskOrderGoodsDOList, Object extraParam) {
        Date now  = DateUtil.date();
        AddTaskOrderRequest addTaskOrderRequest = (AddTaskOrderRequest)extraParam;
        //任务配置商品
        Long taskId = userTask.getTaskId();
        LambdaQueryWrapper<TaskGoodsRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(TaskGoodsRelationDO::getTaskId,taskId);

        List<TaskGoodsRelationDO> relations = taskGoodsRelationService.list(lambdaQueryWrapper);
        HashMap<Long,BigDecimal> priceMap  = Maps.newHashMapWithExpectedSize(relations.size());

        relations.forEach(taskGoodsRelationDO -> {
            priceMap.put(taskGoodsRelationDO.getGoodsId(),taskGoodsRelationDO.getSalePrice());
        });
        //计算每个商品的任务进度
        List<AddTaskOrderGoodsRequest> addTaskOrderGoodsRequests = addTaskOrderRequest.getOrderGoodsAddDTOList();
        //计算订单中每个商品的售卖总金额 按基价算
        HashMap<Long,Integer> orderGoodsMap  = Maps.newHashMapWithExpectedSize(addTaskOrderGoodsRequests.size());
        addTaskOrderGoodsRequests.forEach(goods->{
            orderGoodsMap.put(goods.getGoodsId(), NumberUtil.mul(goods.getAmount(),priceMap.get(goods.getGoodsId())).multiply(BigDecimal.valueOf(100)).intValue() );
        });
        LambdaQueryWrapper<UserTaskGoodsDO> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(UserTaskGoodsDO::getUserTaskId,userTask.getId());
        List<UserTaskGoodsDO> userTaskGoodsList = userTaskGoodsService.list(lambdaQuery);
        List<Long> orderGoodsIds  = addTaskOrderGoodsRequests.stream().map(AddTaskOrderGoodsRequest::getGoodsId).collect(Collectors.toList());
        userTaskGoodsList.stream().filter(u->orderGoodsIds.contains(u.getGoodsId())).collect(Collectors.toList()).forEach(userTaskGoods -> {
            Integer finishTotal = userTaskGoods.getFinishValue()+orderGoodsMap.get(userTaskGoods.getGoodsId());
            userTaskGoods.setFinishValue(finishTotal);
            int percent  = BigDecimal.valueOf(finishTotal).divide(BigDecimal.valueOf(userTaskGoods.getGoalValue()),2,BigDecimal.ROUND_HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100)).intValue();
            if(percent >= 100){
                percent = 100;
            }
            String p = String.valueOf(percent);
            userTaskGoods.setPercent(p);
            userTaskGoods.setUpdateTime(now);
        });
        userTask.setUpdatedTime(now);
        //用户任务进度计算
        this.computeUserTaskProgress(taskDO,userTask,taskOrder,userTaskGoodsList);
        //平台任务才算佣金
        if(taskDO.getTaskType().equals(TaskTypeEnum.PLATFORM.getCode())){
            // 佣金计算
            this.saveCommission(userTask,taskOrderGoodsDOList,taskOrder,taskDO);
        }
        LambdaQueryWrapper<UserSelectedTerminalDO> t = Wrappers.lambdaQuery();
        t.eq(UserSelectedTerminalDO::getUserId,userTask.getUserId());
        t.eq(UserSelectedTerminalDO::getUserTaskId,taskOrder.getUserTaskId());
        t.eq(UserSelectedTerminalDO::getTerminalId,taskOrder.getTerminalId()).last("limit 1");
        UserSelectedTerminalDO userSelectedTerminal = userSelectedTerminalService.getOne(t);
        UserTaskStepDO userTaskStepDO = new UserTaskStepDO();;
        this.handleStep(taskDO,userTaskStepDO,userTask);
        userTaskService.updateTaskProgress(userTask,userTaskGoodsList,taskOrder,taskOrderGoodsDOList,userSelectedTerminal,userTaskStepDO);
        if(Objects.isNull(userSelectedTerminal)){
            log.error("未查到用户终端信息");
            // throw new BusinessException(IE"未查到用户终端信息");
        }
        return true;
    }

    private void computeUserTaskProgress(MarketTaskDO taskDO,UserTaskDO userTask, TaskOrderDO taskOrder,List<UserTaskGoodsDO> userTaskGoodsList ){
        //计算方式 单品计算还是多品计算
        String computeRuleValue = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.COMPUTE_MODE.toString());
        //多品累计计算 达到目标值
        if( ComputeTypeEnum.SINGLE.getCode().toString().equals(computeRuleValue)){
            //元转成分存储
            Integer orderTotal = taskOrder.getRealMoney().multiply(BigDecimal.valueOf(100)).intValue();
            //任务总完成金额
            Integer finishTotal = userTask.getFinishValue()+orderTotal;
            int percent  = BigDecimal.valueOf(finishTotal).divide(BigDecimal.valueOf(userTask.getGoal()),2,BigDecimal.ROUND_HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100)).intValue();
            if(percent >= 100){
                percent = 100;
            }
            String p = String.valueOf(percent);
            userTask.setPercent(p);
            userTask.setFinishValue(finishTotal);
            if(!UserTaskStatusEnum.FINISHED.getStatus().equals(userTask.getTaskStatus()) && finishTotal.compareTo(userTask.getGoal()) >=0){
                userTask.setTaskStatus(UserTaskStatusEnum.FINISHED.getStatus());
                userTask.setPercent("100");
            }
        }

        //每个单品都要达到目标值
        if( ComputeTypeEnum.MORE.getCode().toString().equals(computeRuleValue)){
            Integer finishGoods = (int) userTaskGoodsList.stream().filter(u->u.getFinishValue().compareTo(u.getGoalValue())>=0).count();
            int percent  = BigDecimal.valueOf(finishGoods).divide(BigDecimal.valueOf(userTask.getTaskGoodsTotal()),2,BigDecimal.ROUND_HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100)).intValue();
            if(percent >= 100){
                percent = 100;
            }
            String p = String.valueOf(percent);
            userTask.setPercent(p);
            if(!UserTaskStatusEnum.FINISHED.getStatus().equals(userTask.getTaskStatus()) && finishGoods.compareTo(userTask.getTaskGoodsTotal())>=0){
                userTask.setTaskStatus(UserTaskStatusEnum.FINISHED.getStatus());
                userTask.setPercent("100");
            }
            userTask.setFinishGoods(finishGoods);
        }
    }
}
