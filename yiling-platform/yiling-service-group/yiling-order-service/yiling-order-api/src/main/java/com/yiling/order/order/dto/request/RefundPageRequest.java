package com.yiling.order.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页查询退款单
 *
 * @author: yong.zhang
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundPageRequest extends QueryPageListRequest {

    private String orderNo;

    private String sellerName;

    private String buyerName;

    private Integer refundStatus;

    private Integer refundType;

    /**
     * 退款来源
     * @see com.yiling.order.order.enums.RefundSourceEnum
     */
    private Integer refundSource;

    private Date createStartTime;

    private Date createStopTime;
}
