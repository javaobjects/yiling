package com.yiling.dataflow.flowcollect.api.impl;

import java.util.List;

import com.yiling.dataflow.flowcollect.entity.FlowMonthSalesDO;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowMonthSalesApi;
import com.yiling.dataflow.flowcollect.dto.FlowMonthSalesDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthSalesRequest;
import com.yiling.dataflow.flowcollect.service.FlowMonthSalesService;

import lombok.extern.slf4j.Slf4j;

/**
 * 月流向销售数据 API实现
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
@Slf4j
@DubboService
public class FlowMonthSalesApiImpl implements FlowMonthSalesApi {

    @Autowired
    private FlowMonthSalesService flowMonthSalesService;

    @Override
    public  boolean updateFlowMonthSalesAndTask(Long opUserId,Long recordId) {
        return flowMonthSalesService.updateFlowMonthSalesAndTask(opUserId,recordId);
    }

    @Override
    public boolean saveBatch(List<SaveFlowMonthSalesRequest> requests) {
        return flowMonthSalesService.saveBatch(PojoUtils.map(requests, FlowMonthSalesDO.class));
    }

    @Override
    public Page<FlowMonthSalesDTO> queryFlowMonthSalePage(QueryFlowMonthPageRequest request) {
        return flowMonthSalesService.queryFlowMonthSalePage(request);
    }

}
