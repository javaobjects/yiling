package com.yiling.basic.mq.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;
import com.yiling.basic.mq.entity.MqMessageConsumeFailureDO;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/10/8
 */
@Slf4j
public class MqMessageConsumeFailureServiceTest extends BaseTest {

    @Autowired
    MqMessageConsumeFailureService mqMessageConsumeFailureService;

    @Test
    public void save() {
        MqMessageConsumeFailureDO entity = new MqMessageConsumeFailureDO();
        entity.setMsgId("1");
        entity.setTopic("topic");
        entity.setTags("tags");
        entity.setKeys("keys");
        entity.setBody("body");
        entity.setFailedMsg("failed message");
        mqMessageConsumeFailureService.save(entity);
    }
}
