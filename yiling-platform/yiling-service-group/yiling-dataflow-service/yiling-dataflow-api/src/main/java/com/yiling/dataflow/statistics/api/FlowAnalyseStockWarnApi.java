package com.yiling.dataflow.statistics.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.dto.StockWarnDTO;
import com.yiling.dataflow.statistics.dto.StockWarnIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockWarnIconRequest;
import com.yiling.dataflow.statistics.dto.request.StockWarnPageRequest;

public interface FlowAnalyseStockWarnApi {
    Page<StockWarnDTO> getStockWarnPage(StockWarnPageRequest request);
    Page<StockWarnIconDTO> getSaleDaysIconWarn(StockWarnIconRequest request);
}
