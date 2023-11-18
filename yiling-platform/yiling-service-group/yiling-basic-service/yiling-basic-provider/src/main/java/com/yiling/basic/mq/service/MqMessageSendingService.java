package com.yiling.basic.mq.service;

import java.util.List;

import com.yiling.basic.mq.entity.MqMessageSendingDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * mq发送中的消息表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-12
 */
public interface MqMessageSendingService extends BaseService<MqMessageSendingDO> {

    /**
     * 获取重试发送消息列表
     *
     * @return
     */
    List<MqMessageSendingDO> queryRetryList();

    /**
     * 更新为成功
     *
     * @param id ID
     * @param msgId 消息ID
     * @return
     */
    boolean updateSuccessful(Long id, String msgId);

    /**
     * 更新为失败
     *
     * @param id ID
     * @param msgId 消息ID
     * @param failedMsg 发送失败信息
     * @return
     */
    boolean updateFailed(Long id, String msgId, String failedMsg);

    /**
     * 更新发送失败信息
     *
     * @param id ID
     * @param msgId 消息ID
     * @param failedMsg 发送失败信息
     * @return
     */
    boolean updateFailedMsg(Long id, String msgId, String failedMsg);

    /**
     * 更新失败次数
     *
     * @param id ID
     * @return
     */
    boolean updateRetryTimes(Long id);
}
