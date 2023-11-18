package com.yiling.framework.rocketmq.mq;

import java.util.List;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.framework.rocketmq.exception.MqContextException;
import com.yiling.framework.rocketmq.exception.MqSendException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RocketMqProducerService {

    @Autowired
    @Qualifier("defaultProducer")
    private DefaultMQProducer  rocketProducer;

    private RocketSendCallback rocketSendCallback = new RocketSendCallback();

    /**
     * 单边发送
     *
     * @param topic topic
     * @param tag tag
     * @param content 字符串消息体
     */
    public void sendOneway(String topic, String tag, String content) {
        this.sendOneway(topic, tag, "", content);
    }

    /**
     * 单边发送
     */
    public void sendOneway(String topic, String tag, String keys, String content) {
        try {
            Message msg = getMessage(topic, tag, keys, content);
            rocketProducer.sendOneway(msg);
            this.logMsg(msg);
        } catch (Exception e) {
            log.error("单边发送消息失败", e);
            throw new MqSendException(e);
        }
    }

    /**
     * 异步发送 默认回调函数
     *
     * @param topic topic
     * @param tag tag
     * @param content 字符串消息体
     */
    public void sendAsyncDefault(String topic, String tag, String content) {
        this.sendAsyncDefault(topic, tag, "", content);
    }

    /**
     * 异步发送 默认回调函数
     */
    public void sendAsyncDefault(String topic, String tag, String keys, String content) {
        Message msg = getMessage(topic, tag, keys, content);
        try {
            rocketProducer.send(msg, rocketSendCallback);
        } catch (Exception e) {
            log.error("异步发送消息失败", e);
            throw new MqSendException(e);
        }
        this.logMsg(msg);
    }

    /**
     * 异步发送
     *
     * @param topic topic
     * @param tag tag
     * @param content 字符串消息体
     */
    public void sendAsync(String topic, String tag, String content, SendCallback sendCallback) {
        this.sendAsync(topic, tag, "", content, sendCallback);
    }

    /**
     * 异步发送
     */
    public void sendAsync(String topic, String tag, String keys, String content, SendCallback sendCallback) {
        Message msg = getMessage(topic, tag, keys, content);
        try {
            rocketProducer.send(msg, sendCallback);
        } catch (Exception e) {
            log.error("异步发送消息失败", e);
            throw new MqSendException(e);
        }
        this.logMsg(msg);
    }

    /**
     * 同步发送
     *
     * @param topic topic
     * @param tag tag
     * @param content 字符串消息体
     * @return 可能返回null
     */
    public SendResult sendSync(String topic, String tag, String content) {
        return this.sendSync(topic, tag, "", content);
    }

    /**
     * 同步发送
     *
     * @param topic topic
     * @param tag tag
     * @param content 字符串消息体
     * @return 可能返回null
     */
    public SendResult sendSync(String topic, String tag, String keys, String content) {
        Message msg = getMessage(topic, tag, keys, content);
        try {
            SendResult sendResult = rocketProducer.send(msg);
            return sendResult;
        } catch (Exception e) {
            log.error("同步发送消息失败", e);
            throw new MqSendException(e);
        }
    }

    /**
     * 有顺序发送
     *
     * @param orderId 相同的orderId 的消息会被有顺序的消费
     */
    public SendResult sendOrderly(String topic, String tag, String content, int orderId) {
        return this.sendOrderly(topic, tag, "", content, orderId);
    }

    /**
     * 有顺序发送
     */
    public SendResult sendOrderly(String topic, String tag, String keys, String content, int orderId) {
        Message msg = getMessage(topic, tag, keys, content);
        try {
            SendResult sendResult = rocketProducer.send(msg, (List<MessageQueue> mqs, Message message, Object arg) -> {
                Integer id = (Integer) arg;
                int index = id % mqs.size();
                return mqs.get(index);
            }, orderId);
            this.logMsg(msg, sendResult);
            return sendResult;
        } catch (Exception e) {
            log.error("有顺序发送消息失败", e);
            throw new MqSendException(e);
        }
    }

    /**
     * 有顺序发送
     */
    public void sendOrderly(String topic, String tag, String keys, String content, int orderId, SendCallback sendCallback) {
        Message msg = getMessage(topic, tag, keys, content);
        try {
            rocketProducer.send(msg, (List<MessageQueue> mqs, Message message, Object arg) -> {
                Integer id = (Integer) arg;
                int index = id % mqs.size();
                return mqs.get(index);
            }, orderId, sendCallback);
        } catch (Exception e) {
            log.error("有顺序发送消息失败", e);
            throw new MqSendException(e);
        }
        this.logMsg(msg);
    }

    /**
     * 延迟发送
     */
    public SendResult sendDelay(String topic, String tags, String content, MqDelayLevel delayLevel) {
        return this.sendDelay(topic, tags, "", content, delayLevel);
    }

    /**
     * 延迟发送
     */
    public SendResult sendDelay(String topic, String tags, String keys, String content, MqDelayLevel delayLevel) {
        Message msg = getMessage(topic, tags, keys, content);
        try {
            msg.setDelayTimeLevel(delayLevel.getLevel());
            SendResult sendResult = rocketProducer.send(msg);
            this.logMsg(msg, sendResult);
            return sendResult;
        } catch (Exception e) {
            log.error("延迟发送消息失败", e);
            throw new MqSendException(e);
        }
    }

    /**
     * 延迟发送
     */
    public void sendDelay(String topic, String tags, String keys, String content, MqDelayLevel delayLevel, SendCallback sendCallback) {
        Message msg = getMessage(topic, tags, keys, content);
        try {
            msg.setDelayTimeLevel(delayLevel.getLevel());
            rocketProducer.send(msg, sendCallback);
        } catch (Exception e) {
            log.error("延迟发送消息失败", e);
            throw new MqSendException(e);
        }
        this.logMsg(msg);
    }

    /**
     * 构造message
     */
    private Message getMessage(String topic, String tag, String keys, String content) {
        return new Message(topic, tag, keys, content.getBytes());
    }

    /**
     * 打印消息topic等参数方便后续查找问题
     */
    private void logMsg(Message message) {
        log.info("消息队列发送完成：topic={}, tags={}, keys={}", message.getTopic(), message.getTags(), message.getKeys());
    }

    /**
     * 打印消息topic等参数方便后续查找问题
     */
    private void logMsg(Message message, SendResult sendResult) {
        log.info("消息队列发送完成：topic={}, tags={}, keys={}, sendResult={}", message.getTopic(), message.getTags(), message.getKeys(), sendResult);
    }

    class RocketSendCallback implements SendCallback {
        @Override
        public void onSuccess(SendResult sendResult) {
            log.info("send message success. topic={}, msgId={}", sendResult.getMessageQueue().getTopic(), sendResult.getMsgId());
        }

        @Override
        public void onException(Throwable e) {
            if (e instanceof MqContextException) {
                MqContextException context = (MqContextException) e;
                log.error("send message failed. topic={}, msgId={}", context.getTopic(), context.getMessageId());
            } else {
                log.error("send message failed.", e);
            }
        }
    }
}
