package com.yiling.dataflow.report.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportFlowBO extends BaseDTO {

    /**
     * 销售总额
     */
    private BigDecimal number;

    /**
     * 销售时间
     */
    private Date time;
}
