package com.yiling.goods.inventory.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.goods.inventory.enums.SubscriptionTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 UpdateSubscriptionQtyRequest
 * @描述
 * @创建时间 2022/11/23
 * @修改人 shichen
 * @修改时间 2022/11/23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateSubscriptionQtyRequest extends BaseRequest {

    /**
     * 被订阅企业id
     */
    private Long subscriptionEid;

    /**
     * 内码
     */
    private String inSn;

    /**
     * 订阅类型 1 本店 2：erp订阅 3：pop订阅
     */
    private SubscriptionTypeEnum subscriptionTypeEnum;

    /**
     * 变动库存数量的库存id
     */
    private Long inventoryId;

    /**
     * 库存
     */
    private Long qty;
}
