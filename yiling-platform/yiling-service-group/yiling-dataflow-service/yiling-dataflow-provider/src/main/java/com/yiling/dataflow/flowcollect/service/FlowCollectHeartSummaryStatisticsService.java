package com.yiling.dataflow.flowcollect.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartSummaryStatisticsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 日流向心跳统计汇总表 服务类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
public interface FlowCollectHeartSummaryStatisticsService extends BaseService<FlowCollectHeartSummaryStatisticsDO> {
    Page<FlowDayHeartStatisticsBO> pageList(QueryDayCollectStatisticsRequest request);
    List<FlowCollectHeartSummaryStatisticsDO> findListByCrmEnterpriseIds(List<Long> crmEnterpriseIds);
    Long create(SaveFlowCollectHeartSummaryStatisticsRequest request);
    boolean updateBatch(List<SaveFlowCollectHeartSummaryStatisticsRequest> requestList);
    boolean deleteByIds(List<Long> ids);
    List<Long> findCrmList();
}
