package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 团购基础信息
 */
@Data
public class GbMainInfoVO {

    /**
     * 团购单位ID
     */
    @ApiModelProperty(value = "团购单位ID")
    private Long customerId;

    /**
     * 团购单位名称
     */
    @ApiModelProperty(value = "团购单位名称")
    private String customerName;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private Date month;

    /**
     * 申请团购政策-返利金额（元）
     */
    @ApiModelProperty(value = "申请团购政策-返利金额(元)")
    private BigDecimal rebateAmount;

    /**
     * 团购区域：1-国内 2-海外
     */
    @ApiModelProperty(value = "团购区域：1-国内 2-海外")
    private Integer regionType;

    /**
     * 团购性质：1-普通团购 2-政府采购
     */
    @ApiModelProperty(value = "团购性质：1-普通团购 2-政府采购")
    private Integer gbType;

    /**
     * 核实团购性质：1-普通团购 2-政府采购
     */
    @ApiModelProperty(value = "市场运营部核实团购性质：1-普通团购 2-政府采购")
    private Integer  gbReviewType;

    /**
     * 是否地级市下机构：1-是 2-否
     */
    @ApiModelProperty(value = "是否地级市下机构：1-是 2-否")
    private Integer gbCityBelow;

    /**
     * 团购证据-证据类型（多个以,分隔）
     */
    @ApiModelProperty(value = "团购证据-证据类型（多个以,分隔）")
    private String evidences;

    /**
     * 团购证据-其他
     */
    @ApiModelProperty(value = "团购证据-其他")
    private String otherEvidence;


    /**
     * 取消理由
     */
    @ApiModelProperty(value = "取消理由")
    private String cancelReason;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
