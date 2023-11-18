package com.yiling.dataflow.statistics.service;

import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.bo.PeriodDaySaleQuantityBO;
import com.yiling.dataflow.statistics.dto.StockForecastSaleIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDaySaleQuantityDO;
import com.yiling.framework.common.base.BaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-12-30
 */
public interface FlowAnalyseDaySaleQuantityService extends BaseService<FlowAnalyseDaySaleQuantityDO> {

    public List<PeriodDaySaleQuantityBO> getPeriodDaySaleQuantity(StockForecastInfoRequest request, List<Long> crmIds);

    Long getAvg90SaleQuantity(StockForecastInfoRequest request, List<Long> crmIds);

    List<StockForecastSaleIconDTO> getSaleData(StockForecastInfoRequest request, List<Long> crmIds);

    int insertBatchFlowAnalyseDaySaleQuantity(List<FlowAnalyseDaySaleQuantityDO> list);

    List<DaySaleStatisticsBO> selectAvgSaleByEidsAndSpecIds(List<Long> cEIds,  List<Long> specIds, Integer day);

}
