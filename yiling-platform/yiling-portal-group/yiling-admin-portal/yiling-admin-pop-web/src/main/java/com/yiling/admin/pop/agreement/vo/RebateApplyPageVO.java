package com.yiling.admin.pop.agreement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/8/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("已申请入账vo")
public class RebateApplyPageVO<T> extends Page<T> {

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String     name;

    /**
     * 企业编码
     */
    @ApiModelProperty(value = "企业编码")
    private String     easCode;

    /**
     * 申请单号
     */
    @ApiModelProperty(value = "申请单号")
    private String     code;

    /**
     * 渠道ID
     */
    @ApiModelProperty(value = "渠道ID")
    private Long       channelId;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "企业状态：1-启用 2-停用")
    private Integer    status;

    /**
     * 申请总金额
     */
    @ApiModelProperty(value = "申请总金额")
    private BigDecimal totalAmount;

    /**
     * 申请状态状态 1-待审核 2-已入账 3-驳回
     */
    @ApiModelProperty(value = "申请状态状态 1-待审核 2-已入账 3-驳回")
    private Integer    applyStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date       createTime;

}
