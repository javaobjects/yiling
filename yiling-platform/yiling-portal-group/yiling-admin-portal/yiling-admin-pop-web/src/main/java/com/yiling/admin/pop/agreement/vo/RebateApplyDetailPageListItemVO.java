package com.yiling.admin.pop.agreement.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

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
public class RebateApplyDetailPageListItemVO extends BaseVO {

    /**
     * 返利申请表id
     */
    @ApiModelProperty(value = "返利申请表id")
    private Long       rebateApplyId;

    /**
     * 申请单号
     */
    @ApiModelProperty(value = "申请单号")
    private String     applyCode;

    /**
     * 明细类型 1-协议类型 2-其他
     */
    @ApiModelProperty(value = "明细类型 1-协议类型 2-其他")
    private Integer    detailType;

    /**
     * 协议id
     */
    @ApiModelProperty(value = "协议id")
    private Long       agreementId;

    /**
     * 协议名称
     */
    @ApiModelProperty(value = "协议名称")
    private String     name;

    /**
     * 协议内容
     */
    @ApiModelProperty(value = "协议内容")
    private String     content;

    /**
     * 入账原因
     */
    @ApiModelProperty(value = "入账原因")
    private String     entryDescribe;

    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    private Integer    orderCount;

    /**
     * 返利金额
     */
    @ApiModelProperty(value = "返利金额")
    private BigDecimal amount;

    /**
     * 销售组织
     */
    @ApiModelProperty(value = "配送商名称")
    private String     sellerName;

    /**
     * 返利种类
     */
    @ApiModelProperty(value = "返利种类")
    private String     rebateCategory;

    /**
     * 费用科目
     */
    @ApiModelProperty(value = "费用科目")
    private String     costSubject;

    /**
     * 费用归属部门
     */
    @ApiModelProperty(value = "费用归属部门")
    private String     costDept;

    /**
     * 执行部门
     */
    @ApiModelProperty(value = "执行部门")
    private String     executeDept;

    /**
     * 批复代码
     */
    @ApiModelProperty(value = "批复代码")
    private String     replyCode;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private String     applyTime;

}
