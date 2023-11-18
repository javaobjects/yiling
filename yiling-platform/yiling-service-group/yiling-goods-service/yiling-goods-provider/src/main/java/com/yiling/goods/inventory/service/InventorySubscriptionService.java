package com.yiling.goods.inventory.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.dto.request.BatchSaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.QueryInventorySubscriptionRequest;
import com.yiling.goods.inventory.dto.request.SaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.UpdateSubscriptionQtyRequest;
import com.yiling.goods.inventory.entity.InventorySubscriptionDO;
import com.yiling.goods.inventory.enums.SubscriptionTypeEnum;

/**
 * @author shichen
 * @类名 InventorySubscriptionService
 * @描述
 * @创建时间 2022/7/25
 * @修改人 shichen
 * @修改时间 2022/7/25
 **/
public interface InventorySubscriptionService  extends BaseService<InventorySubscriptionDO> {

    /**
     * 获取订阅关系列表
     * @param request
     * @return
     */
    List<InventorySubscriptionDTO> getInventorySubscriptionList(QueryInventorySubscriptionRequest request);

    /**
     * 保存库存订阅关系
     */
    boolean saveSubscriptionList(BatchSaveSubscriptionRequest request);

    /**
     * 保存本店库存订阅
     * @param request
     * @return
     */
    boolean saveSelfSubscription(SaveSubscriptionRequest request);

    /**
     * 汇总该库存id下面所有正常状态库存
     * @param inventoryId
     * @return
     */
    Long getTotalInventory(Long inventoryId);


    /**
     * 库存id 批量获取本店库存订阅
     * @param inventoryIds
     * @return
     */
    List<InventorySubscriptionDTO> getSelfSubscriptionByInventoryIds(List<Long> inventoryIds);

    /**
     * 根据订阅id修改订阅库存 并通过mq更新商品库存
     * @param requestList
     * @return
     */
    Boolean batchUpdateSubscriptionQty(List<SaveSubscriptionRequest> requestList);

    /**
     * 根据订阅关系汇总库存
     * @param inventoryId
     */
    InventoryDTO refreshQtyBySubscription(Long inventoryId,Long opUserId);

    /**
     * 刷新POP订阅关系的库存
     * @param request
     */
    void refreshSubscriptionQtyByPop(UpdateSubscriptionQtyRequest request);
}
