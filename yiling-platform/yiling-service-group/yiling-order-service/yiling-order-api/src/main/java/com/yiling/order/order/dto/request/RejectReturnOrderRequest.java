package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退货单审核驳回请求参数
 *
 * @author: yong.zhang
 * @date: 2021/8/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RejectReturnOrderRequest extends BaseRequest {

    /**
     * 退款订单编号
     */
    private Long returnId;

    /**
     * 驳回原因
     */
    private String failReason;
}
