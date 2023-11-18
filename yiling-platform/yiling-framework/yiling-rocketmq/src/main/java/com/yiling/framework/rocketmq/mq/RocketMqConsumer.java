package com.yiling.framework.rocketmq.mq;

import java.util.Map;

import cn.hutool.core.map.MapUtil;
import com.yiling.framework.common.util.AopTargetUtil;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.annotation.RocketMqOrderListener;
import com.yiling.framework.rocketmq.config.RocketMqProperties;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.GeneratorId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

/**
 * 消费者启动类
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
@Slf4j
public class RocketMqConsumer {

    public           ApplicationContext                 context;
    private volatile boolean                            init = false;
    private          RocketMqProperties                 configuration;
    private          Map<String, DefaultMQPushConsumer> consumerMap;

    public RocketMqConsumer(RocketMqProperties configuration, ApplicationContext context) {
        this.context = context;
        this.configuration = configuration;
    }

    public synchronized void start() throws Exception {
        if (this.init) {
            log.warn("请不要重复初始化RocketMQ消费者");
            return;
        }
        this.consumerMap = MapUtil.newConcurrentHashMap();
        initializeConsumer(this.consumerMap);
        init = true;
    }

    /**
     * 初始化消费者，同项目内不允许对同一个topic多次加载
     *
     * @param map 存储消费者
     */
    private void initializeConsumer(Map<String, DefaultMQPushConsumer> map) throws Exception {
        Map<String, String> topicMap = MapUtil.newHashMap();
        Map<String, String> consumerGroupMap = MapUtil.newHashMap();
        //初始化普通消息消费者
        initializeConcurrentlyConsumer(map, topicMap, consumerGroupMap);
        //初始化有序消息消费者
        initializeOrderConsumer(map, topicMap, consumerGroupMap);

        consumerMap.forEach((key, consumer) -> {
            try {
                String instanceName = System.currentTimeMillis() + GeneratorId.nextFormatId();
                consumer.setInstanceName(instanceName);
                consumer.start();
                log.info("自建RocketMQ成功加载 Topic-tag:{}", key);
            } catch (MQClientException e) {
                log.error("自建RocketMQ加载失败 Topic-tag:{}", key, e);
                throw new RuntimeException(e.getMessage(), e);
            }
        });

        log.info("-------------- 成功初始化所有消费者到自建mq --------------");
    }

    /**
     * 初始化普通消息消费者
     */
    private void initializeConcurrentlyConsumer(Map<String, DefaultMQPushConsumer> map, Map<String, String> topicMap,
                                                Map<String, String> consumerGroupMap) throws MQClientException {
        Map<String, Object> beansWithAnnotationMap = context.getBeansWithAnnotation(RocketMqListener.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
            Object bean = entry.getValue();
            if (AopUtils.isCglibProxy(bean)) {
                try {
                    bean = AopTargetUtil.getTarget(bean);
                } catch (Exception e) {
                    log.error("获取目标对象出错", e);
                    continue;
                }
            }

            // 获取到实例对象的class信息
            Class<?> classIns = bean.getClass();
            RocketMqListener rocketMqListenerAnnotaion = classIns.getDeclaredAnnotation(RocketMqListener.class);
            if (rocketMqListenerAnnotaion == null) {
                continue;
            }

            String topic = rocketMqListenerAnnotaion.topic();
            String tag = rocketMqListenerAnnotaion.tag();
            String consumerGroup = rocketMqListenerAnnotaion.consumerGroup();
            int maxThread = rocketMqListenerAnnotaion.maxThread();
            validate(topicMap, consumerGroupMap, classIns, topic, consumerGroup);

            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
            //设置名称服务器地址
            consumer.setNamesrvAddr(this.configuration.getNamesrvAddr());
            //最大重试消费次数
            consumer.setMaxReconsumeTimes(this.configuration.getMaxReconsumeTimes());
            //设置最大线程数据
            if (maxThread > 0 && maxThread < 20) {
                consumer.setConsumeThreadMin(maxThread);
                consumer.setConsumeThreadMax(maxThread);
            }
            consumer.subscribe(topic, tag);
            //注册消费回调
            consumer.registerMessageListener((MessageListenerConcurrently) (msgList, context) -> {
                try {
                    for (MessageExt msg : msgList) {
                        MessageListener listener = (MessageListener) entry.getValue();
                        MqAction action = listener.consume(msg, context);
                        switch (action) {
                            case ReconsumeLater:
                                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                            default:
                        }
                    }
                } catch (Exception e) {
                    log.error("消费失败", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            topicMap.put(topic, classIns.getSimpleName());
            consumerGroupMap.put(consumerGroup, classIns.getSimpleName());
            map.put(String.format("%s-%s", topic, tag), consumer);
        }
    }

    private void validate(Map<String, String> topicMap, Map<String, String> consumerGroupMap, Class<?> classIns,
                          String topic, String consumerGroup) {
        if (StringUtils.isBlank(topic)) {
            throw new RuntimeException(classIns.getSimpleName() + ":topic不能为空");
        }
        if (StringUtils.isBlank(consumerGroup)) {
            throw new RuntimeException(classIns.getSimpleName() + ":consumerGroup不能为空");
        }

        if (topicMap.containsKey(topic)) {
            throw new RuntimeException(
                    String.format("Topic:%s 已经由%s监听 请勿重复监听同一Topic", topic, classIns.getSimpleName()));
        }

        if (consumerGroupMap.containsKey(consumerGroup)) {
            throw new RuntimeException(String.format("consumerGroup:%s 已经由%s监听 请勿重复监听同一consumerGroup", consumerGroup,
                    classIns.getSimpleName()));
        }
    }

    /**
     * 初始化有序消息消费者
     */
    private void initializeOrderConsumer(Map<String, DefaultMQPushConsumer> map, Map<String, String> topicMap,
                                         Map<String, String> consumerGroupMap) throws MQClientException {
        Map<String, Object> beansWithAnnotationMap = context.getBeansWithAnnotation(RocketMqOrderListener.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
            Object bean = entry.getValue();
            if (AopUtils.isCglibProxy(bean)) {
                try {
                    bean = AopTargetUtil.getTarget(bean);
                } catch (Exception e) {
                    log.error("获取目标对象出错", e);
                    continue;
                }
            }

            // 获取到实例对象的class信息
            Class<?> classIns = bean.getClass();
            RocketMqOrderListener rocketMqListenerAnnotaion = classIns.getDeclaredAnnotation(RocketMqOrderListener.class);
            if (rocketMqListenerAnnotaion == null) {
                continue;
            }

            String topic = rocketMqListenerAnnotaion.topic();
            String tag = rocketMqListenerAnnotaion.tag();
            String consumerGroup = rocketMqListenerAnnotaion.consumerGroup();
            int maxThread = rocketMqListenerAnnotaion.maxThread();
            validate(topicMap, consumerGroupMap, classIns, topic, consumerGroup);
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
            //设置名称服务器地址
            consumer.setNamesrvAddr(this.configuration.getNamesrvAddr());
            //最大重试消费次数
            consumer.setMaxReconsumeTimes(this.configuration.getMaxReconsumeTimes());
            consumer.subscribe(topic, tag);
            //设置最大线程数据
            if (maxThread > 0 && maxThread < 20) {
                consumer.setConsumeThreadMin(maxThread);
                consumer.setConsumeThreadMax(maxThread);
            }
            //注册消费回调
            consumer.registerMessageListener((MessageListenerOrderly) (msgList, context) -> {
                try {
                    for (MessageExt msg : msgList) {
                        MessageOrderListener listener = (MessageOrderListener) entry.getValue();
                        MqAction action = listener.consume(msg, context);
                        switch (action) {
                            case ReconsumeLater:
                                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                            default:
                        }
                    }
                } catch (Exception e) {
                    log.error("消费失败", e);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
                return ConsumeOrderlyStatus.SUCCESS;
            });

            topicMap.put(topic, classIns.getSimpleName());
            consumerGroupMap.put(consumerGroup, classIns.getSimpleName());
            map.put(String.format("%s-%s", topic, tag), consumer);
        }
    }
}
