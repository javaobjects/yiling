package com.yiling.dataflow.sale.listenter;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.dataflow.sale.dto.request.GenerateCrmConfigMouldRequest;
import com.yiling.dataflow.sale.service.SaleDepartmentTargetService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2022-04-17
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GENERATE_CRM_SALE_DEPARTMENT_TARGET_CONFIG, consumerGroup = Constants.TAG_GENERATE_CRM_SALE_DEPARTMENT_TARGET_CONFIG)
public class GenerateCrmTargetConfigListener extends AbstractMessageListener {


    @Autowired
    SaleDepartmentTargetService departmentTargetService;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        GenerateCrmConfigMouldRequest request = JSONObject.parseObject(body, GenerateCrmConfigMouldRequest.class);
        try {
            departmentTargetService.generateMould(request.getSaleTargetId(), request.getDepartId());
        } catch (BusinessException e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), body, e.getMessage());
            //业务异常更新生成状态无需重试
            String sub = StrUtil.sub(message.getMsgId(), 0, 450);
            updateTaskStatus(request.getSaleTargetId(), request.getDepartId(),message.getMsgId()+"----"+ sub);
            return MqAction.CommitMessage;
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), body, e);
            throw new ServiceException(ResultCode.FAILED);
        }
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);

        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }

    private void updateTaskStatus(Long targetId,Long deptId,String errMsg){
        departmentTargetService.updateTaskStatus(targetId,deptId,errMsg);
    }

}
