package com.yiling.settlement.report.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdjustReportRequest extends BaseRequest {

    /**
     * 报表id
     */
    private Long reportId;

    /**
     * 调整金额
     */
    private BigDecimal adjustAmount;

    /**
     * 调整原因
     */
    private String adjustReason;
}
