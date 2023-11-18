package com.yiling.mall.order.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhigang.guo
 * @date: 2021/09/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDistributePageRequest extends QueryPageListRequest {

    private static final long serialVersionUID=7230218091190534456L;
    /**
     * 采购商eid
     */
    private Long purchaseEid;

    /**
     * 订单类型
     */
    private Integer orderType;

}
