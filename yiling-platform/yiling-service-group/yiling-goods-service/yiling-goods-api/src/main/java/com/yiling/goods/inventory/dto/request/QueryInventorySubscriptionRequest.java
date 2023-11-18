package com.yiling.goods.inventory.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryInventorySubscriptionRequest
 * @描述
 * @创建时间 2022/7/25
 * @修改人 shichen
 * @修改时间 2022/7/25
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryInventorySubscriptionRequest extends BaseRequest {
    /**
     * 订阅方eid
     */
    private Long eid;
    /**
     * 被订阅方企业id
     */
    private Long subscriptionEid;

    private Long inventoryId;

    /**
     * 0 ：正常  1：停用
     */
    private Integer status;

    /**
     * 订阅类型 1 本店 2：erp订阅 3：pop订阅
     */
    private Integer subscriptionType;

    private List<String> inSnList;

    /**
     * 是否包含本店自己
     */
    private Boolean isIncludeSelf;
}
