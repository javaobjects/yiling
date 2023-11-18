package com.yiling.open.erp.listener;

import java.text.MessageFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONArray;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.open.erp.util.ErpConstants;
import com.yiling.open.erp.dao.ErpGoodsBatchFlowMapper;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpGoodsBatchFlowHandler;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.ftp.service.impl.FlowFtpClientServiceImpl;
import com.yiling.open.util.LogUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 库存流向消息消费监听
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@Slf4j
@RocketMqListener(topic = "erp_goods_batch_flow", consumerGroup = "erp_goods_batch_flow")
public class ErpGoodsBatchFlowMqListener implements MessageListener {

    @Autowired
    private ErpGoodsBatchFlowHandler erpGoodsBatchFlowHandler;
    @Autowired
    private ErpGoodsBatchFlowMapper erpGoodsBatchFlowMapper;
    @Autowired
    private ErpClientService erpClientService;
    @DubboReference
    private FlowGoodsBatchApi flowGoodsBatchApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            LogUtil.info(MessageFormat.format(ErpConstants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
            Long suId = Long.parseLong(message.getTags());
            String keys = message.getKeys();
            Object dateObject = stringRedisTemplate.opsForHash().get(FlowFtpClientServiceImpl.key, message.getTags());
            if (dateObject != null) {
                String dateTime = String.valueOf(dateObject);
                log.info("suid={}库存对比时间，消息时间{}，缓存时间{}", suId, keys, dateTime);
                if (StrUtil.isNotEmpty(dateTime)) {
                    //删除时间
                    Date time = DateUtil.parseDateTime(dateTime);
                    Date keysTime = DateUtil.parseDateTime(keys);
                    if (time.getTime() > keysTime.getTime()) {
                        return MqAction.CommitMessage;
                    }
                }
            }
            erpGoodsBatchFlowHandler.handleGoodsBatchFlowMqSync(suId, ErpTopicName.ErpGoodsBatchFlow.getMethod(), JSONArray.parseArray(msg, ErpGoodsBatchFlowDTO.class));
        } catch (Exception e) {
            log.info(MessageFormat.format(ErpConstants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(),message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }

}
