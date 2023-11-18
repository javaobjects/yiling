package com.yiling.dataflow.statistics.service;

import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.bo.PeriodDayStockQuantityBO;
import com.yiling.dataflow.statistics.dto.StockForecastIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDayStockQuantityDO;
import com.yiling.framework.common.base.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-12-30
 */
public interface FlowAnalyseDayStockQuantityService extends BaseService<FlowAnalyseDayStockQuantityDO> {

    List<PeriodDayStockQuantityBO> getPeriodDayStockQuantity(StockForecastInfoRequest request, List<Long> crmIds);

    Long safeStockQuantity(StockForecastInfoRequest referenceTime1Request, List<Long> crmIds);

    Long getCurStockQuantity(StockForecastInfoRequest request, List<Long> crmIds);

    List<DaySaleStatisticsBO> getCurStock2QuantityBatch(List<Long> specificationIdList,List<Long> crmIds);

    List<DaySaleStatisticsBO> getCurStockQuantityBatch(List<Long> specificationIdList,List<Long> crmIds);

    List<StockForecastIconDTO> getNear10StockQuantity(StockForecastInfoRequest request,List<Long> crmIds);

    int insertBatchFlowAnalyseDayStockQuantity(List<FlowAnalyseDayStockQuantityDO> list);

    List<DaySaleStatisticsBO> getSafeStockQuantityBatch(List<Long> cEIds, List<Long> specificationIdList);
}
