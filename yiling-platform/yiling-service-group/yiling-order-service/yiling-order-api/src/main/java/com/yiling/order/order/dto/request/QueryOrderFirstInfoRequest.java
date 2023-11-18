package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2022/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderFirstInfoRequest extends BaseRequest {

    /**
     * 用户企业ID
     */
    private Long buyerEid;

    /**
     * 订单类型
     */
    private Integer orderType;
}
