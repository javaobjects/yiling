package com.yiling.dataflow.flow.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.flow.api.FlowMonthBiJobApi;
import com.yiling.dataflow.flow.dto.FlowMonthBiJobDTO;
import com.yiling.dataflow.flow.entity.FlowMonthBiJobDO;
import com.yiling.dataflow.flow.service.FlowMonthBiJobService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/8/5
 */
@DubboService
@Slf4j
public class FlowMonthBiJobApiImpl implements FlowMonthBiJobApi {

    @Autowired
    private FlowMonthBiJobService flowMonthBiJobService;

    @Override
    public List<FlowMonthBiJobDTO> listByDate(Date date) {
        QueryWrapper<FlowMonthBiJobDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowMonthBiJobDO::getTaskTime, date);
        List<FlowMonthBiJobDO> flowBiJobDOList = flowMonthBiJobService.list(queryWrapper);
        return PojoUtils.map(flowBiJobDOList, FlowMonthBiJobDTO.class);
    }

    @Override
    public Long save(FlowMonthBiJobDTO flowMonthBiJobDTO) {
        FlowMonthBiJobDO flowMonthBiJobDO = PojoUtils.map(flowMonthBiJobDTO, FlowMonthBiJobDO.class);
        Date opTime = flowMonthBiJobDO.getOpTime() != null ? flowMonthBiJobDO.getOpTime() : new Date();
        Long opUserId = flowMonthBiJobDO.getOpUserId() != null ? flowMonthBiJobDO.getOpUserId() : 0L;
        flowMonthBiJobDO.setOpTime(opTime);
        flowMonthBiJobDO.setOpUserId(opUserId);
        flowMonthBiJobService.save(flowMonthBiJobDO);
        return flowMonthBiJobDO.getId();
    }
}
