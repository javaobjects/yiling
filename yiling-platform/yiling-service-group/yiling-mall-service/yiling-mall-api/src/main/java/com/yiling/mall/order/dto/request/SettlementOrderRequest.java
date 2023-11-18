package com.yiling.mall.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单结算
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.order.dto.request
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SettlementOrderRequest extends BaseRequest {

    private static final long serialVersionUID=-6926855319017902776L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 订单类型
     */
    private Integer orderType;

}
