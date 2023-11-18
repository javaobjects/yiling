package com.yiling.basic.sms.service;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.dto.MqMessageSendingDTO;
import com.yiling.basic.sms.enums.SmsSignatureEnum;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
@Slf4j
public class SmsServiceTest extends BaseTest {

    @Autowired
    SmsService smsService;
    @Autowired
    TxSmsChannelService txSmsChannelService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @DubboReference(async = true)
    MqMessageSendApi asyncMqMessageSendApi;

    @Test
    public void send() {
        smsService.send("13900000000", "测试 2021-6-8", SmsTypeEnum.VERIFY_CODE);
        ThreadUtil.sleep(10000);
    }

    @Test
    public void sendVerifyCode() {
        smsService.sendVerifyCode("158270101192", SmsVerifyCodeTypeEnum.LOGIN);
        ThreadUtil.sleep(10000);
    }

    @Test
    public void checkVerifyCode() {
        boolean result = smsService.checkVerifyCode("13900000000", "888888", SmsVerifyCodeTypeEnum.LOGIN);
        log.info("result = {}", result);
    }

    @Test
    public void testTxSend() {
        String mobile = "15827010119";
        String content = "验证码279394，您正在以岭 POP 平台办理注册，2分钟内输入有效。";
        txSmsChannelService.sendSms(mobile, content, SmsSignatureEnum.YILING_PHARMACEUTICAL.getName());
    }

    @Test
    public void retry() {
        // 获取重试发送消息列表
        List<MqMessageSendingDTO> retryMqMessageSendingDTOList = mqMessageSendApi.queryRetryList();
        if (CollUtil.isEmpty(retryMqMessageSendingDTOList)) {
            log.info("未获取到待重试发送消息记录");
            return;
        }

        log.info("获取到{}条待重试发送消息记录", retryMqMessageSendingDTOList.size());
        asyncMqMessageSendApi.retry(retryMqMessageSendingDTOList);

        ThreadUtil.sleep(30000);
    }
}
