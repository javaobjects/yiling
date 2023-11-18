package com.yiling.dataflow.statistics.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.statistics.dao.FlowAnalyseCalculationResultMapper;
import com.yiling.dataflow.statistics.dto.StockWarnDTO;
import com.yiling.dataflow.statistics.dto.StockWarnIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockWarnIconRequest;
import com.yiling.dataflow.statistics.dto.request.StockWarnPageRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseCalculationResultDO;
import com.yiling.dataflow.statistics.service.FlowAnalyseCalculationResultService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 天数计算结果表 服务实现类
 * </p>
 *
 * @author handy
 * @date 2023-01-11
 */
@Service
public class FlowAnalyseCalculationResultServiceImpl extends BaseServiceImpl<FlowAnalyseCalculationResultMapper, FlowAnalyseCalculationResultDO> implements FlowAnalyseCalculationResultService {

    @Override
    public Page<StockWarnIconDTO> getSaleDaysIconWarn(StockWarnIconRequest request) {
        return baseMapper.getSaleDaysIconWarn(request.getPage(), request);
    }

    @Override
    public List<StockWarnDTO> getSaleDaysIconWarnList(StockWarnPageRequest request, List<String> specificationIds) {
        return baseMapper.getSaleDaysIconWarnList(request,specificationIds);
    }

    @Override
    public Page<StockWarnDTO> getStockWarnPage(StockWarnPageRequest request) {
        return baseMapper.getStockWarnPage(request.getPage(), request);
    }

    @Override
    public int insertBatchFlowAnalyseCalculationResult(List<FlowAnalyseCalculationResultDO> list) {
        List<List<FlowAnalyseCalculationResultDO>> lists = Lists.partition(list, 10000);
        for (List<FlowAnalyseCalculationResultDO> newList : lists) {
            baseMapper.insertBatchSomeColumn(newList);
        }
        return 1;
    }

    @Override
    public int deleteFlowAnalyseCalculationResult() {
        return baseMapper.deleteFlowAnalyseCalculationResult();
    }
}
