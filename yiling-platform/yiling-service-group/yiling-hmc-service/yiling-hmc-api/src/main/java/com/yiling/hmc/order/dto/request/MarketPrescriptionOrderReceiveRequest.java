package com.yiling.hmc.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 处方订单收货
 *
 * @author: fan.shen
 * @date: 2023/03/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MarketPrescriptionOrderReceiveRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long ihOrderId;


}
