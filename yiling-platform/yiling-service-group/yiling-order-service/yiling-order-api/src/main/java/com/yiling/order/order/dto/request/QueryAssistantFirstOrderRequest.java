package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/3/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAssistantFirstOrderRequest extends BaseRequest {
    private static final long serialVersionUID = 1718340214637542591L;
    /**
     * 买家企业ID
     */
    private Long buyerEid;
}