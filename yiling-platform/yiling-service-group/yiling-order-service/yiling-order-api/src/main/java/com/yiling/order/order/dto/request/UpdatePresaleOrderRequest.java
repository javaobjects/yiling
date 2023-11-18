package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePresaleOrderRequest extends BaseRequest {

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 是否已支付定金
     */
    private Integer isPayDeposit;

    /**
     * 是否已支付尾款
     */
    private Integer isPayBalance;

    /**
     * 是否发送支付短信
     */
    private Integer hasSendPaySms;

    /**
     * 是否发送取消短息
     */
    private Integer hasSendCancelSms;
}
