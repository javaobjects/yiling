package com.yiling.settlement.report.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.settlement.report.enums.ReportStatusEnum;

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
public class ConfirmReportRequest extends BaseRequest {

    /**
     * 报表id
     */
    private Long reportId;

    /**
     * 状态
     */
    private ReportStatusEnum statusEnum;
}