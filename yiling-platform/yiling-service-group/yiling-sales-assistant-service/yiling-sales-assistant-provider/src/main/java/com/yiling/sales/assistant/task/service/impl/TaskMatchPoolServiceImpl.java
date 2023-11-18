package com.yiling.sales.assistant.task.service.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.sales.assistant.task.dao.TaskMatchPoolMapper;
import com.yiling.sales.assistant.task.entity.TaskMatchPoolDO;
import com.yiling.sales.assistant.task.service.TaskMatchPoolService;

/**
 * <p>
 * 任务和随货同行单匹配池 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-01-31
 */
@Service
public class TaskMatchPoolServiceImpl extends BaseServiceImpl<TaskMatchPoolMapper, TaskMatchPoolDO> implements TaskMatchPoolService {
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public void consume() {
        List<TaskMatchPoolDO> list = this.list();
        list.forEach(taskMatchPoolDO -> {
            MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_SA_BILL_MATCH, Constants.TAG_SA_BILL_MATCH, taskMatchPoolDO.getAccompanyingBillId().toString());
            mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
            mqMessageSendApi.send(mqMessageTwoBO);
        });
    }
}
