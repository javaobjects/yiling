package com.yiling.dataflow.flowcollect.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowCollectDataStatisticsApi;
import com.yiling.dataflow.flowcollect.api.FlowCollectHeartStatisticsApi;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectDataStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsRequest;
import com.yiling.dataflow.flowcollect.service.FlowCollectDataStatisticsDetailService;
import com.yiling.dataflow.flowcollect.service.FlowCollectDataStatisticsService;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartStatisticsDetailService;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartStatisticsService;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService
@Slf4j
public class FlowCollectDataStatisticsApiImpl implements FlowCollectDataStatisticsApi {
    @Resource
    private FlowCollectDataStatisticsService flowCollectDataStatisticsService;
    @Resource
    private FlowCollectDataStatisticsDetailService flowCollectDataStatisticsDetailService;

    @Override
    public Page<FlowDayHeartStatisticsBO> page(QueryDayCollectStatisticsRequest request) {
        return flowCollectDataStatisticsService.pageList(request);
    }

    @Override
    public List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds) {
        return flowCollectDataStatisticsDetailService.listDetailsByFchsIds(flowIds);
    }

    @Override
    public List<FlowCollectDataStatisticsDTO> findListByCrmEnterpriseIds(List<Long> ids) {
        return PojoUtils.map(flowCollectDataStatisticsService.findListByCrmEnterpriseIds(ids),FlowCollectDataStatisticsDTO.class);
    }

    @Override
    public Long create(SaveFlowCollectDataStatisticsRequest request) {
        return flowCollectDataStatisticsService.create(request);
    }

    @Override
    public boolean updateBatch(List<SaveFlowCollectDataStatisticsRequest> requestList) {
        return flowCollectDataStatisticsService.updateBatch(requestList);
    }

    @Override
    public boolean deleteDetailByFchIds(List<Long> fcdIds) {
        return flowCollectDataStatisticsDetailService.deleteDetailByFchIds(fcdIds);
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        return flowCollectDataStatisticsService.deleteByIds(ids);
    }

    @Override
    public boolean saveBatchDetail(List<SaveFlowCollectDataStatisticsDetailRequest> requests) {
        return flowCollectDataStatisticsDetailService.saveBatchDetail(requests);
    }

    @Override
    public List<Long> findCrmList() {
        return flowCollectDataStatisticsService.findCrmList();
    }
}
