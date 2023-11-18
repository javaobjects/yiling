package com.yiling.goods.inventory.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.dao.InventorySubscriptionMapper;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.dto.request.BatchSaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.InventoryLogRequest;
import com.yiling.goods.inventory.dto.request.QueryInventorySubscriptionRequest;
import com.yiling.goods.inventory.dto.request.SaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.UpdateSubscriptionQtyRequest;
import com.yiling.goods.inventory.entity.InventoryDO;
import com.yiling.goods.inventory.entity.InventorySubscriptionDO;
import com.yiling.goods.inventory.enums.InventoryErrorCode;
import com.yiling.goods.inventory.enums.InventoryLogEnum;
import com.yiling.goods.inventory.enums.InventorySubscriptionStatusEnum;
import com.yiling.goods.inventory.enums.SubscriptionTypeEnum;
import com.yiling.goods.inventory.service.GoodsInventoryLogService;
import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.inventory.service.InventorySubscriptionService;
import com.yiling.goods.medicine.entity.GoodsDO;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.medicine.service.GoodsService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 InventorySubscriptionServiceImpl
 * @描述
 * @创建时间 2022/7/25
 * @修改人 shichen
 * @修改时间 2022/7/25
 **/
@Service
@Slf4j
public class InventorySubscriptionServiceImpl extends BaseServiceImpl<InventorySubscriptionMapper, InventorySubscriptionDO> implements InventorySubscriptionService {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsInventoryLogService goodsInventoryLogService;

    @Autowired
    @Lazy
    InventorySubscriptionServiceImpl _this;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public List<InventorySubscriptionDTO> getInventorySubscriptionList(QueryInventorySubscriptionRequest request) {
        QueryWrapper<InventorySubscriptionDO> queryWrapper=new QueryWrapper();
        if(null!=request.getInventoryId() && request.getInventoryId() > 0){
            queryWrapper.lambda().eq(InventorySubscriptionDO::getInventoryId,request.getInventoryId());
        }
        if(null!=request.getEid() && request.getEid() > 0){
            queryWrapper.lambda().eq(InventorySubscriptionDO::getEid,request.getEid());
        }
        if(null!=request.getSubscriptionEid() && request.getSubscriptionEid() > 0){
            queryWrapper.lambda().eq(InventorySubscriptionDO::getSubscriptionEid,request.getSubscriptionEid());
        }
        if(CollectionUtil.isNotEmpty(request.getInSnList())){
            queryWrapper.lambda().in(InventorySubscriptionDO::getInSn,request.getInSnList());
        }
        if(null!=request.getStatus()){
            queryWrapper.lambda().eq(InventorySubscriptionDO::getStatus, request.getStatus());
        }
        List<Integer> typeList= Lists.newArrayList();
        if(null!=request.getSubscriptionType()){
            typeList.add(request.getSubscriptionType());
        }
        if(null!=request.getIsIncludeSelf()){
            if(request.getIsIncludeSelf()){
                typeList.add(SubscriptionTypeEnum.SELF.getType());
            }else {
                queryWrapper.lambda().ne(InventorySubscriptionDO::getSubscriptionType,SubscriptionTypeEnum.SELF.getType());
            }
        }
        if(CollectionUtil.isNotEmpty(typeList)){
            queryWrapper.lambda().in(InventorySubscriptionDO::getSubscriptionType,typeList);
        }
        return PojoUtils.map(this.list(queryWrapper),InventorySubscriptionDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSubscriptionList(BatchSaveSubscriptionRequest request) {
        if(null==request.getInventoryId()){
            throw new BusinessException(InventoryErrorCode.INVENTORY_MISSING,"绑定库存id不存在");
        }
        QueryInventorySubscriptionRequest queryRequest = new QueryInventorySubscriptionRequest();
        queryRequest.setInventoryId(request.getInventoryId());
        queryRequest.setIsIncludeSelf(false);
        List<InventorySubscriptionDTO> subscriptionDTOS = this.getInventorySubscriptionList(queryRequest);
        List<Long> subscriptionIdList = subscriptionDTOS.stream().map(InventorySubscriptionDTO::getId).collect(Collectors.toList());
        List<Long> deleteIdList = ListUtil.toList();
        if(CollectionUtil.isEmpty(request.getSubscriptionList())){
            if(CollectionUtil.isNotEmpty(subscriptionIdList)){
                deleteIdList=subscriptionIdList;
            }
        }else {
            List<Long> saveIdList = request.getSubscriptionList().stream().filter(e -> null != e.getId()).map(SaveSubscriptionRequest::getId).collect(Collectors.toList());
            deleteIdList = (List<Long>)CollectionUtil.subtract(subscriptionIdList, saveIdList);
            List<InventorySubscriptionDO> saveOrUpdateList = PojoUtils.map(request.getSubscriptionList(), InventorySubscriptionDO.class);
            saveOrUpdateList.forEach(item->{
                item.setOpUserId(request.getOpUserId());
                item.setOpTime(request.getOpTime());
                item.setEid(request.getEid());
                item.setInventoryId(request.getInventoryId());
            });
            this.saveOrUpdateBatch(saveOrUpdateList);
        }
        if(CollectionUtil.isNotEmpty(deleteIdList)){
            InventorySubscriptionDO subscriptionDO = new InventorySubscriptionDO();
            subscriptionDO.setOpUserId(request.getOpUserId());
            subscriptionDO.setOpTime(request.getOpTime());
            QueryWrapper<InventorySubscriptionDO> deleteWrapper=new QueryWrapper();
            deleteWrapper.lambda().in(InventorySubscriptionDO::getId,deleteIdList);
            this.batchDeleteWithFill(subscriptionDO,deleteWrapper);
        }
        this.updateQtyBySubscription(request.getInventoryId(),request.getOpUserId());
        return true;
    }

    /**
     * 更新当前库存id下的所有订阅库存，并刷新总库存
     * @param inventoryId
     * @param opUserId
     * @return
     */
    private Boolean updateQtyBySubscription(Long inventoryId,Long opUserId){
        QueryInventorySubscriptionRequest queryRequest = new QueryInventorySubscriptionRequest();
        queryRequest.setInventoryId(inventoryId);
        queryRequest.setIsIncludeSelf(false);
        List<InventorySubscriptionDTO> subscriptionList = this.getInventorySubscriptionList(queryRequest);
        if(CollectionUtil.isEmpty(subscriptionList)){
            //更新当前库存id的总库存数量
            this.refreshQtyBySubscription(inventoryId,opUserId);
            return true;
        }
        List<InventorySubscriptionDO> updateQtyList = Lists.newLinkedList();
        subscriptionList.forEach(subscription->{
            //只更新pop订阅的库存数量，erp绑定关系暂不更新库存数量，等待下次erp同步后更新
            if(SubscriptionTypeEnum.POP.getType().equals(subscription.getSubscriptionType())){
                QueryWrapper<InventorySubscriptionDO> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().eq(InventorySubscriptionDO::getSubscriptionEid,subscription.getSubscriptionEid())
                        .eq(InventorySubscriptionDO::getInSn,subscription.getInSn())
                        .eq(InventorySubscriptionDO::getSubscriptionType,SubscriptionTypeEnum.SELF.getType())
                        .last(" limit 1");
                InventorySubscriptionDO one = this.getOne(queryWrapper);
                InventorySubscriptionDO updateDO = new InventorySubscriptionDO();
                //查询该订阅关系是否存在本店订阅关系
                if(null!=one){
                    InventoryDO inventoryDO = inventoryService.getById(one.getInventoryId());
                    Long saleQty=inventoryDO.getQty()>inventoryDO.getFrozenQty()? inventoryDO.getQty()-inventoryDO.getFrozenQty():0L;
                    updateDO.setId(subscription.getId());
                    updateDO.setQty(saleQty);
                    updateDO.setOpUserId(opUserId);
                }else {
                    updateDO.setId(subscription.getId());
                    updateDO.setQty(0L);
                    updateDO.setOpUserId(opUserId);
                }
                updateQtyList.add(updateDO);
            }
        });
        if(CollectionUtil.isNotEmpty(updateQtyList)){
            //更新订阅关系的库存
            this.updateBatchById(updateQtyList);
        }
        //更新当前库存id的总库存数量
        this.refreshQtyBySubscription(inventoryId,opUserId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSelfSubscription(SaveSubscriptionRequest request) {
        GoodsDO goodsDO = goodsService.getById(request.getGoodsId());
        if(null==goodsDO){
            return false;
        }
        if(!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDO.getAuditStatus())){
            return false;
        }
        request.setEid(goodsDO.getEid());
        request.setSubscriptionEid(goodsDO.getEid());
        request.setSubscriptionEname(goodsDO.getEname());

        QueryWrapper<InventorySubscriptionDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(InventorySubscriptionDO::getInventoryId,request.getInventoryId());
        queryWrapper.lambda().eq(InventorySubscriptionDO::getSubscriptionType,SubscriptionTypeEnum.SELF.getType());
        InventorySubscriptionDO one = this.getOne(queryWrapper);
        if(null!=one){
            request.setId(one.getId());
        }
        InventorySubscriptionDO subscriptionDO = PojoUtils.map(request, InventorySubscriptionDO.class);
        subscriptionDO.setSubscriptionType(SubscriptionTypeEnum.SELF.getType());
        if(null!=subscriptionDO.getId() && subscriptionDO.getId() > 0){
            this.updateById(subscriptionDO);
            //以岭本部切换子公司情况,刷新改库存id下所有订阅关系的eid
            if(!request.getEid().equals(one.getEid())){
                //查询该库存id下非本店订阅以外的订阅关系
                QueryWrapper<InventorySubscriptionDO> wrapper = new QueryWrapper();
                wrapper.lambda().eq(InventorySubscriptionDO::getInventoryId,request.getInventoryId());
                wrapper.lambda().ne(InventorySubscriptionDO::getSubscriptionType,SubscriptionTypeEnum.SELF.getType());
                List<InventorySubscriptionDO> doList = this.list(wrapper);
                if(CollectionUtil.isNotEmpty(doList)){
                    List<InventorySubscriptionDO> updateEidDOList;
                    List<Long> deleteIdList=doList.stream().filter(subscription -> request.getEid().equals(subscription.getSubscriptionEid())).map(InventorySubscriptionDO::getId).collect(Collectors.toList());
                    //若切换的公司eid等于被订阅方eid，则要删除，防止自己订阅自己
                    if(CollectionUtil.isNotEmpty(deleteIdList)){
                        InventorySubscriptionDO deleteDO = new InventorySubscriptionDO();
                        QueryWrapper<InventorySubscriptionDO> deleteWrapper = new QueryWrapper();
                        deleteWrapper.lambda().in(InventorySubscriptionDO::getId,deleteIdList);
                        this.batchDeleteWithFill(deleteDO,deleteWrapper);
                        updateEidDOList = doList.stream().filter(subscription -> !request.getEid().equals(subscription.getSubscriptionEid())).collect(Collectors.toList());
                    }else {
                        updateEidDOList = doList;
                    }
                    //更新eid
                    updateEidDOList.forEach(subscription->{
                        subscription.setEid(request.getEid());
                    });
                    this.updateBatchById(updateEidDOList);
                }
            }
        }else {
            this.save(subscriptionDO);
        }
        return true;
    }

    @Override
    public Long getTotalInventory(Long inventoryId) {
        QueryInventorySubscriptionRequest request = new QueryInventorySubscriptionRequest();
        request.setInventoryId(inventoryId);
        request.setStatus(InventorySubscriptionStatusEnum.NORMAL.getCode());
        List<InventorySubscriptionDTO> subscriptionList = this.getInventorySubscriptionList(request);
        if(CollectionUtil.isEmpty(subscriptionList)){
            return 0L;
        }
        Long qty = subscriptionList.stream().collect(Collectors.summingLong(InventorySubscriptionDTO::getQty));
        return qty;
    }

    @Override
    public List<InventorySubscriptionDTO> getSelfSubscriptionByInventoryIds(List<Long> inventoryIds) {
        if(CollectionUtil.isEmpty(inventoryIds)){
            return Lists.newArrayList();
        }
        QueryWrapper<InventorySubscriptionDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(InventorySubscriptionDO::getInventoryId,inventoryIds)
                .eq(InventorySubscriptionDO::getSubscriptionType,SubscriptionTypeEnum.SELF.getType());
        return PojoUtils.map(this.list(queryWrapper),InventorySubscriptionDTO.class);
    }

    @Override
    public Boolean batchUpdateSubscriptionQty(List<SaveSubscriptionRequest> requestList) {
        if(CollectionUtil.isEmpty(requestList)){
            return true;
        }
        List<InventorySubscriptionDO> subscriptionDOList = requestList.stream().map(subscriptionRequest->{
            InventorySubscriptionDO subscriptionDO = new InventorySubscriptionDO();
            subscriptionDO.setId(subscriptionRequest.getId());
            subscriptionDO.setQty(subscriptionRequest.getQty());
            return subscriptionDO;
        }).collect(Collectors.toList());
        //更新订阅关系库存数量
        boolean b = this.updateBatchById(subscriptionDOList);
        if(b){
            //更新订阅库存成功后，通过mq重新汇总库存表的总库存数量
            List<Long> inventoryIds = requestList.stream().map(SaveSubscriptionRequest::getInventoryId).filter(id->null!=id && id > 0).distinct().collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(inventoryIds)){
                String inventoryIdsJSON = JSON.toJSONString(inventoryIds);
                this.sendMq(Constants.TOPIC_QTY_REFRESH_BY_SUBSCRIPTION,Constants.TAG_QTY_REFRESH_BY_SUBSCRIPTION,inventoryIdsJSON);
            }
        }
        return b;
    }

    @Override
    public InventoryDTO refreshQtyBySubscription(Long inventoryId,Long opUserId) {
        InventoryDO getDO = inventoryService.getById(inventoryId);
        if(null!=getDO){
            //汇总所有可用订阅关系库存
            Long totalQty = this.getTotalInventory(inventoryId);
            //汇总和当前库存不一致的时候更新
            if(!totalQty.equals(getDO.getQty())){
                InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
                inventoryLogRequest.setGid(getDO.getGid());
                inventoryLogRequest.setInventoryId(inventoryId);
                inventoryLogRequest.setChangeQty(totalQty);
                inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.SUBSCRIPTION_MODIFY);
                inventoryLogRequest.setOpUserId(opUserId);
                goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
                InventoryDO saveDO = new InventoryDO();
                saveDO.setId(getDO.getId());
                saveDO.setQty(totalQty);
                saveDO.setOpUserId(opUserId);
                inventoryService.updateById(saveDO);
                getDO.setQty(totalQty);
            }
        }
        return PojoUtils.map(getDO,InventoryDTO.class);
    }

    @Override
    public void refreshSubscriptionQtyByPop(UpdateSubscriptionQtyRequest request) {
        QueryInventorySubscriptionRequest queryRequest = new QueryInventorySubscriptionRequest();
        queryRequest.setSubscriptionEid(request.getSubscriptionEid());
        queryRequest.setInSnList(Lists.newArrayList(request.getInSn()));
        //只查询作为被订阅方的订阅库存
        queryRequest.setSubscriptionType(SubscriptionTypeEnum.POP.getType());
        queryRequest.setStatus(InventorySubscriptionStatusEnum.NORMAL.getCode());
        //获取订阅该企业的pop订阅
        List<InventorySubscriptionDTO> popSubscriptionList = this.getInventorySubscriptionList(queryRequest);
        log.info("刷新pop订阅库存[refreshSubscriptionQtyByPop] 请求参数：{} ,刷新的订阅关系：{}", JSONUtil.toJsonStr(request),JSONUtil.toJsonStr(popSubscriptionList));
        if(CollectionUtil.isNotEmpty(popSubscriptionList)){
            List<SaveSubscriptionRequest> saveRequestList = Lists.newArrayList();
            popSubscriptionList.forEach(subscription -> {
                if(!request.getQty().equals(subscription.getQty())){
                    SaveSubscriptionRequest saveRequest = PojoUtils.map(subscription, SaveSubscriptionRequest.class);
                    saveRequest.setQty(request.getQty());
                    saveRequestList.add(saveRequest);
                }
            });
            //更新订阅关系的库存
            this.batchUpdateSubscriptionQty(saveRequestList);
        }
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic,topicTag,msg);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg) {

        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

}
