package com.yiling.hmc.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @date: 2023/03/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MarketOrderDeliveryRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 平台运营备注
     */
    private String deliveryCompany;

    /**
     * 商家备注
     */
    private String deliverNo;

}
