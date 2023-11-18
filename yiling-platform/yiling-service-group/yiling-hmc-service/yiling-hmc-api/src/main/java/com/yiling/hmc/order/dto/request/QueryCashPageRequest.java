package com.yiling.hmc.order.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询保单兑付记录
 * @author: gxl
 * @date: 2022/4/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCashPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 9022136738238303731L;

    /**
     * 购买保险id
     */
    private Long insuranceRecordId;
}