package com.yiling.hmc.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: fan.shen
 * @date: 2022/4/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 下单人
     */
    private Long orderUser;
}
