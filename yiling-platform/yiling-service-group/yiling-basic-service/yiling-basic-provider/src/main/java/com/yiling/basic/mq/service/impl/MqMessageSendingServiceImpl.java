package com.yiling.basic.mq.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.mq.dao.MqMessageSendingMapper;
import com.yiling.basic.mq.entity.MqMessageSendingDO;
import com.yiling.basic.mq.entity.MqMessageSendingFailureDO;
import com.yiling.basic.mq.service.MqMessageSendingFailureService;
import com.yiling.basic.mq.service.MqMessageSendingService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * mq发送中的消息表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-12
 */
@Service
@RefreshScope
public class MqMessageSendingServiceImpl extends BaseServiceImpl<MqMessageSendingMapper, MqMessageSendingDO> implements MqMessageSendingService {

    @Value("${mq.sender.retryInterval}")
    private Integer retryInterval;
    @Value("${mq.sender.retryListSize}")
    private Integer retryListSize;
    @Value("${mq.sender.removeSuccessMessage}")
    private Boolean removeSuccessMessage;

    @Autowired
    MqMessageSendingFailureService mqMessageSendingFailureService;

    @Override
    public List<MqMessageSendingDO> queryRetryList() {
        QueryWrapper<MqMessageSendingDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(MqMessageSendingDO::getStatus, 1)
                .le(MqMessageSendingDO::getNextRetryTime, new Date())
                .orderByAsc(MqMessageSendingDO::getId);

        if (retryListSize > 0) {
            queryWrapper.last("limit " + retryListSize);
        }

        return this.list(queryWrapper);
    }

    @Override
    public boolean updateSuccessful(Long id, String msgId) {
        if (removeSuccessMessage) {
            return this.removeById(id);
        } else {
            MqMessageSendingDO entity = new MqMessageSendingDO();
            entity.setId(id);
            entity.setMsgId(msgId);
            entity.setStatus(2);
            entity.setFailedMsg("");

            return this.updateById(entity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFailed(Long id, String msgId, String failedMsg) {
        MqMessageSendingDO mqMessageSendingDO = this.getById(id);

        MqMessageSendingFailureDO mqMessageSendingFailureDO = PojoUtils.map(mqMessageSendingDO, MqMessageSendingFailureDO.class);
        mqMessageSendingFailureDO.setStatus(1);
        mqMessageSendingFailureDO.setOpUserId(0L);
        mqMessageSendingFailureService.save(mqMessageSendingFailureDO);

        return this.removeById(id);
    }

    @Override
    public boolean updateFailedMsg(Long id, String msgId, String failedMsg) {
        MqMessageSendingDO entity = new MqMessageSendingDO();
        entity.setId(id);
        entity.setMsgId(msgId);
        entity.setFailedMsg(failedMsg);

        return this.updateById(entity);
    }

    @Override
    public boolean updateRetryTimes(Long id) {
        MqMessageSendingDO entity = this.getById(id);
        entity.setRetryTimes(entity.getRetryTimes() + 1);
        entity.setNextRetryTime(DateUtil.offset(new Date(), DateField.SECOND, retryInterval));
        return this.updateById(entity);
    }

}
