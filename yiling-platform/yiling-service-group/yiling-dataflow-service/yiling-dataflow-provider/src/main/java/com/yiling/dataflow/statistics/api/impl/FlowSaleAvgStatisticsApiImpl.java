package com.yiling.dataflow.statistics.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockForecastApi;
import com.yiling.dataflow.statistics.api.FlowSaleAvgStatisticsApi;
import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.bo.PeriodDaySaleQuantityBO;
import com.yiling.dataflow.statistics.dto.StockReferenceTimeDTO;
import com.yiling.dataflow.statistics.dto.request.StockReferenceTimeRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseCalculationResultDO;
import com.yiling.dataflow.statistics.service.FlowAnalyseCalculationResultService;
import com.yiling.dataflow.statistics.service.FlowAnalyseDaySaleQuantityService;
import com.yiling.dataflow.statistics.service.FlowAnalyseDayStockQuantityService;
import com.yiling.dataflow.statistics.service.FlowAnalyseGoodsService;
import com.yiling.dataflow.statistics.service.FlowDistributionEnterpriseService;
import com.yiling.dataflow.statistics.service.FlowSaleAvgStatisticsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/12/29
 */
@DubboService
public class FlowSaleAvgStatisticsApiImpl implements FlowSaleAvgStatisticsApi {

    @Autowired
    private FlowSaleAvgStatisticsService        flowSaleAvgStatisticsService;
    @Autowired
    private FlowAnalyseCalculationResultService flowAnalyseCalculationResultService;
    @Autowired
    private FlowAnalyseGoodsService             flowAnalyseGoodsService;
    @Autowired
    private FlowDistributionEnterpriseService   flowDistributionEnterpriseService;
    @Resource
    private FlowAnalyseStockForecastApi         flowAnalyseStockForecastApi;
    @Autowired
    private FlowAnalyseDayStockQuantityService  flowAnalyseDayStockQuantityService;
    @Autowired
    private FlowAnalyseDaySaleQuantityService   flowAnalyseDaySaleQuantityService;

    @Override
    public void saleAvg(List<Long> cEIds) {
        List<Long> specIds = flowAnalyseGoodsService.getSpecificationIdList();
        flowSaleAvgStatisticsService.saleAvg(cEIds, specIds);
        flowAnalyseCalculationResultService.deleteFlowAnalyseCalculationResult();
        List<DaySaleStatisticsBO> daySaleStatisticsBOList = flowAnalyseDayStockQuantityService.getSafeStockQuantityBatch(cEIds, specIds);
        Map<String, Long> map = new HashMap<>();
        for (DaySaleStatisticsBO daySaleStatisticsBO : daySaleStatisticsBOList) {
            map.put(daySaleStatisticsBO.getCrmEnterpriseId() + "-" + daySaleStatisticsBO.getSpecId(), daySaleStatisticsBO.getQuantity().longValue());
        }
        List<DaySaleStatisticsBO> daySaleStatisticsBOList1 = flowAnalyseDayStockQuantityService.getCurStockQuantityBatch(specIds, cEIds);
        Map<String, Long> map1 = new HashMap<>();
        for (DaySaleStatisticsBO daySaleStatisticsBO : daySaleStatisticsBOList1) {
            map1.put(daySaleStatisticsBO.getCrmEnterpriseId() + "-" + daySaleStatisticsBO.getSpecId(), daySaleStatisticsBO.getQuantity().longValue());
        }
        List<DaySaleStatisticsBO> daySaleStatisticsBOList2 = flowAnalyseDayStockQuantityService.getCurStock2QuantityBatch(specIds, cEIds);
        Map<String, Long> map2 = new HashMap<>();
        for (DaySaleStatisticsBO daySaleStatisticsBO : daySaleStatisticsBOList2) {
            map2.put(daySaleStatisticsBO.getCrmEnterpriseId() + "-" + daySaleStatisticsBO.getSpecId(), daySaleStatisticsBO.getQuantity().longValue());
        }
        List<DaySaleStatisticsBO> daySaleStatisticsBOList3 = flowAnalyseDaySaleQuantityService.selectAvgSaleByEidsAndSpecIds(cEIds, specIds, 3);
        Map<String, Long> map3 = new HashMap<>();
        for (DaySaleStatisticsBO daySaleStatisticsBO : daySaleStatisticsBOList3) {
            map3.put(daySaleStatisticsBO.getCrmEnterpriseId() + "-" + daySaleStatisticsBO.getSpecId(), daySaleStatisticsBO.getQuantity().longValue());
        }
        List<DaySaleStatisticsBO> daySaleStatisticsBOList30 = flowAnalyseDaySaleQuantityService.selectAvgSaleByEidsAndSpecIds(cEIds, specIds, 30);
        Map<String, Long> map4 = new HashMap<>();
        for (DaySaleStatisticsBO daySaleStatisticsBO : daySaleStatisticsBOList30) {
            map4.put(daySaleStatisticsBO.getCrmEnterpriseId() + "-" + daySaleStatisticsBO.getSpecId(), daySaleStatisticsBO.getQuantity().longValue());
        }
        for (Long specId : specIds) {
            List<FlowAnalyseCalculationResultDO> list = new ArrayList<>();
            List<PeriodDaySaleQuantityBO> stockForastDayList3 = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                PeriodDaySaleQuantityBO bo = new PeriodDaySaleQuantityBO();
                bo.setDateTime(DateUtil.format(DateUtil.offsetDay(new Date(), 3 - i), "yyyy-MM-dd"));
                bo.setSoQuantity(0L);
                bo.setSpecificationId(specId);
                stockForastDayList3.add(bo);
            }
            List<PeriodDaySaleQuantityBO> stockForastDayList30 = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                PeriodDaySaleQuantityBO bo = new PeriodDaySaleQuantityBO();
                bo.setDateTime(DateUtil.format(DateUtil.offsetDay(new Date(), 3 - i), "yyyy-MM-dd"));
                bo.setSoQuantity(0L);
                bo.setSpecificationId(specId);
                stockForastDayList30.add(bo);
            }
            for (Long ceid : cEIds) {
                StockReferenceTimeRequest stockReferenceTimeRequest3 = new StockReferenceTimeRequest();
                stockReferenceTimeRequest3.setCrmEnterpriseId(ceid);
                stockReferenceTimeRequest3.setSpecificationId(specId);
                stockReferenceTimeRequest3.setReferenceTime(3L);
                stockReferenceTimeRequest3.setReplenishDays(15L);
                stockReferenceTimeRequest3.setSafeStockQuantity(map.getOrDefault(ceid + "-" + specId, 0L));
                Long curStockQuantity = map1.getOrDefault(ceid + "-" + specId, 0L);
                if (curStockQuantity == 0) {
                    curStockQuantity = map2.getOrDefault(ceid + "-" + specId, 0L);
                }
                stockReferenceTimeRequest3.setAleSaleDays(map3.getOrDefault(ceid + "-" + specId, 0L));
                stockReferenceTimeRequest3.setCurStockQuantity(curStockQuantity);
                stockReferenceTimeRequest3.setStockForastDayList(stockForastDayList3);
                StockReferenceTimeDTO stockReferenceTimeInfo3DTO = flowAnalyseStockForecastApi.getStockReferenceTimeInfoByParam(stockReferenceTimeRequest3);
                if (stockReferenceTimeInfo3DTO != null) {
                    FlowAnalyseCalculationResultDO flowAnalyseCalculationResultDO3 = new FlowAnalyseCalculationResultDO();
                    flowAnalyseCalculationResultDO3.setCrmEnterpriseId(ceid);
                    flowAnalyseCalculationResultDO3.setSpecificationId(specId);
                    flowAnalyseCalculationResultDO3.setDay(3);
                    flowAnalyseCalculationResultDO3.setSaleAvg(stockReferenceTimeInfo3DTO.getAverageDailySales());
                    flowAnalyseCalculationResultDO3.setStockQuantity(stockReferenceTimeInfo3DTO.getCurStockQuantity());
                    flowAnalyseCalculationResultDO3.setSupplementQuantity(stockReferenceTimeInfo3DTO.getReplenishStockQuantity());
                    flowAnalyseCalculationResultDO3.setSupportDay(new BigDecimal(stockReferenceTimeInfo3DTO.getReferenceAbleSaleDays()));
                    flowAnalyseCalculationResultDO3.setDelFlag(0);
                    flowAnalyseCalculationResultDO3.setCreateTime(new Date());
                    flowAnalyseCalculationResultDO3.setCreateUser(0L);
                    flowAnalyseCalculationResultDO3.setUpdateUser(0L);
                    flowAnalyseCalculationResultDO3.setUpdateTime(new Date());
                    flowAnalyseCalculationResultDO3.setRemark("");
                    if (!isDataInvalid(flowAnalyseCalculationResultDO3)) {
                        list.add(flowAnalyseCalculationResultDO3);
                    }
                }

                StockReferenceTimeRequest stockReferenceTimeRequest30 = new StockReferenceTimeRequest();
                stockReferenceTimeRequest30.setCrmEnterpriseId(ceid);
                stockReferenceTimeRequest30.setSpecificationId(specId);
                stockReferenceTimeRequest30.setReferenceTime(30L);
                stockReferenceTimeRequest30.setReplenishDays(15L);
                stockReferenceTimeRequest30.setSafeStockQuantity(map.getOrDefault(ceid + "-" + specId, 0L));
                stockReferenceTimeRequest30.setCurStockQuantity(curStockQuantity);
                stockReferenceTimeRequest30.setAleSaleDays(map4.getOrDefault(ceid + "-" + specId, 0L));
                stockReferenceTimeRequest30.setStockForastDayList(stockForastDayList30);
                StockReferenceTimeDTO stockReferenceTimeInfo30DTO = flowAnalyseStockForecastApi.getStockReferenceTimeInfoByParam(stockReferenceTimeRequest30);
                if (stockReferenceTimeInfo30DTO != null) {
                    FlowAnalyseCalculationResultDO flowAnalyseCalculationResultDO30 = new FlowAnalyseCalculationResultDO();
                    flowAnalyseCalculationResultDO30.setCrmEnterpriseId(ceid);
                    flowAnalyseCalculationResultDO30.setSpecificationId(specId);
                    flowAnalyseCalculationResultDO30.setDay(30);
                    flowAnalyseCalculationResultDO30.setSaleAvg(stockReferenceTimeInfo30DTO.getAverageDailySales());
                    flowAnalyseCalculationResultDO30.setStockQuantity(stockReferenceTimeInfo30DTO.getCurStockQuantity());
                    flowAnalyseCalculationResultDO30.setSupplementQuantity(stockReferenceTimeInfo30DTO.getReplenishStockQuantity());
                    flowAnalyseCalculationResultDO30.setSupportDay(new BigDecimal(stockReferenceTimeInfo30DTO.getReferenceAbleSaleDays()));
                    flowAnalyseCalculationResultDO30.setDelFlag(0);
                    flowAnalyseCalculationResultDO30.setCreateTime(new Date());
                    flowAnalyseCalculationResultDO30.setCreateUser(0L);
                    flowAnalyseCalculationResultDO30.setUpdateUser(0L);
                    flowAnalyseCalculationResultDO30.setUpdateTime(new Date());
                    flowAnalyseCalculationResultDO30.setRemark("");
                    if (!isDataInvalid(flowAnalyseCalculationResultDO30)) {
                        list.add(flowAnalyseCalculationResultDO30);
                    }
                }
            }
            flowAnalyseCalculationResultService.insertBatchFlowAnalyseCalculationResult(list);
        }
    }

    /**
     * 库存预警页面，计算出的数据结果，如果近3天日均销量 、近30天日均销量、库存数量 3个数值都等于0，则此数据结果删除
     *
     * @param dto
     * @return
     */
    private static boolean isDataInvalid(FlowAnalyseCalculationResultDO dto) {
        if (Objects.isNull(dto.getSaleAvg())) {
            dto.setSaleAvg(0L);
        }
        if (Objects.isNull(dto.getStockQuantity())) {
            dto.setStockQuantity(0L);
        }
        return dto.getSaleAvg() <= 0 && dto.getStockQuantity() <= 0;
    }
}
