package com.yiling.dataflow.wash.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchSafePageRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchSafeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 安全库存表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-03-06
 */
public interface FlowGoodsBatchSafeService extends BaseService<FlowGoodsBatchSafeDO> {

    Page<FlowGoodsBatchSafeDO> listPage(QueryFlowGoodsBatchSafePageRequest request);


}
