package com.yiling.dataflow.flowcollect.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectDataStatisticsDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectDataStatisticsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 日流向数据统计表 服务类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
public interface FlowCollectDataStatisticsService extends BaseService<FlowCollectDataStatisticsDO> {
    Page<FlowDayHeartStatisticsBO> pageList(QueryDayCollectStatisticsRequest request);
    List<FlowCollectDataStatisticsDO> findListByCrmEnterpriseIds(List<Long> ids);
    boolean deleteByIds(List<Long> ids);
    List<Long> findCrmList();
    Long create(SaveFlowCollectDataStatisticsRequest request);
    boolean updateBatch(List<SaveFlowCollectDataStatisticsRequest> requestList);
}
