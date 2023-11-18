package com.yiling.goods.inventory.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveSubscriptionRequest
 * @描述
 * @创建时间 2022/11/21
 * @修改人 shichen
 * @修改时间 2022/11/21
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSubscriptionRequest extends BaseRequest {
    private Long id;

    /**
     * 库存Id
     */
    private Long inventoryId;

    /**
     * 订阅方企业id
     */
    private Long eid;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 被订阅企业id
     */
    private Long subscriptionEid;

    /**
     * 订阅企业名称
     */
    private String subscriptionEname;

    /**
     * 内码
     */
    private String inSn;

    /**
     * 订阅类型 1 本店 2：erp订阅 3：pop订阅
     */
    private Integer subscriptionType;

    /**
     * 库存数量
     */
    private Long qty;

}
