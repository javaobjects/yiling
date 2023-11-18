package com.yiling.dataflow.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.dto.StockWarnDTO;
import com.yiling.dataflow.statistics.dto.StockWarnIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockWarnIconRequest;
import com.yiling.dataflow.statistics.dto.request.StockWarnPageRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseCalculationResultDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 天数计算结果表 服务类
 * </p>
 *
 * @author handy
 * @date 2023-01-11
 */
public interface FlowAnalyseCalculationResultService extends BaseService<FlowAnalyseCalculationResultDO> {

    Page<StockWarnIconDTO> getSaleDaysIconWarn(StockWarnIconRequest request);
    List<StockWarnDTO> getSaleDaysIconWarnList(StockWarnPageRequest request, List<String> specificationIds);
    Page<StockWarnDTO> getStockWarnPage(StockWarnPageRequest request);
    int insertBatchFlowAnalyseCalculationResult(List<FlowAnalyseCalculationResultDO> list);
    int deleteFlowAnalyseCalculationResult();
}
