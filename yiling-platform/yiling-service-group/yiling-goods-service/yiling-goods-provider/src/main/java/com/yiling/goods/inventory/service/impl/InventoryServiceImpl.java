package com.yiling.goods.inventory.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.goods.inventory.bo.GoodsInventoryQtyBO;
import com.yiling.goods.inventory.dao.InventoryMapper;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.inventory.dto.request.InventoryLogRequest;
import com.yiling.goods.inventory.dto.request.SaveInventoryRequest;
import com.yiling.goods.inventory.dto.request.SaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.UpdateSubscriptionQtyRequest;
import com.yiling.goods.inventory.entity.InventoryDO;
import com.yiling.goods.inventory.enums.InventoryErrorCode;
import com.yiling.goods.inventory.enums.InventoryLogEnum;
import com.yiling.goods.inventory.enums.SubscriptionTypeEnum;
import com.yiling.goods.inventory.service.GoodsInventoryLogService;
import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.inventory.service.InventorySubscriptionService;
import com.yiling.goods.medicine.entity.GoodsSkuDO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.medicine.service.GoodsSkuService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 商品库存表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-18
 */
@Service
public class InventoryServiceImpl extends BaseServiceImpl<InventoryMapper, InventoryDO> implements InventoryService {

    @Autowired
    GoodsService    goodsService;
    @Autowired
    GoodsSkuService goodsSkuService;
    @Autowired
    InventorySubscriptionService inventorySubscriptionService;
    @Autowired
    private GoodsInventoryLogService goodsInventoryLogService;
    @Autowired
    @Lazy
    InventoryServiceImpl _this;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public Long save(SaveInventoryRequest request) {
        InventoryDO entity = PojoUtils.map(request, InventoryDO.class);
        Long id = entity.getId();

        //更新本店订阅库存基础数据
        SaveSubscriptionRequest saveSelfSubscriptionRequest = new SaveSubscriptionRequest();
        saveSelfSubscriptionRequest.setInSn(request.getInSn());
        saveSelfSubscriptionRequest.setQty(request.getQty());
        saveSelfSubscriptionRequest.setGoodsId(request.getGid());
        saveSelfSubscriptionRequest.setInventoryId(id);

        if (id == null || id == 0) {
            //新增库存是需要判断是否已经存在内码了
            InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
            inventoryLogRequest.setGid(request.getGid());
            inventoryLogRequest.setChangeQty(request.getQty());
            inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
            inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.INSTOCK);
            inventoryLogRequest.setOpUserId(request.getOpUserId());
            if (StrUtil.isNotEmpty(request.getInSn())) {
                QueryWrapper<InventoryDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(InventoryDO::getGid, request.getGid()).eq(InventoryDO::getInSn, request.getInSn()).last(" limit 1");
                InventoryDO inventoryDO = this.getOne(queryWrapper);
                if (null!=inventoryDO) {
                    if(request.getQty() != null){
                        entity.setId(inventoryDO.getId());
                        //修改库存时，先更新本店订阅库存，再获取库存总计，最后进行库存修改操作
                        saveSelfSubscriptionRequest.setInventoryId(inventoryDO.getId());
                        inventorySubscriptionService.saveSelfSubscription(saveSelfSubscriptionRequest);
                        Long totalQty = inventorySubscriptionService.getTotalInventory(inventoryDO.getId());
                        //插入日志
                        inventoryLogRequest.setChangeQty(totalQty);
                        inventoryLogRequest.setInventoryId(inventoryDO.getId());
                        inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.MODIFY);
                        goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
                        //更新库存数据
                        entity.setQty(totalQty);
                        this.updateById(entity);
                    }
                    return inventoryDO.getId();
                }
            }
            //插入日志
            inventoryLogRequest.setInventoryId(0L);
            inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.INSTOCK);
            goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
            //保存库存数据
            this.save(entity);
            id = entity.getId();
            //新增库存时，先新增库存数据再更新本店库存订阅数据
            saveSelfSubscriptionRequest.setInventoryId(id);
            inventorySubscriptionService.saveSelfSubscription(saveSelfSubscriptionRequest);
        } else {
            inventorySubscriptionService.saveSelfSubscription(saveSelfSubscriptionRequest);
            if (request.getQty() != null) {
                //修改库存时，先更新本店订阅库存，再获取库存总计，最后进行库存修改操作
                Long totalQty = inventorySubscriptionService.getTotalInventory(id);
                //插入日志
                InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
                inventoryLogRequest.setInventoryId(id);
                inventoryLogRequest.setGid(request.getGid());
                inventoryLogRequest.setChangeQty(totalQty);
                inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
                inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.MODIFY);
                inventoryLogRequest.setOpUserId(request.getOpUserId());
                goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
                //更新库存数据
                entity.setQty(totalQty);
            }
            this.updateById(entity);
        }
        return id;
    }

    @Override
    public InventoryDTO getBySkuId(Long skuId) {
        GoodsSkuDO goodsSkuDO = goodsSkuService.getById(skuId);
        if(null == goodsSkuDO){
            return null;
        }
        InventoryDO inventoryDO = this.getById(goodsSkuDO.getInventoryId());
        return PojoUtils.map(inventoryDO, InventoryDTO.class);
    }

    @Override
    public InventoryDTO getByGidAndInSn(Long gid, String inSn) {
        if(StringUtils.isBlank(inSn)){
            return null;
        }
        QueryWrapper<InventoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InventoryDO::getGid, gid).eq(InventoryDO::getInSn, inSn);
        queryWrapper.lambda().last(" limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),InventoryDTO.class);
    }

    @Override
    public List<InventoryDO> getB2BListByGid(List<Long> goodsIds) {
        QueryWrapper<GoodsSkuDO> skuWrapper = new QueryWrapper<>();
        skuWrapper.lambda().in(GoodsSkuDO::getGoodsId,goodsIds)
                .eq(GoodsSkuDO::getStatus, GoodsSkuStatusEnum.NORMAL.getCode())
                .eq(GoodsSkuDO::getGoodsLine, GoodsLineEnum.B2B.getCode());
        List<GoodsSkuDO> skuDOList = goodsSkuService.getBaseMapper().selectList(skuWrapper);
        if(CollectionUtil.isNotEmpty(skuDOList)){
            List<Long> inventoryIds = skuDOList.stream().map(GoodsSkuDO::getInventoryId).distinct().collect(Collectors.toList());
            return this.listByIds(inventoryIds);
        }
        return ListUtil.empty();
    }

    @Override
    public Map<Long, List<InventoryDTO>> getInventoryMapByGids(List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }
        List<InventoryDO> inventoryDOList = this.getB2BListByGid(goodsIds);
        List<InventoryDTO> inventoryDTOList = PojoUtils.map(inventoryDOList, InventoryDTO.class);
        return inventoryDTOList.stream().collect(Collectors.groupingBy(InventoryDTO::getGid));
    }

    @Override
    public Map<Long, Long> getAvailableQtyByGoodsIds(List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }
        Map<Long, Long> map = new HashMap<>();
        List<InventoryDO> inventoryDOList = this.getB2BListByGid(goodsIds);
        Map<Long, List<InventoryDO>> inventoryMap = inventoryDOList.stream().collect(Collectors.groupingBy(InventoryDO::getGid));
        for (Long goodsId : goodsIds) {
            List<InventoryDO> list = inventoryMap.get(goodsId);
            Long qty = 0L;
            if (CollUtil.isNotEmpty(list)) {
                for (InventoryDO inventoryDO : list) {
                    Long availableQty = inventoryDO.getQty() - inventoryDO.getFrozenQty();
                    if (availableQty >= 0) {
                        qty = qty + availableQty;
                    }
                }
            }
            map.put(goodsId, qty);
        }
        return map;
    }

    @Override
    public Map<Long, InventoryDTO> getMapBySkuIds(List<Long> skuIds) {
        Map<Long, InventoryDTO> map = new HashMap<>();
        List<GoodsSkuDO> list = goodsSkuService.listByIds(skuIds);
        if(CollectionUtils.isEmpty(list)){
            return map;
        }
        List<Long> inventorIds = list.stream().map(e -> e.getInventoryId()).collect(Collectors.toList());
        List<InventoryDO> inventoryDOList = this.listByIds(inventorIds);
        Map<Long, InventoryDO> inventoryDOMap = inventoryDOList.stream().collect(Collectors.toMap(InventoryDO::getId, Function.identity()));
        for (GoodsSkuDO goodsSkuDO : list) {
            InventoryDTO inventoryDTO = PojoUtils.map(inventoryDOMap.get(goodsSkuDO.getInventoryId()), InventoryDTO.class);
            inventoryDTO.setSkuId(goodsSkuDO.getId());
            map.put(goodsSkuDO.getId(), inventoryDTO);
        }
        return map;
    }

    @Override
    public List<InventoryDTO> getInventoryByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(this.listByIds(ids), InventoryDTO.class);
    }

    @Override
    public int addFrozenQty(AddOrSubtractQtyRequest request) {
        //如果扣减的skuId对应的库存Id
        GoodsSkuDO goodsSkuDO = goodsSkuService.getById(request.getSkuId());
        if (goodsSkuDO == null) {
            throw new BusinessException(InventoryErrorCode.INVENTORY_SKU_MISSING);
        }
        InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
        inventoryLogRequest.setGid(goodsSkuDO.getGoodsId());
        inventoryLogRequest.setInventoryId(goodsSkuDO.getInventoryId());
        inventoryLogRequest.setBusinessNo(request.getOrderNo());
        inventoryLogRequest.setChangeQty(0L);
        inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
        inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.FROZEN);
        inventoryLogRequest.setOpUserId(request.getOpUserId());

        goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
        return this.baseMapper.addFrozenQty(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchAddFrozenQty(List<AddOrSubtractQtyRequest> requestList) {

        if (CollUtil.isEmpty(requestList)) {
            return 0;
        }

        for (AddOrSubtractQtyRequest request : requestList) {
            //如果扣减的skuId对应的库存Id
            GoodsSkuDO goodsSkuDO = goodsSkuService.getById(request.getSkuId());
            if (goodsSkuDO == null) {
                throw new BusinessException(InventoryErrorCode.INVENTORY_SKU_MISSING);
            }
            request.setInventoryId(goodsSkuDO.getInventoryId());
            InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
            inventoryLogRequest.setGid(goodsSkuDO.getGoodsId());
            inventoryLogRequest.setInventoryId(goodsSkuDO.getInventoryId());
            inventoryLogRequest.setBusinessNo(request.getOrderNo());
            inventoryLogRequest.setChangeQty(0L);
            inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
            inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.FROZEN);
            inventoryLogRequest.setOpUserId(request.getOpUserId());

            goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
            if (this.baseMapper.addFrozenQty(request) <= 0) {
                throw new BusinessException(InventoryErrorCode.INVENTORY_MISSING);
            }
        }
        return 1;
    }

    @Override
    public int subtractFrozenQtyAndQty(AddOrSubtractQtyRequest request) {
        //如果扣减的skuId对应的库存Id
        GoodsSkuDO goodsSkuDO = goodsSkuService.getById(request.getSkuId());
        if (goodsSkuDO == null) {
            throw new BusinessException(InventoryErrorCode.INVENTORY_SKU_MISSING);
        }
        request.setInventoryId(goodsSkuDO.getInventoryId());

        InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
        inventoryLogRequest.setGid(goodsSkuDO.getGoodsId());
        inventoryLogRequest.setInventoryId(goodsSkuDO.getInventoryId());
        inventoryLogRequest.setBusinessNo(request.getOrderNo());
        inventoryLogRequest.setChangeQty(request.getQty());
        inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
        inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.OUTSTOCK);
        inventoryLogRequest.setOpUserId(request.getOpUserId());

        goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
        return this.baseMapper.subtractFrozenQtyAndQty(request);
    }

    @Override
    public int subtractFrozenQty(AddOrSubtractQtyRequest request) {
        //如果扣减的skuId对应的库存Id
        GoodsSkuDO goodsSkuDO = goodsSkuService.getById(request.getSkuId());
        if (goodsSkuDO == null) {
            throw new BusinessException(InventoryErrorCode.INVENTORY_SKU_MISSING);
        }
        request.setInventoryId(goodsSkuDO.getInventoryId());

        InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
        inventoryLogRequest.setGid(goodsSkuDO.getGoodsId());
        inventoryLogRequest.setInventoryId(goodsSkuDO.getInventoryId());
        inventoryLogRequest.setBusinessNo(request.getOrderNo());
        inventoryLogRequest.setChangeQty(0L);
        inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
        inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.UNFROZEN);
        inventoryLogRequest.setOpUserId(request.getOpUserId());

        goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
        return this.baseMapper.subtractFrozenQty(request);
    }

    @Override
    @Transactional
    public int batchSubtractFrozenQty(List<AddOrSubtractQtyRequest> requestList) {
        if (CollUtil.isEmpty(requestList)) {
            return 0;
        }
        for(AddOrSubtractQtyRequest request : requestList){
            GoodsSkuDO goodsSkuDO = goodsSkuService.getById(request.getSkuId());
            if (goodsSkuDO == null) {
                throw new BusinessException(InventoryErrorCode.INVENTORY_SKU_MISSING);
            }
            request.setInventoryId(goodsSkuDO.getInventoryId());

            InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
            inventoryLogRequest.setGid(goodsSkuDO.getGoodsId());
            inventoryLogRequest.setInventoryId(goodsSkuDO.getInventoryId());
            inventoryLogRequest.setBusinessNo(request.getOrderNo());
            inventoryLogRequest.setChangeQty(0L);
            inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
            inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.BATCH_UNFROZEN);
            inventoryLogRequest.setOpUserId(request.getOpUserId());

            goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
            if (this.baseMapper.subtractFrozenQty(request) <= 0) {
                throw new BusinessException(InventoryErrorCode.INVENTORY_MISSING,"退还冻结失败");
            }
        }
        return 1;
    }

    @Override
    public int backFrozenQtyAndQty(AddOrSubtractQtyRequest request) {
        //如果扣减的skuId对应的库存Id
        GoodsSkuDO goodsSkuDO = goodsSkuService.getById(request.getSkuId());
        if (goodsSkuDO == null) {
            throw new BusinessException(InventoryErrorCode.INVENTORY_SKU_MISSING);
        }
        request.setInventoryId(goodsSkuDO.getInventoryId());

        InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
        inventoryLogRequest.setGid(goodsSkuDO.getGoodsId());
        inventoryLogRequest.setInventoryId(goodsSkuDO.getInventoryId());
        inventoryLogRequest.setBusinessNo(request.getOrderNo());
        inventoryLogRequest.setChangeQty(request.getQty());
        inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
        inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.BACKSTOCK);
        inventoryLogRequest.setOpUserId(request.getOpUserId());

        goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
        return this.baseMapper.backFrozenQtyAndQty(request);
    }

    @Override
    public void sendInventoryMessage(List<Long> gids) {
        for (Long gid : gids) {
            GoodsInventoryQtyBO goodsInventoryQtyBO = new GoodsInventoryQtyBO();
            goodsInventoryQtyBO.setCount(0);
            goodsInventoryQtyBO.setGid(gid);
            _this.sendMq("goods_inventory_qty", String.valueOf(gid), JSON.toJSONString(goodsInventoryQtyBO), gid, null);
        }
    }

    @Override
    public void sendDelayInventoryMessage(GoodsInventoryQtyBO goodsInventoryQtyBO) {
        MqDelayLevel mqDelayLevel = MqDelayLevel.TEN_SECONDS;
        _this.sendMq("goods_inventory_qty", String.valueOf(goodsInventoryQtyBO.getGid()), JSON.toJSONString(goodsInventoryQtyBO), null,mqDelayLevel);
    }

    @Override
    public int addHmcFrozenQty(AddOrSubtractQtyRequest request) {
        //如果扣减的skuId对应的库存Id
        GoodsSkuDO goodsSkuDO = goodsSkuService.getById(request.getSkuId());
        if (goodsSkuDO == null) {
            throw new BusinessException(InventoryErrorCode.INVENTORY_SKU_MISSING);
        }
        InventoryLogRequest inventoryLogRequest = new InventoryLogRequest();
        inventoryLogRequest.setGid(goodsSkuDO.getGoodsId());
        inventoryLogRequest.setInventoryId(goodsSkuDO.getInventoryId());
        inventoryLogRequest.setBusinessNo(request.getOrderNo());
        inventoryLogRequest.setChangeQty(0L);
        inventoryLogRequest.setChangeFrozenQty(request.getFrozenQty());
        inventoryLogRequest.setInventoryLogEnum(InventoryLogEnum.FROZEN);
        inventoryLogRequest.setOpUserId(request.getOpUserId());

        goodsInventoryLogService.insertGoodInventoryLog(inventoryLogRequest);
        return this.baseMapper.addHmcFrozenQty(request);
    }

    @Override
    public int batchAddHmcFrozenQty(List<AddOrSubtractQtyRequest> requestList) {
        if(CollectionUtils.isEmpty(requestList)){
            return 0;
        }
        requestList.forEach(request->{
            addHmcFrozenQty(request);
        });
        return requestList.size();
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg,Long id,MqDelayLevel delayLevel) {

        Integer intId=null;
        if(null !=id && 0<id){
            intId=id.intValue();
        }
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg,intId,delayLevel);
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
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg,Integer id,MqDelayLevel delayLevel) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag,msg,id,delayLevel);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }
}
