package com.yiling.basic.mq.dao;

import org.springframework.stereotype.Repository;

import com.yiling.basic.mq.entity.MqMessageConsumeFailureDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * mq消费失败的消息表 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-26
 */
@Repository
public interface MqMessageConsumeFailureMapper extends BaseMapper<MqMessageConsumeFailureDO> {

}
