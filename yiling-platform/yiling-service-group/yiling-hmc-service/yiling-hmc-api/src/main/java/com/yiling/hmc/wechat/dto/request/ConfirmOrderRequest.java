package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 确认订单 Request
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ConfirmOrderRequest extends BaseRequest {

    /**
     * 订单ID
     */
    @NotNull
    private Long orderId;

    /**
     * C端用户ID
     */
    @NotNull
    private Long userId;

}
