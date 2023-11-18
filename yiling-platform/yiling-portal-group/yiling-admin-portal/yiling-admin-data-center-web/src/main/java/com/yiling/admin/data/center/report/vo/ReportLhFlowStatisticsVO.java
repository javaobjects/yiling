package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class ReportLhFlowStatisticsVO extends BaseVO {
    /**
     * 销售时间
     */
    @ApiModelProperty(value = "时间")
    private Date time;

    /**
     * 供应商数量
     */
    @ApiModelProperty(value = "供应商数量")
    private Integer count;

    /**
     * 销售总额
     */
    @ApiModelProperty(value = "销售总额")
    private BigDecimal amount;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "销售数量")
    private Long number;

    /**
     * 销售总额
     */
    @ApiModelProperty(value = "连花销售总额")
    private BigDecimal lhAmount;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "连花销售数量")
    private Long lhNumber;

    /**
     * 销售总额
     */
    @ApiModelProperty(value = "非连花销售总额")
    private BigDecimal unLhAmount;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "非连花销售数量")
    private Long unLhNumber;

}
