package com.yiling.dataflow.flowcollect.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectHeartSummaryStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsRequest;

import java.util.List;

public interface FlowCollectSummaryStatisticsApi {
    Page<FlowDayHeartStatisticsBO> page(QueryDayCollectStatisticsRequest request);

    List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds);

    List<FlowCollectHeartSummaryStatisticsDTO> findListByCrmEnterpriseIds(List<Long> crmEnterpriseIds);

    Long create(SaveFlowCollectHeartSummaryStatisticsRequest request);

    boolean updateBatch(List<SaveFlowCollectHeartSummaryStatisticsRequest> requestList);

    boolean deleteByIds(List<Long> ids);

    List<Long> findCrmList();

    boolean deleteDetailByFchIds(List<Long> fchIds);

    boolean saveBatchDetail(List<SaveFlowCollectHeartSummaryStatisticsDetailRequest> requests);

}
