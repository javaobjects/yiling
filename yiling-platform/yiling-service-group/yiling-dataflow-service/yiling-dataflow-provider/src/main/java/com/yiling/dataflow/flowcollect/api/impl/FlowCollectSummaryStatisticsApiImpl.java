package com.yiling.dataflow.flowcollect.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowCollectSummaryStatisticsApi;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectHeartSummaryStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsRequest;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartStatisticsDetailService;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartStatisticsService;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartSummaryStatisticsDetailService;
import com.yiling.dataflow.flowcollect.service.FlowCollectHeartSummaryStatisticsService;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shuan
 */
@Slf4j
@DubboService
public class FlowCollectSummaryStatisticsApiImpl implements FlowCollectSummaryStatisticsApi {

    @Resource
    private FlowCollectHeartSummaryStatisticsService flowCollectHeartSummaryStatisticsService;
    @Resource
    private FlowCollectHeartSummaryStatisticsDetailService flowCollectHeartSummaryStatisticsDetailService;

    @Override
    public Page<FlowDayHeartStatisticsBO> page(QueryDayCollectStatisticsRequest request) {
        return flowCollectHeartSummaryStatisticsService.pageList(request);
    }

    @Override
    public List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds) {
        return flowCollectHeartSummaryStatisticsDetailService.listDetailsByFchsIds(flowIds);
    }

    @Override
    public List<FlowCollectHeartSummaryStatisticsDTO> findListByCrmEnterpriseIds(List<Long> crmEnterpriseIds) {
        return PojoUtils.map(flowCollectHeartSummaryStatisticsService.findListByCrmEnterpriseIds(crmEnterpriseIds),FlowCollectHeartSummaryStatisticsDTO.class);
    }

    @Override
    public Long create(SaveFlowCollectHeartSummaryStatisticsRequest request) {
        return flowCollectHeartSummaryStatisticsService.create(request);
    }

    @Override
    public boolean updateBatch(List<SaveFlowCollectHeartSummaryStatisticsRequest> requestList) {
        return flowCollectHeartSummaryStatisticsService.updateBatch(requestList);
    }

    @Override
    public boolean deleteDetailByFchIds(List<Long> fchIds) {
        return flowCollectHeartSummaryStatisticsDetailService.deleteDetailByFchIds(fchIds);
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        return flowCollectHeartSummaryStatisticsService.deleteByIds(ids);
    }

    @Override
    public boolean saveBatchDetail(List<SaveFlowCollectHeartSummaryStatisticsDetailRequest> requests) {
        return flowCollectHeartSummaryStatisticsDetailService.saveBatchDetail(requests);
    }

    @Override
    public List<Long> findCrmList() {
        return flowCollectHeartSummaryStatisticsService.findCrmList();
    }
}
