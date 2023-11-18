package com.yiling.dataflow.report.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveFlowWashStockReportRequest extends BaseRequest {
    /**
     * 年月
     */
    private String soMonth;

    /**
     * 机构编码
     */
    private Long crmId;

}
