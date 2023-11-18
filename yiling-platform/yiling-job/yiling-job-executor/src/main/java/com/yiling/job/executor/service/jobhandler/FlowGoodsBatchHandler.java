package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowGoodsBatchDetailApi;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.job.executor.log.JobLog;

import lombok.extern.slf4j.Slf4j;

/**
 * 统计库存流向商品库存数量
 *
 * @author: houjie.sun
 * @date: 2022/6/21
 */
@Component
@Slf4j
public class FlowGoodsBatchHandler {

    @DubboReference(async = true)
    private FlowGoodsBatchApi flowGoodsBatchApi;

    @DubboReference(timeout = 5 * 1000)
    private ErpClientApi erpClientApi;

    @DubboReference(async = true)
    private FlowGoodsBatchDetailApi flowGoodsBatchDetailApi;

    @JobLog
    @XxlJob("flowGoodsBatchTotalNumberJobHandler")
    public ReturnT<String> flowGoodsBatchTotalNumberJobHandler(String param) throws Exception {
        log.info("任务开始：库存流向商品库存数量统计任务执行");
        long start = System.currentTimeMillis();
        flowGoodsBatchApi.statisticsFlowGoodsBatchTotalNumber();
        log.info("任务结束：库存流向商品库存数量统计任务执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("statisticsFlowGoodsBatchHandler")
    public ReturnT<String> statisticsFlowGoodsBatchControl(String param) throws Exception {
        log.info("任务开始：每天备份库存流向信息执行");
        long start = System.currentTimeMillis();
        ErpClientQueryRequest request = new ErpClientQueryRequest();
        request.setClientStatus(1);

        List<Long> suIdList = new ArrayList<>();
        //需要循环调用
        Page<ErpClientDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = erpClientApi.page(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            for (ErpClientDTO erpClientDTO : page.getRecords()) {
                suIdList.add(erpClientDTO.getRkSuId());
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        flowGoodsBatchDetailApi.statisticsFlowGoodsBatch(suIdList);

        log.info("任务结束：每天备份库存流向信息执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

}
