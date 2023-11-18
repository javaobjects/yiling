package com.yiling.dataflow.flowcollect.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartStatisticsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 日流向心跳统计表 服务类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
public interface FlowCollectHeartStatisticsService extends BaseService<FlowCollectHeartStatisticsDO> {

    Page<FlowDayHeartStatisticsBO> pageList(QueryDayCollectStatisticsRequest request);

    List<FlowCollectHeartStatisticsDO> findListByCrmEnterpriseIds(List<Long> crmEnterpriseIds);

    Long create(SaveFlowCollectHeartStatisticsRequest request);

    boolean updateBatch(List<SaveFlowCollectHeartStatisticsRequest> requestList);

    List<Long> findCrmList();

    boolean deleteByIds(List<Long> ids);
}
