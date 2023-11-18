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
 * @date: 2021/8/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("已使用返利vo")
public class RebateApplyUsePageVO<T> extends Page<T> {

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
     * 申请单状态1-草稿 2-提交 3-审核成功 4-驳回 -5撤回
     */
    @ApiModelProperty(value = "申请单状态1-草稿 2-提交 3-审核成功 4-驳回 -5撤回")
    private Integer status;

    /**
     * 渠道ID
     */
    @ApiModelProperty(value = "渠道ID")
    private Long       channelId;

    /**
     * 申请总金额
     */
    @ApiModelProperty(value = "申请总金额")
    private BigDecimal totalAmount;

    /**
     * 返利方式：执行方式 1- 票折 2- 现金 3-冲红 4-健康城卡
     */
    @ApiModelProperty(value = "执行方式1- 票折 2- 现金 3-冲红 4-健康城卡")
    private Integer    executeMeans;

    /**
     * 执行部门
     */
    @ApiModelProperty(value = "执行部门")
    private String     executeDept;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private Date       createTime;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "企业状态：1-启用 2-停用")
    private Integer entStatus;

}
