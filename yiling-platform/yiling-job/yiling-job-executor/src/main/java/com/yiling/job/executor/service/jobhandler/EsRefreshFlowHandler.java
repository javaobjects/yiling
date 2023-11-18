package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.index.api.EsDispatchRecordApi;
import com.yiling.dataflow.index.dto.request.SaveEsDispatchRecordRequest;
import com.yiling.dataflow.index.enums.IndexErrorEnum;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.enums.ErpTopicName;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: shuang.zhang
 * @date: 2022/11/15
 */
@Component
@Slf4j
public class EsRefreshFlowHandler {

    @DubboReference
    private ErpClientApi            erpClientApi;
    @DubboReference
    private EsDispatchRecordApi     esDispatchRecordApi;
    @DubboReference
    private FlowSaleApi             flowSaleApi;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;


    @JobLog
    @XxlJob("syncRefreshFlowHandler")
    public ReturnT<String> syncRefreshFlowHandler(String param) throws Exception {
        log.info("任务开始：es刷新flow索引任务开始");
        //param=1 全量跑 param=0 增量跑
        long start = System.currentTimeMillis();
        param = XxlJobHelper.getJobParam();
        Date endTime = new Date();
        Date startTime = null;
        if (StrUtil.isEmpty(param)) {
            log.error("param参数必填0增量1全量 其他就是年份");
            return ReturnT.SUCCESS;
        }

        if (param.equals("1")) {
            startTime = DateUtil.parse("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        } else if (param.equals("0")) {
            startTime = esDispatchRecordApi.getLastMaxTime();
            if (startTime == null) {
                startTime = DateUtil.parse("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
            }
        }

        ErpClientQueryRequest erpClientQueryRequest = new ErpClientQueryRequest();
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
            for(ErpClientDTO erpClientDTO:page.getRecords()){
                UpdateFlowIndexRequest request = new UpdateFlowIndexRequest();
                request.setStartUpdateTime(startTime);
                request.setEndUpdateTime(endTime);
                request.setEid(erpClientDTO.getRkSuId());
                request.setYear(Integer.parseInt(param));
                SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_INDEX_REFRESH, Constants.TAG_FLOW_INDEX_REFRESH, DateUtil.formatDate(new Date()), JSON.toJSONString(request));
                if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    throw new BusinessException(IndexErrorEnum.FLOW_INDEX_REFRESH_ERROR);
                }
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        SaveEsDispatchRecordRequest recordRequest = new SaveEsDispatchRecordRequest();
        recordRequest.setStartTime(startTime);
        recordRequest.setEndTime(endTime);
        esDispatchRecordApi.insertEsDispatchRecord(recordRequest);
        log.info("任务结束：es刷新flow索引任务开始。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }


    @JobLog
    @XxlJob("syncRefreshTaskFlowHandler")
    public ReturnT<String> syncRefreshTaskFlowHandler(String param) throws Exception {
        log.info("任务开始：es刷新flow索引任务开始");
        //param=1 全量跑 param=0 增量跑
        long start = System.currentTimeMillis();

        ErpClientQueryRequest erpClientQueryRequest = new ErpClientQueryRequest();
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
            for(ErpClientDTO erpClientDTO:page.getRecords()){
                UpdateFlowIndexRequest request = new UpdateFlowIndexRequest();
                request.setStartUpdateTime(DateUtil.parse("2023-01-01","yyyy-MM-dd"));
                request.setEndUpdateTime(new Date());
                request.setEid(erpClientDTO.getRkSuId());
//                request.setYear(Integer.parseInt(param));
                request.setTaskCode(ErpTopicName.ErpGoodsBatchFlow.getMethod());
                SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_INDEX_REFRESH, Constants.TAG_FLOW_INDEX_REFRESH, DateUtil.formatDate(new Date()), JSON.toJSONString(request));
                if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    throw new BusinessException(IndexErrorEnum.FLOW_INDEX_REFRESH_ERROR);
                }
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        log.info("任务结束：es刷新flow索引任务开始。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

}
