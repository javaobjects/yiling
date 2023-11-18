package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashTaskDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashTaskPageRequest;
import com.yiling.dataflow.wash.enums.FlowWashTaskStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.job.executor.log.JobLog;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/4/4
 */
@Slf4j
@Component
public class FlowMonthWashTaskJobHandler {

    @DubboReference
    private FlowMonthWashTaskApi flowMonthWashTaskApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @JobLog
    @XxlJob("flowMonthWashTaskJobHandler")
    public ReturnT<String> flowMonthWashTaskJobHandler(String param) throws Exception {
        Integer year = null;
        Integer month = null;
        if (StringUtils.isNotEmpty(param)) {
            JSONObject json = JSONUtil.parseObj(param);
            year = json.getInt("year");
            month = json.getInt("month");
        }

        List<FlowMonthWashTaskDTO> list = new ArrayList<>();
        int current = 1;
        int size = 100;
        while (true) {
            QueryFlowMonthWashTaskPageRequest request = new QueryFlowMonthWashTaskPageRequest();
            request.setCurrent(current);
            request.setSize(size);
            request.setYear(year);
            request.setMonth(month);
            request.setWashStatus(FlowWashTaskStatusEnum.FAIL.getCode());
            Page<FlowMonthWashTaskDTO> resultPage = flowMonthWashTaskApi.listPage(request);
            list.addAll(resultPage.getRecords());
            if (resultPage.getRecords().size() < size) {
                break;
            }
            current++;
        }

        // 重新清洗
        for (FlowMonthWashTaskDTO flowMonthWashTaskDTO : list) {
            Long fmwtId = flowMonthWashTaskDTO.getId();
            flowMonthWashTaskApi.updateWashStatusById(flowMonthWashTaskDTO.getId(), FlowWashTaskStatusEnum.NOT_WASH.getCode());

            SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_WASH_TASK, Constants.TAG_FLOW_WASH_TASK, DateUtil.formatDate(new Date()), String.valueOf(fmwtId));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("流向清洗任务：" + flowMonthWashTaskDTO.getId() + "， 重新清洗消费失败");
            }
        }

        return ReturnT.SUCCESS;
    }
}
