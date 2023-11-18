package com.yiling.dataflow.flowcollect.api.impl;

import java.util.List;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowCollectHeartStatisticsApi;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectHeartStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsRequest;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartStatisticsDetailService;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartStatisticsService;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@Slf4j
public class FlowCollectHeartStatisticsApiImpl implements FlowCollectHeartStatisticsApi {
    @Resource
    private FlowCollectHeartStatisticsService       flowCollectHeartStatisticsService;
    @Resource
    private FlowCollectHeartStatisticsDetailService flowCollectHeartStatisticsDetailService;

    @Override
    public Page<FlowDayHeartStatisticsBO> page(QueryDayCollectStatisticsRequest request) {
        return flowCollectHeartStatisticsService.pageList(request);
    }

    @Override
    public List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds) {
        return flowCollectHeartStatisticsDetailService.listDetailsByFchsIds(flowIds);
    }

    @Override
    public List<FlowCollectHeartStatisticsDTO> findListByCrmEnterpriseIds(List<Long> crmEnterpriseIds) {
        return PojoUtils.map(flowCollectHeartStatisticsService.findListByCrmEnterpriseIds(crmEnterpriseIds), FlowCollectHeartStatisticsDTO.class);
    }

    @Override
    public Long create(SaveFlowCollectHeartStatisticsRequest request) {
        return flowCollectHeartStatisticsService.create(request);
    }

    @Override
    public boolean updateBatch(List<SaveFlowCollectHeartStatisticsRequest> requestList) {
        return flowCollectHeartStatisticsService.updateBatch(requestList);
    }

    @Override
    public boolean deleteDetailByFchIds(List<Long> fchIds) {
        return flowCollectHeartStatisticsDetailService.deleteDetailByFchIds(fchIds);
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        return flowCollectHeartStatisticsService.deleteByIds(ids);
    }

    @Override
    public boolean saveBatchDetail(List<SaveFlowCollectHeartStatisticsDetailRequest> requests) {
        return flowCollectHeartStatisticsDetailService.saveBatchDetail(requests);
    }

    @Override
    public List<Long> findCrmList() {
        return flowCollectHeartStatisticsService.findCrmList();
    }
}
