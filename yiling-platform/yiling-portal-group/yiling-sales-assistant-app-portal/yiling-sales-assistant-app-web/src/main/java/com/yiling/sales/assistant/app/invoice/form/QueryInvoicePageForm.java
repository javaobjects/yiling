package com.yiling.sales.assistant.app.invoice.form;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发票
 * @author:wei.wang
 * @date:2021/9/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryInvoicePageForm extends QueryPageListRequest {


    /**
     * 销售主题id
     */
    @ApiModelProperty(value = "销售主题id")
    private Long distributorEid;

    /**
     * 开始发票申请时间
     */
    @ApiModelProperty(value = "开始发票申请时间")
    private Date startInvoiceTime;

    /**
     * 结束发票申请时间
     */
    @ApiModelProperty(value = "结束发票申请时间")
    private Date endInvoiceTime;

    /**
     * 开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    @ApiModelProperty(value = "开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废 6-不开票 7-修改发票")
    private Integer invoiceStatus;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 订单单号
     */
    @ApiModelProperty(value = "订单单号")
    private String orderNo;

    /**
     * 票单号
     */
    @ApiModelProperty(value = "票单号")
    private String invoiceNo;
}
