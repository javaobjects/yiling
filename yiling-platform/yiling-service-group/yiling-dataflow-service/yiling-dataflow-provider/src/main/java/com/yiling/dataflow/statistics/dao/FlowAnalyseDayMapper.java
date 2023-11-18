package com.yiling.dataflow.statistics.dao;

import com.yiling.dataflow.statistics.entity.FlowAnalyseDayDO;
import com.yiling.framework.common.base.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author handy
 * @date 2023-01-10
 */
@Repository
public interface FlowAnalyseDayMapper extends BaseMapper<FlowAnalyseDayDO> {

    Integer selectByCurDay();
}
