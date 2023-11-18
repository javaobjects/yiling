package com.yiling.basic.mq.service.impl;

import org.springframework.stereotype.Service;

import com.yiling.basic.mq.dao.MqMessageConsumeFailureMapper;
import com.yiling.basic.mq.entity.MqMessageConsumeFailureDO;
import com.yiling.basic.mq.service.MqMessageConsumeFailureService;
import com.yiling.framework.common.base.BaseServiceImpl;

/**
 * <p>
 * mq消费失败的消息表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-26
 */
@Service
public class MqMessageConsumeFailureServiceImpl extends BaseServiceImpl<MqMessageConsumeFailureMapper, MqMessageConsumeFailureDO> implements MqMessageConsumeFailureService {
}
