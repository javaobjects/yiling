package com.yiling.dataflow.statistics.service.impl;

import com.yiling.dataflow.statistics.entity.FlowAnalyseDayDO;
import com.yiling.dataflow.statistics.dao.FlowAnalyseDayMapper;
import com.yiling.dataflow.statistics.service.FlowAnalyseDayService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author handy
 * @date 2023-01-10
 */
@Service
public class FlowAnalyseDayServiceImpl extends BaseServiceImpl<FlowAnalyseDayMapper, FlowAnalyseDayDO> implements FlowAnalyseDayService {

    @Override
    public Integer selectByCurDay() {
        return baseMapper.selectByCurDay();
    }
}
