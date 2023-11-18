package com.yiling.hmc.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存订单票据请求参数
 *
 * @author: yong.zhang
 * @date: 2022/6/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReceiptsSaveRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单票据，逗号分隔
     */
    private String orderReceipts;
}
