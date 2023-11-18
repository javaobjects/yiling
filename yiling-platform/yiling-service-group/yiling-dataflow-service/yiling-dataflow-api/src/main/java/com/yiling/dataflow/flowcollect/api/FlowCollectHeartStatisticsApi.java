package com.yiling.dataflow.flowcollect.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectHeartStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsRequest;

import java.util.List;

public interface FlowCollectHeartStatisticsApi {
    /**
     * 日流向收集心跳统计
     * @param request
     * @return
     */
    Page<FlowDayHeartStatisticsBO> page(QueryDayCollectStatisticsRequest request);

    List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds);

    List<FlowCollectHeartStatisticsDTO> findListByCrmEnterpriseIds(List<Long> crmEnterpriseIds);

    Long create(SaveFlowCollectHeartStatisticsRequest request);

    boolean updateBatch(List<SaveFlowCollectHeartStatisticsRequest> requestList);

    boolean deleteDetailByFchIds(List<Long> fchIds);

    boolean deleteByIds(List<Long> ids);

    boolean saveBatchDetail(List<SaveFlowCollectHeartStatisticsDetailRequest> requests);

    List<Long> findCrmList();

}
