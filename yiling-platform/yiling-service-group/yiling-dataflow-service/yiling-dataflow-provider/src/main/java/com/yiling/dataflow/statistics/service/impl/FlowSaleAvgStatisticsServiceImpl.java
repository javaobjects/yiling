package com.yiling.dataflow.statistics.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockForecastApi;
import com.yiling.dataflow.statistics.bo.DaySaleStatisticsBO;
import com.yiling.dataflow.statistics.dao.FlowAnalyseDaySaleQuantityMapper;
import com.yiling.dataflow.statistics.dao.FlowAnalyseDayStockQuantityMapper;
import com.yiling.dataflow.statistics.dto.request.StockReferenceTimeRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDayDO;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDaySaleQuantityDO;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDayStockQuantityDO;
import com.yiling.dataflow.statistics.service.FlowAnalyseDaySaleQuantityService;
import com.yiling.dataflow.statistics.service.FlowAnalyseDayService;
import com.yiling.dataflow.statistics.service.FlowAnalyseDayStockQuantityService;
import com.yiling.dataflow.statistics.service.FlowSaleAvgStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: shuang.zhang
 * @date: 2022/12/29
 */
@Service
public class FlowSaleAvgStatisticsServiceImpl implements FlowSaleAvgStatisticsService {


    @Autowired
    private FlowAnalyseDaySaleQuantityMapper   flowAnalyseDaySaleQuantityMapper;
    @Autowired
    private FlowAnalyseDayStockQuantityMapper  flowAnalyseDayStockQuantityMapper;
    @Autowired
    private FlowAnalyseDaySaleQuantityService  flowAnalyseDaySaleQuantityService;
    @Autowired
    private FlowAnalyseDayStockQuantityService flowAnalyseDayStockQuantityService;

    @Resource
    private FlowAnalyseDayService              flowAnalyseDayService;

    @Override
    public void saleAvg(List<Long> cEIds, List<Long> specIds) {
        //添加当天的时间时间
        //添加每天销量情况90天以内
        flowAnalyseDayStockQuantityMapper.deleteDayStockQuantity();
        flowAnalyseDaySaleQuantityMapper.deleteDaySaleQuantity();
        //查询当天时间是否已存储
        int dayCount = flowAnalyseDayService.selectByCurDay();
        if (dayCount == 0) {
            FlowAnalyseDayDO flowAnalyseDayDO = new FlowAnalyseDayDO();
            flowAnalyseDayDO.setCreateTime(new Date());
            flowAnalyseDayDO.setDateTime(DateUtil.beginOfDay(new Date()));
            flowAnalyseDayDO.setCreateUser(1L);
            flowAnalyseDayDO.setUpdateTime(new Date());
            flowAnalyseDayService.save(flowAnalyseDayDO);
        }
        List<DaySaleStatisticsBO> daySaleStatisticsBOList = flowAnalyseDaySaleQuantityMapper.selectDaySaleQuantityByEidsAndSpecIds(cEIds,specIds);
        List<FlowAnalyseDaySaleQuantityDO> flowAnalyseDaySaleQuantityDOList = new ArrayList<>();
        for (DaySaleStatisticsBO daySaleStatisticsBO : daySaleStatisticsBOList) {
            FlowAnalyseDaySaleQuantityDO flowAnalyseDaySaleQuantityDO = new FlowAnalyseDaySaleQuantityDO();
         //   flowAnalyseDaySaleQuantityDO.setEid(daySaleStatisticsBO.getEid());
            flowAnalyseDaySaleQuantityDO.setCrmEnterpriseId(daySaleStatisticsBO.getCrmEnterpriseId());
            flowAnalyseDaySaleQuantityDO.setSpecificationId(daySaleStatisticsBO.getSpecId());
            flowAnalyseDaySaleQuantityDO.setDateTime(daySaleStatisticsBO.getDateTime());
            flowAnalyseDaySaleQuantityDO.setSoQuantity(daySaleStatisticsBO.getQuantity().longValue());
            flowAnalyseDaySaleQuantityDO.setDelFlag(0);
            flowAnalyseDaySaleQuantityDO.setCreateTime(new Date());
            flowAnalyseDaySaleQuantityDO.setCreateUser(0L);
            flowAnalyseDaySaleQuantityDO.setUpdateUser(0L);
            flowAnalyseDaySaleQuantityDO.setUpdateTime(new Date());
            flowAnalyseDaySaleQuantityDO.setRemark("");
            flowAnalyseDaySaleQuantityDOList.add(flowAnalyseDaySaleQuantityDO);
        }
        flowAnalyseDaySaleQuantityService.insertBatchFlowAnalyseDaySaleQuantity(flowAnalyseDaySaleQuantityDOList);
        //查询60天内库存信息
        List<DaySaleStatisticsBO> dayStockStatisticsBOList = flowAnalyseDayStockQuantityMapper.selectDayStockQuantityByEidsAndSpecIds(cEIds, specIds);
        List<FlowAnalyseDayStockQuantityDO> flowAnalyseDayStockQuantityDOList = new ArrayList<>();
        for (DaySaleStatisticsBO daySaleStatisticsBO : dayStockStatisticsBOList) {
            FlowAnalyseDayStockQuantityDO flowAnalyseDayStockQuantityDO = new FlowAnalyseDayStockQuantityDO();
           // flowAnalyseDayStockQuantityDO.setEid(daySaleStatisticsBO.getEid());
            flowAnalyseDayStockQuantityDO.setCrmEnterpriseId(daySaleStatisticsBO.getCrmEnterpriseId());
            flowAnalyseDayStockQuantityDO.setSpecificationId(daySaleStatisticsBO.getSpecId());
            flowAnalyseDayStockQuantityDO.setDateTime(daySaleStatisticsBO.getDateTime());
            flowAnalyseDayStockQuantityDO.setStockQuantity(daySaleStatisticsBO.getQuantity().longValue());
            flowAnalyseDayStockQuantityDO.setDelFlag(0);
            flowAnalyseDayStockQuantityDO.setCreateTime(new Date());
            flowAnalyseDayStockQuantityDO.setCreateUser(0L);
            flowAnalyseDayStockQuantityDO.setUpdateUser(0L);
            flowAnalyseDayStockQuantityDO.setUpdateTime(new Date());
            flowAnalyseDayStockQuantityDO.setRemark("");
            flowAnalyseDayStockQuantityDOList.add(flowAnalyseDayStockQuantityDO);
        }
        flowAnalyseDayStockQuantityService.insertBatchFlowAnalyseDayStockQuantity(flowAnalyseDayStockQuantityDOList);
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.beginOfDay(new Date()));
    }
}
