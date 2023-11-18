package com.yiling.basic.mq.listener;

import org.apache.dubbo.rpc.RpcContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.MDC;

import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.TraceIdUtil;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象消费者监听
 *
 * @author: xuan.zhou
 * @date: 2022/1/26
 */
@Slf4j
public abstract class AbstractMessageListener implements MessageListener {

    /**
     * 最大重试消费次数，10s、30s、1mins、2mins、3mins...10min、20min等，7次相当于15分40秒
     */
    public static final int DEFAULT_RECONSUME_TIMES = 7;

    @Override
    public final MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String body = null;
        try {
            // 设置日志跟踪ID
            this.setTraceId();

            // 提取消息内容
            body = MqMsgConvertUtil.bytes2String(message.getBody(), "utf-8");
            log.info("准备消费消息 -> msgId={}, body={}", message.getMsgId(), body);

            // 调用消费逻辑
            MqAction mqAction = this.consume(body, message, context);
            log.info("消息消费完毕 -> msgId={}, body={}, mqAction={}", message.getMsgId(), body, mqAction.name());

            return mqAction;
        } catch (Exception e) {
            log.error("消息消费失败 -> msgId={}, body={}", message.getMsgId(), body, e);

            // 取值界定在[0-16]的区间内
            int reconsumeTimes = this.getMaxReconsumeTimes();
            reconsumeTimes = reconsumeTimes < 0 ? 0 : reconsumeTimes;
            reconsumeTimes = reconsumeTimes > 16 ? 16 : reconsumeTimes;

            // 达到最大重试消费次数后不在重试
            if (message.getReconsumeTimes() == reconsumeTimes) {
                log.error("消息已达到最大重试消费次数，不再重试消费 -> reconsumeTimes={}, msgId={}, message={}", reconsumeTimes, message.getMsgId(), JSONUtil.toJsonStr(message));
                this.handlError(body, message, context, e);
                return MqAction.CommitMessage;
            }

            return MqAction.ReconsumeLater;
        } finally {
            MDC.clear();
        }
    }

    private void setTraceId() {
        String traceId = MDC.get(Constants.TRACE_ID);
        if (StrUtil.isEmpty(traceId)) {
            traceId = TraceIdUtil.genTraceId();
            MDC.put(Constants.TRACE_ID, traceId);
            RpcContext.getContext().setAttachment(Constants.TRACE_ID, traceId);
        }
    }

    private void handlError(String body, MessageExt message, ConsumeConcurrentlyContext context, Exception e) {
        IConsumeFailureHandler consumeFailureHandler = this.getConsumeFailureHandler();
        if (consumeFailureHandler != null) {
            consumeFailureHandler.handler(body, message, context, e);
        }
    }

    /**
     * 消费逻辑（不用catch异常）
     *
     * @param body
     * @param message
     * @param context
     * @return
     */
    protected abstract MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context);

    /**
     * 设置最大重试次数，介于[0-16]的闭区间值<br/>
     * 每次的间隔时间为：10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     *
     * @return
     */
    protected abstract int getMaxReconsumeTimes();

    /**
     * 消费失败时的处理器
     *
     * @return
     */
    protected abstract IConsumeFailureHandler getConsumeFailureHandler();
}
