package com.yiling.basic.mq.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.mq.dao.MqMessageSendingFailureMapper;
import com.yiling.basic.mq.entity.MqMessageSendingFailureDO;
import com.yiling.basic.mq.service.MqMessageSendingFailureService;
import com.yiling.framework.common.base.BaseServiceImpl;

/**
 * <p>
 * mq发送失败的消息表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-10-27
 */
@Service
public class MqMessageSendingFailureServiceImpl extends BaseServiceImpl<MqMessageSendingFailureMapper, MqMessageSendingFailureDO> implements MqMessageSendingFailureService {

    @Override
    public int countUnprocessedSendingFailureMessages() {
        QueryWrapper<MqMessageSendingFailureDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(MqMessageSendingFailureDO::getStatus, 1);

        return this.count(queryWrapper);
    }
}
