package com.yiling.dataflow.statistics.dao;

import java.util.List;
import java.util.Map;

import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.bo.PeriodDayStockQuantityBO;
import com.yiling.dataflow.statistics.dto.StockForecastIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDayStockQuantityDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-12-30
 */
@Repository
public interface FlowAnalyseDayStockQuantityMapper extends BaseMapper<FlowAnalyseDayStockQuantityDO> {
    Integer deleteDayStockQuantity();

    List<DaySaleStatisticsBO> selectDayStockQuantityByEidsAndSpecIds(@Param("eids") List<Long> eids, @Param("specIds") List<Long> specIds);

    List<PeriodDayStockQuantityBO> getPeriodDayStockQuantity(@Param("request") StockForecastInfoRequest request, @Param("eids") List<Long> eidList);

    Long safeStockQuantity(@Param("request") StockForecastInfoRequest referenceTime1Request, @Param("crmIds") List<Long> crmIds);

    Long getCurStockQuantity(@Param("request") StockForecastInfoRequest request, @Param("crmIds") List<Long> crmIds);

    Long getCurStock2Quantity(@Param("request") StockForecastInfoRequest request, @Param("crmIds") List<Long> crmIds);

    List<DaySaleStatisticsBO> getCurStockQuantityBatch(@Param("specificationIds") List<Long> specificationIdList,@Param("cEIds") List<Long> cEIds);

    List<DaySaleStatisticsBO> getCurStock2QuantityBatch(@Param("specificationIds") List<Long> specificationIdList,@Param("cEIds") List<Long> cEIds);

    List<StockForecastIconDTO> getNear10StockQuantity(@Param("request") StockForecastInfoRequest request, @Param("crmIds") List<Long> crmIds);

    List<DaySaleStatisticsBO> getSafeStockQuantityBatch(@Param("cEIds") List<Long> cEIds, @Param("specificationIds") List<Long> specificationIdList);
}
