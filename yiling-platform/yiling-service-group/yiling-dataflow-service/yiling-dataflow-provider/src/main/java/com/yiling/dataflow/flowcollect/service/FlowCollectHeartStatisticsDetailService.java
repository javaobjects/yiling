package com.yiling.dataflow.flowcollect.service;

import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartStatisticsDetailDO;
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
public interface FlowCollectHeartStatisticsDetailService extends BaseService<FlowCollectHeartStatisticsDetailDO> {

    List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds);

    boolean deleteDetailByFchIds(List<Long> fchIds);

    boolean saveBatchDetail(List<SaveFlowCollectHeartStatisticsDetailRequest> requests);
}
