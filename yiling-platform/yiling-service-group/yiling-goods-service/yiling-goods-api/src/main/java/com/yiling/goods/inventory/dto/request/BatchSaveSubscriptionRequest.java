package com.yiling.goods.inventory.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 BatchSaveSubscriptionRequest
 * @描述
 * @创建时间 2022/11/21
 * @修改人 shichen
 * @修改时间 2022/11/21
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchSaveSubscriptionRequest extends BaseRequest {

    /**
     * 库存Id
     */
    private Long inventoryId;

    /**
     * 订阅方企业id
     */
    private Long eid;

    /**
     * 订阅列表
     */
    private List<SaveSubscriptionRequest> subscriptionList;
}
