package com.yiling.job.executor.service.jobhandler;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.dto.MqMessageSendingDTO;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.job.executor.log.JobLog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * MQ消息任务处理器
 *
 * @author: xuan.zhou
 * @date: 2022/1/12
 */
@Slf4j
@Component
public class MqMessageJobHandler {

    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @DubboReference(timeout = 30000)
    MqMessageSendApi mqMessageSendApi2;
    @DubboReference
    SmsApi smsApi;

    @JobLog
    @XxlJob("mqSendingMessagesRetryJobHandler")
    public ReturnT<String> retry(String param) throws Exception {
        // 获取重试发送消息列表
        List<MqMessageSendingDTO> retryMqMessageSendingDTOList = mqMessageSendApi.queryRetryList();
        if (CollUtil.isEmpty(retryMqMessageSendingDTOList)) {
            XxlJobHelper.log("未获取到待重试发送消息记录");
            return ReturnT.SUCCESS;
        }

        XxlJobHelper.log("获取到{}条待重试发送消息记录", retryMqMessageSendingDTOList.size());
        mqMessageSendApi2.retry(retryMqMessageSendingDTOList);

        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("mqSendingFailureMessagesMonitorJobHandler")
    public ReturnT<String> monitorSendingFailureMessages() throws Exception {
        // 获取重试发送消息列表
        int count = mqMessageSendApi.countUnprocessedSendingFailureMessages();
        if (count > 0) {
            XxlJobHelper.log("找到{}条发送失败待处理数据，请及时处理", count);

            String paramJsonStr = XxlJobHelper.getJobParam();
            if (StrUtil.isNotEmpty(paramJsonStr) && JSONUtil.isJson(paramJsonStr)) {
                MonitorSendingFailureMessagesParam monitorSendingFailureMessagesParam = JSONUtil.toBean(paramJsonStr, MonitorSendingFailureMessagesParam.class);
                List<String> mobiles = monitorSendingFailureMessagesParam.getMobiles();
                if (CollUtil.isNotEmpty(mobiles)) {
                    mobiles.forEach(e -> {
                        smsApi.send(e, StrUtil.format("找到{}条发送失败待处理数据，请及时处理", count), SmsTypeEnum.MONITOR_REMINDER);
                    });
                }
            }
        } else {
            XxlJobHelper.log("无发送失败待处理数据");
        }

        return ReturnT.SUCCESS;
    }

    @Data
    public static class MonitorSendingFailureMessagesParam {
        private List<String> mobiles;
    }
}
