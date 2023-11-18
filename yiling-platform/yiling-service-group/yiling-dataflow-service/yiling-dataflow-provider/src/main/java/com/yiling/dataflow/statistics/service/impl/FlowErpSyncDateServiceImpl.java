package com.yiling.dataflow.statistics.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.statistics.dao.FlowErpSyncDateMapper;
import com.yiling.dataflow.statistics.dto.request.SaveFlowErpSyncDateRequest;
import com.yiling.dataflow.statistics.entity.FlowErpSyncDateDO;
import com.yiling.dataflow.statistics.service.FlowErpSyncDateService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2023/1/4
 */
@Slf4j
@Service
public class FlowErpSyncDateServiceImpl extends BaseServiceImpl<FlowErpSyncDateMapper, FlowErpSyncDateDO> implements FlowErpSyncDateService {

    @Override
    public boolean insertBatch(List<SaveFlowErpSyncDateRequest> list) {
        Assert.notEmpty(list, "参数 list 不能为空");
        list.forEach(o -> {
            Assert.notNull(o.getEid(), "参数 eid 不能为空");
            Assert.notNull(o.getTaskTime(), "参数 taskTime 不能为空");
            Assert.notNull(o.getSyncFlag(), "参数 syncFlag 不能为空");
        });
        List<FlowErpSyncDateDO> saveList = PojoUtils.map(list, FlowErpSyncDateDO.class);
        return this.saveBatch(saveList, 1000);
    }

    @Override
    public FlowErpSyncDateDO getOneByEidAndTaskTime(Long eid, String taskTime) {
        Assert.notNull(eid, "参数 eid 不能为空");
        Assert.notBlank(taskTime, "参数 taskTime 不能为空");
        LambdaQueryWrapper<FlowErpSyncDateDO> wrapper = new LambdaQueryWrapper();
        wrapper.eq(FlowErpSyncDateDO::getEid, eid);
        wrapper.eq(FlowErpSyncDateDO::getTaskTime, taskTime);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public Integer deleteByEidAndTaskTime(Long eid, String taskTime) {
        return this.baseMapper.deleteByEidAndTaskTime(eid, taskTime);
    }

    @Override
    public Integer deleteByEidListAndTaskTime(List<Long> eidList, String taskTime) {
        return this.baseMapper.deleteByEidListAndTaskTime(eidList, taskTime);
    }

    @Override
    public Date getMaxTaskTimeByEid(Long eid) {
        return this.baseMapper.getMaxTaskTimeByEid(eid);
    }
}
