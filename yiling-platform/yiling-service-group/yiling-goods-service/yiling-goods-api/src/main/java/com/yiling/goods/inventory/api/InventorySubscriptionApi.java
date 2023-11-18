package com.yiling.goods.inventory.api;

import java.util.List;

import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.dto.request.BatchSaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.QueryInventorySubscriptionRequest;
import com.yiling.goods.inventory.dto.request.SaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.UpdateSubscriptionQtyRequest;

/**
 * @author shichen
 * @类名 InventorySubscriptionApi
 * @描述
 * @创建时间 2022/7/27
 * @修改人 shichen
 * @修改时间 2022/7/27
 **/
public interface InventorySubscriptionApi {

    /**
     * 获取订阅关系列表
     * @param request
     * @return
     */
    List<InventorySubscriptionDTO> getInventorySubscriptionList(QueryInventorySubscriptionRequest request);

    /**
     * 根据订阅id修改订阅库存 并通过mq更新商品库存
     * @param requestList
     * @return
     */
    Boolean batchUpdateSubscriptionQty(List<SaveSubscriptionRequest> requestList);


    /**
     * 保存订阅关系
     * @param request
     * @return
     */
    Boolean saveInventorySubscription(BatchSaveSubscriptionRequest request);

    /**
     * 刷新POP订阅关系的库存
     * @param request
     */
    void refreshSubscriptionQtyByPop(UpdateSubscriptionQtyRequest request);
}
