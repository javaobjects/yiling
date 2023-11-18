package com.yiling.settlement.report.service.impl;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.reflect.TypeToken;
import com.yiling.dataflow.relation.api.FlowGoodsRelationEditTaskApi;
import com.yiling.dataflow.relation.dto.FlowGoodsRelationEditTaskDTO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskSyncStatusEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.dao.ReportSyncTaskMapper;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.request.QuerySyncTaskPageListRequest;
import com.yiling.settlement.report.dto.request.ReportSyncTaskDTO;
import com.yiling.settlement.report.entity.ReportSyncTaskDO;
import com.yiling.settlement.report.enums.ReportStatusEnum;
import com.yiling.settlement.report.enums.ReportSyncTaskEnum;
import com.yiling.settlement.report.enums.ReportSyncTaskStatusEnum;
import com.yiling.settlement.report.service.FlowSaleOrderSyncService;
import com.yiling.settlement.report.service.ReportSyncTaskService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 返利报表同步任务表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-10-13
 */
@Slf4j
@Service
public class ReportSyncTaskServiceImpl extends BaseServiceImpl<ReportSyncTaskMapper, ReportSyncTaskDO> implements ReportSyncTaskService {

    @Autowired
    FlowSaleOrderSyncService flowSaleOrderSyncService;

    @DubboReference
    FlowGoodsRelationEditTaskApi flowGoodsRelationEditTaskApi;


    @Override
    public void addSyncTask(String syncData, ReportSyncTaskEnum syncTaskEnum) {
        ReportSyncTaskDO syncTaskDO=new ReportSyncTaskDO();
        syncTaskDO.setSyncData(syncData);
        syncTaskDO.setType(syncTaskEnum.getCode());
        syncTaskDO.setOpTime(new Date());
        boolean isSave = save(syncTaskDO);
        if (!isSave){
            log.error("保存报表同步任务失败，参数={}",syncTaskDO);
        }
    }

    @Override
    public Page<ReportSyncTaskDTO> querySyncPageList(QuerySyncTaskPageListRequest request) {
        LambdaQueryWrapper<ReportSyncTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(request.getType())&&ObjectUtil.notEqual(request.getType(),0),ReportSyncTaskDO::getType,request.getType());
        wrapper.in(CollUtil.isNotEmpty(request.getStatus()),ReportSyncTaskDO::getStatus,request.getStatus());
        Page<ReportSyncTaskDO> page = baseMapper.selectPage(request.getPage(), wrapper);
        return PojoUtils.map(page,ReportSyncTaskDTO.class);
    }

    @Override
    public void syncFlowOrder() {
        //分页查询同步失败或未同步的任务
        int current = 1;
        Page<ReportSyncTaskDTO> orderPage;
        QuerySyncTaskPageListRequest queryRequest = new QuerySyncTaskPageListRequest();
        queryRequest.setStatus(ListUtil.toList(ReportSyncTaskStatusEnum.UN_SYNC.getCode(),ReportSyncTaskStatusEnum.FAIL.getCode()));

        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(50);
            //分页查询符合结算条件的订单
            orderPage = querySyncPageList(queryRequest);
            if (CollUtil.isEmpty(orderPage.getRecords())) {
                break;
            }
            List<ReportSyncTaskDTO> records = orderPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                //查询流向订单同步任务
                List<FlowGoodsRelationEditTaskDTO> relationEditTaskList = flowGoodsRelationEditTaskApi.getByIdList(records.stream().map(e -> {
                    String syncData = e.getSyncData();
                    return Long.valueOf(syncData);
                }).distinct().collect(Collectors.toList()));
                Map<Long, FlowGoodsRelationEditTaskDTO> taskDTOMap = relationEditTaskList.stream().collect(Collectors.toMap(FlowGoodsRelationEditTaskDTO::getId, e -> e));
                Type type = new TypeToken<List<Long>>() {
                }.getType();

                for (ReportSyncTaskDTO orderSync : records) {
                    //同步流向订单
                    String flowRelationTaskId = orderSync.getSyncData();
                    FlowGoodsRelationEditTaskDTO taskDTO = taskDTOMap.get(Long.valueOf(flowRelationTaskId));
                    List<Long> list = JSONObject.parseObject(taskDTO.getSyncMsg(), type);
                    Boolean isSuccess = flowSaleOrderSyncService.syncFlowOrder(list);

                    ReportSyncTaskDO syncTaskDO=new ReportSyncTaskDO();
                    syncTaskDO.setId(orderSync.getId());
                    syncTaskDO.setOpTime(new Date());
                    if (!isSuccess){
                        syncTaskDO.setStatus(ReportSyncTaskStatusEnum.SUCCESS.getCode());
                        boolean isUpdate = updateById(syncTaskDO);
                        if (!isUpdate){
                            log.error("定时执行同步流向订单时，同步订单失败后更新报表同步任务失败，参数={}",syncTaskDO);
                        }
                        continue;
                    }
                    syncTaskDO.setStatus(ReportSyncTaskStatusEnum.SUCCESS.getCode());
                    //更新修改关系任务同步任务状态
                    Integer row = flowGoodsRelationEditTaskApi.updateSyncStatusByIdList(ListUtil.toList(taskDTO.getId()), FlowGoodsRelationEditTaskSyncStatusEnum.DONE.getCode());
                    if (row<=0){
                        log.error("同步流向订单成功后，更新同步任务状态失败，参数={}",ListUtil.toList(taskDTO.getId()));
                    }
                    //更新报表同步任务状态
                    boolean isUpdate = updateById(syncTaskDO);
                    if (!isUpdate){
                        log.error("定时执行同步流向订单时，同步订单成功后更新报表同步任务失败，参数={}",syncTaskDO);
                    }
                }
            }
            current = current + 1;
        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));
    }
}
