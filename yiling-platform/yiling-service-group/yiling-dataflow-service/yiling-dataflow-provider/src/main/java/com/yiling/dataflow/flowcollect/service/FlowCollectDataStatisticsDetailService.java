package com.yiling.dataflow.flowcollect.service;

import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectDataStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectHeartStatisticsDetailRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectDataStatisticsDetailDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 日流向数据统计明细表 服务类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
public interface FlowCollectDataStatisticsDetailService extends BaseService<FlowCollectDataStatisticsDetailDO> {

    List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(List<Long> flowIds);

    boolean deleteDetailByFchIds(List<Long> fcdIds);

    boolean saveBatchDetail(List<SaveFlowCollectDataStatisticsDetailRequest> requests);
}
