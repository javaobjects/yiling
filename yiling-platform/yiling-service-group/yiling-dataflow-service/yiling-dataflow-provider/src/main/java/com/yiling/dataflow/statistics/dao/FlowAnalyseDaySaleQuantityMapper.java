package com.yiling.dataflow.statistics.dao;

import java.util.List;

import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.bo.PeriodDaySaleQuantityBO;
import com.yiling.dataflow.statistics.dto.StockForecastSaleIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDaySaleQuantityDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-12-30
 */
@Repository
public interface FlowAnalyseDaySaleQuantityMapper extends BaseMapper<FlowAnalyseDaySaleQuantityDO> {
    Integer deleteDaySaleQuantity();
    /**
     * 平均销量
     * @param eids
     * @param specIds
     * @param day
     * @return
     */
    List<DaySaleStatisticsBO> selectAvgSaleByEidsAndSpecIds(@Param("cEIds") List<Long> cEIds, @Param("specIds") List<Long> specIds, @Param("day") Integer day);
//
//    /**
//     * 获取当前库存
//     * @param eids
//     * @param specIds
//     * @return
//     */
//    List<DaySaleStatisticsBO> selectCurrentStockByEidsAndSpecIds(@Param("eids") List<Long> eids, @Param("specIds") List<Long> specIds);
//
//    /**
//     * 获取安全库存
//     * @param eids
//     * @param specIds
//     * @return
//     */
//    List<DaySaleStatisticsBO> selectSafetyStockByEidsAndSpecIds(@Param("eids") List<Long> eids, @Param("specIds") List<Long> specIds);

    /**
     * 90天以内销量统计
     */
    List<DaySaleStatisticsBO> selectDaySaleQuantityByEidsAndSpecIds(@Param("eids") List<Long> eids, @Param("specIds") List<Long> specIds);

    List<PeriodDaySaleQuantityBO> getPeriodDaySaleQuantity( @Param("request") StockForecastInfoRequest request,@Param("crmIds") List<Long> crmIds);
    List<PeriodDaySaleQuantityBO> getStockForastDay( StockForecastInfoRequest request);
    Long getAvg90SaleQuantity(@Param("request")StockForecastInfoRequest request,@Param("crmIds") List<Long> crmIds);

    List<StockForecastSaleIconDTO> getSaleData(@Param("request")StockForecastInfoRequest request,@Param("crmIds") List<Long> crmIds);
}
