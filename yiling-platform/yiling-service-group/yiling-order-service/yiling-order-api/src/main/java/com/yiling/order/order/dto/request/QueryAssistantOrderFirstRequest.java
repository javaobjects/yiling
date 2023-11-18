package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wei.wang
 * @date: 2022/10/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAssistantOrderFirstRequest extends BaseRequest {

    /**
     * 用户企业ID
     */
    private Long buyerEid;

    /**
     * 下单人ID
     */
    private Long createUser;
}
