package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发货商品明细
 *
 * @author:wei.wang
 * @date:2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderDeliveryInfoRequest extends BaseRequest {


    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 发货内容
     */
    private List<DeliveryInfoRequest> deliveryInfoList;

}
