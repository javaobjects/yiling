package com.yiling.basic.mq.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/10/27
 */
@Slf4j
public class MqMessageSendingServiceTest extends BaseTest {

    @Autowired
    MqMessageSendingService mqMessageSendingService;

    @Test
    public void updateFailed() {
        mqMessageSendingService.updateFailed(1486185493885988865L, "AC105206D16D2E5D6D97881ECDA10000", null);
    }
}
