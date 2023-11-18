package com.yiling.dataflow.statistics.service;

import com.yiling.dataflow.statistics.entity.FlowAnalyseDayDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author handy
 * @date 2023-01-10
 */
public interface FlowAnalyseDayService extends BaseService<FlowAnalyseDayDO> {

    Integer selectByCurDay();
}
