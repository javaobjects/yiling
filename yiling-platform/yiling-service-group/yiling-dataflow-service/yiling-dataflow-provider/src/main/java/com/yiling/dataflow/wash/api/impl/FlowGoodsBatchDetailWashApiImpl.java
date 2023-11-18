package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowGoodsBatchDetailWashApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchDetailWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@DubboService
public class FlowGoodsBatchDetailWashApiImpl implements FlowGoodsBatchDetailWashApi {

    @Autowired
    private FlowGoodsBatchDetailWashService flowGoodsBatchDetailWashService;

    @Override
    public Page<FlowGoodsBatchDetailWashDTO> listPage(QueryFlowGoodsBatchDetailWashPageRequest request) {
        return PojoUtils.map(flowGoodsBatchDetailWashService.listPage(request), FlowGoodsBatchDetailWashDTO.class);
    }

    @Override
    public void batchInsert(List<FlowGoodsBatchDetailWashDTO> flowGoodsBatchDetailWashDTOList) {
        List<FlowGoodsBatchDetailWashDO> flowGoodsBatchDetailWashDOList = PojoUtils.map(flowGoodsBatchDetailWashDTOList, FlowGoodsBatchDetailWashDO.class);
        flowGoodsBatchDetailWashService.batchInsert(flowGoodsBatchDetailWashDOList);
    }


}
