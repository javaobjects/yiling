package com.yiling.sales.assistant.task.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.dao.TaskAccompanyingBillMapper;
import com.yiling.sales.assistant.task.dto.TaskAccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskAccompanyBillPageRequest;
import com.yiling.sales.assistant.task.entity.AccompanyingBillDO;
import com.yiling.sales.assistant.task.entity.AccompanyingBillMatchDO;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;
import com.yiling.sales.assistant.task.entity.MatchDetailDO;
import com.yiling.sales.assistant.task.entity.TaskAccompanyingBillDO;
import com.yiling.sales.assistant.task.entity.TaskDistributorDO;
import com.yiling.sales.assistant.task.entity.TaskGoodsRelationDO;
import com.yiling.sales.assistant.task.entity.UserTaskDO;
import com.yiling.sales.assistant.task.enums.FinishTypeEnum;
import com.yiling.sales.assistant.task.enums.FlowMatchResultEnum;
import com.yiling.sales.assistant.task.enums.TaskStatusEnum;
import com.yiling.sales.assistant.task.service.AccompanyingBillMatchService;
import com.yiling.sales.assistant.task.service.AccompanyingBillService;
import com.yiling.sales.assistant.task.service.MarketTaskService;
import com.yiling.sales.assistant.task.service.MatchDetailService;
import com.yiling.sales.assistant.task.service.TaskAccompanyingBillService;
import com.yiling.sales.assistant.task.service.TaskDistributorService;
import com.yiling.sales.assistant.task.service.TaskGoodsRelationService;
import com.yiling.sales.assistant.task.service.TaskOrderService;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.sales.assistant.task.service.strategy.TaskStrategyContext;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-01-11
 */
@Slf4j
@Service
public class TaskAccompanyingBillServiceImpl extends BaseServiceImpl<TaskAccompanyingBillMapper, TaskAccompanyingBillDO> implements TaskAccompanyingBillService {

    @Autowired
    private AccompanyingBillMatchService accompanyingBillMatchService;

    @Autowired
    private AccompanyingBillService accompanyingBillService;

    @Autowired
    private MatchDetailService matchDetailService;

    @Autowired
    private MarketTaskService marketTaskService;

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private TaskDistributorService taskDistributorService;

    @Autowired
    private TaskGoodsRelationService taskGoodsRelationService;

    @Autowired
    private TaskOrderService taskOrderService;

    @Autowired
    private TaskAccompanyingBillService taskAccompanyingBillService;

    @Autowired
    private TaskStrategyContext taskStrategyContext;


    @DubboReference
    private EnterpriseApi enterpriseApi;

    @Override
    public Page<TaskAccompanyingBillDTO> queryPage(QueryTaskAccompanyBillPageRequest queryTaskAccompanyBillPageRequest) {
        LambdaQueryWrapper<TaskAccompanyingBillDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskAccompanyingBillDO::getUserTaskId,queryTaskAccompanyBillPageRequest.getUserTaskId()).orderByDesc(TaskAccompanyingBillDO::getId);
        Page<TaskAccompanyingBillDO> page = this.page(queryTaskAccompanyBillPageRequest.getPage(),wrapper);
        Page<TaskAccompanyingBillDTO> result = PojoUtils.map(page,TaskAccompanyingBillDTO.class);
        if(page.getTotal()==0){
            return result;
        }
        List<TaskAccompanyingBillDTO> records = result.getRecords();
        List<Long> billIdList = records.stream().map(TaskAccompanyingBillDTO::getAccompanyingBillId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<AccompanyingBillMatchDO> billWrapper = Wrappers.lambdaQuery();
        billWrapper.in(AccompanyingBillMatchDO::getAccompanyingBillId,billIdList);
        List<AccompanyingBillMatchDO> accompanyingBillDOList = accompanyingBillMatchService.list(billWrapper);
        Map<Long, AccompanyingBillMatchDO> accompanyingBillDOMap = accompanyingBillDOList.stream().collect(Collectors.toMap(AccompanyingBillMatchDO::getAccompanyingBillId, v -> v, (v1, v2) -> v1));

        LambdaQueryWrapper<AccompanyingBillDO> accbillWrapper = Wrappers.lambdaQuery();
        accbillWrapper.in(AccompanyingBillDO::getId,billIdList);
        List<AccompanyingBillDO> accBillMatchDOS = accompanyingBillService.list(accbillWrapper);
        Map<Long, AccompanyingBillDO> billDOMap = accBillMatchDOS.stream().collect(Collectors.toMap(AccompanyingBillDO::getId, v -> v, (v1, v2) -> v1));
        records.forEach(taskAccompanyingBillDTO -> {
            AccompanyingBillMatchDO accompanyingBillDO = accompanyingBillDOMap.get(taskAccompanyingBillDTO.getAccompanyingBillId());
            AccompanyingBillDO aDo = billDOMap.get(taskAccompanyingBillDTO.getAccompanyingBillId());
            taskAccompanyingBillDTO.setRecvEname(accompanyingBillDO.getErpRecvName()).setUploadTime(aDo.getUploadTime());
        });
        /*List<Long> recvEidList = records.stream().map(TaskAccompanyingBillDTO::getRecvEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(recvEidList);
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, v -> v, (v1, v2) -> v1));
        records.forEach(taskAccompanyingBillDTO -> {
            EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(taskAccompanyingBillDTO.getRecvEid());
            taskAccompanyingBillDTO.setRecvEname(enterpriseDTO.getName());
        });*/
        return result;
    }

    @Override
    public void handleTaskAccompanyingBill(Long accompanyingBillId) {
        //查询上个月份的上传任务
        LambdaQueryWrapper<MarketTaskDO> taskWrapper = Wrappers.lambdaQuery();
        taskWrapper.eq(MarketTaskDO::getFinishType, FinishTypeEnum.UPLOAD_ACCOMPANYING_BILL.getCode()).eq(MarketTaskDO::getTaskStatus, TaskStatusEnum.END.getStatus());
        DateTime dateTime = DateUtil.offset(new Date(), DateField.MONTH, -1);
        taskWrapper.between(MarketTaskDO::getStartTime,DateUtil.beginOfMonth(dateTime),DateUtil.endOfMonth(dateTime)).last("limit 1");
        MarketTaskDO taskDO = marketTaskService.getOne(taskWrapper);
        if(Objects.isNull(taskDO)){
            log.info("无上传资料任务任务");
            return;
        }
        AccompanyingBillDO accompanyingBillDO = accompanyingBillService.getById(accompanyingBillId);
        //查询随货同行单商品数据
        LambdaQueryWrapper<MatchDetailDO> matchWrapper = Wrappers.lambdaQuery();
        matchWrapper.eq(MatchDetailDO::getAccompanyingBillId,accompanyingBillDO.getId());
        List<MatchDetailDO> matchDetailDOS = matchDetailService.list(matchWrapper);
        if(CollUtil.isEmpty(matchDetailDOS)){
            log.error("匹配成功的流向数据商品信息异常accompanyingBillId={}",accompanyingBillId);
            return;
        }
        //查询用户已承接的任务
        LambdaQueryWrapper<UserTaskDO> wrapper = Wrappers.lambdaQuery();
        Long uploadUserId = accompanyingBillDO.getCreateUser();
        wrapper.eq(UserTaskDO::getUserId, uploadUserId).eq(UserTaskDO::getTaskId,taskDO.getId()).last("limit 1");
        UserTaskDO userTaskDO = userTaskService.getOne(wrapper);
        if (Objects.isNull(userTaskDO)) {
            log.info("用户未承接上传资料任务 userID={}", uploadUserId);
            return;
        }
        if(accompanyingBillDO.getUploadTime().compareTo(userTaskDO.getCreatedTime())<=0 ){
            log.info("上传资料时间早于任务开始时间不计算任务进度accompanyingBillId={}",accompanyingBillId);
            return;
        }
        LambdaQueryWrapper<AccompanyingBillMatchDO> flowWrapper = Wrappers.lambdaQuery();
        flowWrapper.eq(AccompanyingBillMatchDO::getAccompanyingBillId,accompanyingBillId).eq(AccompanyingBillMatchDO::getResult, FlowMatchResultEnum.ERP_CRM_EQ.getCode());
        AccompanyingBillMatchDO billMatchDO = accompanyingBillMatchService.getOne(flowWrapper);
        if(StrUtil.isEmpty(billMatchDO.getErpRecvName())){
            log.error("收货单位名称空accompanyingBillId={}",accompanyingBillId);
            return;
        }
        QueryEnterpriseByNameRequest request = new QueryEnterpriseByNameRequest();
        request.setName(billMatchDO.getErpRecvName());
        List<EnterpriseDTO> enterpriseListByName = enterpriseApi.getEnterpriseListByName(request);
        if(CollUtil.isEmpty(enterpriseListByName)){
            log.info("收货单位信息不存在系统中RecvName={}",request.getName());
            return;
        }
        // 企业类型限制校验
        if(StrUtil.isNotEmpty(taskDO.getEnterpriseType()) && !taskDO.getEnterpriseType().contains(enterpriseListByName.get(0).getType().toString())){
            log.info("企业类型不在任务配置企业类型范围内");
              return;
        }
        //匹配供应商
        LambdaQueryWrapper<TaskDistributorDO> dwrapper = Wrappers.lambdaQuery();
        dwrapper.eq(TaskDistributorDO::getTaskId, taskDO.getId());
        List<TaskDistributorDO> distributorDOS = taskDistributorService.list(dwrapper);
        if (CollUtil.isNotEmpty(distributorDOS)) {
            List<Long> collect = distributorDOS.stream().map(TaskDistributorDO::getDistributorEid).collect(Collectors.toList());
            boolean present = collect.stream().filter(l -> l.equals(accompanyingBillDO.getDistributorEid())).findAny().isPresent();
            if (!present) {
                log.info("订单供应商不在任务设置范围内collect={}", collect.toString());
                return;
            }
        }
        LambdaQueryWrapper<TaskGoodsRelationDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(TaskGoodsRelationDO::getTaskId, userTaskDO.getTaskId());
        //任务配置商品
        List<TaskGoodsRelationDO> relations = taskGoodsRelationService.list(lambdaQueryWrapper);
        List<Long> taskGoodsIds = relations.stream().map(TaskGoodsRelationDO::getGoodsId).collect(Collectors.toList());
        //此处并非做真正的任务订单存储只是为了复用计算任务进度逻辑
        AddTaskOrderRequest addTaskOrderRequest = new AddTaskOrderRequest();
        addTaskOrderRequest.setOrderId(accompanyingBillDO.getId()).setUserId(accompanyingBillDO.getCreateUser()).setOrderNo(accompanyingBillDO.getDocCode()).setDataFix(false);
        List<AddTaskOrderGoodsRequest> orderGoodsAddDTOList = Lists.newArrayList();
        addTaskOrderRequest.setOrderGoodsAddDTOList(orderGoodsAddDTOList);
        matchDetailDOS.forEach(matchDetailDO -> {
            if((CollUtil.isNotEmpty(taskGoodsIds)) && !taskGoodsIds.contains(matchDetailDO.getYlGoodsId())){
                log.info("商品不在此任务中taskid={},商品id={}",taskDO.getId(),matchDetailDO.getYlGoodsId());
                return;
            }
            AddTaskOrderGoodsRequest addTaskOrderGoodsRequest = new AddTaskOrderGoodsRequest();
            addTaskOrderGoodsRequest.setGoodName(matchDetailDO.getYlGoodsName()).setSpecifications(matchDetailDO.getYlGoodsSpecifications()).setGoodsId(matchDetailDO.getYlGoodsId());
            addTaskOrderGoodsRequest.setAmount(matchDetailDO.getQuantity().intValue()).setPrice(matchDetailDO.getPrice());
            orderGoodsAddDTOList.add(addTaskOrderGoodsRequest);
        });

        if(CollUtil.isEmpty(orderGoodsAddDTOList)){
            log.info("没有以岭品");
            return;
        }
        LambdaQueryWrapper<TaskAccompanyingBillDO> taWrapper = Wrappers.lambdaQuery();
        taWrapper.eq(TaskAccompanyingBillDO::getUserTaskId,userTaskDO.getId()).eq(TaskAccompanyingBillDO::getAccompanyingBillId,accompanyingBillDO.getId()).last("limit 1");
        TaskAccompanyingBillDO oldTaskAccompanyingBillDO = taskAccompanyingBillService.getOne(taWrapper);
        if(Objects.nonNull(oldTaskAccompanyingBillDO)){
            log.info("此单据已消费 docCode={}",accompanyingBillDO.getDocCode());
            return;
        }
        taskOrderService.computeTaskOrder(taskGoodsIds, addTaskOrderRequest, taskDO, userTaskDO);
        TaskAccompanyingBillDO taskAccompanyingBillDO = new TaskAccompanyingBillDO();
        taskAccompanyingBillDO.setAccompanyingBillId(accompanyingBillId).setDocCode(accompanyingBillDO.getDocCode()).setTaskId(taskDO.getId()).setUserTaskId(userTaskDO.getId());
        this.save(taskAccompanyingBillDO);
    }
}
