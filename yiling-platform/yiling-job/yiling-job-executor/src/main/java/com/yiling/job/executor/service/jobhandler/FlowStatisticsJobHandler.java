package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.statistics.api.FlowSaleAvgStatisticsApi;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author: shuang.zhang
 * @date: 2022/10/28
 */
@Component
@Slf4j
public class FlowStatisticsJobHandler {

    @DubboReference(timeout = 1000 * 60 )
    private ErpClientApi             erpClientApi;
    @DubboReference(async = true)
    private FlowSaleAvgStatisticsApi flowSaleAvgStatisticsApi;

    @JobLog
    @XxlJob("flowStatisticsJobHandler")
    public ReturnT<String> flowStatisticsJobHandler(String param) throws Exception {
        log.info("job任务开始：当月无销售的企业统计任务执行");
        long start = System.currentTimeMillis();
        erpClientApi.statisticsNoFlowSaleEidList();
        log.info("job任务结束：当月无销售的企业统计任务执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("flowStatisticsDaySaleJobHandler")
    public ReturnT<String> flowStatisticsDaySaleJobHandler(String param) throws Exception {
        log.info("job任务开始：统计商业公司和商品规格每天销量");
        long start = System.currentTimeMillis();
        ErpClientQueryRequest request = new ErpClientQueryRequest();
        //List<Long> eidList = new ArrayList<>();
        List<Long> cEIds=new ArrayList<>();
        //需要循环调用
        Page<ErpClientDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(2000);
            page = erpClientApi.page(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            //eidList.addAll(page.getRecords().stream().map(e -> e.getRkSuId()).collect(Collectors.toList()));
            cEIds.addAll(page.getRecords().stream().filter(e->e.getCrmEnterpriseId()!=null&&e.getCrmEnterpriseId().longValue()>0).map(e->e.getCrmEnterpriseId()).collect(Collectors.toList()));
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        flowSaleAvgStatisticsApi.saleAvg(cEIds);
        log.info("job任务结束：统计商业公司和商品规格每天销量任务执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }
}
