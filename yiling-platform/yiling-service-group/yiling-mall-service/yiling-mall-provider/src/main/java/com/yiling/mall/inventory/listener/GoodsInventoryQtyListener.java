package com.yiling.mall.inventory.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.api.InventorySubscriptionApi;
import com.yiling.goods.inventory.bo.GoodsInventoryQtyBO;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.UpdateSubscriptionQtyRequest;
import com.yiling.goods.inventory.enums.SubscriptionTypeEnum;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.search.goods.api.EsGoodsSearchApi;
import com.yiling.search.goods.dto.request.EsGoodsInventoryIndexRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费监听
 *
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = "goods_inventory_qty", consumerGroup = "goods_inventory_qty")
public class GoodsInventoryQtyListener implements MessageListener {

    @DubboReference
    private InventoryApi     inventoryApi;
    @DubboReference
    private B2bGoodsApi      b2bGoodsApi;
    @DubboReference
    private GoodsApi         goodsApi;
    @DubboReference(async = true)
    private EsGoodsSearchApi esGoodsSearchApi;
    @DubboReference
    private InventorySubscriptionApi inventorySubscriptionApi;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            Boolean jsonBool = JSONUtil.isJson(msg);
            if (jsonBool) {
                this.refreshInventoryIndexByJson(msg);
                log.info("成功");
            } else {
                for (String gid : msg.split(",")) {
                    if (NumberUtil.isLong(gid)) {
                        this.refreshInventoryIndexByGid(Long.parseLong(gid));
                        log.info("成功");
                    }
                }
            }

            return MqAction.CommitMessage;
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
        }
        return MqAction.ReconsumeLater;
    }

    public void refreshInventoryIndexByGid(Long gid) {
        GoodsDTO goodsDTO = goodsApi.queryInfo(gid);
        if (!goodsDTO.getAuditStatus().equals(GoodsStatusEnum.AUDIT_PASS.getCode())) {
            return;
        }
        this.refreshPopSubscription(goodsDTO.getId());
        List<GoodsSkuDTO> skuDTOS = goodsApi.getGoodsSkuByGoodsId(goodsDTO.getId());
        List<GoodsSkuDTO> b2bSkuList = skuDTOS.stream().filter(sku -> GoodsLineEnum.B2B.getCode().equals(sku.getGoodsLine())).collect(Collectors.toList());
        if(CollUtil.isEmpty(b2bSkuList)){
            return;
        }else {
            Integer hasB2bStock = 0;
            for(GoodsSkuDTO b2bsku:b2bSkuList){
                if(null!=b2bsku.getQty() && null!=b2bsku.getFrozenQty()){
                    if((b2bsku.getQty()-b2bsku.getFrozenQty()) > 0){
                        hasB2bStock = 1;
                    }
                }
            }
            EsGoodsInventoryIndexRequest request = new EsGoodsInventoryIndexRequest();
            request.setHasB2bStock(hasB2bStock);
            request.setGoodsId(gid);
            esGoodsSearchApi.updateQtyFlag(request);
        }
    }

    public void refreshInventoryIndexByJson(String json) {
        GoodsInventoryQtyBO goodsInventoryQtyBO = JSON.parseObject(json, GoodsInventoryQtyBO.class);
        GoodsDTO goodsDTO = goodsApi.queryInfo(goodsInventoryQtyBO.getGid());
        if (!goodsDTO.getAuditStatus().equals(GoodsStatusEnum.AUDIT_PASS.getCode())) {
            return;
        }
        this.refreshPopSubscription(goodsDTO.getId());
        List<GoodsSkuDTO> skuDTOS = goodsApi.getGoodsSkuByGoodsId(goodsDTO.getId());
        List<GoodsSkuDTO> b2bSkuList = skuDTOS.stream().filter(sku -> GoodsLineEnum.B2B.getCode().equals(sku.getGoodsLine())).collect(Collectors.toList());
        if(CollUtil.isEmpty(b2bSkuList)){
            return;
        }else {
            Integer hasB2bStock = 0;
            for(GoodsSkuDTO b2bsku:b2bSkuList){
                if(null!=b2bsku.getQty() && null!=b2bsku.getFrozenQty()){
                    if((b2bsku.getQty()-b2bsku.getFrozenQty()) > 0){
                        hasB2bStock = 1;
                    }
                }
            }
            EsGoodsInventoryIndexRequest request = new EsGoodsInventoryIndexRequest();
            request.setHasB2bStock(hasB2bStock);
            request.setGoodsId(goodsDTO.getId());
            esGoodsSearchApi.updateQtyFlag(request);
        }
    }

    /**
     * 刷新pop订阅
     * @param gid
     */
    private void refreshPopSubscription(Long gid){
        List<GoodsSkuDTO> skuDTOS = goodsApi.getGoodsSkuByGoodsIdAndStatus(gid, ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode(),GoodsSkuStatusEnum.HIDE.getCode()));
        skuDTOS.forEach(skuDTO -> {
            if(StringUtils.isNotBlank(skuDTO.getInSn())){
                UpdateSubscriptionQtyRequest updateSubscriptionQtyRequest = new UpdateSubscriptionQtyRequest();
                updateSubscriptionQtyRequest.setInventoryId(skuDTO.getInventoryId());
                updateSubscriptionQtyRequest.setSubscriptionEid(skuDTO.getEid());
                updateSubscriptionQtyRequest.setInSn(skuDTO.getInSn());
                Long usableQty = skuDTO.getQty() > skuDTO.getFrozenQty() ? skuDTO.getQty() - skuDTO.getFrozenQty() : 0L;
                updateSubscriptionQtyRequest.setQty(usableQty);
                updateSubscriptionQtyRequest.setSubscriptionTypeEnum(SubscriptionTypeEnum.POP);
                inventorySubscriptionApi.refreshSubscriptionQtyByPop(updateSubscriptionQtyRequest);
            }
        });
    }
}
