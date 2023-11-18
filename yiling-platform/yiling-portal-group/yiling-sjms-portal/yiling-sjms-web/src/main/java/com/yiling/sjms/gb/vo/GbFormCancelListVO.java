package com.yiling.sjms.gb.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 团购列表信息
 */
@Data
public class GbFormCancelListVO extends BaseVO {
    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购取消编号")
    private String gbNo;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private Date month;

    /**
     * 事业部
     */
    @ApiModelProperty(value = "事业部")
    private String orgName;

    /**
     * 团购单位
     */
    @ApiModelProperty(value = "团购单位")
    private String customerName;

    /**
     * 团购出库终端名称
     */
    @ApiModelProperty(value = "团购出库终端名称")
    private String termainalCompanyName;

    /**
     * 团购出库商业
     */
    @ApiModelProperty(value = "团购出库商业")
    private String businessCompanyName;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    @ApiModelProperty(value = "状态：10-待提交 20-审批中 200-已通过 201-已驳回")
    private Integer status;


    /**
     * 审批完成时间
     */
    @ApiModelProperty(value = "审批完成时间")
    private Date approveTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 提交时间
     */
    @ApiModelProperty(value = "提交时间")
    private Date submitTime;

    /**
     * 来源团购编号
     */
    @ApiModelProperty(value = "来源团购编号")
    private String srcGbNo;

    /**
     * 是否复核: 1-否 2-是
     */
    @ApiModelProperty(value = "复核状态 1-否 2-是")
    private Integer reviewStatus;

    /**
     * 发起人名称
     */
    @ApiModelProperty(value = "发起人名称")
    private String empName;

    /**
     * gbId
     */
    @ApiModelProperty(value = "gbId团购取消id")
    private Long gbId;

}
