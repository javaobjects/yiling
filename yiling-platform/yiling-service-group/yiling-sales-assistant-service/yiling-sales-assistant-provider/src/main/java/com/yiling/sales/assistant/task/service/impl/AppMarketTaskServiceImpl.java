package com.yiling.sales.assistant.task.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.sales.assistant.task.bo.LocationTreeBO;
import com.yiling.sales.assistant.task.constant.TaskConstant;
import com.yiling.sales.assistant.task.dao.MarketTaskMapper;
import com.yiling.sales.assistant.task.dto.app.TaskDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.TaskGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.app.GetTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.TaskAreaRelationDO;
import com.yiling.sales.assistant.task.entity.TaskDistributorDO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.enums.ComputeTypeEnum;
import com.yiling.sales.assistant.task.enums.ExecuteTypeEnum;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.RuleKeyEnum;
import com.yiling.sales.assistant.task.enums.RuleTypeEnum;
import com.yiling.sales.assistant.task.enums.TaskStatusEnum;
import com.yiling.sales.assistant.task.enums.TaskTypeEnum;
import com.yiling.sales.assistant.task.service.AppMarketTaskService;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.TaskAreaRelationService;
import com.yiling.sales.assistant.task.service.TaskDeptRelationService;
import com.yiling.sales.assistant.task.service.TaskDistributorService;
import com.yiling.sales.assistant.task.service.TaskGoodsRelationService;
import com.yiling.sales.assistant.task.service.TaskMemberBuyService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserSalesAreaDTO;
import com.yiling.user.system.enums.UserTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * app端任务
 * @author: ray
 * @date: 2021/9/27
 */
@Service
@Slf4j
public class AppMarketTaskServiceImpl  extends BaseServiceImpl<MarketTaskMapper, MarketTaskDO> implements AppMarketTaskService {

    @Autowired
    private TaskAreaRelationService taskAreaRelationService;

    @Autowired
    private MarketTaskService       marketTaskService;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private TaskGoodsRelationService taskGoodsRelationService;

    @Autowired
    private TaskMemberBuyService taskMemberBuyService;
    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private UserApi                userApi;
    @Autowired
    private FileService            fileService;
    @Autowired
    private TaskDistributorService taskDistributorService;
    @Autowired
    private TaskDeptRelationService taskDeptRelationService;

    @Autowired
    private RedisService redisService;
    @Override
    public Page<TaskDTO> listTaskPage(QueryTaskPageRequest queryTaskPageRequest) {
        Page<TaskDTO> vpage = queryTaskPageRequest.getPage();

        // 查询已发布的任务（即到达开始时间）
        LambdaQueryWrapper<MarketTaskDO> lambda = new QueryWrapper<MarketTaskDO>().lambda();
        if (!queryTaskPageRequest.getTaskType().equals(TaskTypeEnum.PLATFORM.getCode())) {
            lambda.eq(MarketTaskDO::getEid, queryTaskPageRequest.getEid());
        }
        lambda.eq(MarketTaskDO::getTaskStatus, TaskStatusEnum.IN_PROGRESS.getStatus())
                .eq(MarketTaskDO::getTaskType, queryTaskPageRequest.getTaskType())
                .select(MarketTaskDO::getId, MarketTaskDO::getFullCover);
        List<MarketTaskDO> taskBaseList = this.list(lambda);
        if (CollUtil.isEmpty(taskBaseList)) {
            return vpage;
        }
        Integer userType = queryTaskPageRequest.getUserType().getCode()-1;
        taskBaseList = taskBaseList.stream().filter(taskDO -> {
            String value =  marketTaskService.getRuleValue(taskDO.getId(),RuleTypeEnum.TAKE.getCode(),RuleKeyEnum.TAKE_USER_GROUP.toString());
            if(value.contains(userType.toString())){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (CollUtil.isEmpty(taskBaseList)) {
            return vpage;
        }
        List<Long> ids = taskBaseList.stream().map(MarketTaskDO::getId).collect(Collectors.toList());
        //用户类型校验
        return this.listPlatformTaskPage(queryTaskPageRequest, ids, taskBaseList);

    }

    private Page<TaskDTO> listPlatformTaskPage(QueryTaskPageRequest queryTaskPageRequest, List<Long> taskIds, List<MarketTaskDO> list) {
        Page<TaskDTO> vpage = queryTaskPageRequest.getPage();
        // 查询当前用户的销售区域 -到市
        UserSalesAreaDTO saleArea = null;
        List<String> cityCodeList = Lists.newArrayList();
        if(queryTaskPageRequest.getUserType().equals(UserTypeEnum.ZIRANREN)){
            saleArea = userApi.getSaleAreaByUserId(queryTaskPageRequest.getUserId());
            if(Objects.isNull(saleArea)){
                log.info("未设置销售区域看不到任务");
                return queryTaskPageRequest.getPage();
            }

            if(saleArea.getSalesAreaAllFlag()==0){
                cityCodeList = userApi.getSaleAreaDetailByUserId(queryTaskPageRequest.getUserId(),2);
                if(CollUtil.isEmpty(cityCodeList)){
                    log.info("未设置销售区域看不到任务");
                    return queryTaskPageRequest.getPage();
                }
            }
        }else{
            //小三元取企业
            EnterpriseSalesAreaDTO enterpriseSalesArea = enterpriseApi.getEnterpriseSalesArea(queryTaskPageRequest.getEid());
            if(Objects.isNull(enterpriseSalesArea)){
                log.info("小三元未设置企业销售区域看不到任务");
                return queryTaskPageRequest.getPage();
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
        lambda.in(TaskAreaRelationDO::getTaskId, taskIds).select(TaskAreaRelationDO::getTaskId, TaskAreaRelationDO::getTaskId,
                TaskAreaRelationDO::getCityCode);
        List<TaskAreaRelationDO> taskAreaRelations = taskAreaRelationService.list(lambda);
        // 匹配的任务id集合
        List<String> finalCityCodeList = cityCodeList;
        List<Long> matchTaskIds = Lists.newArrayList();
        if(null== saleArea || saleArea.getSalesAreaAllFlag()==0){
            matchTaskIds = taskAreaRelations.stream()
                    .filter(taskAreaRelation -> finalCityCodeList.contains(taskAreaRelation.getCityCode()))
                    .map(TaskAreaRelationDO::getTaskId).distinct().collect(Collectors.toList());
        }else{
            matchTaskIds = taskAreaRelations.stream().map(TaskAreaRelationDO::getTaskId).distinct().collect(Collectors.toList());
        }


        // 不限制区域的企业任务
        List<Long> noAreaLimit = list.stream().filter(t -> t.getFullCover().equals(1)).map(MarketTaskDO::getId)
                .distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(matchTaskIds) && CollUtil.isEmpty(noAreaLimit)) {
            return queryTaskPageRequest.getPage();
        }
        List<Long> ids = Lists.newArrayList();
        ids.addAll(matchTaskIds);
        ids.addAll(noAreaLimit);
        // 资料上传任务匹配  根据小三元所属商业公司商务负责人所在部门 匹配任务所在部门
        if(null!=queryTaskPageRequest.getEid() && queryTaskPageRequest.getEid()>0 && queryTaskPageRequest.getUserType().equals(UserTypeEnum.XIAOSANYUAN)){
         ids = taskDeptRelationService.filterTaskByDept(ids, queryTaskPageRequest.getEid());
        }
        Page<MarketTaskDO> page = queryTaskPageRequest.getPage();
        LambdaQueryWrapper<MarketTaskDO> lambdaTask = new QueryWrapper<MarketTaskDO>().lambda();
        lambdaTask.in(MarketTaskDO::getId, ids);
        lambdaTask.orderByDesc(MarketTaskDO::getId);
        IPage<MarketTaskDO> taskBaseListPage = this.page(page, lambdaTask);
        List<MarketTaskDO> taskBaseList = taskBaseListPage.getRecords();
        List<TaskDTO> taskDTOList = Lists.newArrayListWithExpectedSize(taskBaseList.size());
        taskBaseList.forEach(taskDO -> {
            TaskDTO taskDTO = new TaskDTO();
            PojoUtils.map(taskDO, taskDTO);
            //收益
            if (taskDO.getTaskType().equals(TaskTypeEnum.PLATFORM.getCode())) {
                taskDTO.setProfit(this.getCommission(taskDO.getId()));
            }
            if(taskDO.getFinishType().equals(FinishTypeEnum.MONEY.getCode()) || taskDO.getFinishType().equals(FinishTypeEnum.AMOUNT.getCode())){
              LambdaQueryWrapper<TaskGoodsRelationDO> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(TaskGoodsRelationDO::getTaskId,taskDO.getId());
                int count = taskGoodsRelationService.count(wrapper);
                taskDTO.setGoodsCount(count);
            }
            Integer times = userTaskService.count(new QueryWrapper<UserTaskDO>().lambda()
                    .eq(UserTaskDO::getTaskId, taskDO.getId()).eq(UserTaskDO::getUserId, queryTaskPageRequest.getUserId()));
                    //.eq(UserTaskDO::getTaskStatus, UserTaskStatusEnum.IN_PROGRESS.getStatus()));
            if (times > 0) {
                taskDTO.setTaked(true);
            } else {
                taskDTO.setTaked(false);
            }
            // 其他条件
           // String finishRuleValue = marketTaskService.getRuleValue(taskDO.getId(), RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.FINISH_TYPE.toString());
            taskDTOList.add(taskDTO);
        });
        vpage.setTotal(taskBaseListPage.getTotal());
        vpage.setRecords(taskDTOList);
        return vpage;
    }

    @Override
    public String appendRuleStr(Long taskId, String finishRuleValue) {
        StringBuilder ruleStr = new StringBuilder();
        // 任务完成类型
        String stepRuleValue = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());

        int finishValue = Integer.valueOf(finishRuleValue);
        String saleRuleValue = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(),
                RuleKeyEnum.SALE_CONDITION.toString());

        //阶梯任务
        if(stepRuleValue.equals(TaskConstant.ONE)){
            String[] commision = marketTaskService.getRuleValue(taskId, RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.COMMISSION.toString()).split(",");

            String[] saleRule =  saleRuleValue.split(",");
            for (int i = 0; i < saleRule.length; i++) {
                this.appendStr(ruleStr,taskId,finishValue,saleRule[i],commision[i],i+1);
            }
            return ruleStr.toString();
        }
        // 计算方式 单品计算还是多品计算 目前只有多品
        String computeRuleValue = "2";

        // 交易量 单品
        if ((FinishTypeEnum.AMOUNT.getCode() == finishValue || FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode() == finishValue)
                && ComputeTypeEnum.MORE.getCode().toString().equals(computeRuleValue)) {
            ruleStr.append("每种商品各完成").append(saleRuleValue).append("盒销售");
        }
        // 交易额 单品
        if ((FinishTypeEnum.MONEY.getCode() == finishValue || FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode() == finishValue)
                && ComputeTypeEnum.MORE.getCode().toString().equals(computeRuleValue)) {
            // 大于9999 以万单位进行展示，保留小数点后2位（1.12万元）
            ruleStr.append("每种商品各完成").append(saleRuleValue).append("元销售额");
        }
        // 交易量 多品
        if ((FinishTypeEnum.AMOUNT.getCode() == finishValue || FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode() == finishValue)
                && ComputeTypeEnum.SINGLE.getCode().toString().equals(computeRuleValue)) {
            ruleStr.append("总计销售").append(saleRuleValue).append("盒");
        }
        // 交易额 单品
        if ((FinishTypeEnum.MONEY.getCode() == finishValue || FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode() == finishValue)
                && ComputeTypeEnum.SINGLE.getCode().toString().equals(computeRuleValue)) {
            // 大于9999 以万单位进行展示，保留小数点后2位（1.12万元）
            ruleStr.append("销售").append(saleRuleValue).append("元");
        }
        //拉新户
        if(FinishTypeEnum.NEW_ENT.getCode() == finishValue || FinishTypeEnum.NEW_USER.getCode() == finishValue){
            String ruleValue = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_LIMIT.toString());
            //无限制
            if(TaskConstant.UNLIMIT.equals(ruleValue)){
                ruleStr.append("无限制");
            }else{
                ruleStr.append("有限制：");
                //首单还是累计
                String newRuleValue = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_CONDITION.toString());
                String condition = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.NEW_CUSTOMER_AMOUNT.toString());

                if(TaskConstant.ONE.equals(newRuleValue)){
                   //累计
                    ruleStr.append("累计采购以岭品达到").append(condition).append("元");
                }else{
                    //首单
                    ruleStr.append("首单采购以岭品达到").append(condition).append("元");;
                }
            }
        }
        return ruleStr.toString();
    }

    //阶梯任务规则展示拼接
    private void appendStr(StringBuilder ruleStr,Long taskId,int finishValue,String saleRuleValue,String commissionRuleValue,Integer step){
        // 计算方式 单品计算还是多品计算
        String computeRuleValue = "2";
        //固定金额 销售额%
       /* String commissionMode = marketTaskService.getRuleValue(taskId, RuleTypeEnum.COMMISSION.getCode(),
                RuleKeyEnum.COMMISSION_MODE.toString());*/
        String reward = null;
        //固定金额 commissionMode.equals(TaskConstant.ONE) 目前都是固定金额
        if(true){
            reward = "每盒给予"+commissionRuleValue+"元|" ;
        }else{
            reward = "每个商品给予销售额的"+commissionRuleValue+"%作为佣金奖励|" ;
        }

        // 交易量 单品
        if ((FinishTypeEnum.AMOUNT.getCode() == finishValue || FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode() == finishValue)
                && ComputeTypeEnum.MORE.getCode().toString().equals(computeRuleValue)) {
            ruleStr.append("每种商品各完成").append(saleRuleValue).append("盒销售");
        }
        // 交易额 单品
        if (FinishTypeEnum.MONEY.getCode() == finishValue
                && ComputeTypeEnum.MORE.getCode().toString().equals(computeRuleValue)) {
            // 大于9999 以万单位进行展示，保留小数点后2位（1.12万元）
            ruleStr.append("每种商品各完成").append(saleRuleValue).append("元销售额");
        }
        // 交易量 多品
        if ((FinishTypeEnum.AMOUNT.getCode() == finishValue || FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode() == finishValue)
                && ComputeTypeEnum.SINGLE.getCode().toString().equals(computeRuleValue)) {
            ruleStr.append("完成").append(saleRuleValue).append("盒销售");
        }
        // 交易额 单品
        if (FinishTypeEnum.MONEY.getCode() == finishValue
                && ComputeTypeEnum.SINGLE.getCode().toString().equals(computeRuleValue)) {
            // 大于9999 以万单位进行展示，保留小数点后2位（1.12万元）
            ruleStr.append("完成").append(saleRuleValue).append("元销售额");
        }
        ruleStr.append(reward);
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
        if (!finishValue.equals(FinishTypeEnum.MONEY.getCode())
                && !finishValue.equals(FinishTypeEnum.AMOUNT.getCode()) && !finishValue.equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())) {
            profit = commissionRule + "元";
            if(finishValue.equals(FinishTypeEnum.NEW_USER.getCode())){
                profit =profit+"/每人";
            }
            if(finishValue.equals(FinishTypeEnum.NEW_ENT.getCode())){
                profit =profit+"/每户";
            }
            if(finishValue.equals(FinishTypeEnum.MEMBER_BUY.getCode())){
                profit =profit+"/每次";
            }
        } else {
            // 按交易额或者交易量才存在 单品执行 多品执行
            String taskRule = marketTaskService.getRuleValue(taskId, RuleTypeEnum.COMMISSION.getCode(),
                    RuleKeyEnum.EXECUTE_TYPE.toString());
            int excType = Integer.parseInt(taskRule);

            // 按交易额
            if (!Objects.isNull(taskRule) && finishValue.equals(FinishTypeEnum.MONEY.getCode())
                    && excType == ExecuteTypeEnum.MORE.getCode()) {
                // 统一执行
                profit = "每盒预收益额" + commissionRule + "元";
            }
            if (!Objects.isNull(taskRule) && finishValue.equals(FinishTypeEnum.MONEY.getCode())
                    && excType == ExecuteTypeEnum.SINGLE.getCode()) {
                // 单独执行 最高和最低值 用逗号分隔
                if (!Objects.isNull(commissionRule) && !StringUtils.isEmpty(commissionRule)) {
                    String[] commissionRuleArray = commissionRule.split(",");
                    profit = "每盒预收益额" + commissionRuleArray[0] + "元~" + commissionRuleArray[commissionRuleArray.length-1] + "元";
                }

            }

            // 按交易量
            if (!Objects.isNull(taskRule) && (finishValue.equals(FinishTypeEnum.AMOUNT.getCode()) || finishValue.equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode()))
                    && excType == ExecuteTypeEnum.MORE.getCode()) {
                // 统一执行
                if (!Objects.isNull(commissionRule) && !StringUtils.isEmpty(commissionRule)) {
                    String[] commissionRuleArray = commissionRule.split(",");
                    profit = commissionRuleArray[0] + "~" + commissionRuleArray[commissionRuleArray.length-1] + "元";
                }else{
                    profit = commissionRule + "元";
                }
            }
            if (!Objects.isNull(taskRule) && (finishValue.equals(FinishTypeEnum.AMOUNT.getCode()) || finishValue.equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode()))
                    && excType == ExecuteTypeEnum.SINGLE.getCode()) {
                // 单独执行 最高和最低值 用逗号分隔
                if (!Objects.isNull(commissionRule) && !StringUtils.isEmpty(commissionRule)) {
                    LambdaQueryWrapper<TaskGoodsRelationDO> wrapper = Wrappers.lambdaQuery();
                    wrapper.eq(TaskGoodsRelationDO::getTaskId,taskId).select(TaskGoodsRelationDO::getCommission);
                    List<TaskGoodsRelationDO> list = taskGoodsRelationService.list(wrapper);
                    List<BigDecimal> decimals = list.stream().map(TaskGoodsRelationDO::getCommission).collect(Collectors.toList());
                    if(decimals.size()==1){
                        profit = decimals.get(0)+"元";
                    }else{
                        BigDecimal min = decimals.stream().min((a,b)->a.compareTo(b)).get();
                        BigDecimal max = decimals.stream().max((a,b)->a.compareTo(b)).get();
                        if(min.compareTo(max)==0){
                            profit = min+"元";
                        }else{
                            profit = min+"~"+max+"元";
                        }

                    }

                }

            }
        }

        return profit;
    }

    @Override
    public TaskDetailDTO getTaskDetail(GetTaskDetailRequest getTaskDetailRequest) {
        Long taskId = getTaskDetailRequest.getTaskId();
        MarketTaskDO task = this.getById(taskId);
        String finishRuleValue = marketTaskService.getRuleValue(task.getId(), RuleTypeEnum.FINISH_TYPE.getCode(),
                RuleKeyEnum.FINISH_TYPE.toString());
        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
        taskDetailDTO.setTaskDesc(task.getTaskDesc());
        taskDetailDTO.setFinishType(task.getFinishType());
        taskDetailDTO.setFlag(0);
        LambdaQueryWrapper<UserTaskDO> mytask = Wrappers.lambdaQuery();
        mytask.eq(UserTaskDO::getTaskId, taskId);
        mytask.orderByDesc(UserTaskDO::getId);
        mytask.last("limit 1");
       // UserTaskDO myTask = userTaskService.getOne(mytask);

        MarketTaskDO taskBase = marketTaskService.getById(taskId);
        //平台任务 收益计算
        if(TaskTypeEnum.PLATFORM.getCode().equals(task.getTaskType())){
            String profit = this.getCommission(taskId);
            taskDetailDTO.setProfit(profit);

        }
        String ruleStr = this.appendRuleStr(taskId, finishRuleValue);
        List<String> stringList = Arrays.asList(ruleStr.split("\\|"));
        taskDetailDTO.setTaskRule(stringList);
        taskDetailDTO.setTaskName(taskBase.getTaskName());

        BeanUtils.copyProperties(taskBase, taskDetailDTO);
        Integer times = userTaskService.count(new QueryWrapper<UserTaskDO>().lambda().eq(UserTaskDO::getTaskId, taskId)
                .eq(UserTaskDO::getUserId, getTaskDetailRequest.getUserId()));
        if (times > 0) {
            taskDetailDTO.setFlag(3);
        }
        //会员海报
        if(task.getFinishType().equals(FinishTypeEnum.MEMBER_BUY.getCode())){
            taskDetailDTO.setTaskMember(taskMemberBuyService.getMemberById(taskId));
        }
        if(StringUtils.isNotEmpty(taskBase.getEnterpriseType())){
            List<String> split= Arrays.asList(taskBase.getEnterpriseType().split(","));
            List<String> typeList = Lists.newArrayList();
            split.forEach(s -> {
                EnterpriseTypeEnum typeEnum = EnterpriseTypeEnum.getByCode(Integer.valueOf(s));
                typeList.add(typeEnum.getName());
            });
            taskDetailDTO.setEnterpriseType(StringUtils.join(typeList,"、"));
        }
        //交易量
        if(task.getFinishType().equals(FinishTypeEnum.AMOUNT.getCode()) || task.getFinishType().equals(FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode())){
            //配送商数量
            LambdaQueryWrapper<TaskDistributorDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TaskDistributorDO::getTaskId,taskId);
            int count = taskDistributorService.count(wrapper);
            taskDetailDTO.setDistributorCount(count);
            String stepRuleValue = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.STEP_CONDITION.toString());
            if(TaskConstant.ONE.equals(stepRuleValue)){
                taskDetailDTO.setIsStepTask(true);
            }else{
                taskDetailDTO.setIsStepTask(false);
            }
            //是否会员
            String isMember = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.IS_MEMBER.toString());
            if(StringUtils.isEmpty(isMember)){
                taskDetailDTO.setIsMember("不限制");
            }else{
                if(isMember.equals(TaskConstant.ONE)){
                    taskDetailDTO.setIsMember("是");
                }else{
                    taskDetailDTO.setIsMember("否");
                }
            }
        }
        if(task.getFinishType().equals(FinishTypeEnum.AMOUNT.getCode()) || task.getFinishType().equals(FinishTypeEnum.NEW_ENT.getCode())){
            //支付方式
            String paymentMethod = marketTaskService.getRuleValue(taskId, RuleTypeEnum.FINISH_TYPE.getCode(), RuleKeyEnum.PAYMENT_METHOD.toString());
            if(StringUtils.isEmpty(paymentMethod)){
                taskDetailDTO.setPaymentMethod("不限制");
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
                taskDetailDTO.setPaymentMethod(methodStr);
            }
        }
        taskDetailDTO.setCommissionRule(this.getCommissionRule(taskId));

        return taskDetailDTO;
        
    }

    @Override
    public String getCommissionRule(Long taskId){
        String ruleValue = marketTaskService.getRuleValue(taskId, RuleTypeEnum.COMMISSION.getCode(), RuleKeyEnum.GIVE_INVITEE_AWARD.toString());
        String rule = "是否有上下线分成，";
        if(TaskConstant.UNLIMIT.equals(ruleValue)){
           return rule+"无";
        }
        return rule + "有，上线分成比例"+ruleValue+"%";
    }

    @Override
    public List<TaskGoodsDTO> listTaskGoodsPage(QueryTaskGoodsRequest queryTaskGoodsRequest) {
        // 查询任务关联商品
        LambdaQueryWrapper<TaskGoodsRelationDO> lambdaQueryWrapper = new QueryWrapper<TaskGoodsRelationDO>().lambda();
        lambdaQueryWrapper.eq(TaskGoodsRelationDO::getTaskId, queryTaskGoodsRequest.getTaskId());
        List<TaskGoodsRelationDO> goodsRelationList = taskGoodsRelationService.list(lambdaQueryWrapper);
        if (CollectionUtil.isEmpty(goodsRelationList)) {
            return Lists.newArrayList();
        }

        List<Long> goodsIds = goodsRelationList.stream().map(TaskGoodsRelationDO::getGoodsId)
                .collect(Collectors.toList());
        List<GoodsDTO> goodsInfoDTOS = goodsApi.batchQueryInfo(goodsIds);

        List<TaskGoodsDTO> taskGoodsDTOList = Lists.newArrayListWithExpectedSize(goodsRelationList.size());
        goodsRelationList.forEach(taskGoods -> {
           TaskGoodsDTO taskGoodsDTO = new TaskGoodsDTO();
            PojoUtils.map(taskGoods, taskGoodsDTO);

            GoodsDTO goodsInfoDTO = goodsInfoDTOS.stream().filter(g -> g.getId().equals(taskGoods.getGoodsId()))
                    .findAny().orElse(null);
            if (!Objects.isNull(goodsInfoDTO)) {
                taskGoodsDTO.setCanSplit(goodsInfoDTO.getCanSplit());
                taskGoodsDTO.setGoodsPic(fileService.getUrl(goodsInfoDTO.getPic(), FileTypeEnum.GOODS_PICTURE));
                taskGoodsDTO.setManufacturer(goodsInfoDTO.getManufacturer());
                //taskGoodsDTO.setSupplier(goodsInfoDTO.getEname());
                taskGoodsDTO.setSpecifications(goodsInfoDTO.getSellSpecifications());
                taskGoodsDTO.setSellSpecifications(goodsInfoDTO.getSellSpecifications());
                taskGoodsDTO.setMiddlePackage(goodsInfoDTO.getMiddlePackage());
                taskGoodsDTO.setName(goodsInfoDTO.getName());
                taskGoodsDTO.setPrice(goodsInfoDTO.getPrice());
                // 忽略上下架状态，任务配了之后只允许临时下架
            }
            taskGoodsDTOList.add(taskGoodsDTO);
        });

        return taskGoodsDTOList;
    }
}