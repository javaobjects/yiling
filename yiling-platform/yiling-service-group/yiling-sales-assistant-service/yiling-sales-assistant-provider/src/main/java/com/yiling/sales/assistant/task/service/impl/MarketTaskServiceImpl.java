package com.yiling.sales.assistant.task.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderAssistantApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.QueryAssistantFirstOrderRequest;
import com.yiling.order.order.dto.request.QueryAssistantOrderFirstRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.sales.assistant.commissions.api.CommissionsApi;
import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.enums.CommissionsSourcesEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsStatusEnum;
import com.yiling.sales.assistant.task.constant.TaskConstant;
import com.yiling.sales.assistant.task.dao.MarketTaskMapper;
import com.yiling.sales.assistant.task.dto.TaskAreaDTO;
import com.yiling.sales.assistant.task.dto.TaskCountDTO;
import com.yiling.sales.assistant.task.dto.TaskDTO;
import com.yiling.sales.assistant.task.dto.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.TaskGoodsDTO;
import com.yiling.sales.assistant.task.dto.TaskRuleDTO;
import com.yiling.sales.assistant.task.dto.TaskTraceDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskAreaRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskGoodsRelationRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskMemberPromotionRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskMemberRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskRuleRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.dto.request.StopTaskRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateTaskGoodsRelationRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateTaskRequest;
import com.yiling.sales.assistant.task.dto.request.UpdateTaskRuleRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.SaTaskRegisterTerminalDO;
import com.yiling.sales.assistant.task.entity.TaskAreaJsonDO;
import com.yiling.sales.assistant.task.entity.TaskAreaRelationDO;
import com.yiling.sales.assistant.task.entity.TaskCustomerCountDO;
import com.yiling.sales.assistant.task.entity.TaskDeptRelationDO;
import com.yiling.sales.assistant.task.entity.TaskDistributorDO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;
import com.yiling.sales.assistant.task.entity.TaskMemberBuyDO;
import com.yiling.sales.assistant.task.entity.TaskMemberPromotionDO;
import com.yiling.sales.assistant.task.entity.TaskRuleDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.entity.UserTaskStepDO;
import com.yiling.sales.assistant.task.enums.AssistantErrorCode;
import com.yiling.sales.assistant.task.enums.ExecuteTypeEnum;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.sales.assistant.task.enums.SaleTypeEnum;
import com.yiling.sales.assistant.task.enums.TaskStatusEnum;
import com.yiling.sales.assistant.task.enums.TaskTypeEnum;
import com.yiling.sales.assistant.task.enums.UserTaskStatusEnum;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.SaTaskRegisterTerminalService;
import com.yiling.sales.assistant.task.service.TaskAreaJsonService;
import com.yiling.sales.assistant.task.service.TaskAreaRelationService;
import com.yiling.sales.assistant.task.service.TaskCustomerCountService;
import com.yiling.sales.assistant.task.service.TaskDeptRelationService;
import com.yiling.sales.assistant.task.service.TaskDeptUserService;
import com.yiling.sales.assistant.task.service.TaskDistributorService;
import com.yiling.sales.assistant.task.service.TaskGoodsRelationService;
import com.yiling.sales.assistant.task.service.TaskMemberBuyService;
import com.yiling.sales.assistant.task.service.TaskMemberPromotionService;
import com.yiling.sales.assistant.task.service.TaskModifyNotifyService;
import com.yiling.sales.assistant.task.service.TaskOrderService;
import com.yiling.sales.assistant.task.service.TaskRuleService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.sales.assistant.task.service.UserTaskStepService;
import com.yiling.sales.assistant.userteam.api.UserTeamApi;
import com.yiling.sales.assistant.userteam.dto.UserTeamDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.usercustomer.api.UserCustomerApi;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 任务基本信息表  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Service
@Slf4j
public class MarketTaskServiceImpl extends BaseServiceImpl<MarketTaskMapper, MarketTaskDO> implements MarketTaskService {

    @Autowired
    private TaskAreaJsonService taskAreaJsonService;

    @Autowired
    private TaskAreaRelationService taskAreaRelationService;

    @Autowired
    private TaskDeptRelationService taskDeptRelationService;

    @Autowired
    private TaskGoodsRelationService taskGoodsRelationService;

    @Autowired
    private TaskRuleService taskRuleService;

    @Autowired
    private TaskDeptUserService taskDeptUserService;
    @DubboReference
    private GoodsApi goodsApi;
    @DubboReference
    private PopGoodsApi popGoodsApi;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private TaskOrderService taskOrderService;

    @Autowired
    private SaTaskRegisterTerminalService saTaskRegisterTerminalService;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private UserCustomerApi userCustomerApi;

    @Autowired
    private TaskCustomerCountService taskCustomerCountService;
    @Autowired
    private UserTaskStepService userTaskStepService;
    @DubboReference
    private CommissionsApi commissionsApi;

    @DubboReference
    private UserTeamApi userTeamApi;
    @Autowired
    private FileService fileService;


    @Autowired
    private TaskDistributorService taskDistributorService;
    @Autowired
    private TaskMemberBuyService taskMemberBuyService;
    @Autowired
    private TaskMemberPromotionService taskMemberPromotionService;

    @DubboReference
    private OrderAssistantApi orderAssistantApi;

    @DubboReference
    private OrderApi orderApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private OrderDetailChangeApi orderDetailChangeApi;

    @DubboReference
    private OrderDetailApi orderDetailApi;

    @DubboReference
    private GoodsYilingPriceApi goodsYilingPriceApi;


    @DubboReference
    private MemberApi memberApi;

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @Autowired
    private TaskModifyNotifyService taskModifyNotifyService;
    /**
     * 任务区域
     *
     * @param addTaskAreaRequests
     * @return
     */
    private List<TaskAreaRelationDO> getRealation(List<AddTaskAreaRequest> addTaskAreaRequests) {
        if (CollectionUtil.isEmpty(addTaskAreaRequests)) {
            return null;
        }
        List<TaskAreaRelationDO> taskAreaRelations = Lists.newArrayList();
        addTaskAreaRequests.forEach(province -> {

            province.getChildren().forEach(city -> {
                TaskAreaRelationDO relation = new TaskAreaRelationDO();
                relation.setProvinceCode(province.getCode());
                relation.setCityCode(city);
                taskAreaRelations.add(relation);
            });
        });
        return taskAreaRelations;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTask(AddTaskRequest addTaskRequest) {
        addTaskRequest.setEndTime(DateUtil.endOfDay(addTaskRequest.getEndTime()));
        log.info("创建任务addTaskRequest={}", JSON.toJSONString(addTaskRequest));
        MarketTaskDO marketTaskDO = new MarketTaskDO();
        PojoUtils.map(addTaskRequest, marketTaskDO);
        //任务区域
        List<AddTaskAreaRequest> addTaskAreaRequests = addTaskRequest.getAddTaskAreaList();
        List<TaskAreaRelationDO> taskAreaRelations = null;
        //平台任务配置区域
        if (addTaskRequest.getTaskType().equals(TaskTypeEnum.PLATFORM.getCode()) && !addTaskRequest.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())) {
            if (CollectionUtil.isNotEmpty(addTaskAreaRequests)) {
                taskAreaRelations = this.getRealation(addTaskAreaRequests);
                long provinceSize = taskAreaRelations.stream().map(TaskAreaRelationDO::getProvinceCode).distinct().count();
                long citySize = taskAreaRelations.stream().map(TaskAreaRelationDO::getCityCode).distinct().count();
                StringBuilder taskArea = new StringBuilder().append(provinceSize).append("个省,").append(citySize).append("个市");
                // 任务投放区域省市区个数统计 逗号分隔
                // 基本信息
                marketTaskDO.setTaskArea(taskArea.toString());
            } else {
                throw new BusinessException(AssistantErrorCode.TASK_AREA_NOT_SET);
            }
        }
        //不限制销售区域
        if(addTaskRequest.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())){
            marketTaskDO.setFullCover(1);
        }
        if (addTaskRequest.getFinishType().equals(FinishTypeEnum.NEW_ENT.getCode()) || addTaskRequest.getFinishType().equals(FinishTypeEnum.NEW_USER.getCode())  || addTaskRequest.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())) {
            LambdaQueryWrapper<MarketTaskDO> inviteWrapper = Wrappers.lambdaQuery();
            inviteWrapper.eq(MarketTaskDO::getFinishType, addTaskRequest.getFinishType()).in(MarketTaskDO::getTaskStatus, TaskStatusEnum.IN_PROGRESS.getStatus(), TaskStatusEnum.UNSTART.getStatus());
            List<MarketTaskDO> inviteTaskList = this.list(inviteWrapper);
            if (CollUtil.isNotEmpty(inviteTaskList)) {
                inviteTaskList.forEach(t->{
                    if(addTaskRequest.getStartTime().compareTo(t.getStartTime())>=0 && addTaskRequest.getStartTime().compareTo(t.getEndTime())<=0){
                        log.info("已存在同类型的任务inviteTask={}", t.toString());
                        throw new BusinessException(AssistantErrorCode.SAME_TYPE_EXIST);
                    }
                    if(addTaskRequest.getEndTime().compareTo(t.getStartTime())>=0 && addTaskRequest.getEndTime().compareTo(t.getEndTime())<=0){
                        log.info("已存在同类型的任务inviteTask={}", t.toString());
                        throw new BusinessException(AssistantErrorCode.SAME_TYPE_EXIST);
                    }
                });
            }
        }
        // 一个月内只能有一个上传资料的任务
        if(addTaskRequest.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())){
            LambdaQueryWrapper<MarketTaskDO> billWrapper = Wrappers.lambdaQuery();
            DateTime beginOfMonth = DateUtil.beginOfMonth(addTaskRequest.getStartTime());
            DateTime endOfMonth = DateUtil.endOfMonth(addTaskRequest.getStartTime());
            billWrapper.between(MarketTaskDO::getStartTime,beginOfMonth,endOfMonth).eq(MarketTaskDO::getFinishType,FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode());;
            MarketTaskDO one = this.getOne(billWrapper);
            if(Objects.nonNull(one)){
                throw new BusinessException(AssistantErrorCode.SAME_TYPE_EXIST);
            }
        }
        if (addTaskRequest.getNeverExpires() == 1) {
            marketTaskDO.setEndTime(DateUtil.parseDateTime(TaskConstant.MAX_DATE));
        }
        if (!CollectionUtil.isEmpty(addTaskRequest.getAddTaskGoodsRelationList()) && addTaskRequest.getAddTaskGoodsRelationList().size() > 1) {
            marketTaskDO.setSaleType(SaleTypeEnum.MORE.getCode());
        } else if (!CollectionUtil.isEmpty(addTaskRequest.getAddTaskGoodsRelationList()) && addTaskRequest.getAddTaskGoodsRelationList().size() == 1) {
            marketTaskDO.setSaleType(SaleTypeEnum.SINGLE.getCode());
        } else {
            marketTaskDO.setSaleType(SaleTypeEnum.MORE.getCode());
        }
        List<AddTaskRuleRequest> ruleDTOList = addTaskRequest.getAddTaskRuleList();
        AddTaskRuleRequest finishTypeRule = ruleDTOList.stream().filter(r -> r.getRuleKey().equals(RuleKeyEnum.FINISH_TYPE.toString())).findAny().orElse(null);
        marketTaskDO.setFinishType(Integer.parseInt(finishTypeRule.getRuleValue()));
        Date now = new Date();
        //开始时间在当天直接进行中
        if(addTaskRequest.getStartTime().compareTo(DateUtil.beginOfDay(now))>=0 && addTaskRequest.getStartTime().compareTo(DateUtil.endOfDay(now))<0){
            marketTaskDO.setTaskStatus(TaskStatusEnum.IN_PROGRESS.getStatus());
        }
        this.save(marketTaskDO);
        if(CollUtil.isNotEmpty(addTaskRequest.getAddTaskDistributorList())){
            //配送商
            List<TaskDistributorDO> taskDistributorDOS = PojoUtils.map(addTaskRequest.getAddTaskDistributorList(), TaskDistributorDO.class);
            taskDistributorDOS.forEach(taskDistributorDO -> {
                taskDistributorDO.setCreateUser(addTaskRequest.getOpUserId()).setTaskId(marketTaskDO.getId());
            });
            taskDistributorService.saveBatch(taskDistributorDOS);
        }

        Long taskId = marketTaskDO.getId();
        if (null != taskAreaRelations) {
            TaskAreaJsonDO taskAreaJson = new TaskAreaJsonDO();
            taskAreaJson.setTaskId(marketTaskDO.getId());
            taskAreaJson.setAreaJson(JSON.toJSONString(addTaskAreaRequests));
            taskAreaJsonService.save(taskAreaJson);
            taskAreaRelations.forEach(relation -> {
                relation.setTaskId(taskId);
            });
            taskAreaRelationService.saveBatch(taskAreaRelations);
        }
        //部门
        if(CollUtil.isNotEmpty(addTaskRequest.getDeptIdList())){
            List<TaskDeptRelationDO> taskDeptRelationDOS = Lists.newArrayList();
            addTaskRequest.getDeptIdList().forEach(deptId -> {
                TaskDeptRelationDO taskDeptRelationDO = new TaskDeptRelationDO();
                taskDeptRelationDO.setDeptId(deptId).setTaskId(taskId);
                taskDeptRelationDOS.add(taskDeptRelationDO);
            });
            taskDeptRelationService.saveBatch(taskDeptRelationDOS);
        }

        if (!CollectionUtil.isEmpty(addTaskRequest.getAddTaskGoodsRelationList())) {
            List<Long> goodsIds = addTaskRequest.getAddTaskGoodsRelationList().stream().map(AddTaskGoodsRelationRequest::getGoodsId).collect(Collectors.toList());
            List<GoodsDTO> goodsInfoDTOS = goodsApi.batchQueryInfo(goodsIds);

            addTaskRequest.getAddTaskGoodsRelationList().forEach(taskGoodsRelationRequest -> {
                TaskGoodsRelationDO taskGoodsRelationDO = new TaskGoodsRelationDO();
                taskGoodsRelationDO.setTaskId(taskId);
                PojoUtils.map(taskGoodsRelationRequest, taskGoodsRelationDO);
                GoodsDTO goodsInfoDTO = goodsInfoDTOS.stream().filter(g -> g.getId().equals(taskGoodsRelationRequest.getGoodsId())).findAny().orElse(null);
                if (!Objects.isNull(goodsInfoDTO)) {
                    taskGoodsRelationDO.setSellSpecificationsId(goodsInfoDTO.getSellSpecificationsId());
                }
                // 任务商品
                taskGoodsRelationService.save(taskGoodsRelationDO);
            });
        }
        //会员
        if (Objects.nonNull(addTaskRequest.getAddTaskMember())) {
            AddTaskMemberRequest addTaskMemberRequest = addTaskRequest.getAddTaskMember();
            TaskMemberBuyDO taskMemberBuyDO = new TaskMemberBuyDO();
            taskMemberBuyDO.setTaskId(taskId);
            taskMemberBuyDO.setMemberId(addTaskMemberRequest.getMemberId()).setMemberStageId(addTaskMemberRequest.getMemberStageId()).setPlaybill(addTaskMemberRequest.getPlaybill());
            taskMemberBuyService.save(taskMemberBuyDO);
        }
        //活动
        if (Objects.nonNull(addTaskRequest.getAddTaskMemberPromotion())) {
            AddTaskMemberPromotionRequest addTaskMemberPromotionRequest = addTaskRequest.getAddTaskMemberPromotion();
            TaskMemberPromotionDO taskMemberPromotionDO = new TaskMemberPromotionDO();
            PojoUtils.map(addTaskMemberPromotionRequest, taskMemberPromotionDO);
            taskMemberPromotionDO.setTaskId(taskId);
            taskMemberPromotionService.save(taskMemberPromotionDO);
        }
        AtomicInteger flag = new AtomicInteger();

        Map<String, Integer> map = Maps.newHashMap();
        int size = ruleDTOList.stream().map(AddTaskRuleRequest::getRuleKey).distinct().collect(Collectors.toList()).size();
        if (size < ruleDTOList.size()) {
            log.info("规则重复 rule={}", JSON.toJSONString(ruleDTOList));
            throw new BusinessException(AssistantErrorCode.RULE_REPEAT);
        }
        boolean commissionFlag = true;
        boolean commissionrule = ruleDTOList.stream().filter(r -> r.getRuleKey().contains(RuleKeyEnum.COMMISSION.toString())).findAny().isPresent();
        for (AddTaskRuleRequest taskRuleDTO : ruleDTOList) {
            //非统一执行佣金处理
            if (!CollectionUtil.isEmpty(addTaskRequest.getAddTaskGoodsRelationList())) {
                //当只有一个商品时
                if (addTaskRequest.getAddTaskGoodsRelationList().size() == 1 && commissionFlag && !commissionrule) {
                    commissionFlag = false;
                    TaskRuleDO t = new TaskRuleDO();
                    t.setTaskId(taskId);
                    t.setRuleValue(addTaskRequest.getAddTaskGoodsRelationList().get(0).getCommission().toString());
                    t.setRuleKey(RuleKeyEnum.COMMISSION.toString());
                    t.setRuleType(RuleTypeEnum.COMMISSION.getCode());
                    taskRuleService.save(t);
                    TaskRuleDO TaskRuleDO = new TaskRuleDO();
                    TaskRuleDO.setTaskId(taskId);
                    BeanUtils.copyProperties(taskRuleDTO, TaskRuleDO);
                    // 任务规则
                    taskRuleService.save(TaskRuleDO);
                } else {
                    this.commissionHandle(flag, map, taskRuleDTO, taskId, addTaskRequest);
                }
            } else {
                TaskRuleDO TaskRuleDO = new TaskRuleDO();
                TaskRuleDO.setTaskId(taskId);
                BeanUtils.copyProperties(taskRuleDTO, TaskRuleDO);
                // 任务规则
                taskRuleService.save(TaskRuleDO);
            }
        }

    }

    private void commissionHandle(AtomicInteger flag, Map<String, Integer> map, AddTaskRuleRequest taskRuleDTO, Long taskId, AddTaskRequest addTaskRequest) {
        // 单独执行 计算佣金比例范围
        if (taskRuleDTO.getRuleKey().equals(RuleKeyEnum.EXECUTE_TYPE.toString()) && taskRuleDTO.getRuleValue().equals(ExecuteTypeEnum.SINGLE.getCode().toString())) {
            flag.getAndIncrement();
        }
        if (taskRuleDTO.getRuleKey().equals(RuleKeyEnum.FINISH_TYPE.toString()) && taskRuleDTO.getRuleValue().equals(FinishTypeEnum.MONEY.getCode().toString())) {
            flag.getAndIncrement();
            map.put(TaskConstant.FINISH_TYPE_CAL, FinishTypeEnum.MONEY.getCode());
        }
        if (taskRuleDTO.getRuleKey().equals(RuleKeyEnum.FINISH_TYPE.toString()) && taskRuleDTO.getRuleValue().equals(FinishTypeEnum.AMOUNT.getCode().toString())) {
            flag.getAndIncrement();
            map.put(TaskConstant.FINISH_TYPE_CAL, FinishTypeEnum.AMOUNT.getCode());
        }
        if (taskRuleDTO.getRuleKey().equals(RuleKeyEnum.FINISH_TYPE.toString()) && taskRuleDTO.getRuleValue().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode().toString())) {
            flag.getAndIncrement();
            map.put(TaskConstant.FINISH_TYPE_CAL, FinishTypeEnum.AMOUNT.getCode());
        }
        if (flag.get() == TaskConstant.COMMISSION_FLAG && addTaskRequest.getAddTaskGoodsRelationList().size() > 1) {
            flag.getAndIncrement();
            TaskRuleDO t = new TaskRuleDO();
            if (map.get(TaskConstant.FINISH_TYPE_CAL).equals(FinishTypeEnum.MONEY.getCode())) {
                List<BigDecimal> rateStr = addTaskRequest.getAddTaskGoodsRelationList().stream().map(AddTaskGoodsRelationRequest::getCommission).collect(Collectors.toList());
                List<BigDecimal> rates = rateStr;
                BigDecimal min = rates.stream().min(BigDecimal::compareTo).orElse(null);
                BigDecimal max = rates.stream().max(BigDecimal::compareTo).orElse(null);
                t.setRuleValue(min.toString() + "," + max.toString());
            }
            if (map.get(TaskConstant.FINISH_TYPE_CAL).equals(FinishTypeEnum.AMOUNT.getCode())) {
                List<BigDecimal> rates = addTaskRequest.getAddTaskGoodsRelationList().stream().map(AddTaskGoodsRelationRequest::getCommission).collect(Collectors.toList());
                BigDecimal min = rates.stream().min(BigDecimal::compareTo).orElse(null);
                BigDecimal max = rates.stream().max(BigDecimal::compareTo).orElse(null);
                if(min.compareTo(max)==0){
                    t.setRuleValue(min.toString());
                }else{
                    t.setRuleValue(min.toString() + "," + max.toString());
                }
            }

            t.setTaskId(taskId);
            t.setRuleKey(RuleKeyEnum.COMMISSION.toString());
            t.setRuleType(RuleTypeEnum.COMMISSION.getCode());
            taskRuleService.save(t);
        }
        TaskRuleDO TaskRuleDO = new TaskRuleDO();
        TaskRuleDO.setTaskId(taskId);
        BeanUtils.copyProperties(taskRuleDTO, TaskRuleDO);
        // 任务规则
        taskRuleService.save(TaskRuleDO);
    }

    @Override
    public TaskCountDTO getTaskCount() {
        List<MarketTaskDO> list = this.list();
        long platformCount = list.stream().filter(MarketTaskDO -> Objects.equals(MarketTaskDO.getTaskType(), TaskTypeEnum.PLATFORM.getCode())).count();

        long enterpriseCount = list.stream().filter(MarketTaskDO -> Objects.equals(MarketTaskDO.getTaskType(), TaskTypeEnum.ENTERPRISE.getCode())).count();
        TaskCountDTO dto = new TaskCountDTO();
        dto.setCount(Long.valueOf(list.size()));
        dto.setEnterpriseCount(enterpriseCount);
        dto.setPlatformCount(platformCount);
        return dto;
    }

    @Override
    public Integer getTaskStatus(Long taskId) {
        MarketTaskDO taskDO = this.getById(taskId);
        return taskDO.getTaskStatus();
    }

    @CacheEvict(value = TaskConstant.TASK_RULE_KEY_PRE, beforeInvocation = true, keyGenerator = "simpleKeyGenerator")
    public void clearRuleCache(Long taskId, Integer ruleType, String ruleKey) {
        log.info("清除任务规则缓存taskId={},ruleType={},ruleKey={}", taskId, ruleType, ruleKey);
    }

    @Override
    public void updateTask(UpdateTaskRequest updateTaskRequest) {
        log.info("修改任务入参 updateTaskRequest={}", JSON.toJSONString(updateTaskRequest));

        Long id = updateTaskRequest.getTaskId();
        MarketTaskDO task = this.getById(id);
        if (Objects.isNull(task)) {
            log.info("任务不存在 id={}", id);
            throw new BusinessException(AssistantErrorCode.TASK_NOT_EXIST);
        }
        MarketTaskServiceImpl marketTaskService = SpringUtil.getBean(MarketTaskServiceImpl.class);
        // 任务状态校验 开始后只允许改大完成条件其他字段不可修改
        MarketTaskDO utask = new MarketTaskDO();
        utask.setId(id);
        PojoUtils.map(updateTaskRequest, utask);

        //任务区域
        List<TaskAreaRelationDO> taskAreaRelations = this.getRealation(updateTaskRequest.getUpdateTaskAreaList());
        if (!CollectionUtil.isEmpty(updateTaskRequest.getUpdateTaskAreaList())) {
            long provinceSize = taskAreaRelations.stream().map(TaskAreaRelationDO::getProvinceCode).distinct().count();
            long citySize = taskAreaRelations.stream().map(TaskAreaRelationDO::getCityCode).distinct().count();
            StringBuilder taskArea = new StringBuilder().append(provinceSize).append("个省,").append(citySize).append("个市");
            // 任务投放区域省市区个数统计 逗号分隔
            utask.setTaskArea(taskArea.toString());
        }
        // 修改规则
        List<UpdateTaskRuleRequest> ruleUpdateDTOS = updateTaskRequest.getUpdateTaskRuleList();
        if (!CollectionUtil.isEmpty(ruleUpdateDTOS)) {
            ruleUpdateDTOS.forEach(ruleUpdate -> {
                TaskRuleDO TaskRuleDO = new TaskRuleDO();
                TaskRuleDO.setId(ruleUpdate.getTaskRuleId());
                TaskRuleDO.setRuleValue(ruleUpdate.getRuleValue());
                taskRuleService.updateById(TaskRuleDO);
                TaskRuleDO = taskRuleService.getById(ruleUpdate.getTaskRuleId());
                marketTaskService.clearRuleCache(TaskRuleDO.getTaskId(), TaskRuleDO.getRuleType(), TaskRuleDO.getRuleKey());
            });
        }
        if (CollUtil.isNotEmpty(updateTaskRequest.getAddTaskRuleList())) {
            updateTaskRequest.getAddTaskRuleList().forEach(addTaskRuleRequest -> {
                TaskRuleDO TaskRuleDO = new TaskRuleDO();
                TaskRuleDO.setTaskId(updateTaskRequest.getTaskId());
                BeanUtils.copyProperties(addTaskRuleRequest, TaskRuleDO);
                if (addTaskRuleRequest.getRuleKey().equals(RuleKeyEnum.COMMISSION.toString())) {
                    //删除原有的
                    TaskRuleDO t = this.getRule(id, RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.COMMISSION.toString());
                    if (!Objects.isNull(t)) {
                        t.setRuleValue(addTaskRuleRequest.getRuleValue());
                        taskRuleService.updateById(t);
                        marketTaskService.clearRuleCache(t.getTaskId(), t.getRuleType(), t.getRuleKey());
                        return;
                    }
                }
                // 任务规则
                taskRuleService.save(TaskRuleDO);
            });

        }
        AtomicInteger addGoods = new AtomicInteger();
        if (true) {
            // 修改商品 或者追加关联商品
            List<UpdateTaskGoodsRelationRequest> goodsUpdateDTOS = updateTaskRequest.getUpdateTaskGoodsRelationList();
            if (!CollectionUtil.isEmpty(goodsUpdateDTOS)) {
                goodsUpdateDTOS.forEach(goodsUpdate -> {
                    TaskGoodsRelationDO goodsRelation = new TaskGoodsRelationDO();
                    goodsRelation.setCommission(goodsUpdate.getCommission());
                    goodsRelation.setSalePrice(goodsUpdate.getSalePrice());
                    goodsRelation.setCommissionRate(goodsUpdate.getCommissionRate());
                    // 修改
                    if (!Objects.isNull(goodsUpdate.getTaskGoodsId())) {
                        goodsRelation.setId(goodsUpdate.getTaskGoodsId());
                        taskGoodsRelationService.updateById(goodsRelation);
                    } else {
                        // 追加
                        goodsRelation.setTaskId(id);
                        goodsRelation.setGoodsId(goodsUpdate.getGoodsId());
                        goodsRelation.setGoodsName(goodsUpdate.getGoodsName());
                        goodsRelation.setCommissionRate(goodsUpdate.getCommissionRate());
                        goodsRelation.setSellPrice(goodsUpdate.getSellPrice()).setOutPrice(goodsUpdate.getOutPrice());
                        addGoods.getAndIncrement();
                        taskGoodsRelationService.save(goodsRelation);
                        if (task.getSaleType().equals(SaleTypeEnum.SINGLE.getCode())) {
                            utask.setSaleType(SaleTypeEnum.MORE.getCode());
                        }
                    }
                });

            }

            // 删除地区或者追加地区
            if (null != taskAreaRelations) {
                taskAreaRelations.forEach(relation -> {
                    relation.setTaskId(id);
                });
                LambdaQueryWrapper<TaskAreaRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
                lambdaQueryWrapper.eq(TaskAreaRelationDO::getTaskId, id);
                taskAreaRelationService.remove(lambdaQueryWrapper);
                taskAreaRelationService.saveBatch(taskAreaRelations);
                TaskAreaJsonDO areaJson = new TaskAreaJsonDO();
                areaJson.setAreaJson(JSON.toJSONString(updateTaskRequest.getUpdateTaskAreaList()));
                areaJson.setTaskId(id);
                taskAreaJsonService.saveOrUpdate(areaJson);
            }
            if (null != updateTaskRequest.getFullCover() && updateTaskRequest.getFullCover() == 1) {
                LambdaQueryWrapper<TaskAreaRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
                lambdaQueryWrapper.eq(TaskAreaRelationDO::getTaskId, id);
                taskAreaRelationService.remove(lambdaQueryWrapper);
            }
            //删除部门或者追加部门
            if(CollUtil.isNotEmpty(updateTaskRequest.getDeptIdList())){
                LambdaQueryWrapper<TaskDeptRelationDO> deptLambdaQueryWrapper = Wrappers.lambdaQuery();
                deptLambdaQueryWrapper.eq(TaskDeptRelationDO::getTaskId, id);
                taskDeptRelationService.remove(deptLambdaQueryWrapper);
                List<TaskDeptRelationDO> taskDeptRelationDOS = Lists.newArrayList();
                updateTaskRequest.getDeptIdList().forEach(deptId -> {
                    TaskDeptRelationDO taskDeptRelationDO = new TaskDeptRelationDO();
                    taskDeptRelationDO.setDeptId(deptId).setTaskId(updateTaskRequest.getTaskId());
                    taskDeptRelationDOS.add(taskDeptRelationDO);
                });
                taskDeptRelationService.saveBatch(taskDeptRelationDOS);
            }
        }
        // 配送商
        List<TaskDistributorDO> taskDistributorDOS = PojoUtils.map(updateTaskRequest.getUpdateTaskDistributorList(), TaskDistributorDO.class);
        taskDistributorDOS.forEach(taskDistributorDO -> {
            taskDistributorDO.setCreateUser(updateTaskRequest.getOpUserId()).setTaskId(id);
        });
        taskDistributorService.saveBatch(taskDistributorDOS);
        if(CollUtil.isNotEmpty(taskDistributorDOS) || addGoods.intValue()>0){
            taskModifyNotifyService.addGoodsOrDistributorNotify(task);
        }
        //会员
        if (Objects.nonNull(updateTaskRequest.getUpdateTaskMember())) {
            TaskMemberBuyDO taskMemberBuyDO = new TaskMemberBuyDO();
            PojoUtils.map(updateTaskRequest.getUpdateTaskMember(), taskMemberBuyDO);
            taskMemberBuyService.updateById(taskMemberBuyDO);
        }
        Date now = new Date();
        //开始时间在当天直接进行中
        if(updateTaskRequest.getStartTime().compareTo(DateUtil.beginOfDay(now))>=0 && updateTaskRequest.getStartTime().compareTo(DateUtil.endOfDay(now))<0){
            utask.setTaskStatus(TaskStatusEnum.IN_PROGRESS.getStatus());
        }
        // 一个月内只能有一个上传资料的任务
        if(task.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())){
            LambdaQueryWrapper<MarketTaskDO> billWrapper = Wrappers.lambdaQuery();
            DateTime beginOfMonth = DateUtil.beginOfMonth(updateTaskRequest.getStartTime());
            DateTime endOfMonth = DateUtil.endOfMonth(updateTaskRequest.getStartTime());
            billWrapper.between(MarketTaskDO::getStartTime,beginOfMonth,endOfMonth).eq(MarketTaskDO::getFinishType,FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode());
            billWrapper.ne(MarketTaskDO::getId,updateTaskRequest.getTaskId());
            MarketTaskDO one = this.getOne(billWrapper);
            if(Objects.nonNull(one)){
                throw new BusinessException(AssistantErrorCode.SAME_TYPE_EXIST);
            }
        }
        if (task.getTaskStatus().equals(TaskStatusEnum.UNSTART.getStatus()) || task.getTaskStatus().equals(TaskStatusEnum.IN_PROGRESS.getStatus())) {
            utask.setEndTime(DateUtil.endOfDay(updateTaskRequest.getEndTime()));
            utask.setUpdateUser(updateTaskRequest.getOpUserId());
            marketTaskService.updateById(utask);
        }
    }

    /**
     * 查询任务规则
     *
     * @param taskId
     * @param ruleType
     * @param ruleKey
     * @return
     */
    private TaskRuleDO getRule(Long taskId, Integer ruleType, String ruleKey) {
        LambdaQueryWrapper<TaskRuleDO> lambdaQueryWrapper = new QueryWrapper<TaskRuleDO>().lambda();
        lambdaQueryWrapper.eq(TaskRuleDO::getTaskId, taskId).eq(TaskRuleDO::getRuleType, ruleType).eq(TaskRuleDO::getRuleKey, ruleKey).last("limit 1");
        return taskRuleService.getOne(lambdaQueryWrapper);
    }

    @Override
    public TaskDetailDTO getDetailById(Long id) {
        MarketTaskDO task = this.getById(id);
        if (Objects.isNull(task)) {
            log.info("任务不存在 id={}", id);
            throw new BusinessException(AssistantErrorCode.TASK_NOT_EXIST);
        }
        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
        PojoUtils.map(task, taskDetailDTO);
        // 查询任务规则
        List<TaskRuleDO> taskRuleList = taskRuleService.list(new QueryWrapper<TaskRuleDO>().lambda().eq(TaskRuleDO::getTaskId, task.getId()));
        // 参与条件
        List<TaskRuleDTO> takeRuleVOList = this.getByRuleType(taskRuleList, task.getId(), RuleTypeEnum.TAKE.getCode());
        taskDetailDTO.setTakeRuleVOList(takeRuleVOList);
        // 任务完成条件
        List<TaskRuleDTO> finishRuleVOList = this.getByRuleType(taskRuleList, task.getId(), RuleTypeEnum.FINISH_TYPE.getCode());
        taskDetailDTO.setFinishRuleVOList(finishRuleVOList);
        // 佣金规则
        List<TaskRuleDTO> commissionRuleVOList = this.getByRuleType(taskRuleList, task.getId(), RuleTypeEnum.COMMISSION.getCode());
        taskDetailDTO.setCommissionRuleVOList(commissionRuleVOList);
        String finishType = SpringUtil.getBean(MarketTaskServiceImpl.class).getRuleValue(id, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.FINISH_TYPE.toString());
        //会员购买
        if (FinishTypeEnum.MEMBER_BUY.getCode().toString().equals(finishType)) {
            taskDetailDTO.setTaskMember(taskMemberBuyService.getMemberById(id));
        }
        // 会员满赠
        if (FinishTypeEnum.MEMBER_PROMOTION.getCode().toString().equals(finishType)) {
            taskDetailDTO.setTaskMemberPromotion(taskMemberPromotionService.getMemberPromotion(id));
        }
        //部门
        if(finishType.equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode().toString())){
            LambdaQueryWrapper<TaskDeptRelationDO> deptLambdaQueryWrapper = new QueryWrapper<TaskDeptRelationDO>().lambda();
            deptLambdaQueryWrapper.eq(TaskDeptRelationDO::getTaskId, task.getId());
            List<TaskDeptRelationDO> taskDeptRelationDOS = taskDeptRelationService.list(deptLambdaQueryWrapper);
            List<Long> deptIdList = taskDeptRelationDOS.stream().map(TaskDeptRelationDO::getDeptId).collect(Collectors.toList());
            taskDetailDTO.setDeptIdList(deptIdList);
        }

        // 查询任务关联商品
        LambdaQueryWrapper<TaskGoodsRelationDO> lambdaQueryWrapper = new QueryWrapper<TaskGoodsRelationDO>().lambda();
        lambdaQueryWrapper.eq(TaskGoodsRelationDO::getTaskId, task.getId());
        List<TaskGoodsRelationDO> goodsRelationList = taskGoodsRelationService.list(lambdaQueryWrapper);
        if (CollectionUtil.isEmpty(goodsRelationList)) {
            return taskDetailDTO;
        }

        List<Long> goodsIds = goodsRelationList.stream().map(TaskGoodsRelationDO::getGoodsId).collect(Collectors.toList());
        // 商品基价取的pop价格

        List<GoodsDTO> goodsInfoDTOS = goodsApi.batchQueryInfo(goodsIds);

        List<TaskGoodsDTO> taskGoodsDTOList = Lists.newArrayListWithExpectedSize(goodsRelationList.size());
       // List<GoodsYilingPriceDTO> priceParamNameList = goodsYilingPriceApi.getPriceParamNameList(goodsIds, new Date());
        goodsRelationList.forEach(taskGoods -> {
            TaskGoodsDTO taskGoodsDTO = new TaskGoodsDTO();
            PojoUtils.map(taskGoods, taskGoodsDTO);

            GoodsDTO goodsInfoDTO = goodsInfoDTOS.stream().filter(g -> g.getId().equals(taskGoods.getGoodsId())).findAny().orElse(null);
            if (!Objects.isNull(goodsInfoDTO)) {
                taskGoodsDTO.setCanSplit(goodsInfoDTO.getCanSplit());
                taskGoodsDTO.setGoodsPic(fileService.getUrl(goodsInfoDTO.getPic(), FileTypeEnum.GOODS_PICTURE));
                taskGoodsDTO.setManufacturer(goodsInfoDTO.getManufacturer());
                //taskGoodsDTO.setSupplier(goodsInfoDTO.getEname());
                taskGoodsDTO.setSpecifications(goodsInfoDTO.getSellSpecifications());
                taskGoodsDTO.setSellSpecifications(goodsInfoDTO.getSellSpecifications());
                taskGoodsDTO.setMiddlePackage(goodsInfoDTO.getMiddlePackage());
                taskGoodsDTO.setGoodsName(goodsInfoDTO.getName());
                taskGoodsDTO.setPrice(goodsInfoDTO.getPrice());
               /* //出货价格
                GoodsYilingPriceDTO goodsYilingPriceDTO = priceParamNameList.stream().filter(p -> p.getGoodsId().equals(taskGoods.getGoodsId()) && p.getParamId().equals(1L)).findAny().orElse(null);
                if(Objects.nonNull(goodsYilingPriceDTO)){
                    taskGoodsDTO.setOutPrice(NumberUtil.round(goodsYilingPriceDTO.getPrice(),2, RoundingMode.HALF_UP));
                }else{
                    taskGoodsDTO.setOutPrice(BigDecimal.ZERO);
                }
                GoodsYilingPriceDTO goodsSellPriceDTO = priceParamNameList.stream().filter(p -> p.getGoodsId().equals(taskGoods.getGoodsId()) && p.getParamId().equals(2L)).findAny().orElse(null);
                //商销售价格
                if(Objects.nonNull(goodsSellPriceDTO)){
                    taskGoodsDTO.setSellPrice(NumberUtil.round(goodsSellPriceDTO.getPrice(),2, RoundingMode.HALF_UP));
                }else{
                    taskGoodsDTO.setSellPrice(BigDecimal.ZERO);
                }*/
            }
            taskGoodsDTO.setTaskGoodsId(taskGoods.getId());
            taskGoodsDTOList.add(taskGoodsDTO);
        });
        if (!CollectionUtil.isEmpty(goodsRelationList) && CollectionUtil.isEmpty(taskGoodsDTOList) && !task.getTaskStatus().equals(TaskStatusEnum.STOP.getStatus())) {
            // 任务商品全部下架，任务停用
            task.setTaskStatus(TaskStatusEnum.STOP.getStatus());
            this.updateById(task);
            log.info("任务商品全部下架task={}", task.getId());
        }
        taskDetailDTO.setTaskGoodsList(taskGoodsDTOList);
        return taskDetailDTO;
    }


    private List<TaskRuleDTO> getByRuleType(List<TaskRuleDO> taskRuleList, Long id, Integer ruleType) {
        List<TaskRuleDO> ruleList = taskRuleList.stream().filter(TaskRuleDO -> TaskRuleDO.getRuleType().equals(ruleType)).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(ruleList) && ruleType.equals(RuleTypeEnum.TAKE.getCode())) {
            log.info("未设置参与条件 id={}", id);
            throw new BusinessException(AssistantErrorCode.TAKE_RULE_NOT_SET);
        }
        if (CollectionUtil.isEmpty(ruleList) && ruleType.equals(RuleTypeEnum.FINISH_TYPE.getCode())) {
            log.info("未设置任务完成条件 id={}", id);
            throw new BusinessException(AssistantErrorCode.FINISH_RULE_NOT_SET);
        }
        /*
         * if(CollectionUtils.isEmpty(ruleList) &&
         * ruleType.equals(RuleTypeEnum.COMMISSION.getCode())){
         * log.info("未设置佣金规则 id={}",id); throw new
         * ServiceException(ResultCode.COMMISSION_RULE_NOT_SET); }
         */
        List<TaskRuleDTO> ruleDTOS = Lists.newArrayList();
        ruleList.forEach(TaskRuleDO -> {
            TaskRuleDTO taskRuleVO = new TaskRuleDTO();
            BeanUtils.copyProperties(TaskRuleDO, taskRuleVO);
            ruleDTOS.add(taskRuleVO);
        });
        return ruleDTOS;
    }

    @Override
    public Page<TaskDTO> queryTaskListPage(QueryTaskPageRequest queryTaskPageRequest) {
        Page<TaskDTO> vpage = new Page<>(queryTaskPageRequest.getCurrent(), queryTaskPageRequest.getSize());
        List<Long> taskIds = null;
        if (!Objects.isNull(queryTaskPageRequest.getRegionCode()) || !Objects.isNull(queryTaskPageRequest.getCityCode()) || !Objects.isNull(queryTaskPageRequest.getProvinceCode())) {
            LambdaQueryWrapper<TaskAreaRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
            if (!Objects.isNull(queryTaskPageRequest.getProvinceCode())) {
                lambdaQueryWrapper.eq(TaskAreaRelationDO::getProvinceCode, queryTaskPageRequest.getProvinceCode());
            }
            if (!Objects.isNull(queryTaskPageRequest.getCityCode())) {
                lambdaQueryWrapper.eq(TaskAreaRelationDO::getCityCode, queryTaskPageRequest.getCityCode());
            }
            if (!Objects.isNull(queryTaskPageRequest.getRegionCode())) {
                lambdaQueryWrapper.eq(TaskAreaRelationDO::getRegionCode, queryTaskPageRequest.getRegionCode());
            }

            taskIds = taskAreaRelationService.list(lambdaQueryWrapper).stream().map(TaskAreaRelationDO::getTaskId).distinct().collect(Collectors.toList());
            if (CollectionUtil.isEmpty(taskIds)) {
                return vpage;
            }
        }
        queryTaskPageRequest.setTaskIds(taskIds);
        if (Objects.nonNull(queryTaskPageRequest.getEndcTime())) {
            queryTaskPageRequest.setEndcTime(DateUtil.endOfDay(queryTaskPageRequest.getEndcTime()));
        }
        if (Objects.nonNull(queryTaskPageRequest.getEndTime())) {
            queryTaskPageRequest.setEndTime(DateUtil.endOfDay(queryTaskPageRequest.getEndTime()));
        }
        if (Objects.nonNull(queryTaskPageRequest.getEndeTime())) {
            queryTaskPageRequest.setEndeTime(DateUtil.endOfDay(queryTaskPageRequest.getEndeTime()));
        }
        IPage<MarketTaskDO> result = this.baseMapper.queryTaskListPage(queryTaskPageRequest.getPage(), queryTaskPageRequest);
        List<MarketTaskDO> taskBaseList = result.getRecords();
        if (!CollectionUtil.isEmpty(taskBaseList)) {
            LinkedList<TaskDTO> taskVOList = Lists.newLinkedList();
            taskBaseList.forEach(MarketTaskDO -> {
                TaskDTO taskDTO = new TaskDTO();
                BeanUtils.copyProperties(MarketTaskDO, taskDTO);
                if (MarketTaskDO.getTaskStatus().equals(TaskStatusEnum.IN_PROGRESS.getStatus())) {
                    if (MarketTaskDO.getEndTime().compareTo(new Date()) <= 0) {
                        taskDTO.setTaskStatus(TaskStatusEnum.END.getStatus());
                    }
                }
                taskVOList.add(taskDTO);
            });
            vpage.setTotal(result.getTotal());
            vpage.setRecords(taskVOList);

        }
        return vpage;
    }


    @Override
    public TaskTraceDTO getTaskTrace(Long taskId) {
        TaskTraceDTO taskTraceDTO = new TaskTraceDTO();
        TaskDetailDTO taskDetailDTO = this.getDetailById(taskId);
       /* if(CollUtil.isNotEmpty(taskDetailDTO.getTaskGoodsList())){
            taskTraceDTO.setCategories(taskDetailDTO.getTaskGoodsList().size());
        }*/
        //统计任务承接人数
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getTaskId, taskId).select(UserTaskDO::getId, UserTaskDO::getTaskStatus, UserTaskDO::getFinishValue);
        List<UserTaskDO> userTaskDOList = userTaskService.list(wrapper);
        if (CollUtil.isEmpty(userTaskDOList)) {
            taskTraceDTO.setFinishCount(0).setTakeCount(0);
            return taskTraceDTO;
        }
        PojoUtils.map(taskDetailDTO, taskTraceDTO);
        String finishType = SpringUtil.getBean(MarketTaskServiceImpl.class).getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.FINISH_TYPE.toString());
        //已完成
        taskTraceDTO.setTakeCount(userTaskDOList.size());
        if (FinishTypeEnum.AMOUNT.getCode().toString().equals(finishType) || FinishTypeEnum.MONEY.getCode().toString().equals(finishType)) {
            int finished = userTaskDOList.stream().filter(userTaskDO -> userTaskDO.getTaskStatus().equals(UserTaskStatusEnum.FINISHED.getStatus())).collect(Collectors.toList()).size();
            taskTraceDTO.setFinishCount(finished);
        }
        //拉人总数
        if (FinishTypeEnum.NEW_USER.getCode().toString().equals(finishType)) {
            taskTraceDTO.setUserCount(userTaskDOList.stream().mapToInt(UserTaskDO::getFinishValue).sum());
        }
        //拉户总数
        if (FinishTypeEnum.NEW_ENT.getCode().toString().equals(finishType)) {
            taskTraceDTO.setEnterpriseCount(userTaskDOList.stream().mapToInt(UserTaskDO::getFinishValue).sum());
        }
        // 会员
        if (FinishTypeEnum.MEMBER_BUY.getCode().toString().equals(finishType)) {
            taskTraceDTO.setMemberBuyCount(userTaskDOList.stream().mapToInt(UserTaskDO::getFinishValue).sum());
        }
        return taskTraceDTO;
    }

    @Override
    @Cacheable(value = TaskConstant.TASK_RULE_KEY_PRE, keyGenerator = "simpleKeyGenerator")
    public String getRuleValue(Long taskId, Integer ruleType, String ruleKey) {
        LambdaQueryWrapper<TaskRuleDO> lambdaQueryWrapper = new QueryWrapper<TaskRuleDO>().lambda();
        lambdaQueryWrapper.eq(TaskRuleDO::getTaskId, taskId).eq(TaskRuleDO::getRuleType, ruleType).eq(TaskRuleDO::getRuleKey, ruleKey).select(TaskRuleDO::getRuleValue);
        lambdaQueryWrapper.last("limit 1");
        TaskRuleDO taskRule = taskRuleService.getOne(lambdaQueryWrapper);
        if (Objects.isNull(taskRule)) {
            return "";
        }
        return taskRule.getRuleValue();
    }

    @Override
    public void handleTaskOrder(AddTaskOrderRequest addTaskOrderRequest) {
        //查询进行中的交易量任务
        LambdaQueryWrapper<MarketTaskDO> taskWrapper = Wrappers.lambdaQuery();
        taskWrapper.in(MarketTaskDO::getFinishType,FinishTypeEnum.AMOUNT.getCode(),FinishTypeEnum.MONEY.getCode()).eq(MarketTaskDO::getTaskStatus,TaskStatusEnum.IN_PROGRESS.getStatus());
        List<MarketTaskDO> taskDOList = this.list(taskWrapper);
        if(CollUtil.isEmpty(taskDOList)){
            log.info("无进行中的交易量或者交易额任务");
            return;
        }
        List<Long> taskIds = taskDOList.stream().map(MarketTaskDO::getId).collect(Collectors.toList());
        //查询用户已承接的任务
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getUserId, addTaskOrderRequest.getUserId()).in(UserTaskDO::getTaskId,taskIds);
        List<UserTaskDO> userTaskDOList = userTaskService.list(wrapper);
        if (CollUtil.isEmpty(userTaskDOList)) {
            log.info("用户未承接任务 userID={}", addTaskOrderRequest.getUserId());
            return;
        }
        //过滤进行中的任务
        userTaskDOList = userTaskDOList.stream().filter(userTaskDO -> UserTaskStatusEnum.IN_PROGRESS.getStatus().equals(userTaskDO.getTaskStatus()) || UserTaskStatusEnum.FINISHED.getStatus().equals(userTaskDO.getTaskStatus())).collect(Collectors.toList());
        if (CollUtil.isEmpty(userTaskDOList)) {
            log.info("用户没有进行中的任务 userID={}", addTaskOrderRequest.getUserId());
            return;
        }
        //交易额交易量任务过滤出来
        userTaskDOList.forEach(userTaskDO -> {
            //任务状态校验
            MarketTaskDO taskDO = this.getById(userTaskDO.getTaskId());
            if (!taskDO.getTaskStatus().equals(TaskStatusEnum.IN_PROGRESS.getStatus())) {
                log.info("任务非进行中状态taskDO={}", taskDO.toString());
                return;
            }
            if (DateUtil.compare(addTaskOrderRequest.getOrderTime(), taskDO.getEndTime()) > 0) {
                log.info("任务已结束taskDO={}", taskDO.toString());
                return;
            }

            //下单时间校验必须大于等于任务开始时间
            if (addTaskOrderRequest.getOrderTime().compareTo(userTaskDO.getCreatedTime()) < 0) {
                log.info("下单时间早于任务开始时间不计算任务进度 orderNo={},userTaskDO={}", addTaskOrderRequest.getOrderNo(), userTaskDO.toString());
                return;
            }
            //支付方式校验
            String payMethodRule = this.getRuleValue(userTaskDO.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.PAYMENT_METHOD.toString());
            if(StrUtil.isNotEmpty(payMethodRule)){
                if(!payMethodRule.contains(addTaskOrderRequest.getPaymentMethod().toString())){
                    log.info("和任务限制的支付方式不匹配addTaskOrderRequest={}",addTaskOrderRequest.toString());
                    return;
                }
            }
            // 会员条件校验
            String isMember = this.getRuleValue(userTaskDO.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.IS_MEMBER.toString());
            if(StringUtils.isNotEmpty(isMember)){
                List<MemberEnterpriseBO> memberListByEid = memberApi.getMemberListByEid(addTaskOrderRequest.getTerminalId());
                if(isMember.equals(TaskConstant.ONE)){
                    if(memberListByEid.size()== 0){
                        log.info("会员条件和任务不匹配eid={}",addTaskOrderRequest.getTerminalId());
                        return;
                    }
               }else{
                    if(memberListByEid.size()> 0){
                        log.info("会员条件和任务不匹配eid={}",addTaskOrderRequest.getTerminalId());
                        return;
                    }
               }
            }
            String finishRule = this.getRuleValue(userTaskDO.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.FINISH_TYPE.toString());
            Integer finishValue = Integer.valueOf(finishRule);
            List<Long> taskGoodsIds = null;
            //交易额或者交易量
            if (finishValue.equals(FinishTypeEnum.AMOUNT.getCode()) || finishValue.equals(FinishTypeEnum.MONEY.getCode())) {
                //匹配供应商
                LambdaQueryWrapper<TaskDistributorDO> dwrapper = Wrappers.lambdaQuery();
                dwrapper.eq(TaskDistributorDO::getTaskId, taskDO.getId());
                List<TaskDistributorDO> distributorDOS = taskDistributorService.list(dwrapper);
                if (CollUtil.isNotEmpty(distributorDOS)) {
                    List<Long> collect = distributorDOS.stream().map(TaskDistributorDO::getDistributorEid).collect(Collectors.toList());
                    boolean present = collect.stream().filter(l -> l.equals(addTaskOrderRequest.getSellerEid())).findAny().isPresent();
                    if (!present) {
                        log.info("订单供应商不在任务设置范围内collect={}", collect.toString());
                        return;
                    }
                }
                LambdaQueryWrapper<TaskGoodsRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
                lambdaQueryWrapper.eq(TaskGoodsRelationDO::getTaskId, userTaskDO.getTaskId());
                //任务配置商品
                List<TaskGoodsRelationDO> relations = taskGoodsRelationService.list(lambdaQueryWrapper);
                taskGoodsIds = relations.stream().map(TaskGoodsRelationDO::getGoodsId).collect(Collectors.toList());
                taskOrderService.computeTaskOrder(taskGoodsIds, addTaskOrderRequest, taskDO, userTaskDO);
            }
        });
    }

    @Override
    public void newCustomerHandler(AddTaskOrderRequest addTaskOrderRequest) {

        UserCustomerDTO userCustomerDTO = userCustomerApi.getByUserAndCustomerEid(addTaskOrderRequest.getUserId(),addTaskOrderRequest.getTerminalId());
        // 判断下单终端是否有邀请人
        if (Objects.isNull(userCustomerDTO)) {
            log.info("此客户没有邀请人addTaskOrderRequest={}", addTaskOrderRequest.toString());
            return;
        }
        Long inviteUserId = userCustomerDTO.getUserId();

        List<Integer> statusList = Lists.newArrayList();
        statusList.add(UserTaskStatusEnum.IN_PROGRESS.getStatus());
        //statusList.add(UserTaskStatusEnum.FINISHED.getStatus());
        //判断邀请人是否承接拉新户任务（理论上来讲只存在一个拉新户的任务）
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserTaskDO::getUserId, inviteUserId).eq(UserTaskDO::getFinishType, FinishTypeEnum.NEW_ENT.getCode());
        wrapper.in(UserTaskDO::getTaskStatus, statusList);
        List<UserTaskDO> inviteUserTaskList = userTaskService.list(wrapper);
        if (CollUtil.isEmpty(inviteUserTaskList)) {
            log.info("邀请人未承接拉新户类型的任务inviteUserId={}", inviteUserId);
            return;
        }
        inviteUserTaskList.forEach(inviteUserTask -> {
            MarketTaskDO marketTaskDO = this.getById(inviteUserTask.getTaskId());
            //  判断客户的创建时间和审核通过时间是否大于等任务开始时间
/*            if (userCustomerDTO.getAuditTime().compareTo(inviteUserTask.getCreatedTime()) < 0) {
                log.info("拉新户任务时间不符合要求addTaskOrderRequest={},inviteUserTask={}", addTaskOrderRequest.toString(),inviteUserTask.toString());
                return;
            }*/
            if (addTaskOrderRequest.getOrderTime().compareTo(inviteUserTask.getCreatedTime()) < 0) {
                log.info("下单时间早于任务开始时间不计算任务进度 orderNo={},inviteUserTask={}", addTaskOrderRequest.getOrderNo(), inviteUserTask.toString());
                return;
            }
            String lockName = RedisKey.generate("newCustomer:commission", PlatformEnum.SALES_ASSIST.getCode().toString(), "add", inviteUserTask.getId().toString());
            String lockId = "";


            try {
                //用户任务进度计算
                lockId = redisDistributedLock.lock2(lockName, 100, 2, TimeUnit.SECONDS);
                LambdaQueryWrapper<SaTaskRegisterTerminalDO> terminalDOLambdaQueryWrapper = Wrappers.lambdaQuery();
                terminalDOLambdaQueryWrapper.eq(SaTaskRegisterTerminalDO::getTerminalId, addTaskOrderRequest.getTerminalId()).eq(SaTaskRegisterTerminalDO::getUserTaskId, inviteUserTask.getId()).last("limit 1");
                SaTaskRegisterTerminalDO terminalDO = saTaskRegisterTerminalService.getOne(terminalDOLambdaQueryWrapper);
                if (Objects.nonNull(terminalDO)) {
                    log.info("拉户记录已存在terminalDO={}", terminalDO.toString());
                    return;
                }
                //拉新户 并且有订单交易限
                String newCustomerRule = this.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_LIMIT.toString());
                //有限制
                if (!newCustomerRule.equals(TaskConstant.UNLIMIT)) {
                    //首单还是累计
                    String newCustomerCondition = this.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_CONDITION.toString());
                    String condition = this.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_AMOUNT.toString());


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
                    //累计
                    if (newCustomerCondition.equals(TaskConstant.ONE)) {
                        //查询收单的时间是否在任务期间
                        if (!addTaskOrderRequest.getIsFirstOrder()) {
                            QueryAssistantFirstOrderRequest request = new QueryAssistantFirstOrderRequest();
                            request.setBuyerEid(addTaskOrderRequest.getTerminalId());
                            QueryAssistantOrderFirstRequest req = new QueryAssistantOrderFirstRequest();
                            req.setBuyerEid(addTaskOrderRequest.getTerminalId());
                            OrderDTO firstOrder = orderApi.getAssistantReceiveFirstOrder(req);
                            if (firstOrder.getCreateTime().compareTo(inviteUserTask.getCreatedTime()) < 0) {
                                log.info("收单下单时间早于任务开始时间不计算任务进度 orderNo={},inviteUserTask={}", firstOrder.getOrderNo(), inviteUserTask.toString());
                                return;
                            }
                            //支付方式校验
                            String payMethodRule = this.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.PAYMENT_METHOD.toString());
                            if(StrUtil.isNotEmpty(payMethodRule)){
                                if(!payMethodRule.contains(firstOrder.getPaymentMethod().toString())){
                                    log.info("和任务限制的支付方式不匹配addTaskOrderRequest={}",addTaskOrderRequest.toString());
                                    return;
                                }
                            }
                        } else {
                            //查询是否在b2b下过包含以岭品的订单 已收货
                            QueryAssistantOrderFirstRequest req = new QueryAssistantOrderFirstRequest();
                            req.setBuyerEid(addTaskOrderRequest.getTerminalId());
                            Boolean receiveB2BOrder = orderApi.verificationReceiveB2BOrder(req);
                            if (receiveB2BOrder) {
                                log.info("此客户在b2b有已收货的包含以岭品的订单BuyerEid={}", addTaskOrderRequest.getTerminalId());
                                return;
                            }
                        }
                        //支付方式校验
                        String payMethodRule = this.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.PAYMENT_METHOD.toString());
                        if(StrUtil.isNotEmpty(payMethodRule)){
                            if(!payMethodRule.contains(addTaskOrderRequest.getPaymentMethod().toString())){
                                log.info("和任务限制的支付方式不匹配addTaskOrderRequest={}",addTaskOrderRequest.toString());
                                return;
                            }
                        }
                        TaskCustomerCountDO taskCustomerCountDO = new TaskCustomerCountDO();
                        taskCustomerCountDO.setTotalPurchaseAmount(addTaskOrderRequest.getTotalAmount());
                        taskCustomerCountDO.setCustomerEid(addTaskOrderRequest.getTerminalId()).setUserTaskId(inviteUserTask.getId());

                        finish = taskCustomerCountService.saveTaskCostumerCount(taskCustomerCountDO, inviteUserTask);

                    } else {//首单
                        //支付方式校验
                        String payMethodRule = this.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.PAYMENT_METHOD.toString());
                        if(StrUtil.isNotEmpty(payMethodRule)){
                            if(!payMethodRule.contains(addTaskOrderRequest.getPaymentMethod().toString())){
                                log.info("和任务限制的支付方式不匹配addTaskOrderRequest={}",addTaskOrderRequest.toString());
                                return;
                            }
                        }
                        //查询是否在b2b下过包含以岭品的订单 已收货
                        QueryAssistantOrderFirstRequest req = new QueryAssistantOrderFirstRequest();
                        req.setBuyerEid(addTaskOrderRequest.getTerminalId());
                        Boolean receiveB2BOrder = orderApi.verificationReceiveB2BOrder(req);
                        if (receiveB2BOrder) {
                            log.info("此客户在b2b有已收货的包含以岭品的订单BuyerEid={}", addTaskOrderRequest.getTerminalId());
                            return;
                        }
                        //首单
                        if (addTaskOrderRequest.getIsFirstOrder() && addTaskOrderRequest.getTotalAmount().compareTo(new BigDecimal(condition)) >= 0) {
                            inviteUserTask.setFinishValue(inviteUserTask.getFinishValue() + 1);
                            userTaskService.updateById(inviteUserTask);
                            finish = true;
                        } else {
                            log.info("非首单或者购买金额未达到任务门槛addTaskOrderRequest={},usertask={}", addTaskOrderRequest.toString(), inviteUserTask.toString());
                            return;
                        }
                    }
                    if (finish) {
                        // 佣金计算
                        SaTaskRegisterTerminalDO saTaskRegisterTerminalDO = new SaTaskRegisterTerminalDO();
                        // 终端联系人和手机号
                        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(addTaskOrderRequest.getTerminalId());
                        saTaskRegisterTerminalDO.setContactor(enterpriseDTO.getContactor()).setContactorMobile(enterpriseDTO.getContactorPhone()).setTaskId(inviteUserTask.getTaskId()).setUserTaskId(inviteUserTask.getId()).setTerminalId(addTaskOrderRequest.getTerminalId()).setTerminalName(addTaskOrderRequest.getTerminalName());
                        saTaskRegisterTerminalService.save(saTaskRegisterTerminalDO);
                        // 佣金计算
                        AddCommissionsToUserRequest addCommissionsToUserRequest = new AddCommissionsToUserRequest();
                        //任务结束更改为已结算
                        addCommissionsToUserRequest.setEffectStatus(CommissionsStatusEnum.UN_SETTLEMENT.getCode()).setFinishType(inviteUserTask.getFinishType()).setTaskId(inviteUserTask.getTaskId()).setTaskName(marketTaskDO.getTaskName()).setUserId(inviteUserTask.getUserId()).setUserTaskId(inviteUserTask.getId()).setSources(CommissionsSourcesEnum.TASK.getCode()).setOpTime(DateUtil.date());
                        //判断是否有上下线分成规则
                        String rule = this.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.GIVE_INVITEE_AWARD.toString());
                        String commissionValue = this.getRuleValue(inviteUserTask.getTaskId(), RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.COMMISSION.toString());
                        BigDecimal commission = new BigDecimal(commissionValue);
                        AddCommissionsToUserRequest.AddCommToUserDetailRequest request = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
                        if (!StringUtils.isEmpty(rule) && !rule.equals(TaskConstant.UNLIMIT)) {
                            // 查询第一添加此客户的人是否有邀请人
                            UserTeamDTO team = userTeamApi.getUserTeamByUserId(inviteUserId);
                            if (Objects.nonNull(team)) {
                                Long inviterId = team.getParentId();
                                //佣金金额
                                AddCommissionsToUserRequest inviterCommission = new AddCommissionsToUserRequest();
                                inviterCommission.setSources(CommissionsSourcesEnum.SUBORDINATE.getCode()).setTaskId(inviteUserTask.getTaskId()).setUserId(inviterId).setUserTaskId(inviteUserTask.getId()).setFinishType(FinishTypeEnum.NEW_ENT.getCode()).setTaskName(marketTaskDO.getTaskName()).setOpTime(DateUtil.date());
                                List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> inviterDetailList = Lists.newArrayList();
                                AddCommissionsToUserRequest.AddCommToUserDetailRequest inviterRequest = new AddCommissionsToUserRequest.AddCommToUserDetailRequest();
                                BigDecimal rate = new BigDecimal(rule);
                                if (rate.compareTo(BigDecimal.ZERO) > 0) {
                                    rate = rate.divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                                    BigDecimal invitecom = commission.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                                    commission = commission.subtract(invitecom).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                                    inviterRequest.setSubAmount(invitecom);
                                    inviterRequest.setNewTime(enterpriseDTO.getCreateTime()).setNewEntId(addTaskOrderRequest.getTerminalId()).setNewEntName(enterpriseDTO.getName());
                                    inviterDetailList.add(inviterRequest);
                                    inviterCommission.setDetailList(inviterDetailList);
                                    try {
                                        commissionsApi.addCommissionsToUser(inviterCommission);
                                    } catch (Exception e) {
                                        log.error("佣金发放失败Exception={}", e.getMessage());
                                    }

                                }
                            }
                        }
                        request.setSubAmount(commission).setNewTime(enterpriseDTO.getCreateTime()).setNewEntId(addTaskOrderRequest.getTerminalId()).setNewEntName(enterpriseDTO.getName()).setOpUserId(inviteUserTask.getUserId());
                        List<AddCommissionsToUserRequest.AddCommToUserDetailRequest> detailList = Lists.newArrayList();
                        detailList.add(request);
                        //订单佣金
                        addCommissionsToUserRequest.setDetailList(detailList);
                        try {
                            commissionsApi.addCommissionsToUser(addCommissionsToUserRequest);
                        } catch (Exception e) {
                            log.error("佣金发放失败Exception={}", e.getMessage());
                        }
                    }


                }
            } finally {
                redisDistributedLock.releaseLock(lockName, lockId);
            }
        });

    }


    @Override
    public List<TaskAreaDTO> queryTaskArea(Long taskId) {
        TaskAreaJsonDO taskAreaJsonDO = taskAreaJsonService.getById(taskId);
        return JSON.parseArray(taskAreaJsonDO.getAreaJson(), TaskAreaDTO.class);
    }

    @Override
    public void publishTask() {
        log.info("任务发布定时任务开始执行");
        LambdaQueryWrapper<MarketTaskDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MarketTaskDO::getTaskStatus, TaskStatusEnum.UNSTART.getStatus());
        List<MarketTaskDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            log.info("没有待开始的任务");
            return;
        }

        list = list.stream().filter(taskBase -> taskBase.getStartTime().compareTo(new Date()) <= 0).collect(Collectors.toList());
        if (CollUtil.isEmpty(list)) {
            log.info("待开始的任务没有到达指定时间");
            return;
        }
        list.forEach(t -> {
            t.setTaskStatus(TaskStatusEnum.IN_PROGRESS.getStatus());

            this.updateById(t);
        });
        // 为企业任务人员自动派发任务
        // userTaskService.dispatchEnterpriseTask(list);
    }

    @Override
    public void endTask() {
        log.info("任务结束定时任务开始执行");
        LambdaQueryWrapper<MarketTaskDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MarketTaskDO::getTaskStatus, TaskStatusEnum.IN_PROGRESS.getStatus());
        lambdaQueryWrapper.le(MarketTaskDO::getEndTime, new Date());
        List<MarketTaskDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            log.info("没有到达的任务");
            return;
        }
        list.forEach(t -> {
            t.setTaskStatus(TaskStatusEnum.END.getStatus());
            this.updateById(t);
            //上传资料的任务只单独结束 每月28日算佣金
            if(t.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())){
               return;
            }
            this.finishedTask(t.getId(), 0L);
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
                // 更新用户任务状态 交易额交易量变更未完成  拉户拉人会员推广只要大于0就算完成
                if (t.getFinishType().equals(FinishTypeEnum.AMOUNT.getCode()) || t.getFinishType().equals(FinishTypeEnum.MONEY.getCode())) {
                    this.updateUserTask(userTasks, updateUserTasks, UserTaskStatusEnum.UN_FINISH);
                } else {
                    List<UserTaskDO> finishUserTasks = userTasks.stream().filter(userTaskDO -> {
                        if (userTaskDO.getFinishValue() > 0) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                    this.updateUserTask(finishUserTasks, updateUserTasks, UserTaskStatusEnum.FINISHED);
                    userTasks.removeAll(finishUserTasks);
                    this.updateUserTask(userTasks, updateUserTasks, UserTaskStatusEnum.UN_FINISH);
                    //已完成的任务变更佣金状态
                    userTaskService.updateTaskCommission(finishUserTasks, 0L);
                }


            } else {
                //阶梯任务 达到阶梯条件的更新已完成 未达到更新为未完成
                List<UserTaskDO> finishUserTaskList = userTasks.stream().filter(userTaskDO -> {
                    LambdaQueryWrapper<UserTaskStepDO> wrapper = Wrappers.lambdaQuery();
                    wrapper.eq(UserTaskStepDO::getUserTaskId, userTaskDO.getId());
                    int count = userTaskStepService.count(wrapper);
                    return count > 0;
                }).collect(Collectors.toList());
                // 阶梯任务 更新佣金状态
                this.updateUserTask(finishUserTaskList, updateUserTasks, UserTaskStatusEnum.FINISHED);
                finishUserTaskList.forEach(userTaskDO -> {
                    userTaskService.addStepTaskCommission(userTaskDO);
                });


                userTasks.removeAll(finishUserTaskList);
                //未完成的不给佣金
                this.updateUserTask(userTasks, updateUserTasks, UserTaskStatusEnum.UN_FINISH);
            }
            userTaskService.updateBatchById(updateUserTasks);
        });

    }


    /**
     * 更新用户任务状态
     *
     * @param userTasks
     * @param updateUserTasks
     * @param userTaskStatusEnum
     */
    @Override
    public void updateUserTask(List<UserTaskDO> userTasks, List<UserTaskDO> updateUserTasks, UserTaskStatusEnum userTaskStatusEnum) {
        if (CollUtil.isEmpty(userTasks)) {
            return;
        }
        List<Long> utaskIds = userTasks.stream().map(UserTaskDO::getId).collect(Collectors.toList());
        utaskIds.forEach(utaskId -> {
            UserTaskDO u = new UserTaskDO();
            u.setId(utaskId);
            u.setTaskStatus(userTaskStatusEnum.getStatus());
            u.setOpTime(DateUtil.date());
            updateUserTasks.add(u);
        });
    }

    @Override
    public void stopTask(StopTaskRequest stopTaskRequest) {
        Long taskId = stopTaskRequest.getId();
        this.finishedTask(taskId, stopTaskRequest.getOpUserId());
        MarketTaskDO taskDO = this.getById(stopTaskRequest.getId());
        if (!taskDO.getTaskStatus().equals(TaskStatusEnum.IN_PROGRESS.getStatus())) {
            return;
        }

        MarketTaskDO marketTaskDO = new MarketTaskDO();
        marketTaskDO.setId(stopTaskRequest.getId());
        marketTaskDO.setTaskStatus(TaskStatusEnum.STOP.getStatus());
        this.updateById(marketTaskDO);
        //已完成的任务发放佣金
        //非阶梯任务处理
        String ruleValue = SpringUtil.getBean(MarketTaskService.class).getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());
        LambdaQueryWrapper<UserTaskDO> userTaskQueryWrapper = new QueryWrapper<UserTaskDO>().lambda();
        userTaskQueryWrapper.eq(UserTaskDO::getTaskStatus, UserTaskStatusEnum.IN_PROGRESS.getStatus());
        userTaskQueryWrapper.eq(UserTaskDO::getTaskId, taskId);
        List<UserTaskDO> userTasks = userTaskService.list(userTaskQueryWrapper);
        if (CollUtil.isEmpty(userTasks)) {
            return;
        }
        List<UserTaskDO> updateUserTasks = Lists.newArrayListWithExpectedSize(userTasks.size());

        if (!TaskConstant.ONE.equals(ruleValue)) {
            // 更新进行中的用户任务为未完成
            // 更新用户任务状态 交易额交易量变更未完成  拉户拉人会员推广只要大于0就算完成
            if (taskDO.getFinishType().equals(FinishTypeEnum.AMOUNT.getCode()) || taskDO.getFinishType().equals(FinishTypeEnum.MONEY.getCode())) {
                this.updateUserTask(userTasks, updateUserTasks, UserTaskStatusEnum.UN_FINISH);
            } else {
                List<UserTaskDO> finishUserTasks = userTasks.stream().filter(userTaskDO -> {
                    if (userTaskDO.getFinishValue() > 0) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                this.updateUserTask(finishUserTasks, updateUserTasks, UserTaskStatusEnum.FINISHED);
                userTasks.removeAll(finishUserTasks);
                this.updateUserTask(userTasks, updateUserTasks, UserTaskStatusEnum.UN_FINISH);
                //已完成的任务变更佣金状态
                userTaskService.updateTaskCommission(finishUserTasks, stopTaskRequest.getOpUserId());
            }
        } else {
            //阶梯任务 达到阶梯条件的更新已完成 未达到更新为未完成
            List<UserTaskDO> finishUserTaskList = userTasks.stream().filter(userTaskDO -> {
                LambdaQueryWrapper<UserTaskStepDO> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(UserTaskStepDO::getUserTaskId, userTaskDO.getId());
                int count = userTaskStepService.count(wrapper);
                return count > 0;
            }).collect(Collectors.toList());
            // 阶梯任务 更新佣金状态
            this.updateUserTask(finishUserTaskList, updateUserTasks, UserTaskStatusEnum.FINISHED);
            finishUserTaskList.forEach(userTaskDO -> {
                userTaskService.addStepTaskCommission(userTaskDO);
            });
            userTasks.removeAll(finishUserTaskList);
            //未完成的不给佣金 停止状态
            this.updateUserTask(userTasks, updateUserTasks, UserTaskStatusEnum.STOP);
        }
        userTaskService.updateBatchById(updateUserTasks);

    }

    /**
     * 已完成的交易量任务
     *
     * @param taskId
     */
    @Override
    public void finishedTask(Long taskId, Long optUserId) {
        log.info("已完成的交易量任务变更佣金状态taskId={}", taskId);
        LambdaQueryWrapper<UserTaskDO> userTaskQueryWrapper = new QueryWrapper<UserTaskDO>().lambda();
        userTaskQueryWrapper.eq(UserTaskDO::getTaskStatus, UserTaskStatusEnum.FINISHED.getStatus());
        userTaskQueryWrapper.eq(UserTaskDO::getTaskId, taskId);
        List<UserTaskDO> userTasks = userTaskService.list(userTaskQueryWrapper);
        if (CollUtil.isNotEmpty(userTasks)) {
            userTaskService.updateTaskCommission(userTasks, optUserId);
        }
    }

    @Override
    public void deleteTask(StopTaskRequest stopTaskRequest) {
        MarketTaskDO taskDO = this.getById(stopTaskRequest.getId());
        if (Objects.isNull(taskDO)) {
            return;
        }
        if (!taskDO.getTaskStatus().equals(TaskStatusEnum.UNSTART.getStatus())) {
            return;
        }
        MarketTaskDO marketTaskDO = new MarketTaskDO();
        marketTaskDO.setId(stopTaskRequest.getId());
        marketTaskDO.setOpUserId(stopTaskRequest.getOpUserId());
        this.deleteByIdWithFill(marketTaskDO);
    }

    @Override
    public AddTaskOrderRequest getOrderByNo(String orderNo) {
        AddTaskOrderRequest addTaskOrderRequest = new AddTaskOrderRequest();
        OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
        if(!orderDTO.getOrderSource().equals(OrderSourceEnum.SA.getCode())){
            log.info("非销售助手订单orderNo={}",orderNo);
            return null;
        }
        UserDTO userDTO = userApi.getById(orderDTO.getCreateUser());
        QueryAssistantFirstOrderRequest request = new QueryAssistantFirstOrderRequest();
        request.setBuyerEid(orderDTO.getBuyerEid());
        QueryAssistantOrderFirstRequest req = new QueryAssistantOrderFirstRequest();
        req.setBuyerEid(orderDTO.getBuyerEid());
        OrderDTO firstOrder = orderApi.getAssistantReceiveFirstOrder(req);
        boolean isFirst = false;
        if (Objects.nonNull(firstOrder)&& orderNo.equals(firstOrder.getOrderNo())) {
            isFirst = true;
        }
        addTaskOrderRequest.setSellerEid(orderDTO.getSellerEid());
        addTaskOrderRequest.setUserName(userDTO.getName()).setUserId(orderDTO.getCreateUser()).setTerminalName(orderDTO.getBuyerEname()).setTerminalId(orderDTO.getBuyerEid()).setOrderTime(orderDTO.getCreateTime()).setOrderNo(orderDTO.getOrderNo()).setOrderId(orderDTO.getId()).setIsFirstOrder(isFirst).setPaymentMethod(orderDTO.getPaymentMethod());
        //订单明细
        List<AddTaskOrderGoodsRequest> orderGoodsAddDTOList = Lists.newArrayList();
        List<OrderDetailDTO> detailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());
        //查询是否以岭品
        List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        final List<Long> goodsIdList = detailDTOList.stream().map(OrderDetailDTO::getGoodsId).collect(Collectors.toList());
        Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList,subEids);
        log.info("任务以岭品校验goodsMap={}", JSON.toJSONString(goodsMap));
        //计算每个商品的收货数量   从order_change表取
        List<OrderDetailChangeDTO> orderDetailChangeDTOS = orderDetailChangeApi.listByOrderId(orderDTO.getId());
        if (CollUtil.isEmpty(orderDetailChangeDTOS)) {
            log.info("实收商品数量全部为零不符合任务要求orderNo={},orderid={}",orderNo,orderDTO.getId());
            return null;
        }
        Map<Long, Integer> map = orderDetailChangeDTOS.stream().collect(Collectors.groupingBy(OrderDetailChangeDTO::getDetailId, Collectors.summingInt(OrderDetailChangeDTO::getReceiveQuantity)));
        List<Long> goodsIds = Lists.newArrayList();
        detailDTOList.forEach(orderDetailDTO -> {
            //非以岭品不参与计算
            if(null == goodsMap.get(orderDetailDTO.getGoodsId()) || goodsMap.get(orderDetailDTO.getGoodsId())==0){
                return;
            }
            Integer recv = map.get(orderDetailDTO.getId());
            if (Objects.isNull(recv) || recv == 0) {
                log.info("为查询到商品收货数量map={},orderdetailid={}",map.toString(),orderDetailDTO.getId());
                return;
            }
            AddTaskOrderGoodsRequest addTaskOrderGoodsRequest = new AddTaskOrderGoodsRequest();
            addTaskOrderGoodsRequest.setGoodsId(orderDetailDTO.getGoodsId()).setAmount(recv).setGoodName(orderDetailDTO.getGoodsName());
            goodsIds.add(orderDetailDTO.getGoodsId());
            orderGoodsAddDTOList.add(addTaskOrderGoodsRequest);
        });
        if (CollUtil.isEmpty(orderGoodsAddDTOList)) {
            log.info("实收商品数量全部为零不符合任务要求map={}",map.toString());
            return null;
        }
        addTaskOrderRequest.setGoodsIds(goodsIds);
        addTaskOrderRequest.setOrderGoodsAddDTOList(orderGoodsAddDTOList);
        return addTaskOrderRequest;
    }

}
