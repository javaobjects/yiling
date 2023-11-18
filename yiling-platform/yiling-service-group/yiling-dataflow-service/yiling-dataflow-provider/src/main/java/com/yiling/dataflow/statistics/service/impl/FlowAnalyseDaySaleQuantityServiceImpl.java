package com.yiling.dataflow.statistics.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.bo.PeriodDaySaleQuantityBO;
import com.yiling.dataflow.statistics.dao.FlowAnalyseDaySaleQuantityMapper;
import com.yiling.dataflow.statistics.dto.StockForecastSaleIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDaySaleQuantityDO;
import com.yiling.dataflow.statistics.service.FlowAnalyseDaySaleQuantityService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-12-30
 */
@Service
public class FlowAnalyseDaySaleQuantityServiceImpl extends BaseServiceImpl<FlowAnalyseDaySaleQuantityMapper, FlowAnalyseDaySaleQuantityDO> implements FlowAnalyseDaySaleQuantityService {


    @Override
    public List<PeriodDaySaleQuantityBO> getPeriodDaySaleQuantity(StockForecastInfoRequest request, List<Long> crmIds) {
        //获取日期数据
        List<PeriodDaySaleQuantityBO> stockForastDayList = null;
        if (Objects.isNull(request.getStockForastDayList())) {
            stockForastDayList = baseMapper.getStockForastDay(request);
        } else {
            //定时任务批量处理
            stockForastDayList = request.getStockForastDayList();
        }

        // 获取时间段销售数据
        List<PeriodDaySaleQuantityBO> periodDaySaleQuantity = baseMapper.getPeriodDaySaleQuantity(request, crmIds);
        //日期数据和销售数据合并
        Map<String, PeriodDaySaleQuantityBO> periodDaySaleQuantityMap = periodDaySaleQuantity.stream().collect(Collectors.toMap(PeriodDaySaleQuantityBO::getDateTime, bo -> bo));
        stockForastDayList.stream().forEach(bo -> {
            if (periodDaySaleQuantityMap.get(bo.getDateTime()) != null) {
                //对象复制操作
                PojoUtils.map(periodDaySaleQuantityMap.get(bo.getDateTime()), bo);
            }
        });
        return stockForastDayList;
    }

    private String convertSaleQuantityBOMapKey(PeriodDaySaleQuantityBO bo) {
        return bo.getDateTime();
    }

    @Override
    public Long getAvg90SaleQuantity(StockForecastInfoRequest request, List<Long> crmIds) {
        return baseMapper.getAvg90SaleQuantity(request, crmIds);
    }

    @Override
    public List<StockForecastSaleIconDTO> getSaleData(StockForecastInfoRequest request, List<Long> crmIds) {
        request.setReferenceTime(Math.max(request.getReferenceTime1(), request.getReferenceTime2()));
        return baseMapper.getSaleData(request, crmIds);
    }

    @Override
    public int insertBatchFlowAnalyseDaySaleQuantity(List<FlowAnalyseDaySaleQuantityDO> list) {
        List<List<FlowAnalyseDaySaleQuantityDO>> lists = Lists.partition(list, 10000);
        for (List<FlowAnalyseDaySaleQuantityDO> newList : lists) {
            baseMapper.insertBatchSomeColumn(newList);
        }
        return 1;
    }

    @Override
    public List<DaySaleStatisticsBO> selectAvgSaleByEidsAndSpecIds(List<Long> cEIds, List<Long> specIds, Integer day) {
        return baseMapper.selectAvgSaleByEidsAndSpecIds(cEIds, specIds, day);
    }
}
