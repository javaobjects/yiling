package com.yiling.dataflow.statistics.api;

import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.dto.StockForecastInfoDTO;
import com.yiling.dataflow.statistics.dto.StockForecastSaleIconDTO;
import com.yiling.dataflow.statistics.dto.StockReferenceTimeDTO;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.dto.request.StockReferenceTimeRequest;

import java.util.List;
import java.util.Map;

/**
 * 库存预警数据分析
 */
public interface FlowAnalyseStockForecastApi {
    /**
     * 库存预警详情数据
     *
     * @param request
     * @return
     */
    StockForecastInfoDTO stockForecastInfoVo(StockForecastInfoRequest request);

    /**
     * 库存预警销售数据
     *
     * @param request
     * @return
     */
    List<StockForecastSaleIconDTO> getSaleData(StockForecastInfoRequest request);

    /**
     * 获取参考时间的内的库存详情数据
     *
     * @param request
     * @return
     */
    StockReferenceTimeDTO getStockReferenceTimeInfoByParam(StockReferenceTimeRequest request);



}
