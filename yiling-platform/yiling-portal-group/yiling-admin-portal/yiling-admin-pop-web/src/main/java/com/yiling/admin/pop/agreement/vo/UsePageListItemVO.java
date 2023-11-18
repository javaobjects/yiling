package com.yiling.admin.pop.agreement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/8/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UsePageListItemVO extends BaseDTO {

    /**
     * 申请单id
     */
    @ApiModelProperty(value = "申请单id", hidden = true)
    @JsonIgnore
    private String     applicantId;

    /**
     * 申请单编号
     */
    @ApiModelProperty(value = "申请单编号")
    private String     applicantCode;

    /**
     * 申请企业easCode
     */
    @ApiModelProperty(value = "申请企业easCode")
    private String     easCode;

    /**
     * 申请企业名称
     */
    @ApiModelProperty(value = "申请企业名称")
    private String     name;

    /**
     * 销售组织名称
     */
    @ApiModelProperty(value = "销售组织名称")
    private String     sellerName;

    /**
     * 申请总金额
     */
    @ApiModelProperty(value = "申请总金额")
    private BigDecimal totalAmount;

    /**
     * 执行方式1- 票折 2- 现金 3-冲红 4-健康城卡
     */
    @ApiModelProperty(value = "执行方式1- 票折 2- 现金 3-冲红 4-健康城卡")
    private Integer    executeMeans;

    /**
     * 申请单状态1-草稿 2-提交 3-审核成功 4-驳回 -5撤回
     */
    @ApiModelProperty(value = "申请单状态1-草稿 2-提交 3-审核成功 4-驳回 -5撤回")
    private Integer    status;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private Date       applicantTime;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private Date       auditTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "申请时间")
    private Date       createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "审核时间")
    private Date       updateTime;

}
