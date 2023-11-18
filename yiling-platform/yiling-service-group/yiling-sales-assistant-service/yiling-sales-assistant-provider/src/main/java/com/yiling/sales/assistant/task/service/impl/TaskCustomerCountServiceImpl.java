package com.yiling.sales.assistant.task.service.impl;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sales.assistant.task.dao.TaskCustomerCountMapper;
import com.yiling.sales.assistant.task.entity.TaskCustomerCountDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.TaskCustomerCountService;
import com.yiling.sales.assistant.task.service.UserTaskService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

/**
 * <p>
 * 任务拉新户采购额统计表 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-24
 */
@Service
public class TaskCustomerCountServiceImpl extends BaseServiceImpl<TaskCustomerCountMapper, TaskCustomerCountDO> implements TaskCustomerCountService {

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private MarketTaskService marketTaskService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTaskCostumerCount(TaskCustomerCountDO taskCustomerCountDO, UserTaskDO inviteUserTask) {
        boolean finish = false;
        LambdaQueryWrapper<TaskCustomerCountDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskCustomerCountDO::getCustomerEid, taskCustomerCountDO.getCustomerEid()).eq(TaskCustomerCountDO::getUserTaskId, taskCustomerCountDO.getUserTaskId()).eq(TaskCustomerCountDO::getInviteeUserId,0);
        TaskCustomerCountDO countDO = this.getOne(wrapper);
        BigDecimal total = BigDecimal.ZERO;
        if(Objects.isNull(countDO)){
            total = taskCustomerCountDO.getTotalPurchaseAmount();
            taskCustomerCountDO.setCreateTime(DateUtil.date());
            taskCustomerCountDO.setTaskId(inviteUserTask.getTaskId());
            taskCustomerCountDO.setUserId(inviteUserTask.getUserId());
            this.save(taskCustomerCountDO);
        }else{
            total = NumberUtil.add(countDO.getTotalPurchaseAmount(), taskCustomerCountDO.getTotalPurchaseAmount()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
            countDO.setTotalPurchaseAmount(total);
            countDO.setUpdateTime(DateUtil.date());
            this.updateById(countDO);
        }

        String value = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_AMOUNT.toString());
        if(new BigDecimal(value).compareTo(total)<=0){
            inviteUserTask.setFinishValue(inviteUserTask.getFinishValue()+1);
            finish = true;
        }
        userTaskService.updateById(inviteUserTask);
        return finish;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTaskUserCount(TaskCustomerCountDO taskCustomerCountDO, UserTaskDO inviteUserTask) {
        boolean finish = false;
        LambdaQueryWrapper<TaskCustomerCountDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskCustomerCountDO::getInviteeUserId, taskCustomerCountDO.getInviteeUserId()).eq(TaskCustomerCountDO::getUserTaskId, taskCustomerCountDO.getUserTaskId());
        TaskCustomerCountDO countDO = this.getOne(wrapper);
        BigDecimal total = BigDecimal.ZERO;
        if(Objects.isNull(countDO)){
            total = taskCustomerCountDO.getTotalPurchaseAmount();
            taskCustomerCountDO.setCreateTime(DateUtil.date());
            taskCustomerCountDO.setTaskId(inviteUserTask.getTaskId());
            taskCustomerCountDO.setUserId(inviteUserTask.getUserId());
            taskCustomerCountDO.setInviteeUserId(taskCustomerCountDO.getInviteeUserId());
            this.save(taskCustomerCountDO);
        }else{
            total = NumberUtil.add(countDO.getTotalPurchaseAmount(), taskCustomerCountDO.getTotalPurchaseAmount()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
            countDO.setTotalPurchaseAmount(total);
            countDO.setUpdateTime(DateUtil.date());
            this.updateById(countDO);
        }

        String value = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_AMOUNT.toString());
        if(new BigDecimal(value).compareTo(total)<=0){
            inviteUserTask.setFinishValue(inviteUserTask.getFinishValue()+1);
            finish = true;
        }
        userTaskService.updateById(inviteUserTask);
        return finish;
    }
}
