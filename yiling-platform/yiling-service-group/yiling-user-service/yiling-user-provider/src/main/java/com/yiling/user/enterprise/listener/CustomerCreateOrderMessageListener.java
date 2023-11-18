package com.yiling.user.enterprise.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.user.enterprise.dto.request.CustomerCreateOrderMqRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CustomerCreateOrderMessageListener
 * @描述
 * @创建时间 2022/11/4
 * @修改人 shichen
 * @修改时间 2022/11/4
 **/
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_CUSTOMER_CREATE_ORDER, consumerGroup = Constants.TOPIC_CUSTOMER_CREATE_ORDER)
public class CustomerCreateOrderMessageListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    EnterpriseCustomerService enterpriseCustomerService;

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        CustomerCreateOrderMqRequest request = JSONObject.parseObject(body, CustomerCreateOrderMqRequest.class);
        if (null == request.getCustomerEid() || null == request.getSellerEid() || null == request.getOrderCreateTime()) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{CustomerEid或SellerEid或者OrderCreateTime为空}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            return MqAction.CommitMessage;
        }
        EnterpriseCustomerDO enterpriseCustomerDO = enterpriseCustomerService.get(request.getSellerEid(), request.getCustomerEid());
        if(null==enterpriseCustomerDO){
            log.warn("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{CustomerEid和SellerEid未找到客户建采关系}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            return MqAction.CommitMessage;
        }
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest();
        updateRequest.setId(enterpriseCustomerDO.getId());
        updateRequest.setLastPurchaseTime(request.getOrderCreateTime());
        enterpriseCustomerService.syncUpdateById(updateRequest);
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
}
