package com.yiling.dataflow.statistics.service.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.bo.PeriodDayStockQuantityBO;
import com.yiling.dataflow.statistics.dao.FlowAnalyseDayStockQuantityMapper;
import com.yiling.dataflow.statistics.dto.StockForecastIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDayStockQuantityDO;
import com.yiling.dataflow.statistics.service.FlowAnalyseDayStockQuantityService;
import com.yiling.framework.common.base.BaseServiceImpl;
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
public class FlowAnalyseDayStockQuantityServiceImpl extends BaseServiceImpl<FlowAnalyseDayStockQuantityMapper, FlowAnalyseDayStockQuantityDO> implements FlowAnalyseDayStockQuantityService {

    @Override
    public List<PeriodDayStockQuantityBO> getPeriodDayStockQuantity(StockForecastInfoRequest request, List<Long> crmIds) {
        return baseMapper.getPeriodDayStockQuantity(request, crmIds);
    }

    @Override
    public Long safeStockQuantity(StockForecastInfoRequest referenceTime1Request, List<Long> crmIds) {
        return baseMapper.safeStockQuantity(referenceTime1Request, crmIds);
    }

    @Override
    public Long getCurStockQuantity(StockForecastInfoRequest request, List<Long> crmIds) {
        Long curStockQuantity = null;
        curStockQuantity = baseMapper.getCurStockQuantity(request, crmIds);
        //如果前一天的当前库存布存在 获取前2天的库存数量
        if (curStockQuantity == null) {
            curStockQuantity = baseMapper.getCurStock2Quantity(request, crmIds);
        }
        return curStockQuantity;
    }

    @Override
    public List<DaySaleStatisticsBO> getCurStock2QuantityBatch(List<Long> specificationIdList, List<Long> crmIds) {
        return baseMapper.getCurStock2QuantityBatch(specificationIdList, crmIds);
    }

    @Override
    public List<DaySaleStatisticsBO> getCurStockQuantityBatch(List<Long> specificationIdList, List<Long> crmIds) {
        return baseMapper.getCurStockQuantityBatch(specificationIdList, crmIds);
    }

    @Override
    public List<StockForecastIconDTO> getNear10StockQuantity(StockForecastInfoRequest request, List<Long> crmIds) {
        return baseMapper.getNear10StockQuantity(request, crmIds);
    }

    @Override
    public int insertBatchFlowAnalyseDayStockQuantity(List<FlowAnalyseDayStockQuantityDO> list) {
        List<List<FlowAnalyseDayStockQuantityDO>> lists = Lists.partition(list, 10000);
        for (List<FlowAnalyseDayStockQuantityDO> newList : lists) {
            baseMapper.insertBatchSomeColumn(newList);
        }
        return 1;
    }

    @Override
    public List<DaySaleStatisticsBO> getSafeStockQuantityBatch(List<Long> cEIds, List<Long> specificationIdList) {
        return baseMapper.getSafeStockQuantityBatch(cEIds, specificationIdList);
    }
//    public Long getPeriodDay60StockQuantity(StockForecastInfoRequest request) {
//        return baseMapper.getPeriodDay60StockQuantity(request);
//    }
}
