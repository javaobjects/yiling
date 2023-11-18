package com.yiling.dataflow.check.api.impl;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.check.dto.request.FlowPurchaseCheckJobRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.check.api.FlowPurchaseCheckJobApi;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckJobDTO;
import com.yiling.dataflow.check.entity.FlowPurchaseCheckJobDO;
import com.yiling.dataflow.check.service.FlowPurchaseCheckJobService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
@DubboService
public class FlowPurchaseCheckJobApiImpl  implements FlowPurchaseCheckJobApi {

    @Autowired
    private FlowPurchaseCheckJobService flowPurchaseCheckJobService;

    @Override
    public FlowPurchaseCheckJobDTO save(FlowPurchaseCheckJobDTO flowPurchaseCheckJobDTO) {
        FlowPurchaseCheckJobDO entity = PojoUtils.map(flowPurchaseCheckJobDTO, FlowPurchaseCheckJobDO.class);
        flowPurchaseCheckJobService.save(entity);
        return PojoUtils.map(entity, FlowPurchaseCheckJobDTO.class);
    }

    @Override
    public List<FlowPurchaseCheckJobDTO> listByDate(FlowPurchaseCheckJobRequest request) {
        return PojoUtils.map(flowPurchaseCheckJobService.listByDate(request), FlowPurchaseCheckJobDTO.class);
    }

}
