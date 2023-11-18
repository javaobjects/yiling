package com.yiling.dataflow.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockForecastApi;
import com.yiling.dataflow.statistics.api.FlowSaleAvgStatisticsApi;
import com.yiling.dataflow.statistics.bo.PeriodDaySaleQuantityBO;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.dto.StockReferenceTimeDTO;
import com.yiling.dataflow.statistics.dto.request.StockReferenceTimeRequest;
import com.yiling.dataflow.statistics.service.FlowBalanceStatisticsJobService;
import com.yiling.dataflow.statistics.service.FlowSaleAvgStatisticsService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/2/24
 */
@Slf4j
public class FlowStatisticeTest extends BaseTest {

    @Autowired
    private FlowBalanceStatisticsJobService flowBalanceStatisticsJobService;

    @Autowired
    private FlowSaleAvgStatisticsService flowSaleAvgStatisticsService;
    @Autowired
    private FlowAnalyseStockForecastApi  flowAnalyseStockForecastApi;
    @Autowired
    private FlowSaleAvgStatisticsApi  flowSaleAvgStatisticsApi;

    @Test
    public void Test1() throws Exception {
        long start=System.currentTimeMillis();
        List<ErpClientDataDTO> erpClientDataDTOList=new ArrayList<>();
        ErpClientDataDTO erpClientDataDTO=new ErpClientDataDTO();
        erpClientDataDTO.setSuId(1270L);
        erpClientDataDTO.setRkSuId(1270L);
        erpClientDataDTO.setClientName("浙江英特药业有限责任公司");
        erpClientDataDTO.setInstallEmployee("郭慕蓉");
        erpClientDataDTO.setFlowMode(1);
        erpClientDataDTOList.add(erpClientDataDTO);
        flowBalanceStatisticsJobService.statisticsFlowBalanceJob(null,erpClientDataDTOList);
        log.info("耗时时间：{}",System.currentTimeMillis()-start);
    }

    @Test
    public void flowSaleAvgStatisticsServiceTest(){
        List<PeriodDaySaleQuantityBO> stockForastDayList=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PeriodDaySaleQuantityBO bo=new PeriodDaySaleQuantityBO();
            bo.setDateTime(DateUtil.format(DateUtil.offsetDay(new Date(),-3+i),"yyyy-MM-dd"));
            bo.setSoQuantity(0l);
            bo.setSpecificationId(61998L);
            stockForastDayList.add(bo);
        }
        StockReferenceTimeRequest stockReferenceTimeRequest3 = new StockReferenceTimeRequest();
        stockReferenceTimeRequest3.setEid(27L);
        stockReferenceTimeRequest3.setSpecificationId(61998L);
        stockReferenceTimeRequest3.setReferenceTime(3L);
        stockReferenceTimeRequest3.setReplenishDays(15L);
        stockReferenceTimeRequest3.setStockForastDayList(stockForastDayList);
        StockReferenceTimeDTO stockReferenceTimeInfo3DTO= flowAnalyseStockForecastApi.getStockReferenceTimeInfoByParam(stockReferenceTimeRequest3);
        System.out.println(stockReferenceTimeInfo3DTO);
    }

    @Test
    public void flowSaleAvgStatisticsServiceTest1(){
        flowSaleAvgStatisticsApi.saleAvg(null);
    }
}
