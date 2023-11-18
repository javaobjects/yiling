package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.flow.api.FlowPurchaseSalesInventoryApi;
import com.yiling.dataflow.flow.api.FlowSaleSummaryApi;
import com.yiling.dataflow.flow.api.FlowSaleSummaryDayApi;
import com.yiling.dataflow.flow.api.SyncFlowSaleApi;
import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.dto.request.UpdateFlowPurchaseSalesInventoryRequest;
import com.yiling.dataflow.flow.dto.request.UpdateFlowSaleSummaryRequest;
import com.yiling.dataflow.order.enums.DataFlowErrorCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: shuang.zhang
 * @date: 2022/11/15
 */
@Component
@Slf4j
public class FlowPurchaseSalesInventoryHandler {

    @DubboReference(async = true)
    private FlowPurchaseSalesInventoryApi flowPurchaseSalesInventoryApi;
    @DubboReference(timeout = 30 * 60 * 1000)
    private FlowSaleSummaryDayApi         flowSaleSummaryDayApi;
    @DubboReference
    private ErpClientApi                  erpClientApi;
    @DubboReference
    private SyncFlowSaleApi               syncFlowSaleApi;
    @Autowired(required = false)
    private RocketMqProducerService       rocketMqProducerService;

    @JobLog
    @XxlJob("syncFlowSaleSummaryHandler")
    public ReturnT<String> syncFlowSaleSummaryHandler(String param) throws Exception {
        log.info("任务开始：同步销售流向汇总任务开始");
        long start = System.currentTimeMillis();
        syncFlowSaleApi.syncFlowSaleSummary();
        log.info("任务结束：同步销售流向汇总任务结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("flowSaleSummaryHandler")
    public ReturnT<String> flowSaleSummaryHandler(String param) throws Exception {
        log.info("任务开始：销售流向汇总任务开始");
        long start = System.currentTimeMillis();
        param = XxlJobHelper.getJobParam();
        String time = DateUtil.today();
        if (!StringUtils.isEmpty(param)) {
            time = param;
        }

        ErpClientQueryRequest erpClientQueryRequest = new ErpClientQueryRequest();
//        erpClientQueryRequest.setClientStatus(1);
//        erpClientQueryRequest.setBiStatus(1);
//        erpClientQueryRequest.setFlowStatus(1);
        List<ErpClientDTO> eidList = new ArrayList<>();
        //需要循环调用
        Page<ErpClientDTO> page = null;
        int current = 1;
        do {
            erpClientQueryRequest.setCurrent(current);
            erpClientQueryRequest.setSize(2000);
            page = erpClientApi.page(erpClientQueryRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            eidList.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        for (ErpClientDTO erpClientDTO : eidList) {
            log.info("销售流向汇总任务执行:eid={}", erpClientDTO.getRkSuId());
            UpdateFlowSaleSummaryRequest request = new UpdateFlowSaleSummaryRequest();
            request.setEid(erpClientDTO.getRkSuId());
            request.setCrmId(erpClientDTO.getCrmEnterpriseId());
            request.setStartTime(DateUtil.beginOfMonth(DateUtil.parseDate(time)));
            request.setEndTime(DateUtil.endOfMonth(DateUtil.parseDate(time)));
            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_SALE_SUMMARY, Constants.TAG_FLOW_SALE_SUMMARY, DateUtil.formatDate(new Date()), JSON.toJSONString(request));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                throw new BusinessException(DataFlowErrorCode.SALE_FLOW_SYNC_ERROR);
            }
        }
        log.info("任务结束：销售流向汇总任务结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }


    @JobLog
    @XxlJob("flowSaleSummaryDayHandler")
    public ReturnT<String> flowSaleSummaryDayHandler(String param) throws Exception {
        log.info("任务开始：销售流向汇总任务开始");
        long start = System.currentTimeMillis();
        param = XxlJobHelper.getJobParam();
        String time = DateUtil.formatDate(DateUtil.offsetDay(new Date(),-1));
        if (!StringUtils.isEmpty(param)) {
            time = param;
        }

        ErpClientQueryRequest erpClientQueryRequest = new ErpClientQueryRequest();
        erpClientQueryRequest.setClientStatus(1);
        erpClientQueryRequest.setBiStatus(1);
        erpClientQueryRequest.setFlowStatus(1);
        List<ErpClientDTO> eidList = new ArrayList<>();
        //需要循环调用
        Page<ErpClientDTO> page = null;
        int current = 1;
        do {
            erpClientQueryRequest.setCurrent(current);
            erpClientQueryRequest.setSize(2000);
            page = erpClientApi.page(erpClientQueryRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            eidList.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        for (ErpClientDTO erpClientDTO : eidList) {
            log.info("销售流向汇总任务执行:eid={}", erpClientDTO.getRkSuId());
            QueryFlowSaleSummaryRequest request = new QueryFlowSaleSummaryRequest();
            request.setEid(erpClientDTO.getRkSuId());
            request.setStartTime(DateUtil.beginOfMonth(DateUtil.parseDate(time)));
            request.setEndTime(DateUtil.endOfMonth(DateUtil.parseDate(time)));
            flowSaleSummaryDayApi.updateFlowSaleSummaryDayByDateTimeAndEid(request);
            flowSaleSummaryDayApi.updateFlowSaleSummaryDayLingShouByTerminalCustomerType(request);
            flowSaleSummaryDayApi.updateFlowSaleSummaryDayPifaByTerminalCustomerType(request);
        }
        log.info("任务结束：销售流向汇总任务结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("flowPurchaseSalesInventoryHandler")
    public ReturnT<String> flowPurchaseSalesInventoryHandler(String param) throws Exception {
        log.info("任务开始：进销存明细统计任务开始");
        long start = System.currentTimeMillis();
        UpdateFlowPurchaseSalesInventoryRequest request = new UpdateFlowPurchaseSalesInventoryRequest();
        Date date = DateUtil.offsetDay(DateUtil.parseDate(DateUtil.today()), -1);
        request.setDateTime(date);
        ErpClientQueryRequest erpClientQueryRequest = new ErpClientQueryRequest();
        erpClientQueryRequest.setClientStatus(1);
        erpClientQueryRequest.setBiStatus(1);
        erpClientQueryRequest.setFlowStatus(1);
        List<ErpClientDTO> eidList = new ArrayList<>();
        //需要循环调用
        Page<ErpClientDTO> page = null;
        int current = 1;
        do {
            erpClientQueryRequest.setCurrent(current);
            erpClientQueryRequest.setSize(2000);
            page = erpClientApi.page(erpClientQueryRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            eidList.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        request.setEids(eidList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList()));
        flowPurchaseSalesInventoryApi.updateFlowPurchaseSalesInventoryByJob(request);
        log.info("任务结束：进销存明细统计任务执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }
}
