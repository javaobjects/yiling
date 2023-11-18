package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class ReportFlowSaleStatisticsVO extends BaseVO {

    /**
     * 销售数量合计
     */
    @ApiModelProperty(value = "销售数量合计")
    private Long numberTotal;

    /**
     * 销售总额合计
     */
    @ApiModelProperty(value = "销售总额合计")
    private BigDecimal amountTotal;

    /**
     * 销售数量合计
     */
    @ApiModelProperty(value = "终端销售数量合计")
    private Long terminalNumberTotal;

    /**
     * 销售总额合计
     */
    @ApiModelProperty(value = "终端销售总额合计")
    private BigDecimal terminalAmountTotal;

    @ApiModelProperty(value = "数据集合")
    private List<ReportFlowStatisticsVO> list;


}
