package com.yiling.order.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/10/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryB2BSettlementPageReuest extends QueryPageListRequest {
    /**
     * 签收开始时间
     */
    private Date startReceiveTime;

    /**
     * 签收结束时间
     */
    private Date endReceiveTime;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

}
