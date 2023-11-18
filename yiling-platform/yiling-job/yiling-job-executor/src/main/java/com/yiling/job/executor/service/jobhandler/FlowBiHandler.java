package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.flow.api.FlowBiJobApi;
import com.yiling.dataflow.flow.api.FlowBiTaskApi;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.dataflow.order.api.FlowGoodsSpecMappingApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.statistics.api.FlowBalanceStatisticsJobApi;
import com.yiling.dataflow.statistics.api.FlowStatisticsJobApi;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.enums.StatisticeErrorEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
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

/**
 * @author: shuang.zhang
 * @date: 2022/4/7
 */
@Component
@Slf4j
public class FlowBiHandler {

    @DubboReference(timeout = 5 * 1000)
    private ErpClientApi erpClientApi;

    @DubboReference(async = true)
    private FlowBiJobApi flowBiJobApi;

    @DubboReference
    private FlowBiTaskApi flowBiTaskApi;

    @DubboReference(async = true)
    private FlowStatisticsJobApi flowStatisticsJobApi;

    @DubboReference(async = true)
    private FlowBalanceStatisticsJobApi flowBalanceStatisticsJobApi;

    @DubboReference(async = true)
    private FlowSaleApi             flowSaleApi;
    @DubboReference(async = true)
    private FlowPurchaseApi         flowPurchaseApi;
    @DubboReference(async = true)
    private FlowGoodsBatchApi       flowGoodsBatchApi;
    @DubboReference(async = true)
    private FlowGoodsSpecMappingApi flowGoodsSpecMappingApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;


    @JobLog
    @XxlJob("flowBiTaskHandler")
    public ReturnT<String> flowBiTaskControl(String param) throws Exception {
        log.info("任务开始：日流向对接BI任务执行");
        long start = System.currentTimeMillis();
        flowBiJobApi.excelFlowBiTask();

        log.info("任务结束：日流向对接BI任务执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("flowStatisticsHandler")
    public ReturnT<String> flowStatisticsControl(String param) throws Exception {
        log.info("任务开始：流向统计任务执行");
        long start = System.currentTimeMillis();

        ErpClientQueryRequest request = new ErpClientQueryRequest();
        request.setClientStatus(1);
        request.setBiStatus(1);
        request.setFlowStatus(1);
        List<ErpClientDTO> eidList = new ArrayList<>();
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
            eidList.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        List<ErpClientDataDTO> erpClientDataDTOList = PojoUtils.map(eidList, ErpClientDataDTO.class);

        flowStatisticsJobApi.statisticsFlowJob(erpClientDataDTOList);

        log.info("任务结束：流向统计任务执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }


    @JobLog
    @XxlJob("flowMonthBiTaskHandler")
    public ReturnT<String> flowMonthBiTaskControl(String param) throws Exception {
        log.info("任务开始：月流向对接BI任务执行");
        long start = System.currentTimeMillis();
        flowBiJobApi.excelMonthFlowBiTask();

        log.info("任务结束：月流向对接BI任务执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("statisticsFlowBalanceHandler")
    public ReturnT<String> statisticsFlowBalanceControl(String param) throws Exception {
        log.info("任务开始：统计每一个商业公司每天的流向平衡数据");
        long start = System.currentTimeMillis();

        ErpClientQueryRequest erpClientQueryRequest = new ErpClientQueryRequest();
        erpClientQueryRequest.setClientStatus(1);

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
            for (ErpClientDTO erpClientDTO : page.getRecords()) {
                ErpClientDataDTO erpClientDataDTO = PojoUtils.map(erpClientDTO, ErpClientDataDTO.class);
                SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_BALANCE_STATISTICS, Constants.TAG_FLOW_BALANCE_STATISTICS, DateUtil.formatDate(new Date()), JSON.toJSONString(erpClientDataDTO));
                if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    throw new BusinessException(StatisticeErrorEnum.FLOW_STATISTICE_ERROR);
                }
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        log.info("任务结束：统计每一个商业公司每天的流向平衡数据执行。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    /**
     * 标记终端crm客户内码，crm商品内码
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("syncFlowSaleCrmSign")
    public ReturnT<String> syncFlowSaleCrmSign(String param) throws Exception {
        log.info("任务开始：标记终端crm客户内码，crm商品内码");
        long start = System.currentTimeMillis();
        ErpClientQueryRequest erpClientQueryRequest = new ErpClientQueryRequest();
        erpClientQueryRequest.setClientStatus(1);
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

        List<ErpClientDataDTO> list = PojoUtils.map(eidList, ErpClientDataDTO.class);
        List<Long> crmIds = list.stream().map(e -> e.getCrmEnterpriseId()).filter(e -> e > 0).collect(Collectors.toList());
        flowSaleApi.updateFlowSaleCrmSign(crmIds);
        log.info("任务结束：标记终端crm客户内码，crm商品内码。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("updateRecommendHandler")
    public ReturnT<String> updateRecommendControl(String param) throws Exception {
        log.info("任务开始：定时任务匹配商品名称和规格");
        long start = System.currentTimeMillis();
        flowGoodsSpecMappingApi.updateRecommendInfoByGoodsNameAndSpec();

        log.info("任务结束：定时任务匹配商品名称和规格。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }
}
