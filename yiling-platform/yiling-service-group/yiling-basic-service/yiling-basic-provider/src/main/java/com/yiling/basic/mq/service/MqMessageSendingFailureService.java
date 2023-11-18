package com.yiling.basic.mq.service;

import com.yiling.basic.mq.entity.MqMessageSendingFailureDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * mq发送失败的消息表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-10-27
 */
public interface MqMessageSendingFailureService extends BaseService<MqMessageSendingFailureDO> {

    /**
     * 统计未处理的发送失败消息数量
     *
     * @return int
     * @author xuan.zhou
     * @date 2022/10/27
     **/
    int countUnprocessedSendingFailureMessages();
}
