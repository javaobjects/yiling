package com.yiling.sales.assistant.task.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.QueryAssistantOrderFirstRequest;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.RemoveCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.UpdateCommissionsEffectiveRequest;
import com.yiling.sales.assistant.commissions.enums.CommissionsSourcesEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsStatusEnum;
import com.yiling.sales.assistant.commissions.service.CommissionsService;
import com.yiling.sales.assistant.task.bo.LocationTreeBO;
import com.yiling.sales.assistant.task.constant.TaskConstant;
import com.yiling.sales.assistant.task.dao.UserTaskMapper;
import com.yiling.sales.assistant.task.dto.TaskMemberDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceUserDTO;
import com.yiling.sales.assistant.task.dto.UserTypeDTO;
import com.yiling.sales.assistant.task.dto.app.GoodsProgressDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskProgressDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskProgressStepDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskTraceTaskUserRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateUserTaskMemberRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateUserTaskNoLimitRequest;
import com.yiling.sales.assistant.task.dto.request.app.InviteTaskMemberRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskRequest;
import com.yiling.sales.assistant.task.dto.request.app.TakeTaskRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.MatchDetailDO;
import com.yiling.sales.assistant.task.entity.SaTaskRegisterTerminalDO;
import com.yiling.sales.assistant.task.entity.SaTaskRegisterUserDO;
import com.yiling.sales.assistant.task.entity.TaskAccompanyingBillDO;
import com.yiling.sales.assistant.task.entity.TaskAreaRelationDO;
import com.yiling.sales.assistant.task.entity.TaskCustomerCountDO;
import com.yiling.sales.assistant.task.entity.TaskDeptUserDO;
import com.yiling.sales.assistant.task.entity.TaskDistributorDO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;
import com.yiling.sales.assistant.task.entity.TaskMemberBuyDO;
import com.yiling.sales.assistant.task.entity.TaskMemberPromotionDO;
import com.yiling.sales.assistant.task.entity.TaskMemberRecordDO;
import com.yiling.sales.assistant.task.entity.TaskOrderDO;
import com.yiling.sales.assistant.task.entity.TaskOrderGoodsDO;
import com.yiling.sales.assistant.task.entity.UserSelectedTerminalDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskGoodsDO;
import com.yiling.sales.assistant.task.entity.UserTaskStepDO;
import com.yiling.sales.assistant.task.enums.AssistantErrorCode;
import com.yiling.sales.assistant.task.enums.ComputeTypeEnum;
import com.yiling.sales.assistant.task.enums.ExecuteTypeEnum;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.sales.assistant.task.enums.TaskStatusEnum;
import com.yiling.sales.assistant.task.enums.TaskTypeEnum;
import com.yiling.sales.assistant.task.enums.UserTaskStatusEnum;
import com.yiling.sales.assistant.task.service.AppMarketTaskService;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.MatchDetailService;
import com.yiling.sales.assistant.task.service.SaTaskRegisterTerminalService;
import com.yiling.sales.assistant.task.service.SaTaskRegisterUserService;
import com.yiling.sales.assistant.task.service.TaskAccompanyingBillService;
import com.yiling.sales.assistant.task.service.TaskAreaRelationService;
import com.yiling.sales.assistant.task.service.TaskCustomerCountService;
import com.yiling.sales.assistant.task.service.TaskDeptUserService;
import com.yiling.sales.assistant.task.service.TaskDistributorService;
import com.yiling.sales.assistant.task.service.TaskGoodsRelationService;
import com.yiling.sales.assistant.task.service.TaskMemberBuyService;
import com.yiling.sales.assistant.task.service.TaskMemberPromotionService;
import com.yiling.sales.assistant.task.service.TaskMemberRecordService;
import com.yiling.sales.assistant.task.service.TaskOrderGoodsService;
import com.yiling.sales.assistant.task.service.TaskOrderService;
import com.yiling.sales.assistant.task.service.UserSelectedTerminalService;
import com.yiling.sales.assistant.task.service.UserTaskGoodsService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.sales.assistant.task.service.UserTaskStepService;
import com.yiling.sales.assistant.userteam.api.UserTeamApi;
import com.yiling.sales.assistant.userteam.dto.UserTeamDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserSalesAreaDTO;
import com.yiling.user.system.dto.request.QueryStaffPageListRequest;
import com.yiling.user.system.enums.UserTypeEnum;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 我的任务 销售承接的任务 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Slf4j
@Service
public class UserTaskServiceImpl extends BaseServiceImpl<UserTaskMapper, UserTaskDO> implements UserTaskService {

    @Autowired
    private MarketTaskService             marketTaskService;
    @Autowired
    private TaskOrderService              taskOrderService;
    @Autowired
    private UserTaskGoodsService          userTaskGoodsService;
    @Autowired
    private TaskOrderGoodsService         taskOrderGoodsService;
    @Autowired
    private SaTaskRegisterTerminalService saTaskRegisterTerminalService;
    @Autowired
    private SaTaskRegisterUserService     saTaskRegisterUserService;
    @Autowired
    private UserSelectedTerminalService   userSelectedTerminalService;
    @Autowired
    private AppMarketTaskService          appMarketTaskService;

    @Autowired
    private UserTaskStepService userTaskStepService;

    @Autowired
    private TaskDeptUserService taskDeptUserService;

    @Autowired
    private TaskGoodsRelationService taskGoodsRelationService;

    @Autowired
    private TaskAreaRelationService taskAreaRelationService;
    @Autowired
    private RedisService redisService;

    @DubboReference
    private UserApi    userApi;
    @DubboReference
    private StaffApi staffApi;

    @DubboReference
    private UserTeamApi    userTeamApi;

    @Autowired
    private CommissionsService commissionsService;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private  UserTeamApi userCustomerApi;

    @Resource
    private RedisTemplate   redisTemplate;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private TaskMemberRecordService taskMemberRecordService;

    @Autowired
    private TaskMemberBuyService taskMemberBuyService;

    @Autowired
    private TaskMemberPromotionService taskMemberPromotionService;

    @Autowired
    private TaskDistributorService taskDistributorService;

    @Autowired
    private TaskCustomerCountService taskCustomerCountService;

    @Autowired
    private TaskAccompanyingBillService taskAccompanyingBillService;

    @Autowired
    private MatchDetailService matchDetailService;

    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private OrderApi orderApi;


    @Override
    public Page<TaskTraceUserDTO> queryTaskUser(QueryTaskTraceTaskUserRequest queryTaskTraceTaskUserRequest) {

        Page<UserTaskDO> page = new Page<>(queryTaskTraceTaskUserRequest.getCurrent(),queryTaskTraceTaskUserRequest.getSize());
        LambdaQueryWrapper<UserTaskDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserTaskDO::getTaskId,queryTaskTraceTaskUserRequest.getTaskId());
        if(Objects.nonNull(queryTaskTraceTaskUserRequest.getStartTime())){
            lambdaQueryWrapper.ge(UserTaskDO::getCreatedTime,queryTaskTraceTaskUserRequest.getStartTime());
        }
        if(Objects.nonNull(queryTaskTraceTaskUserRequest.getEndTime())){
            lambdaQueryWrapper.le(UserTaskDO::getCreatedTime,DateUtil.endOfDay(queryTaskTraceTaskUserRequest.getEndTime()));
        }
        if(StrUtil.isNotEmpty(queryTaskTraceTaskUserRequest.getName()) || StrUtil.isNotEmpty(queryTaskTraceTaskUserRequest.getMobile())){
            QueryStaffPageListRequest request = new QueryStaffPageListRequest();
            request.setName(queryTaskTraceTaskUserRequest.getName()).setMobile(queryTaskTraceTaskUserRequest.getMobile());
            Page<Staff> userDTOPage = staffApi.pageList(request);
            if(userDTOPage.getTotal()>0){
                List<Long> userIdList = userDTOPage.getRecords().stream().map(Staff::getId).distinct().collect(Collectors.toList());
                lambdaQueryWrapper.in(UserTaskDO::getUserId,userIdList);
            }else{
                return  new Page<>(queryTaskTraceTaskUserRequest.getCurrent(),queryTaskTraceTaskUserRequest.getSize());
            }
        }
        Page<UserTaskDO> taskIPage = this.page(page,lambdaQueryWrapper);

        Page<TaskTraceUserDTO> result = new Page<>(queryTaskTraceTaskUserRequest.getCurrent(),queryTaskTraceTaskUserRequest.getSize());
        List<UserTaskDO> userTaskList = taskIPage.getRecords();
        if(CollUtil.isEmpty(userTaskList)){
            return  result;
        }
        // 调接口查用户信息
        List<Long> userIds = userTaskList.stream().map(UserTaskDO::getUserId).collect(Collectors.toList());
        Long taskId = queryTaskTraceTaskUserRequest.getTaskId();
        String stepRuleValue = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());
        List<String> percentList = Lists.newArrayList();
        boolean isStepTask = false;
        List<Integer> stepList = Lists.newArrayList();
        if(stepRuleValue.equals(TaskConstant.ONE)){
            isStepTask = true;
            String[] step = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.SALE_CONDITION.toString()).split(",");
            List<String> steps = Arrays.asList(step);
            for (String s : steps) {
                Integer stepInt = Integer.parseInt(s);
                stepList.add(stepInt);
            }
            BigDecimal pre = BigDecimal.ZERO;
            for (int i=0;i< stepList.size();i++) {
                BigDecimal per = BigDecimal.valueOf(stepList.get(i)).divide(BigDecimal.valueOf(stepList.get(stepList.size() - 1)), 2, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.valueOf(100));
                String percent = per.toString();
                if(i==0){
                    percentList.add(percent);
                }else{
                    percentList.add(per.subtract(pre).setScale(2,BigDecimal.ROUND_HALF_EVEN).toString());
                }
                pre = per;

            }
        }
        String compute = marketTaskService.getRuleValue(queryTaskTraceTaskUserRequest.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.COMPUTE_MODE.toString());
        List<UserDTO> userDTOS = userApi.listByIds(userIds);
        result.setTotal(taskIPage.getTotal());
        List<TaskTraceUserDTO> userVOS = Lists.newArrayList();
        for (UserTaskDO userTaskDO : userTaskList) {
            TaskTraceUserDTO taskTraceUserDTO = new TaskTraceUserDTO();
            PojoUtils.map(userTaskDO, taskTraceUserDTO);
            if (isStepTask) {
                String percent = BigDecimal.valueOf(userTaskDO.getFinishValue()).divide(BigDecimal.valueOf(stepList.get(stepList.size()-1)),2,BigDecimal.ROUND_HALF_EVEN)
                        .multiply(BigDecimal.valueOf(100)).toString();
                //阶梯任务完成百分比不取数据库字段
                if(new BigDecimal(percent).compareTo(BigDecimal.valueOf(100))>0){
                    percent = "100";
                }
                taskTraceUserDTO.setPercent(percent);
            }
            taskTraceUserDTO.setPercentList(percentList);

            if (!StringUtils.isEmpty(compute) && compute.equals(ComputeTypeEnum.MORE.getCode().toString())) {
                taskTraceUserDTO.setEachCompute(true);
            } else {
                taskTraceUserDTO.setEachCompute(false);
            }
            if (userTaskDO.getFinishType().equals(FinishTypeEnum.MONEY.getCode())) {
                String goal = new BigDecimal(userTaskDO.getGoal()).divide(BigDecimal.valueOf(100)).toString();
                taskTraceUserDTO.setGoal(goal);
                String finish = new BigDecimal(userTaskDO.getFinishValue()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                taskTraceUserDTO.setFinishValue(finish);
            } else {
                taskTraceUserDTO.setGoal(userTaskDO.getGoal().toString());
                taskTraceUserDTO.setFinishValue(userTaskDO.getFinishValue().toString());
            }
            taskTraceUserDTO.setUserTaskId(userTaskDO.getId());
            UserDTO userDTO = userDTOS.stream().filter(s -> s.getId().equals(userTaskDO.getUserId())).findAny().orElse(null);
            if (!Objects.isNull(userDTO)) {
                taskTraceUserDTO.setUserName(userDTO.getName());
                taskTraceUserDTO.setMobile(userDTO.getMobile());
            }
            userVOS.add(taskTraceUserDTO);
        }
        result.setRecords(userVOS);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskProgress(UserTaskDO userTaskDO, List<UserTaskGoodsDO> userTaskGoodsList, TaskOrderDO taskOrder, List<TaskOrderGoodsDO> taskOrderGoodsList, UserSelectedTerminalDO userSelectedTerminal, UserTaskStepDO userTaskStepDO) {
        if(!Objects.isNull(userTaskDO)){
            this.updateById(userTaskDO);
        }
        if(!Objects.isNull(taskOrder)){
            taskOrder.setOrderStatus(1);
            taskOrderService.save(taskOrder);
        }

        if(CollUtil.isNotEmpty(taskOrderGoodsList)){
            userTaskGoodsService.updateBatchById(userTaskGoodsList);
        }
        if(CollUtil.isNotEmpty(taskOrderGoodsList)){
            taskOrderGoodsList.forEach(taskOrderGoodsDO -> {
                taskOrderGoodsDO.setTaskOrderId(taskOrder.getId());
            });
            taskOrderGoodsService.saveBatch(taskOrderGoodsList);
        }
        //阶梯任务当前完成阶梯记录
        if(!Objects.isNull(userTaskStepDO.getUserTaskId())){
            LambdaQueryWrapper<UserTaskStepDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(UserTaskStepDO::getUserTaskId,userTaskStepDO.getUserTaskId()).eq(UserTaskStepDO::getStepNo,userTaskStepDO.getStepNo()).last("limit 1");
            UserTaskStepDO old = userTaskStepService.getOne(wrapper);
            if(Objects.isNull(old)){
                userTaskStepService.save(userTaskStepDO);
            }
        }
    }

    @Override
    public void updateInviteCostumerProgress(UserCustomerDTO userCustomerDTO) {
         // 查询客户邀请人(即第一个添加此客户的人)
        Long inviteUserId = userCustomerDTO.getUserId();
        //判断邀请人是否承接拉新户任务（理论上来讲只存在一个拉新户的任务）
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        List<Integer> statusList = Lists.newArrayList();
        statusList.add(UserTaskStatusEnum.IN_PROGRESS.getStatus());
        //statusList.add(UserTaskStatusEnum.FINISHED.getStatus());
        wrapper.eq(UserTaskDO::getUserId, inviteUserId).eq(UserTaskDO::getFinishType,FinishTypeEnum.NEW_ENT.getCode()).in(UserTaskDO::getTaskStatus,statusList);
        List<UserTaskDO> inviteUserTaskList = this.list(wrapper);
        if(CollUtil.isEmpty(inviteUserTaskList)){
            log.info("邀请人未承接拉新户类型的任务inviteUserId={}",inviteUserId);
            return;
        }
        inviteUserTaskList.forEach(inviteUserTask->{
            if(Objects.isNull(inviteUserTask)){
                log.info("邀请人未承接拉新户类型的任务inviteUserId={}",inviteUserId);
                return;
            }

            //拉新户
            String newCustomerRule = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_LIMIT.toString());

            //无限制
            if(newCustomerRule.equals(TaskConstant.UNLIMIT)){
                MarketTaskDO taskBase = marketTaskService.getById(inviteUserTask.getTaskId());
                //  判断客户的创建时间和审核通过时间是否大于等任务开始时间
                if(userCustomerDTO.getCreateTime().compareTo(inviteUserTask.getCreatedTime())<0 || userCustomerDTO.getAuditTime().compareTo(inviteUserTask.getCreatedTime())<0){
                    log.info("拉新户任务时间不符合要求userCustomerDTO={}",userCustomerDTO.toString());
                    return;
                }
                inviteUserTask.setFinishValue(inviteUserTask.getFinishValue()+1);
                this.updateById(inviteUserTask);
                SaTaskRegisterTerminalDO saTaskRegisterTerminalDO = new SaTaskRegisterTerminalDO();
                // 终端联系人和手机号
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(userCustomerDTO.getCustomerEid());
                saTaskRegisterTerminalDO.setContactor(enterpriseDTO.getContactor()).setContactorMobile(enterpriseDTO.getContactorPhone()).setTaskId(inviteUserTask.getTaskId()).setUserTaskId(inviteUserTask.getId())
                        .setTerminalId(userCustomerDTO.getCustomerEid()).setTerminalName(enterpriseDTO.getName());
                saTaskRegisterTerminalService.save(saTaskRegisterTerminalDO);
                // 佣金计算
                AddCommissionsToUserRequest addCommissionsToUserRequest = new AddCommissionsToUserRequest();

                //任务结束更改为已结算
                addCommissionsToUserRequest.setEffectStatus(CommissionsStatusEnum.UN_SETTLEMENT.getCode()).setFinishType(inviteUserTask.getFinishType()).setTaskId(inviteUserTask.getTaskId()).setTaskName(taskBase.getTaskName())
                        .setUserId(inviteUserTask.getUserId()).setUserTaskId(inviteUserTask.getId()).setSources(CommissionsSourcesEnum.TASK.getCode()).setOpTime(DateUtil.date());
                //判断是否有上下线分成规则
                String rule = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.GIVE_INVITEE_AWARD.toString());
                String commissionValue = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.COMMISSION.toString());
                BigDecimal commission = new BigDecimal(commissionValue);
                AddCommissionsToUserRequest.AddCommToUserDetailRequest request = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
                if(!StringUtils.isEmpty(rule) && !rule.equals(TaskConstant.UNLIMIT)){
                    // 查询第一添加此客户的人是否有邀请人
                    UserTeamDTO team = userTeamApi.getUserTeamByUserId(inviteUserId);
                    if(Objects.nonNull(team)){
                        Long inviterId = team.getParentId();
                        //佣金金额
                        AddCommissionsToUserRequest inviterCommission = new AddCommissionsToUserRequest();
                        inviterCommission.setSources(CommissionsSourcesEnum.SUBORDINATE.getCode()).setTaskId(inviteUserTask.getTaskId()).setUserId(inviterId)
                                .setUserTaskId(inviteUserTask.getId()).setFinishType(FinishTypeEnum.NEW_ENT.getCode()).setTaskName(taskBase.getTaskName()).setEffectStatus(CommissionsStatusEnum.UN_SETTLEMENT.getCode()).setOpTime(DateUtil.date());
                        List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> inviterDetailList = Lists.newArrayList();
                        AddCommissionsToUserRequest.AddCommToUserDetailRequest inviterRequest = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
                        BigDecimal rate = new BigDecimal(rule);
                        if(rate.compareTo(BigDecimal.ZERO)>0){
                            rate = rate.divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                            BigDecimal invitecom = commission.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                            commission = commission.subtract(invitecom).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                            inviterRequest.setSubAmount(invitecom);
                            inviterRequest.setNewTime(enterpriseDTO.getCreateTime()).setNewEntId(userCustomerDTO.getCustomerEid()).setNewEntName(enterpriseDTO.getName());
                            inviterDetailList.add(inviterRequest);
                            inviterCommission.setDetailList(inviterDetailList);
                            commissionsService.addCommissionsToUser(inviterCommission);
                        }
                    }
                }
                request.setSubAmount(commission).setNewTime(enterpriseDTO.getCreateTime()).setNewEntId(userCustomerDTO.getCustomerEid()).setNewEntName(enterpriseDTO.getName()).setOpUserId(inviteUserTask.getUserId());

                List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> detailList = Lists.newArrayList();
                detailList.add(request);
                //订单佣金
                addCommissionsToUserRequest.setDetailList(detailList);
                commissionsService.addCommissionsToUser(addCommissionsToUserRequest);

            }
        });

    }

   /* @Override
    public void updateInviteUserProgress(UpdateUserTaskByCostumerRequest updateUserTaskByCostumerRequest) {
        // 查询客户邀请人(即第一个添加此客户的人)
        Long inviteUserId = updateUserTaskByCostumerRequest.getUserId();
        String inviteUserName = "";
        //查询客户邀请人是否有邀请人
        if(false){
            log.info("此企业客户邀请人不存在上线邀请人uid={}",inviteUserId);
            return;
        }
        Long inviterId = 0L;
        //判断邀请人是否承接拉新人任务（理论上来讲只存在一个拉新人的任务）
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getUserId, inviterId).eq(UserTaskDO::getFinishType,FinishTypeEnum.NEW_USER.getCode()).eq(UserTaskDO::getTaskStatus,UserTaskStatusEnum.IN_PROGRESS.getStatus());
        UserTaskDO inviteUserTask = this.getOne(wrapper);
        if(Objects.isNull(inviteUserTask)){
            log.info("邀请人未承接拉新户类型的任务inviterId={}",inviterId);
            return;
        }
        //拉新人
        String newCustomerRule = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_LIMIT.toString());
        //有限制
        if(!newCustomerRule.equals(TaskConstant.UNLIMIT)){
            inviteUserTask.setFinishValue(inviteUserTask.getFinishValue()+1);
            this.updateById(inviteUserTask);
         *//*       SaTaskRegisterTerminalDO saTaskRegisterTerminalDO = new SaTaskRegisterTerminalDO();
            // 终端联系人和手机号
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
            saTaskRegisterTerminalDO.setContactor(enterpriseDTO.getContactor()).setContactorMobile(enterpriseDTO.getContactorPhone()).setTaskId(inviteUserTask.getTaskId()).setUserTaskId(inviteUserTask.getId())
                    .setTerminalId(eid).setTerminalName(enterpriseDTO.getName());
            saTaskRegisterTerminalService.save(saTaskRegisterTerminalDO);*//*
            UserDTO userDTO = userApi.getById(inviteUserId);
            SaTaskRegisterUserDO saTaskRegisterUserDO = new SaTaskRegisterUserDO();
            // 注册时间
            saTaskRegisterUserDO.setMobile(userDTO.getMobile()).setName(userDTO.getName()).setRegisterTime(updateUserTaskByCostumerRequest.get).setTaskId(inviteUserTask.getTaskId()).setUserTaskId(inviteUserTask.getId()).setCreateTime(DateUtil.date());
            saTaskRegisterUserService.save(saTaskRegisterUserDO);
            // 佣金计算
            AddCommissionsToUserRequest addCommissionsToUserRequest = new AddCommissionsToUserRequest();
            MarketTaskDO taskBase = marketTaskService.getById(inviteUserTask.getTaskId());
            addCommissionsToUserRequest.setEffectStatus(CommissionsStatusEnum.SETTLEMENT.getCode()).setFinishType(inviteUserTask.getFinishType()).setTaskId(inviteUserTask.getTaskId()).setTaskName(taskBase.getTaskName())
                    .setUserId(inviteUserTask.getUserId()).setUserTaskId(inviteUserTask.getId()).setSources(CommissionsSourcesEnum.TASK.getCode()).setOpTime(DateUtil.date());
            //判断是否有上下线分成规则
            String rule = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.GIVE_INVITEE_AWARD.toString());
            String commissionValue = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.COMMISSION.toString());
            BigDecimal commission = new BigDecimal(commissionValue);
            AddCommissionsToUserRequest.AddCommToUserDetailRequest request = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
            if(!StringUtils.isEmpty(rule) && !rule.equals(TaskConstant.UNLIMIT)){
                // 查询第一添加此客户的人的邀请人是否有上线
                UserTeamDTO team = userTeamApi.getUserTeamByUserId(inviterId);
                if(Objects.nonNull(team)){
                    Long inviterUserId = team.getParentId();
                    //佣金金额
                    AddCommissionsToUserRequest inviterCommission = new AddCommissionsToUserRequest();
                    inviterCommission.setSources(CommissionsSourcesEnum.SUBORDINATE.getCode()).setTaskId(inviteUserTask.getTaskId()).setUserId(inviterUserId)
                            .setUserTaskId(inviteUserTask.getId()).setFinishType(FinishTypeEnum.NEW_USER.getCode()).setTaskName(taskBase.getTaskName()).setOpTime(DateUtil.date());
                    List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> inviterDetailList = Lists.newArrayList();
                    AddCommissionsToUserRequest.AddCommToUserDetailRequest inviterRequest = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
                    BigDecimal rate = new BigDecimal(rule);
                    if(rate.compareTo(BigDecimal.ZERO)>0){
                        rate = rate.divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                        BigDecimal invitecom = commission.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_EVEN);;
                        commission = commission.subtract(invitecom).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                        inviterRequest.setSubAmount(invitecom);
                        inviterDetailList.add(inviterRequest);
                        inviterCommission.setDetailList(inviterDetailList);
                        commissionsApi.addCommissionsToUser(inviterCommission);
                    }
                }
            }
            // 注册时间
            request.setSubAmount(commission).setNewTime(null).setNewUserId(inviteUserId).setNewUserName(inviteUserName).setOpUserId(inviteUserTask.getUserId());

            List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> detailList = Lists.newArrayList();
            detailList.add(request);
            //订单佣金
            addCommissionsToUserRequest.setDetailList(detailList);
            commissionsApi.addCommissionsToUser(addCommissionsToUserRequest);

        }
    }*/

    @Override
    public void newUserHandler(AddTaskOrderRequest addTaskOrderRequest) {

        //判断下单人是否有邀请人
        // 查询是否有上线
        UserTeamDTO team = userTeamApi.getUserTeamByUserId(addTaskOrderRequest.getUserId());
        if(Objects.isNull(team)) {
            log.info("下单人没有邀请人addTaskOrderRequest={}",addTaskOrderRequest.toString());
            return;
        }
        //判断邀请人是否承接拉新人任务
        Long inviterId = team.getParentId();
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getUserId, inviterId).eq(UserTaskDO::getFinishType,FinishTypeEnum.NEW_USER.getCode()).eq(UserTaskDO::getTaskStatus,UserTaskStatusEnum.IN_PROGRESS.getStatus());
        UserTaskDO inviteUserTask = this.getOne(wrapper);
        if(Objects.isNull(inviteUserTask)){
            log.info("邀请人未承接拉新人类型的任务inviterId={}",inviterId);
            return;
        }
        boolean finish = this.limitInviteUser(inviteUserTask, addTaskOrderRequest);
        if(finish){
            SaTaskRegisterUserDO saTaskRegisterUserDO = new SaTaskRegisterUserDO();
            UserDTO userDTO = userApi.getById(addTaskOrderRequest.getUserId());
            saTaskRegisterUserDO.setMobile(userDTO.getMobile()).setName(userDTO.getName()).setRegisterTime(team.getRegisterTime()).
                    setUserId(addTaskOrderRequest.getUserId()).setTaskId(inviteUserTask.getTaskId()).setUserTaskId(inviteUserTask.getId()).setCreateTime(DateUtil.date());
            saTaskRegisterUserService.save(saTaskRegisterUserDO);
            UpdateUserTaskNoLimitRequest updateUserTaskNoLimitRequest=new UpdateUserTaskNoLimitRequest();
            updateUserTaskNoLimitRequest.setUserId(addTaskOrderRequest.getUserId()).setName(userDTO.getName()).setRegTime(team.getRegisterTime());
            AddCommissionsToUserRequest addCommissionsToUserRequest = this.computeCommission(inviteUserTask,FinishTypeEnum.NEW_USER,updateUserTaskNoLimitRequest,null);
            addCommissionsToUserRequest.getDetailList().get(0).setNewTime(team.getRegisterTime()).setNewUserId(addTaskOrderRequest.getUserId()).setNewUserName(userDTO.getName()).setOpUserId(inviteUserTask.getUserId());
            commissionsService.addCommissionsToUser(addCommissionsToUserRequest);
        }
    }

    @Override
    public void updateInviteUserProgress(UpdateUserTaskNoLimitRequest updateUserTaskNoLimitRequest) {
        //判断邀请人 是否承接拉新人任务
        //判断邀请人是否承接拉新人任务
        Long inviterId = updateUserTaskNoLimitRequest.getInviterUserId();
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        List<Integer> statusList = Lists.newArrayList();
        statusList.add(UserTaskStatusEnum.IN_PROGRESS.getStatus());
        //statusList.add(UserTaskStatusEnum.FINISHED.getStatus());
        wrapper.eq(UserTaskDO::getUserId, inviterId).eq(UserTaskDO::getFinishType,FinishTypeEnum.NEW_USER.getCode()).in(UserTaskDO::getTaskStatus,statusList);
        List<UserTaskDO> inviteUserTaskList = this.list(wrapper);
        if(CollUtil.isEmpty(inviteUserTaskList)){
            log.info("邀请人未承接拉新户类型的任务inviterId={}",inviterId);
            return;
        }
        inviteUserTaskList.forEach(inviteUserTask->{
            if(Objects.isNull(inviteUserTask)){
                log.info("邀请人未承接拉新户类型的任务inviterId={}",inviterId);
                return;
            }
            //MarketTaskDO taskBase = marketTaskService.getById(inviteUserTask.getTaskId());
           // UserTeamDTO team = userTeamApi.getUserTeamByUserId(updateUserTaskNoLimitRequest.getUserId());
            if(updateUserTaskNoLimitRequest.getRegTime().compareTo(inviteUserTask.getCreatedTime())<0 || updateUserTaskNoLimitRequest.getCreateTime().compareTo(inviteUserTask.getCreatedTime())<0){
                log.info("用户注册时间或者被邀请时间早于任务开始时间updateUserTaskNoLimitRequest={}",updateUserTaskNoLimitRequest.toString());
                return;
            }
            //拉新人
             String newCustomerRule = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_LIMIT.toString());
            //无限制 注册成功即可
            if(newCustomerRule.equals(TaskConstant.UNLIMIT)) {
                inviteUserTask.setFinishValue(inviteUserTask.getFinishValue() + 1);
                this.updateById(inviteUserTask);
                SaTaskRegisterUserDO saTaskRegisterUserDO = new SaTaskRegisterUserDO();
                saTaskRegisterUserDO.setMobile(updateUserTaskNoLimitRequest.getMobile()).setName(updateUserTaskNoLimitRequest.getName()).setRegisterTime(updateUserTaskNoLimitRequest.getRegTime()).
                        setUserId(updateUserTaskNoLimitRequest.getUserId()).setTaskId(inviteUserTask.getTaskId()).setUserTaskId(inviteUserTask.getId()).setCreateTime(DateUtil.date());
                saTaskRegisterUserService.save(saTaskRegisterUserDO);
                AddCommissionsToUserRequest addCommissionsToUserRequest = this.computeCommission(inviteUserTask, FinishTypeEnum.NEW_USER, updateUserTaskNoLimitRequest, null);
                addCommissionsToUserRequest.getDetailList().get(0).setNewTime(updateUserTaskNoLimitRequest.getRegTime()).setNewUserId(updateUserTaskNoLimitRequest.getUserId()).setNewUserName(updateUserTaskNoLimitRequest.getName()).setOpUserId(inviteUserTask.getUserId());
                commissionsService.addCommissionsToUser(addCommissionsToUserRequest);
            }
        });
    }

    /**
     * 拉新人有限制
     * @param inviteUserTask
     * @param addTaskOrderRequest
     */
    private boolean limitInviteUser(UserTaskDO inviteUserTask, AddTaskOrderRequest addTaskOrderRequest){
        //首单还是累计
        String newCustomerCondition = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_CONDITION.toString());
        String condition = marketTaskService.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_AMOUNT.toString());


        boolean finish = false;
        //按基价计算
        List<GoodsDTO> goodsDTOS = goodsApi.batchQueryInfo(addTaskOrderRequest.getGoodsIds());
        HashMap<Long, BigDecimal> priceMap = Maps.newHashMapWithExpectedSize(goodsDTOS.size());
        goodsDTOS.forEach(goodsDTO -> {
            priceMap.put(goodsDTO.getId(), goodsDTO.getPrice());
        });
        addTaskOrderRequest.getOrderGoodsAddDTOList().forEach(g -> {
            addTaskOrderRequest.setTotalAmount(addTaskOrderRequest.getTotalAmount().add(NumberUtil.mul(g.getAmount(), priceMap.get(g.getGoodsId())).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
        });
        QueryAssistantOrderFirstRequest firstRequest = new QueryAssistantOrderFirstRequest();
        firstRequest.setCreateUser(addTaskOrderRequest.getUserId());
        OrderDTO firstOrder = orderApi.getAssistantReceiveFirstOrder(firstRequest);
        boolean isFirst = false;
        if (Objects.nonNull(firstOrder)&& addTaskOrderRequest.getOrderNo().equals(firstOrder.getOrderNo())) {
            isFirst = true;
        }
        //累计
        if (newCustomerCondition.equals(TaskConstant.ONE)) {
            //查询收单的时间是否在任务期间
            if (!isFirst) {

                if (firstOrder.getCreateTime().compareTo(inviteUserTask.getCreatedTime()) < 0) {
                    log.info("收单下单时间早于任务开始时间不计算任务进度 orderNo={},inviteUserTask={}", firstOrder.getOrderNo(), inviteUserTask.toString());
                    return false;
                }
            } else {
                //查询是否在b2b下过包含以岭品的订单 已收货
                QueryAssistantOrderFirstRequest req = new QueryAssistantOrderFirstRequest();
                req.setCreateUser(addTaskOrderRequest.getUserId());
                Boolean receiveB2BOrder = orderApi.verificationReceiveB2BOrder(req);
                if (receiveB2BOrder) {
                    log.info("此用户在b2b有已收货的包含以岭品的订单BuyerEid={}", addTaskOrderRequest.getTerminalId());
                    return false;
                }
            }

            TaskCustomerCountDO taskCustomerCountDO = new TaskCustomerCountDO();
            taskCustomerCountDO.setInviteeUserId(addTaskOrderRequest.getUserId()).setCustomerEid(addTaskOrderRequest.getTerminalId());
            taskCustomerCountDO.setTotalPurchaseAmount(addTaskOrderRequest.getTotalAmount()).setUserTaskId(inviteUserTask.getId());
            finish = taskCustomerCountService.saveTaskUserCount(taskCustomerCountDO, inviteUserTask);

        } else {
            //查询是否在b2b下过包含以岭品的订单 已收货
            QueryAssistantOrderFirstRequest req = new QueryAssistantOrderFirstRequest();
            req.setCreateUser(addTaskOrderRequest.getUserId());
            Boolean receiveB2BOrder = orderApi.verificationReceiveB2BOrder(req);
            if (receiveB2BOrder) {
                log.info("此用户在b2b有已收货的包含以岭品的订单BuyerEid={}", addTaskOrderRequest.getTerminalId());
                return false;
            }
            //首单
            if (isFirst && addTaskOrderRequest.getTotalAmount().compareTo(new BigDecimal(condition)) >= 0) {
                inviteUserTask.setFinishValue(inviteUserTask.getFinishValue() + 1);
                userTaskService.updateById(inviteUserTask);
                finish = true;
            } else {
                log.info("非首单或者购买金额未达到任务门槛addTaskOrderRequest={},usertask={}", addTaskOrderRequest.toString(), inviteUserTask.toString());
                return false;
            }
        }
        if (finish) {
            return true;
        }
          return false;
    }
    private AddCommissionsToUserRequest computeCommission(UserTaskDO userTaskDO,FinishTypeEnum finishTypeEnum,UpdateUserTaskNoLimitRequest updateUserTaskNoLimitRequest,UpdateUserTaskMemberRequest updateUserTaskMemberRequest){
        // 佣金计算
        AddCommissionsToUserRequest addCommissionsToUserRequest = new AddCommissionsToUserRequest();
        MarketTaskDO taskBase = marketTaskService.getById(userTaskDO.getTaskId());
        //任务结束后更改佣金结清状态
        addCommissionsToUserRequest.setEffectStatus(CommissionsStatusEnum.UN_SETTLEMENT.getCode()).setFinishType(userTaskDO.getFinishType()).setTaskId(userTaskDO.getTaskId()).setTaskName(taskBase.getTaskName())
                .setUserId(userTaskDO.getUserId()).setUserTaskId(userTaskDO.getId()).setSources(CommissionsSourcesEnum.TASK.getCode()).setOpTime(DateUtil.date());
        //判断是否有上下线分成规则
        String rule = marketTaskService.getRuleValue(userTaskDO.getTaskId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.GIVE_INVITEE_AWARD.toString());
        String commissionValue = marketTaskService.getRuleValue(userTaskDO.getTaskId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.COMMISSION.toString());
        BigDecimal commission = new BigDecimal(commissionValue);
        AddCommissionsToUserRequest.AddCommToUserDetailRequest request = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
        if(!StringUtils.isEmpty(rule) && !rule.equals(TaskConstant.UNLIMIT)){
            // 查询是否有上线
            UserTeamDTO team = userTeamApi.getUserTeamByUserId(userTaskDO.getUserId());
            if(Objects.nonNull(team)){
                Long inviterUserId = team.getParentId();
                //佣金金额
                AddCommissionsToUserRequest inviterCommission = new AddCommissionsToUserRequest();
                inviterCommission.setSources(CommissionsSourcesEnum.SUBORDINATE.getCode()).setTaskId(userTaskDO.getTaskId()).setUserId(inviterUserId)
                        .setUserTaskId(userTaskDO.getId()).setFinishType(finishTypeEnum.getCode()).setTaskName(taskBase.getTaskName()).setEffectStatus(CommissionsStatusEnum.UN_SETTLEMENT.getCode()).setOpTime(DateUtil.date());
                List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> inviterDetailList = Lists.newArrayList();
                AddCommissionsToUserRequest.AddCommToUserDetailRequest inviterRequest = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
                BigDecimal rate = new BigDecimal(rule);
                if(rate.compareTo(BigDecimal.ZERO)>0){
                    rate = rate.divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                    BigDecimal invitecom = commission.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_EVEN);;
                    commission = commission.subtract(invitecom).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                    inviterRequest.setSubAmount(invitecom);
                    inviterDetailList.add(inviterRequest);
                    inviterCommission.setDetailList(inviterDetailList);
                    if(Objects.nonNull(updateUserTaskNoLimitRequest)){
                        inviterRequest.setNewTime(updateUserTaskNoLimitRequest.getRegTime()).setNewUserId(updateUserTaskNoLimitRequest.getUserId()).setNewUserName(updateUserTaskNoLimitRequest.getName());
                    }
                    if(Objects.nonNull(updateUserTaskMemberRequest)){
                        inviterRequest.setBuyMemberName(updateUserTaskMemberRequest.getEname()).setBuyMemberEid(updateUserTaskMemberRequest.getEid()).setNewTime(updateUserTaskMemberRequest.getTradeTime()).setOrderCode(updateUserTaskMemberRequest.getOrderNo());
                    }
                    commissionsService.addCommissionsToUser(inviterCommission);
                }
            }
        }
        request.setSubAmount(commission);
        List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> detailList = Lists.newArrayList();
        detailList.add(request);
        //订单佣金
        addCommissionsToUserRequest.setDetailList(detailList);
        return addCommissionsToUserRequest;
    }
    @Override
    public void updateMemberBuyProgress(UpdateUserTaskMemberRequest updateUserTaskMemberRequest) {

        //查询推广人是否承接会员购买推广任务
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        //会员购买取推广人id 满赠会员取当前用户id
        Long userId = updateUserTaskMemberRequest.getCreateUser();
        if(true) {
            userId = updateUserTaskMemberRequest.getPromoterUserId();
        }
        if(Objects.isNull(userId)){
            return;
        }
        List<Integer> statusList = Lists.newArrayList();
        statusList.add(UserTaskStatusEnum.IN_PROGRESS.getStatus());
        //statusList.add(UserTaskStatusEnum.FINISHED.getStatus());
        wrapper.eq(UserTaskDO::getUserId, userId).eq(UserTaskDO::getFinishType,FinishTypeEnum.MEMBER_BUY.getCode()).in(UserTaskDO::getTaskStatus,statusList);
        List<UserTaskDO> userTaskList = this.list(wrapper);
        if(CollUtil.isEmpty(userTaskList)){
            log.info("用户未承接会员推广类型的任务userId={}",userId);
            return;
        }
        for (UserTaskDO userTask : userTaskList) {
            LambdaQueryWrapper<TaskMemberRecordDO> memberWrapper = Wrappers.lambdaQuery();
            memberWrapper.eq(TaskMemberRecordDO::getOrderNo,updateUserTaskMemberRequest.getOrderNo()).eq(TaskMemberRecordDO::getUserTaskId,userTask.getId());
            TaskMemberRecordDO recordServiceOne = taskMemberRecordService.getOne(memberWrapper);
            if(Objects.nonNull(recordServiceOne)){
                log.info("重复消费updateUserTaskMemberRequest={}",updateUserTaskMemberRequest.toString());
                continue;
            }
            Boolean result = this.validateMemberBuy(updateUserTaskMemberRequest,userTask);
            if(!result){
                log.info("会员任务匹配失败跳出本次循环userTask={}",userTask.toString());
                continue;
            }
            if(updateUserTaskMemberRequest.getTradeTime().compareTo(userTask.getCreatedTime())<0){
                log.info("会员购买时间早于任务承接时间updateUserTaskMemberRequest={}",updateUserTaskMemberRequest.toString());
                continue;
            }
            userTask.setFinishValue(userTask.getFinishValue()+1);
            this.updateById(userTask);
            TaskMemberRecordDO taskMemberRecordDO = new TaskMemberRecordDO();
            PojoUtils.map(updateUserTaskMemberRequest,taskMemberRecordDO);
            taskMemberRecordDO.setCreateUser(userId).setFinishType(FinishTypeEnum.MEMBER_BUY.getCode()).setUserTaskId(userTask.getId()).setTaskId(userTask.getTaskId()).setOrderNo(updateUserTaskMemberRequest.getOrderNo());
            taskMemberRecordService.save(taskMemberRecordDO);
            AddCommissionsToUserRequest addCommissionsToUserRequest = this.computeCommission(userTask,FinishTypeEnum.MEMBER_BUY,null,updateUserTaskMemberRequest);
            addCommissionsToUserRequest.getDetailList().get(0).setBuyMemberName(updateUserTaskMemberRequest.getEname()).setBuyMemberEid(updateUserTaskMemberRequest.getEid()).setNewTime(updateUserTaskMemberRequest.getTradeTime()).setOrderCode(updateUserTaskMemberRequest.getOrderNo()).setOpUserId(userTask.getUserId());
            try {
                commissionsService.addCommissionsToUser(addCommissionsToUserRequest);
            }catch (Exception e){
                log.error("会员任务发放佣金失败Exception={}",e.getMessage());
            }
        }

    }

    private Boolean validateMemberBuy(UpdateUserTaskMemberRequest updateUserTaskMemberRequest,UserTaskDO userTask){

        LambdaQueryWrapper<TaskMemberBuyDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskMemberBuyDO::getTaskId,userTask.getTaskId());
        TaskMemberBuyDO taskMemberBuyDO = taskMemberBuyService.getOne(wrapper);
        if(taskMemberBuyDO.getMemberId().equals(updateUserTaskMemberRequest.getMemberId()) && taskMemberBuyDO.getMemberStageId().equals(updateUserTaskMemberRequest.getMemberStageId())){
            log.info("会员任务配置匹配updateUserTaskMemberRequest={}，userTask={}",updateUserTaskMemberRequest.toString(),userTask.toString());
            return true;
        }
        log.info("和任务配置不匹配updateUserTaskMemberRequest={}",updateUserTaskMemberRequest.toString());
        return false;
    }

    private Boolean validateMemberPromotion(UpdateUserTaskMemberRequest updateUserTaskMemberRequest,UserTaskDO userTask){
        LambdaQueryWrapper<TaskMemberPromotionDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskMemberPromotionDO::getTaskId,userTask.getTaskId());
        TaskMemberPromotionDO taskMemberPromotionDO = taskMemberPromotionService.getOne(wrapper);
       /* if(taskMemberPromotionDO.getPromotionActivityId().equals(updateUserTaskMemberRequest.getPromotionActivityId()) && taskMemberPromotionDO.getGoodsGiftId().equals(updateUserTaskMemberRequest.getGoodsGiftId())){
            return true;
        }*/
        log.info("和任务配置不匹配updateUserTaskMemberRequest={}",updateUserTaskMemberRequest.toString());
        return false;
    }

    @Override
    public Page<MyTaskDTO> listMyTaskPage(QueryMyTaskRequest queryMyTaskRequest) {
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getUserId,queryMyTaskRequest.getOpUserId());
        wrapper.orderByDesc(UserTaskDO::getId);
        wrapper.eq(UserTaskDO::getTaskType,queryMyTaskRequest.getTaskType());
        Page<UserTaskDO> userTaskDOPage = this.page(queryMyTaskRequest.getPage(), wrapper);
        List<UserTaskDO> userTaskDOList = userTaskDOPage.getRecords();
        if(CollUtil.isEmpty(userTaskDOList)){
            return queryMyTaskRequest.getPage();
        }
        List<Long> taskIds = userTaskDOList.stream().map(UserTaskDO::getTaskId).collect(Collectors.toList());
        //查询任务
        LambdaQueryWrapper<MarketTaskDO> taskWrapper = Wrappers.lambdaQuery();
        taskWrapper.in(MarketTaskDO::getId,taskIds);
        List<MarketTaskDO> marketTaskDOList = marketTaskService.list(taskWrapper);
        Map<Long,MarketTaskDO> map = marketTaskDOList.stream().collect(Collectors.toMap(MarketTaskDO::getId, Function.identity()));
        Page<MyTaskDTO> result = queryMyTaskRequest.getPage();
        List<MyTaskDTO> myTaskDTOList = Lists.newArrayListWithExpectedSize(userTaskDOList.size());
        result.setRecords(myTaskDTOList);
        userTaskDOPage.getRecords().forEach(userTaskDO -> {
            MyTaskDTO myTaskDTO = new MyTaskDTO();
            PojoUtils.map(userTaskDO,myTaskDTO);
            MarketTaskDO task = map.get(userTaskDO.getTaskId());
            myTaskDTO.setEndTime(task.getEndTime()).setStartTime(task.getStartTime()).setTaskName(task.getTaskName()).setNeverExpires(task.getNeverExpires());
            //收益（企业任务不需要）
            if(userTaskDO.getTaskType().equals(TaskTypeEnum.PLATFORM.getCode()) ){
                myTaskDTO.setProfit(appMarketTaskService.getCommission(userTaskDO.getTaskId()));
            }
            if(myTaskDTO.getFinishType().equals(FinishTypeEnum.MONEY.getCode()) || task.getFinishType().equals(FinishTypeEnum.AMOUNT.getCode())){
                LambdaQueryWrapper<TaskGoodsRelationDO> twrapper = Wrappers.lambdaQuery();
                twrapper.eq(TaskGoodsRelationDO::getTaskId,task.getId());
                int count = taskGoodsRelationService.count(twrapper);
                myTaskDTO.setGoodsCount(count);
            }
            myTaskDTO.setTaskStatus(task.getTaskStatus());
            myTaskDTOList.add(myTaskDTO);
        });
        result.setTotal(userTaskDOPage.getTotal());
        return result;
    }

    @Override
    public String getCommission(Long taskId) {
        String finishRule = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(),
                RuleKeyEnum.FINISH_TYPE.toString());

        String profit = null;
        // 按交易户或交易频次计算佣金 COMMISSION
        Integer finishValue = Integer.valueOf(finishRule);
        // 佣金值
        String commissionRule = marketTaskService.getRuleValue(taskId, RuleTypeEnum.COMMISSION.getCode(),
                RuleKeyEnum.COMMISSION.toString());

            // 按交易额或者交易量才存在 单品执行 多品执行
            String taskRule = marketTaskService.getRuleValue(taskId, RuleTypeEnum.COMMISSION.getCode(),
                    RuleKeyEnum.EXECUTE_TYPE.toString());
            int excType = Integer.parseInt(taskRule);

            // 按交易额
            if (!Objects.isNull(taskRule) && finishValue.equals(FinishTypeEnum.MONEY.getCode())
                    && excType == ExecuteTypeEnum.MORE.getCode()) {
                // 统一执行
                profit = "订单金额的" + commissionRule + "%";
            }
            if (!Objects.isNull(taskRule) && finishValue.equals(FinishTypeEnum.MONEY.getCode())
                    && excType == ExecuteTypeEnum.SINGLE.getCode()) {
                // 单独执行 最高和最低值 用逗号分隔
                if (!Objects.isNull(commissionRule) && !StringUtils.isEmpty(commissionRule)) {
                    String[] commissionRuleArray = commissionRule.split(",");
                    profit = "订单金额的" + commissionRuleArray[0] + "%~" + commissionRuleArray[1] + "%";
                }

            }

            // 按交易量
            if (!Objects.isNull(taskRule) && finishValue.equals(FinishTypeEnum.AMOUNT.getCode())
                    && excType == ExecuteTypeEnum.MORE.getCode()) {
                // 统一执行
                profit = commissionRule + "元/盒";
            }
            if (!Objects.isNull(taskRule) && finishValue.equals(FinishTypeEnum.AMOUNT.getCode())
                    && excType == ExecuteTypeEnum.SINGLE.getCode()) {
                // 单独执行 最高和最低值 用逗号分隔
                if (!Objects.isNull(commissionRule) && !StringUtils.isEmpty(commissionRule)) {
                    String[] commissionRuleArray = commissionRule.split(",");
                    profit = commissionRuleArray[0] + "~" + commissionRuleArray[1] + "元/盒";
                }

            }

        return profit;
    }

    @Override
    public MyTaskDetailDTO getMyTaskDetail(QueryMyTaskDetailRequest queryMyTaskDetailRequest) {
        MyTaskDetailDTO myTaskDetailDTO = new MyTaskDetailDTO();
        UserTaskDO myTask = this.getById(queryMyTaskDetailRequest.getUserTaskId());
        MarketTaskDO task = marketTaskService.getById(myTask.getTaskId());
        Long taskId = task.getId();
        PojoUtils.map(task,myTaskDetailDTO);
        String finishRuleValue = marketTaskService.getRuleValue(myTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.FINISH_TYPE.toString());
        String ruleStr = appMarketTaskService.appendRuleStr(myTask.getTaskId(), finishRuleValue);
        List<String> stringList = Arrays.asList(ruleStr.split("\\|"));
        myTaskDetailDTO.setTaskRule(stringList);
        myTaskDetailDTO.setFinishType(myTask.getFinishType());
        //会员海报
        if(task.getFinishType().equals(FinishTypeEnum.MEMBER_BUY.getCode())){
            TaskMemberDTO taskMemberDTO = taskMemberBuyService.getMemberById(task.getId());
            InviteTaskMemberRequest inviteTaskMemberRequest = new InviteTaskMemberRequest();
            inviteTaskMemberRequest.setTaskId(myTask.getTaskId()).setEid(queryMyTaskDetailRequest.getEid()).setUserTaskId(queryMyTaskDetailRequest.getUserTaskId()).setOpUserId(queryMyTaskDetailRequest.getUserId());
            taskMemberDTO.setPlaybill(taskMemberBuyService.getPromotionPic(inviteTaskMemberRequest));
            myTaskDetailDTO.setTaskMember(taskMemberDTO);
        }
        if(StringUtils.isNotEmpty(task.getEnterpriseType())){
            List<String> split= Arrays.asList(task.getEnterpriseType().split(","));
            List<String> typeList = Lists.newArrayList();
            split.forEach(s -> {
                EnterpriseTypeEnum typeEnum = EnterpriseTypeEnum.getByCode(Integer.valueOf(s));
                typeList.add(typeEnum.getName());
            });
            myTaskDetailDTO.setEnterpriseType(StringUtils.join(typeList,"、"));
        }
        //配送商数量
        if(task.getFinishType().equals(FinishTypeEnum.AMOUNT.getCode()) || task.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())){
            LambdaQueryWrapper<TaskDistributorDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TaskDistributorDO::getTaskId,task.getId());
            int count = taskDistributorService.count(wrapper);
            myTaskDetailDTO.setDistributorCount(count);
            String stepRuleValue = marketTaskService.getRuleValue(task.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());
            if(TaskConstant.ONE.equals(stepRuleValue)){
                myTaskDetailDTO.setIsStepTask(true);
            }else{
                myTaskDetailDTO.setIsStepTask(false);
            }

            //中途增加商品或者配送商
            String key =String.format(TaskConstant.ADD_GOODS_OR_DISTRIBUTOR_NOTIFY,queryMyTaskDetailRequest.getUserTaskId());
            Object notifyObject = redisService.get(key);
            if(Objects.nonNull(notifyObject)){
                myTaskDetailDTO.setAlert(true);
            }else{
                myTaskDetailDTO.setAlert(false);
            }
            //是否会员
            String isMember = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.IS_MEMBER.toString());
            if(StringUtils.isEmpty(isMember)){
                myTaskDetailDTO.setIsMember("不限制");
            }else{
                if(isMember.equals(TaskConstant.ONE)){
                    myTaskDetailDTO.setIsMember("是");
                }else{
                    myTaskDetailDTO.setIsMember("否");
                }
            }
        }
        if(task.getFinishType().equals(FinishTypeEnum.AMOUNT.getCode()) || task.getFinishType().equals(FinishTypeEnum.NEW_ENT.getCode())){
            //支付方式
            String paymentMethod = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.PAYMENT_METHOD.toString());
            if(StringUtils.isEmpty(paymentMethod)){
                myTaskDetailDTO.setPaymentMethod("不限制");
            }else{
                List<String> paymentMethodStrList = Arrays.asList(paymentMethod.split(","));
                String methodStr = "";
                for (int i = 0; i < paymentMethodStrList.size(); i++) {
                    PaymentMethodEnum methodEnum = PaymentMethodEnum.getByCode(Long.valueOf(paymentMethodStrList.get(i)));
                    if(Objects.nonNull(methodEnum)){
                        if(i==paymentMethodStrList.size()-1){
                            methodStr =  methodStr+methodEnum.getName();
                        }else{
                            methodStr =  methodStr+methodEnum.getName()+",";
                        }
                    }
                }
                myTaskDetailDTO.setPaymentMethod(methodStr);
            }
        }

            if(task.getTaskType().equals(TaskTypeEnum.PLATFORM.getCode()) ){
            myTaskDetailDTO.setProfit(appMarketTaskService.getCommission(task.getId()));
        }
        myTaskDetailDTO.setCommissionRule(appMarketTaskService.getCommissionRule(task.getId()));

        return myTaskDetailDTO;
    }

    @Override
    public MyTaskProgressDTO getMyTaskProgress(Long userTaskId) {
        UserTaskDO userTask = this.getById(userTaskId);
        Long taskId = userTask.getTaskId();
       // String compute = marketTaskService.getRuleValue(userTask.getTaskId(),RuleTypeEnum.FINISH_TYPE.getCode(),RuleKeyEnum.COMPUTE_MODE.toString());

        MyTaskProgressDTO myTaskProgressDTO = new MyTaskProgressDTO();
        myTaskProgressDTO.setFinishGoods(userTask.getFinishGoods());
        if(userTask.getFinishType().equals(FinishTypeEnum.MONEY.getCode())){
            String  goal = new BigDecimal(userTask.getGoal()).divide(BigDecimal.valueOf(100)).toString();
            myTaskProgressDTO.setGoal(goal);
            String  finish = new BigDecimal(userTask.getFinishValue()).divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            myTaskProgressDTO.setFinishValue(finish);
            myTaskProgressDTO.setUnfinish(NumberUtil.sub(new BigDecimal(goal),new BigDecimal(finish)).setScale(2, RoundingMode.HALF_UP).toString());
        }else{
            myTaskProgressDTO.setGoal(userTask.getGoal().toString());
            myTaskProgressDTO.setFinishValue(userTask.getFinishValue().toString());
            Integer unfinish = userTask.getGoal()-userTask.getFinishValue();
            if(unfinish<0){
                unfinish = 0;
            }
            myTaskProgressDTO.setUnfinish(unfinish.toString());
        }
        myTaskProgressDTO.setPercent(userTask.getPercent());
        myTaskProgressDTO.setTaskGoodsTotal(userTask.getTaskGoodsTotal());
   /*     if(!StringUtils.isEmpty(compute) && compute.equals(ComputeTypeEnum.MORE.getCode().toString())){
            myTaskProgressDTO.setEachCompute(true);
        }else{
            myTaskProgressDTO.setEachCompute(false);
        }*/
        String stepRuleValue = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());
        List<MyTaskProgressStepDTO> percentList = Lists.newArrayList();
        //Integer total = 0;
        //boolean isStepTask = false;
        if(stepRuleValue.equals(TaskConstant.ONE)){
            ///isStepTask = true;
            String[] step = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.SALE_CONDITION.toString()).split(",");
            List<String> steps = Arrays.asList(step);
            List<Integer> stepList = Lists.newArrayList();

            for (String s : steps) {
                Integer stepInt = Integer.parseInt(s);
                stepList.add(stepInt);
               // total = total + stepInt;
            }
            BigDecimal pre = BigDecimal.ZERO;
            for (int i=0; i<stepList.size();i++) {
                BigDecimal per = BigDecimal.valueOf(stepList.get(i)).divide(BigDecimal.valueOf(stepList.get(stepList.size() - 1)), 2, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.valueOf(100));

                String percent = per.toString();
                MyTaskProgressStepDTO stepDTO = new MyTaskProgressStepDTO();
                if(i==0){
                    stepDTO.setPercent(percent);
                }else{
                    stepDTO.setPercent(per.subtract(pre).setScale(2,BigDecimal.ROUND_HALF_EVEN).toString());
                }
                pre = per;
                if (stepList.get(i).compareTo(userTask.getFinishValue()) <= 0) {
                    stepDTO.setIsFinished(true);
                } else {
                    stepDTO.setIsFinished(false);
                }
                percentList.add(stepDTO);
            }
            String percent = BigDecimal.valueOf(userTask.getFinishValue()).divide(BigDecimal.valueOf(stepList.get(stepList.size()-1)),2,BigDecimal.ROUND_HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100)).toString();
            //阶梯任务完成百分比不取数据库字段
            if(new BigDecimal(percent).compareTo(BigDecimal.valueOf(100))>0){
                percent = "100";
            }
            myTaskProgressDTO.setPercent(percent);
        }
        myTaskProgressDTO.setPercentList(percentList);
        return myTaskProgressDTO;
    }

    @Override
    public List<GoodsProgressDTO> getGoodsProgress(Long userTaskId) {
        UserTaskDO userTask = this.getById(userTaskId);

        LambdaQueryWrapper<UserTaskGoodsDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserTaskGoodsDO::getUserTaskId,userTaskId);
        List<UserTaskGoodsDO> list = userTaskGoodsService.list(lambdaQueryWrapper);

        //todo 规格处理
        List<GoodsProgressDTO> result = PojoUtils.map(list,GoodsProgressDTO.class);
        result.forEach(goodsProgressDTO -> {
            if(userTask.getFinishType().equals(FinishTypeEnum.MONEY.getCode())){
                String  goal = new BigDecimal(userTask.getGoal()).divide(BigDecimal.valueOf(100)).toString();
                goodsProgressDTO.setGoalValue(goal);
                String  finish = new BigDecimal(userTask.getFinishValue()).divide(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                goodsProgressDTO.setFinishValue(finish);
            }else{
                goodsProgressDTO.setGoalValue(userTask.getGoal().toString());
                goodsProgressDTO.setFinishValue(userTask.getFinishValue().toString());
            }
        });

        return result;
    }

    @Override
    public void dispatchEnterpriseTask(List<MarketTaskDO > marketTaskDOS) {
        List<UserTaskDO> userTaskDOList = Lists.newArrayList();
        marketTaskDOS.forEach(taskDO->{
            LambdaQueryWrapper<TaskDeptUserDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TaskDeptUserDO::getTaskId,taskDO.getId());
            //查询任务承接部门
            List<TaskDeptUserDO> deptUserDOS = taskDeptUserService.list(wrapper);
            if(CollUtil.isEmpty(deptUserDOS)){
                log.info("未配置承接部门taskId={}",taskDO.getId());
                return;
            }
            Integer goal = 0;
            String ruleValue = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.FINISH_TYPE.toString());
            //交易额或者交易量
            String  goalValue = marketTaskService.getRuleValue(taskDO.getId(),RuleTypeEnum.FINISH_TYPE.getCode(),RuleKeyEnum.SALE_CONDITION.toString());
            boolean taskGoods = false;
            if(FinishTypeEnum.MONEY.getCode().toString().equals(goalValue) ){
                goal = Integer.parseInt(goalValue)*100;
                taskGoods = true;
            }else if(FinishTypeEnum.AMOUNT.getCode().toString().equals(goalValue)){
                goal = Integer.parseInt(goalValue);
                taskGoods = true;
            }
            Integer taskGoodsTotal = 0;
            if(taskGoods){
                LambdaQueryWrapper<TaskGoodsRelationDO> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(TaskGoodsRelationDO::getTaskId,taskDO.getId());
                taskGoodsTotal = taskGoodsRelationService.count(queryWrapper);
            }
            //过滤出配置到部门具体人员的数据
            List<TaskDeptUserDO> details = deptUserDOS.stream().filter(taskDeptUserDO -> !taskDeptUserDO.getUserId().equals(0)).collect(Collectors.toList());
            List<UserDTO> userDTOS = userApi.listByIds(details.stream().map(TaskDeptUserDO::getUserId).distinct().collect(Collectors.toList()));
            Map<Long,UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId,Function.identity()));
            if(CollUtil.isNotEmpty(details)){
                for (TaskDeptUserDO taskDeptUserDO : details) {
                    UserTaskDO userTaskDO = new UserTaskDO();
                    userTaskDOList.add(userTaskDO);
                    userTaskDO.setFinishValue(0).setTaskStatus(UserTaskStatusEnum.IN_PROGRESS.getStatus()).setPercent("0").setFinishGoods(0).setTaskId(taskDO.getId()).setTaskType(taskDO.getTaskType()).setFinishType(Integer.parseInt(ruleValue))
                            .setEid(taskDO.getEid()).setGoal(goal).setTaskGoodsTotal(taskGoodsTotal).setUserId(taskDeptUserDO.getUserId()).setMobile(userDTOMap.get(taskDeptUserDO.getUserId()).getMobile()).setUserName(userDTOMap.get(taskDeptUserDO.getUserId()).getName());
                }
            }
            //todo 废弃 查询当前部门下的人员信息
            List<TaskDeptUserDO> deptList = deptUserDOS.stream().filter(taskDeptUserDO -> taskDeptUserDO.getUserId().equals(0)).collect(Collectors.toList());

        });
    }

    @Override
    public Boolean takeTask(TakeTaskRequest takeTaskRequest) {
        String lockKey = String.format(TaskConstant.ADD_TASK_LOCK,takeTaskRequest.getTaskId(),takeTaskRequest.getUserId());
        boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, 1, 1, TimeUnit.SECONDS);
        if(!result){
            throw new BusinessException(AssistantErrorCode.TASK_TASK_REPEAT);
        }
        Boolean terminalExist = false;
        Long userId = takeTaskRequest.getUserId();
        Date now = new Date();
        Long id = takeTaskRequest.getTaskId();
        //判断任务状态
        MarketTaskDO task = marketTaskService.getById(id);
        if(Objects.isNull(task)){
            log.info("任务不存在 id={}",id);
            throw new BusinessException(AssistantErrorCode.TASK_NOT_EXIST);
        }
        if(task.getStartTime().after(now) || task.getEndTime().before(now) || !task.getTaskStatus().equals(TaskStatusEnum.IN_PROGRESS.getStatus())){
            log.info("任务非进行中 id={}",id);
            throw new BusinessException(AssistantErrorCode.TASK_NOT_IN_PROGRESS);
        }
        //this.checkTakeCount(id);
        //this.checkTakeTimes(id,userId);
        List<UserSelectedTerminalDO> userSelectedTerminals = null;
        //判断是否限制终端
       /* String ruleValue = marketTaskService.getRuleValue(id,RuleTypeEnum.TAKE.getCode(),RuleKeyEnum.MAX_SELECTED_TERMINAL_NUM.toString());
        if(!TaskConstant.UNLIMIT.equals(ruleValue)){
            if(CollUtil.isEmpty(takeTaskRequest.getSelectedTerminalList())){
                log.info("请选择终端 takeTaskRequest={}",takeTaskRequest.toString());
                throw new BusinessException(AssistantErrorCode.TO_SELECT_TERMINAL);
            }
            //过滤已被其他人被选中的终端
            LambdaQueryWrapper<UserSelectedTerminalDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(UserSelectedTerminalDO::getTaskId,id);
            wrapper.ne(UserSelectedTerminalDO::getUserId,userId);
            wrapper.eq(UserSelectedTerminalDO::getReleased,false);
            wrapper.in(UserSelectedTerminalDO::getTerminalId,takeTaskRequest.getSelectedTerminalList().stream().map(SelectedTerminalRequest::getTerminalId).collect(Collectors.toList()));
            wrapper.select(UserSelectedTerminalDO::getTerminalId);
            List<Long> otherSelected = userSelectedTerminalService.list(wrapper).stream().map(UserSelectedTerminalDO::getTerminalId).collect(Collectors.toList());
            List<SelectedTerminalRequest> notExist = takeTaskRequest.getSelectedTerminalList();
            if(!CollUtil.isEmpty(otherSelected)){
                //用户选择终端保存
                notExist = takeTaskRequest.getSelectedTerminalList().stream().filter(selectedTerminalDTO -> !otherSelected.contains(selectedTerminalDTO.getTerminalId())).collect(Collectors.toList());
                if(notExist.size()!=takeTaskRequest.getSelectedTerminalList().size()){
                    //您选择的部分终端已被申领
                    terminalExist = true;
                    //取差集 保存到redis
                    takeTaskRequest.getSelectedTerminalList().removeAll(notExist);
                    List<SelectedTerminalDTO> selectedTerminalVOS = PojoUtils.map(takeTaskRequest.getSelectedTerminalList(),SelectedTerminalDTO.class );
                    String key = String.format(TaskConstant.USER_SELECTED_TERMINAL_KEY,userId,id);
                    redisTemplate.opsForValue().set(key,selectedTerminalVOS,30, TimeUnit.MINUTES);
                    if(notExist.size()==0){
                        log.info("所选终端已全部被其他人占用");
                        return terminalExist;
                    }
                }
            }
            //只要有
            if(terminalExist){
                return terminalExist;
            }
            userSelectedTerminals = Lists.newArrayListWithExpectedSize(notExist.size());
            List<UserSelectedTerminalDO> finalUserSelectedTerminals = userSelectedTerminals;
            this.checkSelectedTeminal(id,notExist.size());
            notExist.forEach(selectedTerminalDTO -> {
                UserSelectedTerminalDO userSelectedTerminal = new UserSelectedTerminalDO();
                userSelectedTerminal.setUserId(userId);
                userSelectedTerminal.setTaskId(id);
                userSelectedTerminal.setTerminalAddress(selectedTerminalDTO.getTerminalAddress());
                userSelectedTerminal.setTerminalName(selectedTerminalDTO.getTerminalName());
                userSelectedTerminal.setTerminalId(selectedTerminalDTO.getTerminalId());
                finalUserSelectedTerminals.add(userSelectedTerminal);
            });
        }*/
        //判断任务是否已承接 且任务是否已完成 否则不再创建新的任务
        LambdaQueryWrapper<UserTaskDO> mytask = Wrappers.lambdaQuery();
        mytask.eq(UserTaskDO::getTaskId,id);
        mytask.eq(UserTaskDO::getUserId,userId);
        ///mytask.eq(UserTaskDO::getTaskStatus,UserTaskStatusEnum.IN_PROGRESS.getStatus());
        UserTaskDO myTask = userTaskService.getOne(mytask);
        // 同一个任务没完成不可再次承接
        if(!Objects.isNull(myTask)){
            log.info("任务已承接 userID={},taskid={}",userId,id);
            throw new BusinessException(AssistantErrorCode.UNFINISH_TASK);
        }
        if(task.getFullCover()==0){
            this.validTaskArea(takeTaskRequest,id);
        }
        UserTaskDO userTask = new UserTaskDO();
        userTask.setTaskId(id);
        userTask.setEid(task.getEid());
        userTask.setTaskType(task.getTaskType());
        String finishRuleValue = marketTaskService.getRuleValue(id, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.FINISH_TYPE.toString());
        Integer finishType = Integer.valueOf(finishRuleValue);
        userTask.setFinishType(finishType);
        //目标值（ 交易额 元单位转成分存储）
        Integer goal = 0;
        //交易额或交易量的才有目标值
        if(finishType.equals(FinishTypeEnum.MONEY.getCode()) || finishType.equals(FinishTypeEnum.AMOUNT.getCode()) || finishType.equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())){
            String saleRuleValue = marketTaskService.getRuleValue(id, RuleTypeEnum.FINISH_TYPE.getCode(),RuleKeyEnum.SALE_CONDITION.toString());

           //阶梯任务处理 取最大值作为目标值
            if(saleRuleValue.contains(",")){
                String[]  saleRuleArray = saleRuleValue.split(",");
                saleRuleValue = saleRuleArray[saleRuleArray.length-1];
            }
            if(finishType.equals(FinishTypeEnum.MONEY.getCode())){
                goal = new BigDecimal(saleRuleValue).multiply(BigDecimal.valueOf(100)).intValue();
            }else{
                goal = Integer.valueOf(saleRuleValue);
            }
        }


        UserDTO userDTO = userApi.getById(userId);
        userTask.setGoal(goal);
        userTask.setUserId(userId).setUserName(userDTO.getName()).setMobile(userDTO.getMobile()).setEid(takeTaskRequest.getCurrentEid());

        //如果按交易额和交易量 上传资料 计算  要涉及商品维度 否则不需要保存用户任务商品信息
        //交易频次 交易户
        if(!FinishTypeEnum.MONEY.getCode().equals(finishType) && !FinishTypeEnum.AMOUNT.getCode().equals(finishType) && !FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode().equals(finishType) ){
            SpringUtil.getBean(UserTaskServiceImpl.class).saveTask(userTask,null,userSelectedTerminals);
            return terminalExist;
        }
        //任务关联商品
        LambdaQueryWrapper<TaskGoodsRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(TaskGoodsRelationDO::getTaskId,id);
        List<TaskGoodsRelationDO> taskGoodsRelations = taskGoodsRelationService.list(lambdaQueryWrapper);
        if(CollUtil.isEmpty(taskGoodsRelations)){
            log.error("任务未设关联商品 taskId={}",id);
            throw new BusinessException(AssistantErrorCode.TASK_GOODS_NOT_IN_SET);
        }
        //包涵商品数量
        userTask.setTaskGoodsTotal(taskGoodsRelations.size());
        Integer finalGoal = goal;
        List<UserTaskGoodsDO> userTaskGoodsList = Lists.newArrayListWithExpectedSize(taskGoodsRelations.size());
        taskGoodsRelations.forEach(taskGoodsRelation -> {
            UserTaskGoodsDO userTaskGoods = new UserTaskGoodsDO();
            userTaskGoods.setGoalValue(finalGoal);
            userTaskGoods.setTaskId(id);
            userTaskGoods.setUserTaskId(userTask.getId());
            userTaskGoods.setValueType(finishType);
            userTaskGoods.setGoodsId(taskGoodsRelation.getGoodsId());
            userTaskGoods.setGoodsName(taskGoodsRelation.getGoodsName());
            userTaskGoods.setUserId(userId);
            userTaskGoodsList.add(userTaskGoods);
        });
        SpringUtil.getBean(UserTaskServiceImpl.class).saveTask(userTask,userTaskGoodsList,userSelectedTerminals);

        return false;
    }

    private void validTaskArea(TakeTaskRequest takeTaskRequest,Long taskId){
        // 查询当前用户的销售区域 -到市
        UserSalesAreaDTO saleArea = null;
        List<String> cityCodeList = Lists.newArrayList();
        if(takeTaskRequest.getUserType().equals(UserTypeEnum.ZIRANREN)){
            saleArea = userApi.getSaleAreaByUserId(takeTaskRequest.getUserId());
            if(Objects.isNull(saleArea)){
                throw new BusinessException(AssistantErrorCode.TASK_AREA_NOT_MATCH);
            }

            if(saleArea.getSalesAreaAllFlag()==0){
                cityCodeList = userApi.getSaleAreaDetailByUserId(takeTaskRequest.getUserId(),2);
                if(CollUtil.isEmpty(cityCodeList)){
                    log.info("未设置销售区域看不到任务cityCodeList{}",cityCodeList.toString());
                    throw new BusinessException(AssistantErrorCode.TASK_AREA_NOT_MATCH);
                }
            }else{
                log.info("销售区域设置的全国无需校验takeTaskRequest={}",takeTaskRequest.toString());
                return;
            }
        }else{
            //小三元取企业
            EnterpriseSalesAreaDTO enterpriseSalesArea = enterpriseApi.getEnterpriseSalesArea(takeTaskRequest.getCurrentEid());
            if(Objects.isNull(enterpriseSalesArea)){
                log.info("小三元未设置企业销售区域看不到任务");
                throw new BusinessException(AssistantErrorCode.TASK_AREA_NOT_MATCH);
            }
            String json = enterpriseSalesArea.getJsonContent();
            List<LocationTreeBO> locationTreeBOS = JSON.parseArray(json, LocationTreeBO.class);
            List<List<LocationTreeBO>> collect = locationTreeBOS.stream().map(LocationTreeBO::getChildren).collect(Collectors.toList());
            for (List<LocationTreeBO> l : collect) {
                List<String> codes = l.stream().map(LocationTreeBO::getCode).collect(Collectors.toList());
                cityCodeList.addAll(codes);
            }
        }
        // 根据销售区域 匹配 任务
        LambdaQueryWrapper<TaskAreaRelationDO> lambda = new QueryWrapper<TaskAreaRelationDO>().lambda();
        lambda.eq(TaskAreaRelationDO::getTaskId,taskId).select(TaskAreaRelationDO::getTaskId, TaskAreaRelationDO::getTaskId,
                TaskAreaRelationDO::getCityCode);
        List<TaskAreaRelationDO> taskAreaRelations = taskAreaRelationService.list(lambda);
        List<String> finalCityCodeList = cityCodeList;

        boolean present = taskAreaRelations.stream().filter(taskAreaRelation -> finalCityCodeList.contains(taskAreaRelation.getCityCode())).findAny().isPresent();
        if(!present){
            log.info("任务区域不匹配 cityCodeList={}",cityCodeList.toString());
            throw new BusinessException(AssistantErrorCode.TASK_AREA_NOT_MATCH);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTask(UserTaskDO userTask,  List<UserTaskGoodsDO> userTaskGoodsList , List<UserSelectedTerminalDO> userSelectedTerminals){
        if(!Objects.isNull(userTask)){
            this.save(userTask);
        }
        if(!CollUtil.isEmpty(userTaskGoodsList)){
            userTaskGoodsList.forEach(u->{
                u.setUserTaskId(userTask.getId());
            });
            userTaskGoodsService.saveBatch(userTaskGoodsList);
        }
        if(!CollUtil.isEmpty(userSelectedTerminals)){
            userSelectedTerminals.forEach(u->{
                u.setUserTaskId(userTask.getId());
            });
            userSelectedTerminalService.saveBatch(userSelectedTerminals);
        }
    }

    /**
     * 领取终端数限制
     * @param taskId
     * @param count
     */
    private void checkSelectedTeminal(Long taskId,Integer count){
        String ruleValue = marketTaskService.getRuleValue(taskId,RuleTypeEnum.TAKE.getCode(),RuleKeyEnum.MAX_SELECTED_TERMINAL_NUM.toString());
        if(!StringUtils.isEmpty(ruleValue) && !TaskConstant.UNLIMIT.equals(ruleValue)){
            //查询已参与次数
            Integer rule = Integer.parseInt(ruleValue);
            if(rule.compareTo(count) < 0){
                log.info("领取终端数最多={}",rule);
              //todo 废弃  throw new BusinessException("领取终端数最多"+ruleValue+"个");
            }
        }
    }
    /**
     * 承接人数校验
     * @param taskId
     */
    private void checkTakeCount(Long taskId){
        //参与条件
        String takeRuleValue = marketTaskService.getRuleValue(taskId,RuleTypeEnum.TAKE.getCode(),RuleKeyEnum.MAX_TAKE_PERSON.toString());
        if(!StringUtils.isEmpty(takeRuleValue)&& !TaskConstant.UNLIMIT.equals(takeRuleValue)){
            //查询已参与人数
            Integer takeCount =  this.count(new QueryWrapper<UserTaskDO>().lambda().eq(UserTaskDO::getTaskId,taskId));
            Integer rule = Integer.parseInt(takeRuleValue);
            if(rule.compareTo(takeCount) <= 0){
                log.info("已超出承接人数限制taskId={}",taskId);
                throw new BusinessException(AssistantErrorCode.OVER_TAKE_LIMIT);
            }
        }
    }

    /**
     * 承接次数校验
     * @param taskId
     */
    private void checkTakeTimes(Long taskId,Long userId){
        //参与次数规则
        String timesRuleValue = marketTaskService.getRuleValue(taskId,RuleTypeEnum.TAKE.getCode(),RuleKeyEnum.EACH_PERSON_TIMES.toString());
        if(!TaskConstant.UNLIMIT.equals(timesRuleValue)){
            //查询已参与次数
            Integer times =  this.count(new QueryWrapper<UserTaskDO>().lambda().eq(UserTaskDO::getTaskId,taskId).eq(UserTaskDO::getUserId,userId));
            Integer rule = Integer.parseInt(timesRuleValue);
            if(rule.compareTo(times) <= 0){
                log.info("已超出承接次数限制");
                throw new BusinessException(AssistantErrorCode.OVER_TAKE_TIMES_LIMIT);
            }
        }
    }

    @Override
    public void updateTaskCommission(List<UserTaskDO> userTasks,Long updateUserId) {

        //已完成的非阶梯任务变更佣金状态
        if(CollUtil.isEmpty(userTasks)){
            return;
        }
        //变更佣金状态
        userTasks.forEach(userTaskDO -> {
            UpdateCommissionsEffectiveRequest effectiveRequest = new UpdateCommissionsEffectiveRequest();
            effectiveRequest.setUserId(userTaskDO.getUserId()).setUserTaskId(userTaskDO.getId()).setOpUserId(updateUserId);
            effectiveRequest.setFinishType(userTaskDO.getFinishType());
            log.info("变更佣金状态effectiveRequest={}",effectiveRequest.toString());
            commissionsService.updateCommissionsEffective(effectiveRequest);
        });
    }

    @Override
    public void addStepTaskCommissionV2(UserTaskDO userTask) {
        //查询完成的最高阶梯
        LambdaQueryWrapper<UserTaskStepDO> stepWrapper = Wrappers.lambdaQuery();
        stepWrapper.eq(UserTaskStepDO::getUserTaskId,userTask.getId()).orderByDesc(UserTaskStepDO::getId);
        List<UserTaskStepDO> taskStepDOS = userTaskStepService.list(stepWrapper);
        String maxStepCommissionStr = taskStepDOS.get(0).getCommision();
        BigDecimal maxCommission = new BigDecimal(maxStepCommissionStr);
        //查询所有阶梯任务订单
        LambdaQueryWrapper<TaskOrderDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskOrderDO::getUserTaskId,userTask.getId());
        //伪任务订单只是为了复用以前代码
        List<TaskOrderDO> taskOrderDOList = Lists.newArrayList();
        LambdaQueryWrapper<TaskAccompanyingBillDO> taskAccompanyingBillDOLambdaQueryWrapper = Wrappers.lambdaQuery();
        taskAccompanyingBillDOLambdaQueryWrapper.eq(TaskAccompanyingBillDO::getUserTaskId,userTask.getId());
        List<TaskAccompanyingBillDO> taskAccompanyingBillDOS = taskAccompanyingBillService.list(taskAccompanyingBillDOLambdaQueryWrapper);
        taskAccompanyingBillDOS.forEach(taskAccompanyingBillDO -> {
            TaskOrderDO taskOrderDO = new TaskOrderDO();
            taskOrderDOList.add(taskOrderDO);
            taskOrderDO.setOrderId(taskAccompanyingBillDO.getAccompanyingBillId()).setOrderNo(taskAccompanyingBillDO.getDocCode());
        });
        MarketTaskDO marketTaskDO = marketTaskService.getById(userTask.getTaskId());
        //任务收益
        AddCommissionsToUserRequest addCommissionsToUserRequest = new AddCommissionsToUserRequest();
        Integer effectStatus = CommissionsStatusEnum.SETTLEMENT.getCode();
        addCommissionsToUserRequest.setEffectStatus(effectStatus).setFinishType(userTask.getFinishType()).setTaskId(userTask.getTaskId()).setTaskName(marketTaskDO.getTaskName())
                .setUserId(userTask.getUserId()).setUserTaskId(userTask.getId()).setSources(CommissionsSourcesEnum.TASK.getCode()).setOpTime(DateUtil.date());
        //明细
        List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> detailList = Lists.newArrayList();
        //上线分成
        AddCommissionsToUserRequest inviteCommissions = new AddCommissionsToUserRequest();
        inviteCommissions.setSources(CommissionsSourcesEnum.SUBORDINATE.getCode());
        inviteCommissions.setEffectStatus(CommissionsStatusEnum.SETTLEMENT.getCode()).setFinishType(userTask.getFinishType()).setTaskId(userTask.getTaskId()).setTaskName(marketTaskDO.getTaskName())
                .setUserTaskId(userTask.getId()).setSources(CommissionsSourcesEnum.SUBORDINATE.getCode()).setOpTime(DateUtil.date());
        //明细
        List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> inviteDetailList = Lists.newArrayList();
        taskOrderDOList.forEach(taskOrderDO -> {
            LambdaQueryWrapper<TaskOrderGoodsDO> wrapper1 = Wrappers.lambdaQuery();
            wrapper1.eq(TaskOrderGoodsDO::getTaskOrderId,taskOrderDO.getId());
            List<TaskOrderGoodsDO> taskOrderGoodsList = Lists.newArrayList();
            LambdaQueryWrapper<MatchDetailDO> matchDetailWrapper = Wrappers.lambdaQuery();
            matchDetailWrapper.eq(MatchDetailDO::getAccompanyingBillId,taskOrderDO.getOrderId());
            List<MatchDetailDO> matchDetailDOS = matchDetailService.list(matchDetailWrapper);

            LambdaQueryWrapper<TaskGoodsRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
            lambdaQueryWrapper.eq(TaskGoodsRelationDO::getTaskId, userTask.getTaskId());
            //任务配置商品
            List<TaskGoodsRelationDO> relations = taskGoodsRelationService.list(lambdaQueryWrapper);
            //任务配置商品
            List<Long> taskGoodsIds = relations.stream().map(TaskGoodsRelationDO::getGoodsId).collect(Collectors.toList());

            matchDetailDOS.forEach(matchDetailDO -> {
                TaskOrderGoodsDO taskOrderGoodsDO = new TaskOrderGoodsDO();
                if((CollUtil.isNotEmpty(taskGoodsIds)) && !taskGoodsIds.contains(matchDetailDO.getYlGoodsId())){
                    log.info("商品不在此任务中商品id={}",matchDetailDO.getYlGoodsId());
                    return;
                }
                taskOrderGoodsList.add(taskOrderGoodsDO);
                taskOrderGoodsDO.setRealAmount(matchDetailDO.getQuantity().intValue());
            });
            this.computeStepCommission(addCommissionsToUserRequest,detailList,inviteCommissions,inviteDetailList,userTask,taskOrderGoodsList,taskOrderDO,marketTaskDO,maxCommission);
        });
        if(CollUtil.isNotEmpty(inviteDetailList)){
            log.info("发放上线分成佣金inviteDetailList={}",inviteDetailList.toString());
            commissionsService.addCommissionsToUser(inviteCommissions);
        }
        commissionsService.addCommissionsToUser(addCommissionsToUserRequest);
    }

    @Override
    public void addStepTaskCommission(UserTaskDO userTask) {
        //查询完成的最高阶梯
        LambdaQueryWrapper<UserTaskStepDO> stepWrapper = Wrappers.lambdaQuery();
        stepWrapper.eq(UserTaskStepDO::getUserTaskId,userTask.getId()).orderByDesc(UserTaskStepDO::getId);
        List<UserTaskStepDO> taskStepDOS = userTaskStepService.list(stepWrapper);
        Integer maxStepCommission = taskStepDOS.stream().map(UserTaskStepDO::getStepNo).max((e, u) -> e.compareTo(u)).get();
        String maxStepCommissionStr = taskStepDOS.get(0).getCommision();
        BigDecimal maxCommission = new BigDecimal(maxStepCommissionStr);
        //查询所有阶梯任务订单
        LambdaQueryWrapper<TaskOrderDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskOrderDO::getUserTaskId,userTask.getId());
        List<TaskOrderDO> taskOrderDOList = taskOrderService.list(wrapper);
        MarketTaskDO marketTaskDO = marketTaskService.getById(userTask.getTaskId());
        //任务收益
        AddCommissionsToUserRequest addCommissionsToUserRequest = new AddCommissionsToUserRequest();
        Integer effectStatus = CommissionsStatusEnum.SETTLEMENT.getCode();
        addCommissionsToUserRequest.setEffectStatus(effectStatus).setFinishType(userTask.getFinishType()).setTaskId(userTask.getTaskId()).setTaskName(marketTaskDO.getTaskName())
                .setUserId(userTask.getUserId()).setUserTaskId(userTask.getId()).setSources(CommissionsSourcesEnum.TASK.getCode()).setOpTime(DateUtil.date());
        //明细
        List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> detailList = Lists.newArrayList();
        //上线分成
        AddCommissionsToUserRequest inviteCommissions = new AddCommissionsToUserRequest();
        inviteCommissions.setSources(CommissionsSourcesEnum.SUBORDINATE.getCode());
        inviteCommissions.setEffectStatus(CommissionsStatusEnum.SETTLEMENT.getCode()).setFinishType(userTask.getFinishType()).setTaskId(userTask.getTaskId()).setTaskName(marketTaskDO.getTaskName())
                .setUserTaskId(userTask.getId()).setSources(CommissionsSourcesEnum.SUBORDINATE.getCode()).setOpTime(DateUtil.date());
        //明细
        List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> inviteDetailList = Lists.newArrayList();


        taskOrderDOList.forEach(taskOrderDO -> {
            LambdaQueryWrapper<TaskOrderGoodsDO> wrapper1 = Wrappers.lambdaQuery();
            wrapper1.eq(TaskOrderGoodsDO::getTaskOrderId,taskOrderDO.getId());
            List<TaskOrderGoodsDO> taskOrderGoodsList = taskOrderGoodsService.list(wrapper1);
            this.computeStepCommission(addCommissionsToUserRequest,detailList,inviteCommissions,inviteDetailList,userTask,taskOrderGoodsList,taskOrderDO,marketTaskDO,maxCommission);
        });
        if(CollUtil.isNotEmpty(inviteDetailList)){
            log.info("发放上线分成佣金inviteDetailList={}",inviteDetailList.toString());
            commissionsService.addCommissionsToUser(inviteCommissions);
        }
        commissionsService.addCommissionsToUser(addCommissionsToUserRequest);
    }
    /**
     * 计算阶梯任务佣金
     * @param userTask
     * @param taskOrderGoodsList
     * @param taskOrder
     * @param taskBase
     * @param commmission
     */
    private void computeStepCommission(AddCommissionsToUserRequest addCommissionsToUserRequest,List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> detailList,AddCommissionsToUserRequest inviteCommissions,
            List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> inviteDetailList,UserTaskDO userTask, List<TaskOrderGoodsDO> taskOrderGoodsList, TaskOrderDO taskOrder, MarketTaskDO taskBase, BigDecimal commmission){
        log.info("订单完结计算佣金开始orderno={},taskOrderGoodsList={}",taskOrder.getOrderNo(),taskOrderGoodsList);
        Long taskId = userTask.getTaskId();

        //给邀请人分成佣金明细
        AddCommissionsToUserRequest.AddCommToUserDetailRequest inviteComm = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
        //佣金明细
        AddCommissionsToUserRequest.AddCommToUserDetailRequest request = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
        request.setSubAmount(BigDecimal.ZERO);
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
        //阶梯任务统一执行每个商品分佣金额相同
        BigDecimal invitecom = null;
        if (finalInviteRule && finalInvite) {
            BigDecimal rate = new BigDecimal(rule);
            if (rate.compareTo(BigDecimal.ZERO) > 0) {
                rate = rate.divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                invitecom = commmission.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                commmission = commmission.subtract(invitecom).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            }
        }
        for (TaskOrderGoodsDO g : taskOrderGoodsList) {
            // 佣金 根据完成的最高阶梯计算
            if (finalInviteRule && finalInvite) {
                BigDecimal rate = new BigDecimal(rule);
                if (rate.compareTo(BigDecimal.ZERO) > 0) {
                    inviteComm.setSubAmount(inviteComm.getSubAmount().add(invitecom.multiply(BigDecimal.valueOf(g.getRealAmount()))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
            }
            BigDecimal totalAmount = request.getSubAmount().add(commmission.multiply(BigDecimal.valueOf(g.getRealAmount()))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            request.setSubAmount(totalAmount);

        }
        request.setOrderCode(taskOrder.getOrderNo()).setOrderId(taskOrder.getOrderId());
        detailList.add(request);
        //订单佣金
        addCommissionsToUserRequest.setDetailList(detailList);
       // commissionsApi.addCommissionsToUser(addCommissionsToUserRequest);

        //上线分成
        if(finalInviteRule && finalInvite){

            inviteComm.setNewUserId(taskOrder.getUserId()).setNewUserName(team.getName()).setNewTime(team.getRegisterTime());
            inviteComm.setOrderId(taskOrder.getOrderId());
            inviteComm.setOrderCode(taskOrder.getOrderNo());
            inviteComm.setOpUserId(inviteUserId);
            inviteDetailList.add(inviteComm);
            log.info("上线分成佣金inviteComm={}", JSON.toJSONString(inviteComm));
            inviteCommissions.setDetailList(inviteDetailList);
            inviteCommissions.setUserId(inviteUserId);
            //commissionsApi.addCommissionsToUser(inviteCommissions);
        }
    }

    @Override
    public UserTypeDTO getUserTypeByUserTaskId(Long userTaskId) {
        UserTypeDTO userTypeDTO = new UserTypeDTO();
        UserTaskDO userTaskDO = this.getById(userTaskId);
        if(Objects.isNull(userTaskDO)){
            return null;
        }
        if(userTaskDO.getEid()==0){
            userTypeDTO.setCurrentEid(0L);
            userTypeDTO.setUserTypeEnum(UserTypeEnum.ZIRANREN);
        }else{
            userTypeDTO.setUserTypeEnum(com.yiling.framework.common.util.Constants.YILING_EID.equals(userTaskDO.getEid()) ? UserTypeEnum.YILING : UserTypeEnum.XIAOSANYUAN);
            userTypeDTO.setCurrentEid(userTaskDO.getEid());
        }
        userTypeDTO.setUserId(userTaskDO.getUserId());
        return userTypeDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollbackUserTask(String orderNo) {
        LambdaQueryWrapper<TaskMemberRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskMemberRecordDO::getOrderNo,orderNo);
        List<TaskMemberRecordDO> memberRecordDOList = taskMemberRecordService.list(wrapper);
        if(CollUtil.isEmpty(memberRecordDOList)){
            log.info("未查到购买会员任务记录orderNo={}",orderNo);
            return;
        }
        memberRecordDOList.forEach(memberRecordDO -> {
            UserTaskDO userTaskDO = userTaskService.getById(memberRecordDO.getUserTaskId());
            if(Objects.isNull(userTaskDO)){
                log.info("未查到购买会员任务承接记录orderNo={}",orderNo);
                return;
            }
            if(!userTaskDO.getTaskStatus().equals(UserTaskStatusEnum.IN_PROGRESS.getStatus())){
                log.info("承接任务非进行中userTaskDO={}",userTaskDO.toString());
                return ;
            }
            //任务结束不退
            MarketTaskDO marketTaskDO = marketTaskService.getById(userTaskDO.getTaskId());
            if(new Date().compareTo(marketTaskDO.getEndTime())>0 || marketTaskDO.getTaskStatus().equals(TaskStatusEnum.STOP.getStatus())){
                log.info("任务结束不退orderNo={}，endTime={}",orderNo,marketTaskDO.getEndTime());
                return;
            }
            memberRecordDO.setDelFlag(1).setRemark("会员退款").setUpdateTime(new Date());
            int result = taskMemberRecordService.updateRecordById(memberRecordDO);
            if(result==0){
                log.info("已删除orderNo{}",orderNo);
                return;
            }
            userTaskDO.setFinishValue(userTaskDO.getFinishValue()-1);
            userTaskService.updateById(userTaskDO);
            RemoveCommissionsToUserRequest request = new RemoveCommissionsToUserRequest();
            request.setOrderCode(orderNo).setUserTaskId(userTaskDO.getId()).setOpTime(new Date());
            commissionsService.removeCommissionsToUser(request);
        });

    }

    @Override
    public void handleTaskCommissionForUpload() {
        //查询结束时间是上个月的任务
        log.info("发放上传随货同行单任务佣金定时任务开始执行");
        LambdaQueryWrapper<MarketTaskDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MarketTaskDO::getTaskStatus, TaskStatusEnum.END.getStatus()).eq(MarketTaskDO::getFinishType,FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode());
        DateTime dateTime = DateUtil.offset(new Date(), DateField.MONTH, -1);
        lambdaQueryWrapper.between(MarketTaskDO::getEndTime,DateUtil.beginOfMonth(dateTime),DateUtil.endOfMonth(dateTime)).last("limit 1");
        List<MarketTaskDO> list = marketTaskService.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            log.info("没有到达的任务");
            return;
        }
        list.forEach(t -> {

            marketTaskService.finishedTask(t.getId(), 0L);
            //非阶梯任务处理
            String ruleValue = SpringUtil.getBean(MarketTaskService.class).getRuleValue(t.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());
            LambdaQueryWrapper<UserTaskDO> userTaskQueryWrapper = new QueryWrapper<UserTaskDO>().lambda();
            userTaskQueryWrapper.eq(UserTaskDO::getTaskStatus, UserTaskStatusEnum.IN_PROGRESS.getStatus());
            userTaskQueryWrapper.eq(UserTaskDO::getTaskId, t.getId());
            List<UserTaskDO> userTasks = userTaskService.list(userTaskQueryWrapper);
            if (CollUtil.isEmpty(userTasks)) {
                return;
            }
            List<UserTaskDO> updateUserTasks = Lists.newArrayListWithExpectedSize(userTasks.size());
            if (!TaskConstant.ONE.equals(ruleValue)) {
                // 更新进行中的用户任务为未完成
                marketTaskService.updateUserTask(userTasks, updateUserTasks, UserTaskStatusEnum.UN_FINISH);
            } else {
                //阶梯任务 达到阶梯条件的更新已完成 未达到更新为未完成
                List<UserTaskDO> finishUserTaskList = userTasks.stream().filter(userTaskDO -> {
                    LambdaQueryWrapper<UserTaskStepDO> wrapper = Wrappers.lambdaQuery();
                    wrapper.eq(UserTaskStepDO::getUserTaskId, userTaskDO.getId());
                    int count = userTaskStepService.count(wrapper);
                    return count > 0;
                }).collect(Collectors.toList());
                // 阶梯任务 更新佣金状态
                marketTaskService.updateUserTask(finishUserTaskList, updateUserTasks, UserTaskStatusEnum.FINISHED);
                finishUserTaskList.forEach(userTaskDO -> {
                    userTaskService.addStepTaskCommissionV2(userTaskDO);
                });
                userTasks.removeAll(finishUserTaskList);
                //未完成的不给佣金
                marketTaskService.updateUserTask(userTasks, updateUserTasks, UserTaskStatusEnum.UN_FINISH);
            }
            userTaskService.updateBatchById(updateUserTasks);
        });

    }
}
