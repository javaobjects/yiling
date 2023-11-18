package com.yiling.basic.mq.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.dto.MqMessageSendingDTO;
import com.yiling.basic.mq.entity.MqMessageSendingDO;
import com.yiling.basic.mq.service.MqMessageSendService;
import com.yiling.basic.mq.service.MqMessageSendingFailureService;
import com.yiling.basic.mq.service.MqMessageSendingService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * MQ消息 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/1/10
 */
@Slf4j
@DubboService
public class MqMessageSendApiImpl implements MqMessageSendApi {

    @Autowired
    private MqMessageSendingService mqMessageSendingService;
    @Autowired
    private MqMessageSendService mqMessageSendService;
    @Autowired
    private MqMessageSendingFailureService mqMessageSendingFailureService;

    @Override
    public List<MqMessageSendingDTO> queryRetryList() {
        List<MqMessageSendingDO> list = mqMessageSendingService.queryRetryList();
        return PojoUtils.map(list, MqMessageSendingDTO.class);
    }

    @Override
    public MqMessageBO prepare(MqMessageBO mqMessageBO) {
        return mqMessageSendService.prepare(mqMessageBO);
    }

    @Override
    public void send(MqMessageBO mqMessageBO) {
        mqMessageSendService.send(mqMessageBO);
    }

    @Override
    public void retry(List<MqMessageSendingDTO> mqMessageSendingDTOList) {
        mqMessageSendService.retry(mqMessageSendingDTOList);
    }

    @Override
    public int countUnprocessedSendingFailureMessages() {
        return mqMessageSendingFailureService.countUnprocessedSendingFailureMessages();
    }
}
