package com.yiling.dataflow.relation.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.relation.dao.FlowGoodsRelationEditTaskMapper;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsRelationEditTaskRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationEditTaskDO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskSyncStatusEnum;
import com.yiling.dataflow.relation.service.FlowGoodsRelationEditTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/10/8
 */
@Slf4j
@Service
public class FlowGoodsRelationEditTaskServiceImpl extends BaseServiceImpl<FlowGoodsRelationEditTaskMapper, FlowGoodsRelationEditTaskDO> implements FlowGoodsRelationEditTaskService {

    @Autowired
    private FlowGoodsRelationEditTaskMapper flowGoodsRelationEditTaskMapper;

    @Override
    public FlowGoodsRelationEditTaskDO getById(Long id) {
        Assert.notNull(id, "参数 id 不能为空");
        LambdaQueryWrapper<FlowGoodsRelationEditTaskDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowGoodsRelationEditTaskDO::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<FlowGoodsRelationEditTaskDO> getByIdList(List<Long> idList) {
        Assert.notEmpty(idList, "参数 idList 不能为空");
        LambdaQueryWrapper<FlowGoodsRelationEditTaskDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowGoodsRelationEditTaskDO::getId, idList);
        return this.list(queryWrapper);
    }

    @Override
    public Long saveByRequest(SaveFlowGoodsRelationEditTaskRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getBusinessType(), "参数 businessType 不能为空");
        Assert.notNull(request.getFlowGoodsRelationId(), "参数 flowGoodsRelationId 不能为空");
        Assert.notNull(request.getEid(), "参数 eid 不能为空");
        Assert.notNull(request.getFlowSaleIds(), "参数 flowSaleIds 不能为空");
        // 新增修改任务记录
        FlowGoodsRelationEditTaskDO saveEntity = new FlowGoodsRelationEditTaskDO();
        saveEntity.setBusinessType(request.getBusinessType());
        saveEntity.setFlowGoodsRelationId(request.getFlowGoodsRelationId());
        saveEntity.setEid(request.getEid());
        saveEntity.setFlowSaleIds(request.getFlowSaleIds());
        saveEntity.setSyncStatus(FlowGoodsRelationEditTaskSyncStatusEnum.WAIT.getCode());
        saveEntity.setOpUserId(request.getOpUserId());
        saveEntity.setOpTime(request.getOpTime());
        this.save(saveEntity);
        return saveEntity.getId();
    }

    @Override
    public List<Long> saveByRequestList(List<SaveFlowGoodsRelationEditTaskRequest> requestList) {
        Assert.notEmpty(requestList, "参数 request 不能为空");
        List<Long> idList = new ArrayList<>();
        for (SaveFlowGoodsRelationEditTaskRequest request : requestList) {
            Long id = this.saveByRequest(request);
            idList.add(id);
        }
        return idList;
    }

    @Override
    public Boolean existFlowGoodsRelationEditTask(Long flowGoodsRelationId, Integer syncStatus) {
        Long count = flowGoodsRelationEditTaskMapper.getCountByEid(flowGoodsRelationId, syncStatus);
        if(ObjectUtil.isNotNull(count) && count > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<FlowGoodsRelationEditTaskDO> getListByEid(Long eid) {
        Assert.notNull(eid, "参数 eid 不能为空");
        LambdaQueryWrapper<FlowGoodsRelationEditTaskDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowGoodsRelationEditTaskDO::getEid, eid);
        return this.list(queryWrapper);
    }

    @Override
    public Integer updateSyncStatusByIdList(List<Long> idList, Integer syncStatus) {
        Assert.notEmpty(idList, "参数 idList 不能为空");
        Assert.notNull(syncStatus, "参数 syncStatus 不能为空");
        return this.baseMapper.updateSyncStatusByIdList(idList, syncStatus);
    }
}
