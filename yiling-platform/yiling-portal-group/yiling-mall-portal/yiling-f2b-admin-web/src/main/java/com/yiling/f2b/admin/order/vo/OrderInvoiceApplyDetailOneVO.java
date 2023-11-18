package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发票详情页面VO
 *
 * @author:wei.wang
 * @date:2021/12/27
 */
@Data
public class OrderInvoiceApplyDetailOneVO {
    /**
     * 发票申请编号
     */
    @ApiModelProperty(value = "发票申请编号")
    private Long id;

    @ApiModelProperty("发票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty("申请人ID")
    private Long createUser;

    @ApiModelProperty("申请人名称")
    private String createUserName;

    @ApiModelProperty("发票申请时间")
    private Date createTime;

    @ApiModelProperty("发票张数")
    private Integer invoiceQuantity;

    @ApiModelProperty("发票单号")
    private String invoiceNo;

    @ApiModelProperty("是否使用票折：0-否 1-是")
    private Integer ticketDiscountFlag;

    @ApiModelProperty("票折信息")
    private List<OrderTicketDiscountVO> orderTicketDiscountList;

    @ApiModelProperty("发票转换规则编码")
    private String transitionRuleCode;

    @ApiModelProperty("发票转换规则编码名称")
    private String transitionRuleName;

    @ApiModelProperty("关联开票信息")
    private List<OrderInvoiceGroupVO> orderInvoiceGroupList;

}
