package com.yiling.dataflow.check.service.impl;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.check.dto.request.FlowPurchaseCheckJobRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.check.dao.FlowPurchaseCheckJobMapper;
import com.yiling.dataflow.check.entity.FlowPurchaseCheckJobDO;
import com.yiling.dataflow.check.service.FlowPurchaseCheckJobService;
import com.yiling.framework.common.base.BaseServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
@Slf4j
@Service
public class FlowPurchaseCheckJobServiceImpl extends BaseServiceImpl<FlowPurchaseCheckJobMapper, FlowPurchaseCheckJobDO> implements FlowPurchaseCheckJobService {

    @Override
    public List<FlowPurchaseCheckJobDO> listByDate(FlowPurchaseCheckJobRequest request) {
        LambdaQueryWrapper<FlowPurchaseCheckJobDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowPurchaseCheckJobDO::getTaskTime, request.getTaskTime());
        return this.list(queryWrapper);
    }
}
