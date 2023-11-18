package com.yiling.dataflow.flowcollect.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowFleeingGoodsApi;
import com.yiling.dataflow.flowcollect.dto.FlowFleeingGoodsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowFleeingGoodsRequest;
import com.yiling.dataflow.flowcollect.service.FlowFleeingGoodsService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 窜货申报上传Api
 *
 * @author: yong.zhang
 * @date: 2023/3/16 0016
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FlowFleeingGoodsApiImpl implements FlowFleeingGoodsApi {

    private final FlowFleeingGoodsService flowFleeingGoodsService;

    @Override
    public boolean saveFlowFleeingGoodsAndTask(List<SaveFlowFleeingGoodsRequest> requestList) {
        return flowFleeingGoodsService.saveFlowFleeingGoodsAndTask(requestList);
    }

    @Override
    public boolean updateTaskIdByRecordId(Long recordId, Long taskId, Long opUserId) {
        return flowFleeingGoodsService.updateTaskIdByRecordId(recordId, taskId, opUserId);
    }

    @Override
    public Page<FlowFleeingGoodsDTO> pageList(QueryFlowMonthPageRequest request) {
        return PojoUtils.map(flowFleeingGoodsService.pageList(request), FlowFleeingGoodsDTO.class);
    }
}
