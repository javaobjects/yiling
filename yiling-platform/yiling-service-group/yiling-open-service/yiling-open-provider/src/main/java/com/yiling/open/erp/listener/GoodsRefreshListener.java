package com.yiling.open.erp.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.goods.medicine.dto.GoodsRefreshDTO;
import com.yiling.open.erp.service.ErpGoodsBatchService;
import com.yiling.open.erp.service.ErpGoodsCustomerPriceService;
import com.yiling.open.erp.service.ErpGoodsGroupPriceService;
import com.yiling.open.erp.service.ErpGoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/1/12
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GOODS_REFRESH, consumerGroup = Constants.TOPIC_GOODS_REFRESH)
public class GoodsRefreshListener implements MessageListener {

    @Autowired
    private ErpGoodsCustomerPriceService erpGoodsCustomerPriceService;
    @Autowired
    private ErpGoodsGroupPriceService erpGoodsGroupPriceService;
    @Autowired
    private ErpGoodsService erpGoodsService;

    @Autowired
    private ErpGoodsBatchService erpGoodsBatchService;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            GoodsRefreshDTO goodsRefreshDTO = JSON.parseObject(msg, GoodsRefreshDTO.class);
            erpGoodsCustomerPriceService.refreshErpInventoryList(goodsRefreshDTO.getInSnList(),Long.parseLong(String.valueOf(goodsRefreshDTO.getEid())));
            erpGoodsGroupPriceService.refreshErpInventoryList(goodsRefreshDTO.getInSnList(),Long.parseLong(String.valueOf(goodsRefreshDTO.getEid())));
            erpGoodsService.refreshErpInventoryList(goodsRefreshDTO.getInSnList(),Long.parseLong(String.valueOf(goodsRefreshDTO.getEid())));
            erpGoodsBatchService.refreshErpInventoryList(goodsRefreshDTO.getInSnList(),Long.parseLong(String.valueOf(goodsRefreshDTO.getEid())));
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
