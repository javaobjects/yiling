package com.yiling.goods.inventory.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.goods.inventory.api.InventorySubscriptionApi;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.dto.request.BatchSaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.QueryInventorySubscriptionRequest;
import com.yiling.goods.inventory.dto.request.SaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.UpdateSubscriptionQtyRequest;
import com.yiling.goods.inventory.service.InventorySubscriptionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 InventorySubscriptionApiImpl
 * @描述
 * @创建时间 2022/7/27
 * @修改人 shichen
 * @修改时间 2022/7/27
 **/
@Slf4j
@DubboService
public class InventorySubscriptionApiImpl implements InventorySubscriptionApi {
    @Autowired
    private InventorySubscriptionService inventorySubscriptionService;

    @Override
    public List<InventorySubscriptionDTO> getInventorySubscriptionList(QueryInventorySubscriptionRequest request) {
        return inventorySubscriptionService.getInventorySubscriptionList(request);
    }

    @Override
    public Boolean batchUpdateSubscriptionQty(List<SaveSubscriptionRequest> requestList) {
        return inventorySubscriptionService.batchUpdateSubscriptionQty(requestList);
    }

    @Override
    public Boolean saveInventorySubscription(BatchSaveSubscriptionRequest request) {
        return inventorySubscriptionService.saveSubscriptionList(request);
    }

    @Override
    public void refreshSubscriptionQtyByPop(UpdateSubscriptionQtyRequest request) {
        inventorySubscriptionService.refreshSubscriptionQtyByPop(request);
    }
}
