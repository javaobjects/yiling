package com.yiling.basic.mq.service;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.dto.MqMessageSendingDTO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/1/12
 */
@Slf4j
public class MqMessageSendServiceTest extends BaseTest {

    @DubboReference
    private MqMessageSendApi     mqMessageSendApi;
    @Autowired
    private MqMessageSendService mqMessageSendService;

    @Test
    public void sendMesasge() {
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_SMS_CREATED, "", "1", MqDelayLevel.FIVE_SECONDS);
        mqMessageBO.setTraceId(IdUtil.fastSimpleUUID());
        mqMessageBO = mqMessageSendService.prepare(mqMessageBO);

        mqMessageSendService.send(mqMessageBO);

        // 延时
        ThreadUtil.sleep(300000);
    }

    @Test
    public void queryRetryList() {
        List<MqMessageSendingDTO> list = mqMessageSendApi.queryRetryList();
        log.info("list={}", JSONUtil.toJsonStr(list));
    }
}
