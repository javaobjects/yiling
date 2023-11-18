package com.yiling.basic.mq.dao;

import org.springframework.stereotype.Repository;

import com.yiling.basic.mq.entity.MqMessageSendingFailureDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * mq发送失败的消息表 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-10-27
 */
@Repository
public interface MqMessageSendingFailureMapper extends BaseMapper<MqMessageSendingFailureDO> {

}
