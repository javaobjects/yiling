package com.yiling.dataflow.report.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class FlowWashSaleReportBaseDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 商业编码
     */
    private Long crmId;

    /**
     * 机构编码
     */
    private Long customerCrmId;
}
