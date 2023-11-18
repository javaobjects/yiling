package com.yiling.basic.mq.api;

import java.util.List;

import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.dto.MqMessageSendingDTO;

/**
 * MQ消息 API
 *
 * @author: xuan.zhou
 * @date: 2022/1/10
 */
public interface MqMessageSendApi {

    /**
     * 获取重试发送消息列表
     *
     * @return
     */
    List<MqMessageSendingDTO> queryRetryList();

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

    /**
     * 统计未处理的发送失败消息数量
     *
     * @return int
     * @author xuan.zhou
     * @date 2022/10/27
     **/
    int countUnprocessedSendingFailureMessages();
}
