package com.yiling.mall.customer.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  我的用户确认
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.order.dto.request
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerVerificationRequest extends BaseRequest {

    /**
     * 代客下单的人员用户ID
     */
    @NotNull
    private Long contactUserId;

    /**
     * 当前登录企业EID
     */
    @NotNull
    private Long currentEid;

    /**
     * 客户企业ID
     */
    @NotNull
    private Long customerEid;

    /**
     * 订单类型
     */
    @NotNull
    private Integer orderType;
}
