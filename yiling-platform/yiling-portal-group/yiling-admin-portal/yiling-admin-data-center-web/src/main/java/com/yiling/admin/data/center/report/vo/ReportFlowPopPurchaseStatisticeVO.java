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
public class ReportFlowPopPurchaseStatisticeVO extends BaseVO {

    @ApiModelProperty(value = "统计列表")
    private List<ReportFlowPopPurchaseVO> list;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "erp销售数量")
    private Long erpQuantityTotal;

    /**
     * 销售总额
     */
    @ApiModelProperty(value = "erp销售金额")
    private BigDecimal erpTotalAmountTotal;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "pop销售数量")
    private Long poQuantityTotal;

    /**
     * 销售总额
     */
    @ApiModelProperty(value = "pop销售金额")
    private BigDecimal poTotalAmountTotal;
}
