package com.yiling.sales.assistant.task.service.strategy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.enums.CommissionsSourcesEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsStatusEnum;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;
import com.yiling.sales.assistant.commissions.service.CommissionsService;
import com.yiling.sales.assistant.task.constant.TaskConstant;
import com.yiling.sales.assistant.task.entity.AccompanyingBillDO;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;
import com.yiling.sales.assistant.task.entity.TaskOrderDO;
import com.yiling.sales.assistant.task.entity.TaskOrderGoodsDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskStepDO;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.sales.assistant.task.service.AccompanyingBillService;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.TaskGoodsRelationService;
import com.yiling.sales.assistant.userteam.api.UserTeamApi;
import com.yiling.sales.assistant.userteam.dto.UserTeamDTO;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 任务进度计算
 * @param <R>
 * @param <P>
 */
@Slf4j
public abstract class AbstractTaskProgressHandler<R,P> {
    @Autowired
    private CommissionsService commissionsService;

    @DubboReference
    private UserTeamApi userTeamApi;

    @Autowired
    private TaskGoodsRelationService taskGoodsRelationService;

    @Autowired
    private MarketTaskService marketTaskService;

    @Autowired
    private AccompanyingBillService accompanyingBillService;
    /**
     * 任务类型
     * @return
     */
    public abstract Integer getCurrentType();

    /**
     *
     * 计算任务进度
     * @param taskDO
     * @param userTask
     * @param taskOrder
     * @param extraParam 额外参数
     * @return
     */
    public  abstract R taskHandle(MarketTaskDO taskDO, UserTaskDO userTask, TaskOrderDO taskOrder, List taskOrderGoodsDOList, P extraParam);


    /**
     * 按交易额交易量计算佣金
     * @param userTask
     * @param taskOrderGoodsList
     * @param taskOrder
     * @param taskBase
     */
    protected void saveCommission(UserTaskDO userTask, List<TaskOrderGoodsDO> taskOrderGoodsList, TaskOrderDO taskOrder, MarketTaskDO taskBase){
        log.info("订单完结佣金开始orderno={}",taskOrder.getOrderNo());
        Long taskId = userTask.getTaskId();
        LambdaQueryWrapper<TaskGoodsRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(TaskGoodsRelationDO::getTaskId,taskId);
        lambdaQueryWrapper.in(TaskGoodsRelationDO::getGoodsId,taskOrderGoodsList.stream().map(TaskOrderGoodsDO::getGoodsId).collect(Collectors.toList()));
        //任务配置商品
        List<TaskGoodsRelationDO> relations = taskGoodsRelationService.list(lambdaQueryWrapper);
        //任务收益
        AddCommissionsToUserRequest addCommissionsToUserRequest = new AddCommissionsToUserRequest();
        Integer effectStatus = CommissionsStatusEnum.UN_SETTLEMENT.getCode();
      /*  if(userTask.getTaskStatus().equals(UserTaskStatusEnum.FINISHED.getStatus())){
            effectStatus = CommissionsStatusEnum.SETTLEMENT.getCode();
        }*/
        addCommissionsToUserRequest.setEffectStatus(effectStatus).setFinishType(userTask.getFinishType()).setTaskId(userTask.getTaskId()).setTaskName(taskBase.getTaskName())
                .setUserId(userTask.getUserId()).setUserTaskId(userTask.getId()).setSources(CommissionsSourcesEnum.TASK.getCode()).setOpTime(DateUtil.date());
        //给邀请人分成佣金
        AddCommissionsToUserRequest.AddCommToUserDetailRequest inviteComm = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
        AddCommissionsToUserRequest.AddCommToUserDetailRequest request = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
        request.setSubAmount(BigDecimal.ZERO);
        //上传资料任务给第一次上传时间赋值
        if(userTask.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())){
            AccompanyingBillDO accompanyingBillDO = accompanyingBillService.getById(taskOrder.getOrderId());
            inviteComm.setFirstUploadTime(accompanyingBillDO.getUploadTime());
            request.setFirstUploadTime(accompanyingBillDO.getUploadTime());
        }
        //
        //判断是否有上下线分成规则
        String rule = marketTaskService.getRuleValue(taskId, RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.GIVE_INVITEE_AWARD.toString());
        boolean invite = false;
        boolean inviteRule = false;
        Long inviteUserId= null;
        UserTeamDTO team = null;
        if(!StringUtils.isEmpty(rule) && !rule.equals(TaskConstant.UNLIMIT)){
            inviteRule = true;
            // 判断是否有上线
            team = userTeamApi.getUserTeamByUserId(userTask.getUserId());
            if(Objects.nonNull(team)){
                invite = true;
                inviteUserId = team.getParentId();
            }
        }
        inviteComm.setSubAmount(BigDecimal.ZERO);
        boolean finalInvite = invite;
        boolean finalInviteRule = inviteRule;
        taskOrderGoodsList.forEach(g->{
            // 佣金 根据基价算部根据下单金额计算

           //String commStr = relations.stream().filter(r->r.getGoodsId().equals(g.getGoodsId())).findAny().get().getCommissionRate();
          //  BigDecimal comm  = new BigDecimal(commStr).divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
            BigDecimal commmission = relations.stream().filter(r->r.getGoodsId().equals(g.getGoodsId())).findAny().get().getCommission();
            if(finalInviteRule && finalInvite){
                BigDecimal rate = new BigDecimal(rule);
                if(rate.compareTo(BigDecimal.ZERO)>0){
                    rate = rate.divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                    //单个商品的佣金
                    BigDecimal invitecom = commmission.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                    //todo gxl 先算下线的百分比
                    commmission = commmission.subtract(invitecom).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                    inviteComm.setSubAmount(inviteComm.getSubAmount().add(invitecom.multiply(BigDecimal.valueOf(g.getRealAmount()))).setScale(2,BigDecimal.ROUND_HALF_EVEN));

                }
            }
            BigDecimal   totalAmount =  request.getSubAmount().add(commmission.multiply(BigDecimal.valueOf(g.getRealAmount()))).setScale(2,BigDecimal.ROUND_HALF_EVEN)  ;
            request.setSubAmount(totalAmount);

        });
        List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> detailList = Lists.newArrayList();
        request.setOrderCode(taskOrder.getOrderNo()).setOrderId(taskOrder.getOrderId());
        detailList.add(request);
        //订单佣金
        addCommissionsToUserRequest.setDetailList(detailList);
        commissionsService.addCommissionsToUser(addCommissionsToUserRequest);

        //上线分成
        if(finalInviteRule && finalInvite){
            AddCommissionsToUserRequest inviteCommissions = new AddCommissionsToUserRequest();
            inviteCommissions.setSources(CommissionsSourcesEnum.SUBORDINATE.getCode());
            inviteCommissions.setEffectStatus(EffectStatusEnum.INVALID.getCode()).setFinishType(userTask.getFinishType()).setTaskId(userTask.getTaskId()).setTaskName(taskBase.getTaskName())
                    .setUserId(inviteUserId).setUserTaskId(userTask.getId()).setSources(CommissionsSourcesEnum.SUBORDINATE.getCode()).setOpTime(DateUtil.date());
            List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> inviteDetailList = Lists.newArrayList();
            inviteComm.setNewUserId(userTask.getUserId()).setNewUserName(team.getName()).setNewTime(team.getRegisterTime());
            inviteComm.setOrderId(taskOrder.getOrderId());
            inviteComm.setOrderCode(taskOrder.getOrderNo());
            inviteComm.setOpUserId(inviteUserId);
            inviteDetailList.add(inviteComm);
            log.info("上线分成佣金inviteComm={}", JSON.toJSONString(inviteComm));
            inviteCommissions.setDetailList(inviteDetailList);
            commissionsService.addCommissionsToUser(inviteCommissions);
        }
    }

    /**
     * 阶梯任务处理
     * @param taskDO
     * @param userTaskStepDO
     * @param userTask
     */
    public void handleStep(MarketTaskDO taskDO,UserTaskStepDO userTaskStepDO,UserTaskDO userTask){
        //判断是否是阶梯任务
            String stepRuleValue = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());
            if(stepRuleValue.equals(TaskConstant.ONE)){
                //
                String finishType = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.FINISH_TYPE.toString());
                //todo gxl 验证阶梯和创建时的佣金是否一一匹配
                String[] step = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.SALE_CONDITION.toString()).split(",");
                String[] commision = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.COMMISSION.toString()).split(",");
                List<String> steps = Arrays.asList(step);
                List<Integer> stepList = Lists.newArrayList();
                steps.forEach(s -> {
                    if(finishType.equals(FinishTypeEnum.MONEY.getCode().toString())){
                        stepList.add(Integer.parseInt(s)*100);
                    }else{
                        stepList.add(Integer.parseInt(s));
                    }

                });
                //Collections.sort(stepList);
                Collections.reverse(stepList);
                for (int i = 0; i < stepList.size(); i++) {
                    //金额任务完成值存的分
                    if(userTask.getFinishValue().compareTo(stepList.get(i))>=0){
                        userTaskStepDO.setCommision(commision[commision.length-i-1]).setStepNo(commision.length-i).setUserTaskId(userTask.getId());
                        break;
                    }
                }

            }

    }
}
