package com.yiling.mall.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundReTryRequest extends BaseRequest {

    /**
     * 退款单id
     */
    private Long refundId;

    /**
     * 1-已退款，仅标记已处理  2-未退款，通过接口退款
     */
    private Integer operate;
}
