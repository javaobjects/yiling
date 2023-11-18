package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单报表数据统计
 */
@Data
public class ReportOrderVO {

    /**
     * POP年购进总金额
     */
    @ApiModelProperty(value = "POP年购进总金额")
    private BigDecimal buyPOPYearAmount;

    /**
     * POP月购进总金额
     */
    @ApiModelProperty(value = "POP月购进总金额")
    private BigDecimal buyPOPMonthAmount;

    /**
     * POP日购进总金额
     */
    @ApiModelProperty(value = "POP日购进总金额")
    private BigDecimal buyPOPDayAmount;

    /**
     * POP年购进总金额
     */
    @ApiModelProperty(value = "POP年回款总金额")
    private BigDecimal backPOPYearAmount;

    /**
     * POP月购进总金额
     */
    @ApiModelProperty(value = "POP月回款总金额")
    private BigDecimal backPOPMonthAmount;

    /**
     * POP日回款总金额
     */
    @ApiModelProperty(value = "POP日回款总金额")
    private BigDecimal backPOPDayAmount;


    /**
     * B2B年动销总金额
     */
    @ApiModelProperty(value = "B2B年动销总金额")
    private BigDecimal sellB2BYearAmount;

    /**
     * B2B月动销总金额
     */
    @ApiModelProperty(value = "B2B月动销总金额")
    private BigDecimal sellB2BMonthAmount;

    /**
     * B2B日动销总金额
     */
    @ApiModelProperty(value = "B2B日动销总金额")
    private BigDecimal sellB2BDayAmount;

    /**
     * 自建平台年总金额
     */
    @ApiModelProperty(value = "自建平台年总金额")
    private BigDecimal ownSetPlatformYearAmount;

    /**
     * 自建平台月总金额
     */
    @ApiModelProperty(value = "自建平台月总金额")
    private BigDecimal ownSetPlatformMonthAmount;

    /**
     * 自建平台日总金额
     */
    @ApiModelProperty(value = "自建平台日总金额")
    private BigDecimal ownSetPlatformDayAmount;




}
