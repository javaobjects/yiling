package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
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
public class ReportFlowGoodsBatchStatisticsVO extends BaseVO {

    @ApiModelProperty(value = "统计列表")
    private List<ReportLhFlowStatisticsVO> list;

    @ApiModelProperty(value = "差值库存量")
    private Long diffNumber;

    @ApiModelProperty(value = "日平均库存量")
    private BigDecimal avgNumber;

    @ApiModelProperty(value = "差值库存金额")
    private BigDecimal diffAmount;

    @ApiModelProperty(value = "日平均库存金额")
    private BigDecimal avgAmount;
}
