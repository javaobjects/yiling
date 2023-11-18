package com.yiling.dataflow.flowcollect.service;

import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartSummaryStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartSummaryStatisticsDetailDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 日流向心跳统计明细表 服务类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
public interface FlowCollectHeartSummaryStatisticsDetailService extends BaseService<FlowCollectHeartSummaryStatisticsDetailDO> {

    List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds);

    boolean deleteDetailByFchIds(List<Long> fchIds);

    boolean saveBatchDetail(List<SaveFlowCollectHeartSummaryStatisticsDetailRequest> requests);
}
