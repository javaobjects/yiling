package com.yiling.mall.settlement.b2b.mq.message.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.settlement.b2b.api.SettlementApi;
import com.yiling.settlement.b2b.api.SettlementOrderApi;
import com.yiling.settlement.b2b.dto.request.CompensateSettlementPayStatusRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementByIdRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementStatusRequest;
import com.yiling.settlement.b2b.enums.SettlementErrorCode;
import com.yiling.settlement.b2b.enums.SettlementOperationTypeEnum;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 更新结算单状态补偿监听
 *
 * @author: dexi.yao
 * @date: 2021/12/01
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_B2B_SETTLEMENT_STATUS_NOTIFY, consumerGroup = Constants.TOPIC_B2B_SETTLEMENT_STATUS_NOTIFY)
public class SettlementStatusMessageListener extends AbstractMessageListener {

    @DubboReference
    SettlementApi settlementApi;
    @DubboReference
    SettlementOrderApi settlementOrderApi;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

//    @MdcLog
//    @Override
//    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
//        CompensateSettlementPayStatusRequest request = null;
//        try {
//            request = JSON.parseObject(MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8"), CompensateSettlementPayStatusRequest.class);
//            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), request);
//
//            if (ObjectUtil.isNull(request)) {
//                log.error("结算单列表为空");
//                return MqAction.CommitMessage;
//            }
//
//            Boolean isSuccess = updateSettlement(request.getUpdateList(), request.getSuccessSettList(), request.getFailSettList());
//            if (isSuccess) {
//                //解锁结算单
//                unLockSettlement(request.getUpdateList().stream().map(UpdateSettlementByIdRequest::getId).collect(Collectors.toList()));
//                return MqAction.CommitMessage;
//            } else {
//                log.error("打款后结算单状态补偿消费失败，结算单={}", request);
//                throw new BusinessException(SettlementErrorCode.UPDATE_SETTLEMENT_LOCK_FAIL);
//            }
//        } catch (BusinessException e) {
//
//            log.debug("[{}],errorMsg:{}", request, e.getMessage());
//
//            return MqAction.CommitMessage;
//
//        } catch (Exception e) {
//
//            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), request, e);
//            throw e;
//        }
//    }

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        CompensateSettlementPayStatusRequest request = JSON.parseObject(body, CompensateSettlementPayStatusRequest.class);

        if (ObjectUtil.isNull(request)) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{结算单列表为空}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            return MqAction.CommitMessage;
        }

        Boolean isSuccess = updateSettlement(request.getUpdateList(), request.getSuccessSettList(), request.getFailSettList());
        if (isSuccess) {
            //解锁结算单
            unLockSettlement(request.getUpdateList().stream().map(UpdateSettlementByIdRequest::getId).collect(Collectors.toList()));
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);
        } else {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{打款后结算单状态补偿消费失败}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            throw new ServiceException(SettlementErrorCode.UPDATE_SETTLEMENT_LOCK_FAIL.getMessage());
        }
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

    /**
     * 更新结算单等状态
     *
     * @param updateList
     * @param successSettList
     * @param failSettList
     * @return
     */
    public Boolean updateSettlement(List<UpdateSettlementByIdRequest> updateList, List<UpdateSettlementStatusRequest> successSettList, List<UpdateSettlementStatusRequest> failSettList) {
        //更新结算单状态
        Boolean isSuccess = settlementApi.updateSettlementById(updateList);
        if (!isSuccess) {
            log.error("打款后结算单状态补偿消费失败,请检查。。。");
            CompensateSettlementPayStatusRequest compensateRequest = new CompensateSettlementPayStatusRequest();
            compensateRequest.setUpdateList(updateList);
            compensateRequest.setSuccessSettList(successSettList);
            compensateRequest.setFailSettList(failSettList);
            throw new BusinessException(SettlementErrorCode.UPDATE_SETTLEMENT_STATUS_FAIL);
        }
        //更新结算单订单状态
        isSuccess = settlementOrderApi.updateSettlementStatus(successSettList, failSettList, SettlementOperationTypeEnum.SEND);

        return isSuccess;
    }

    /**
     * 解锁结算单
     *
     * @param settlementIdList
     * @return
     */
    public Boolean unLockSettlement(List<Long> settlementIdList) {
        settlementIdList.forEach(e -> {
            String key = new StringBuilder("b2b_settlement_payment").append(":").append(e.toString()).toString();
            try {
                stringRedisTemplate.delete(key);
            } catch (Exception exception) {
                log.error("结算单释放锁失败，key：{}", key);
            }
        });
        return Boolean.TRUE;
    }

}
