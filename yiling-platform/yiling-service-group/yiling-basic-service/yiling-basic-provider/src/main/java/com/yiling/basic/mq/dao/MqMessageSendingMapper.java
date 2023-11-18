package com.yiling.basic.mq.dao;

import org.springframework.stereotype.Repository;

import com.yiling.basic.mq.entity.MqMessageSendingDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * mq发送中的消息表 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-12
 */
@Repository
public interface MqMessageSendingMapper extends BaseMapper<MqMessageSendingDO> {

}
