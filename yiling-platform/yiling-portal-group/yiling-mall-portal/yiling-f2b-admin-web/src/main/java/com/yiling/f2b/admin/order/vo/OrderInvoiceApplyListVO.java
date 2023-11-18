package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发票申请列表展示
 *
 * @author:wei.wang
 * @date:2021/12/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoiceApplyListVO extends BaseVO {

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "货款总金额")
    private BigDecimal totalAmount;

    /**
     * 现折金额
     */
    @ApiModelProperty(value = "现折总金额")
    private BigDecimal cashDiscountAmount;

    /**
     * 开票状态：1-待申请 2-部分申请  4-部分开票 3-已开票 5-已作废
     */
    @ApiModelProperty(value = "开票状态：1-待申请 2-部分申请  4-部分开票 3-已开票 5-已作废")
    private Integer invoiceStatus;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货  30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;


    /**
     * 发票金额
     */
    @ApiModelProperty(value = "发票总金额")
    private BigDecimal invoiceAmount;

    /**
     * 票折总金额
     */
    @ApiModelProperty(value = "票折总金额")
    private BigDecimal ticketDiscountAmount;

    /**
     * 发票列表明细
     */
    @ApiModelProperty(value = "发票列表明细")
    private List<OrderInvoiceApplyOneVO> orderInvoiceApplyOneList;

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "买家名称")
    private String buyerEname;

    /**
     * 卖家名称
     */
    @ApiModelProperty(value = "卖家名称")
    private String sellerEname;

    @Data
    public static class OrderInvoiceApplyOneVO {

        @ApiModelProperty("发票申请时间")
        private Date       createTime;
        @ApiModelProperty("发票申请编号")
        private Long       id;
        @ApiModelProperty("发票转换规则编码")
        private String     transitionRuleCode;
        @ApiModelProperty("发票转换规则编码名称")
        private String     transitionRuleName;
        @ApiModelProperty("是否使用票折：0-否 1-是")
        private Integer    ticketDiscountFlag;
        @ApiModelProperty("是否使用票折：否 ，是")
        private String     ticketDiscountName;
        @ApiModelProperty("票折金额")
        private BigDecimal ticketDiscountAmount;
        @ApiModelProperty("发票金额")
        private BigDecimal invoiceAmount;
        @ApiModelProperty("发票号码")
        private String     invoiceNo;
        @ApiModelProperty("申请人ID")
        private Long       createUser;
        @ApiModelProperty("申请人名称")
        private String     createUserName;
    }
}
