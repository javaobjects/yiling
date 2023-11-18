package com.yiling.hmc.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReceivedRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 参保记录id
     */
    private Long insuranceRecordId;

    /**
     * 保单人身份证后6位
     */
    private String cardLastSix;
}
