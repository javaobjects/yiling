package com.yiling.settlement.yee.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryYeeSettleListByPageRequest extends QueryPageListRequest {

    /**
     * 结算订单号
     */
    private String summaryNo;

    /**
     * 创建开始时间
     */
    private Date createTimeBegin;

    /**
     * 创建结束时间
     */
    private Date createTimeEnd;
}
