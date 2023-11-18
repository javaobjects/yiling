package com.yiling.goods.inventory.api.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.bo.GoodsInventoryQtyBO;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.inventory.dto.request.SaveInventoryRequest;
import com.yiling.goods.inventory.entity.InventoryDO;
import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.medicine.entity.GoodsSkuDO;
import com.yiling.goods.medicine.service.GoodsSkuService;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 库存管理
 *
 * @author: shuang.zhang
 * @date: 2021/5/18
 */
@Slf4j
@DubboService
public class InventoryApiImpl implements InventoryApi {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private GoodsSkuService  goodsSkuService;

    @Override
    public List<InventoryDTO> getInventoryByIds(List<Long> ids) {
        return inventoryService.getInventoryByIds(ids);
    }

    @Override
    public InventoryDTO getBySkuId(Long skuId) {
        return inventoryService.getBySkuId(skuId);
    }

    @Override
    public Map<Long, List<InventoryDTO>> getInventoryMapByGids(List<Long> goodsIds) {
        return inventoryService.getInventoryMapByGids(goodsIds);
    }

    @Override
    public Map<Long, Long> getAvailableQtyByGoodsIds(List<Long> goodsIds) {
        return inventoryService.getAvailableQtyByGoodsIds(goodsIds);
    }

    @Override
    public Map<Long, InventoryDTO> getMapBySkuIds(List<Long> skuIds) {
        return inventoryService.getMapBySkuIds(skuIds);
    }

    @Override
    public Long save(SaveInventoryRequest request) {
        Long inventoryId = inventoryService.save(request);
        inventoryService.sendInventoryMessage(Arrays.asList(request.getGid()));
        return inventoryId;
    }

    @Override
    public int addFrozenQty(AddOrSubtractQtyRequest request) {
        int i= inventoryService.addFrozenQty(request);
        GoodsSkuDO goodsSkuDO=goodsSkuService.getById(request.getSkuId());
        if(goodsSkuDO!=null){
            inventoryService.sendInventoryMessage(Arrays.asList(goodsSkuDO.getGoodsId()));
        }
        return i;
    }

    @Override
    public int batchAddFrozenQty(List<AddOrSubtractQtyRequest> requestList) {
        try {
            int i= inventoryService.batchAddFrozenQty(requestList);
            List<Long> ids=requestList.stream().map(e->e.getSkuId()).collect(Collectors.toList());
            List<GoodsSkuDO> goodsSkuDOList=goodsSkuService.listByIds(ids);
            if(CollUtil.isNotEmpty(goodsSkuDOList)){
                List<Long> goodsIds=goodsSkuDOList.stream().map(e->e.getGoodsId()).collect(Collectors.toList());
                inventoryService.sendInventoryMessage(goodsIds);
            }
            return i;
        } catch (BusinessException be){
            log.warn(be.getMessage());
            return 0;
        } catch (Exception e) {
            log.error("商品库存下单冻结库存加减失败", e);
            return 0;
        }
    }

    @Override
    public int subtractFrozenQtyAndQty(AddOrSubtractQtyRequest request) {
        int i=inventoryService.subtractFrozenQtyAndQty(request);
        GoodsSkuDO goodsSkuDO=goodsSkuService.getById(request.getSkuId());
        if(goodsSkuDO!=null){
            inventoryService.sendInventoryMessage(Arrays.asList(goodsSkuDO.getGoodsId()));
        }
        return i;
    }

    @Override
    public int subtractFrozenQty(AddOrSubtractQtyRequest request) {
        int i= inventoryService.subtractFrozenQty(request);
        GoodsSkuDO goodsSkuDO=goodsSkuService.getById(request.getSkuId());
        if(goodsSkuDO!=null){
            inventoryService.sendInventoryMessage(Arrays.asList(goodsSkuDO.getGoodsId()));
        }
        return i;
    }

    @Override
    public int batchSubtractFrozenQty(List<AddOrSubtractQtyRequest> requestList) {
        try {
            int i= inventoryService.batchSubtractFrozenQty(requestList);
            List<Long> ids=requestList.stream().map(e->e.getSkuId()).collect(Collectors.toList());
            List<GoodsSkuDO> goodsSkuDOList=goodsSkuService.listByIds(ids);
            if(CollUtil.isNotEmpty(goodsSkuDOList)){
                List<Long> goodsIds=goodsSkuDOList.stream().map(e->e.getGoodsId()).collect(Collectors.toList());
                inventoryService.sendInventoryMessage(goodsIds);
            }
            return i;
        } catch (BusinessException be){
            log.warn(be.getMessage());
            return 0;
        } catch (Exception e) {
            log.error("商品库存批量退还库存失败", e);
            return 0;
        }
    }

    @Override
    public int backFrozenQtyAndQty(AddOrSubtractQtyRequest request) {
        int i=inventoryService.backFrozenQtyAndQty(request);
        GoodsSkuDO goodsSkuDO=goodsSkuService.getById(request.getSkuId());
        if(goodsSkuDO!=null){
            inventoryService.sendInventoryMessage(Arrays.asList(goodsSkuDO.getGoodsId()));
        }
        return i;
    }

    @Override
    public void sendInventoryMessage(List<Long> gids) {
        inventoryService.sendInventoryMessage(gids);
    }

    @Override
    public void sendDelayInventoryMessage(GoodsInventoryQtyBO goodsInventoryQtyBO) {
        inventoryService.sendDelayInventoryMessage(goodsInventoryQtyBO);
    }


}
