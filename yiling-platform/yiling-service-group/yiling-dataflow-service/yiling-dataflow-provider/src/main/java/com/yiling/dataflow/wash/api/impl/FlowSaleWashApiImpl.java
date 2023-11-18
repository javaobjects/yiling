package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashListRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowSaleWashApi;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.service.FlowSaleWashService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/3/2
 */
@DubboService
public class FlowSaleWashApiImpl implements FlowSaleWashApi {

    @Autowired
    private FlowSaleWashService flowSaleWashService;

    @Override
    public Page<FlowSaleWashDTO> listPage(QueryFlowSaleWashPageRequest request) {
        return PojoUtils.map(flowSaleWashService.listPage(request), FlowSaleWashDTO.class);
    }

    @Override
    public void batchInsert(List<FlowSaleWashDTO> flowSaleWashDTOList) {
        List<FlowSaleWashDO> flowSaleWashDOList = PojoUtils.map(flowSaleWashDTOList, FlowSaleWashDO.class);
        flowSaleWashService.batchInsert(flowSaleWashDOList);
    }

    @Override
    public List<FlowSaleWashDTO> getByYearMonth(QueryFlowSaleWashListRequest request) {
        return flowSaleWashService.getByYearMonth(request);
    }
}
