package com.yiling.goods.inventory.listener;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.JsonUtil;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.UpdateSubscriptionQtyRequest;
import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.inventory.service.InventorySubscriptionService;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.entity.GoodsDO;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.medicine.service.GoodsSkuService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 SubscriptionQtyRefreshListener
 * @描述
 * @创建时间 2022/11/22
 * @修改人 shichen
 * @修改时间 2022/11/22
 **/
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_QTY_REFRESH_BY_SUBSCRIPTION, consumerGroup = Constants.TAG_QTY_REFRESH_BY_SUBSCRIPTION)
public class QtyRefreshBySubscriptionListener extends AbstractMessageListener {
    @Autowired
    private InventorySubscriptionService inventorySubscriptionService;

    @Autowired
    private GoodsSkuService goodsSkuService;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);
        List<Long> inventoryIds = JSON.parseArray(body).toJavaList(Long.class);
        if(CollectionUtil.isNotEmpty(inventoryIds)){
            inventoryIds.forEach(inventoryId->{
                //刷新指定库存id库存数据
                InventoryDTO inventoryDTO = inventorySubscriptionService.refreshQtyBySubscription(inventoryId,0L);
                //当该库存数据更新，同步更新该库存作为被订阅方库存时影响的pop订阅库存数据
                if(null!=inventoryDTO && StringUtils.isNotBlank(inventoryDTO.getInSn())){
                    List<GoodsSkuDTO> skuList = goodsSkuService.getGoodsSkuByGidAndInventoryId(inventoryDTO.getGid(), inventoryDTO.getId());
                    if(CollectionUtil.isNotEmpty(skuList)){
                        Long saleQty=inventoryDTO.getQty()>inventoryDTO.getFrozenQty()? inventoryDTO.getQty()-inventoryDTO.getFrozenQty():0L;
                        UpdateSubscriptionQtyRequest request = new UpdateSubscriptionQtyRequest();
                        request.setSubscriptionEid(skuList.get(0).getEid());
                        request.setInSn(inventoryDTO.getInSn());
                        request.setQty(saleQty);
                        inventorySubscriptionService.refreshSubscriptionQtyByPop(request);
                    }
                }
            });
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
}
