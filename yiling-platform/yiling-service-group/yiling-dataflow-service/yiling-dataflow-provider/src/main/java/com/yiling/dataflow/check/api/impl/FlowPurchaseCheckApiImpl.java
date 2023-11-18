package com.yiling.dataflow.check.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.check.api.FlowPurchaseCheckApi;
import com.yiling.dataflow.check.bo.FlowPurchaseSpecificationIdTotalQuantityBO;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckTaskDTO;
import com.yiling.dataflow.check.dto.request.FlowPurchaseSpecificationIdTotalQuantityRequest;
import com.yiling.dataflow.check.dto.request.QueryFlowPurchaseCheckTaskPageRequest;
import com.yiling.dataflow.check.entity.FlowPurchaseCheckTaskDO;
import com.yiling.dataflow.check.service.FlowPurchaseCheckService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
@DubboService
public class FlowPurchaseCheckApiImpl  implements FlowPurchaseCheckApi {

    @Autowired
    private FlowPurchaseCheckService flowPurchaseCheckService;

    @Override
    public List<FlowPurchaseSpecificationIdTotalQuantityBO> getSpecificationIdTotalQuantityByPoTime(FlowPurchaseSpecificationIdTotalQuantityRequest request) {
        return flowPurchaseCheckService.getSpecificationIdTotalQuantityByPoTime(request);
    }

    @Override
    public Boolean saveBatch(List<FlowPurchaseCheckTaskDTO> list) {
        return flowPurchaseCheckService.saveBatchCheck(PojoUtils.map(list, FlowPurchaseCheckTaskDO.class));
    }

    @Override
    public void flowPurchaseCheck() {
        flowPurchaseCheckService.flowPurchaseCheck();
    }

    @Override
    public Long getPurchaseExceptionCount(Date startTime, Date endTime) {
        return flowPurchaseCheckService.getPurchaseExceptionCount(startTime, endTime);
    }

    @Override
    public Page<FlowPurchaseCheckTaskDTO> getPurchaseExceptionListPage(QueryFlowPurchaseCheckTaskPageRequest request) {
        return PojoUtils.map(flowPurchaseCheckService.page(request), FlowPurchaseCheckTaskDTO.class);
    }
}
