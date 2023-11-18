package com.yiling.basic.mq.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.dto.MqMessageSendingDTO;

/**
 * MQ 消息发送服务
 *
 * @author: xuan.zhou
 * @date: 2022/1/12
 */
@Service
public interface MqMessageSendService {

    /**
     * 准备发送消息
     *
     * @param mqMessageBO
     * @return
     */
    MqMessageBO prepare(MqMessageBO mqMessageBO);

    /**
     * 发送消息
     *
     * @param mqMessageBO
     */
    void send(MqMessageBO mqMessageBO);

    /**
     * 重试发送消息
     *
     * @param mqMessageSendingDTOList
     */
    void retry(List<MqMessageSendingDTO> mqMessageSendingDTOList);
}
