package com.yiling.sjms.workflow.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.sjms.workflow.WorkFlowService;
import com.yiling.sjms.workflow.dto.request.WorkFlowBaseRequest;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2023/2/25
 */
@Slf4j
@Service
public class WorkFlowServiceImpl implements WorkFlowService {
    @DubboReference
    MqMessageSendApi mqMessageSendApi;


    @Override
    public void sendSubmitMsg(WorkFlowBaseRequest request) {
        Assert.notNull(request.getId(), "参数id不能为空");
        log.info("发布流程参数:{}", JSON.toJSONString(request));
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_SUBMIT_SEND_WORKFLOW,request.getTag(),JSON.toJSONString(request) );
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
    }

    @Override
    public void resubmitMsg(WorkFlowBaseRequest request) {
        Assert.notNull(request.getId(), "参数id不能为空");
        log.info("驳回后提交流程参数:{}", JSON.toJSONString(request));
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_RESUBMIT_SEND_WORKFLOW,request.getTag(),JSON.toJSONString(request) );
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
    }
}