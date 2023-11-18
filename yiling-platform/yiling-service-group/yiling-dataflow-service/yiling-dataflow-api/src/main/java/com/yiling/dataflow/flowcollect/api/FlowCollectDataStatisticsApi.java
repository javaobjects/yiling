package com.yiling.dataflow.flowcollect.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectDataStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsRequest;

import java.util.List;

public interface FlowCollectDataStatisticsApi {
    Page<FlowDayHeartStatisticsBO> page(QueryDayCollectStatisticsRequest request);

    List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds);

    List<FlowCollectDataStatisticsDTO> findListByCrmEnterpriseIds(List<Long> ids);

    Long create(SaveFlowCollectDataStatisticsRequest request);

    boolean updateBatch(List<SaveFlowCollectDataStatisticsRequest> requestList);

    boolean deleteDetailByFchIds(List<Long> fcdIds);

    boolean deleteByIds(List<Long> ids);

    boolean saveBatchDetail(List<SaveFlowCollectDataStatisticsDetailRequest> requests);

    List<Long> findCrmList();
}
