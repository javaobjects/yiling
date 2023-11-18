package com.yiling.dataflow.flow.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.flow.api.FlowBiJobApi;
import com.yiling.dataflow.flow.dto.FlowBiJobDTO;
import com.yiling.dataflow.flow.entity.FlowBiJobDO;
import com.yiling.dataflow.flow.service.FlowBiJobService;
import com.yiling.dataflow.flow.service.FlowBiTaskService;
import com.yiling.dataflow.flow.service.FlowMonthBiTaskService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/4/7
 */
@DubboService
@Slf4j
public class FlowBiJobApiImpl implements FlowBiJobApi {

    @Autowired
    private FlowBiJobService flowBiJobService;

    @Autowired
    private FlowBiTaskService flowBiTaskService;

    @Autowired
    private FlowMonthBiTaskService flowMonthBiTaskService;


    @Override
    public void excelFlowBiTask() {
        flowBiTaskService.excelFlowBiTask();
    }


    @Override
    public void excelMonthFlowBiTask() {
        flowMonthBiTaskService.excelMonthFlowBiTask();
    }

    @Override
    public List<FlowBiJobDTO> listByDate(Date date) {
        QueryWrapper<FlowBiJobDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowBiJobDO::getTaskTime, date);
        List<FlowBiJobDO> flowBiJobDOList = flowBiJobService.list(queryWrapper);
        return PojoUtils.map(flowBiJobDOList, FlowBiJobDTO.class);
    }

    @Override
    public Long save(FlowBiJobDTO flowBiJobDTO) {
        FlowBiJobDO flowBiJobDO = PojoUtils.map(flowBiJobDTO, FlowBiJobDO.class);
        Date opTime = flowBiJobDO.getOpTime() != null ? flowBiJobDO.getOpTime() : new Date();
        Long opUserId = flowBiJobDO.getOpUserId() != null ? flowBiJobDO.getOpUserId() : 0L;
        flowBiJobDO.setOpTime(opTime);
        flowBiJobDO.setOpUserId(opUserId);
        flowBiJobService.save(flowBiJobDO);
        return flowBiJobDO.getId();
    }

}
