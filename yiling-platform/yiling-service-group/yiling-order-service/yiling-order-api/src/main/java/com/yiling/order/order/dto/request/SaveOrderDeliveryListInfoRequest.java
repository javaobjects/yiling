package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 提交发货Request
 * @author:wei.wang
 * @date:2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderDeliveryListInfoRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 物流类型：1-自有物流 2-第三方物流
     */
    private Integer deliveryType;

    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliveryNo;


    /**
     * 商品信息
     */
    private List<SaveOrderDeliveryInfoRequest> orderDeliveryGoodsInfoList;
}
