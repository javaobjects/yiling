package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 提交票折申请时候修改订单里面发票信息
 * @author:wei.wang
 * @date:2021/7/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOrderInvoiceInfoRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 票折单号
     */
    private String ticketDiscountNo;

    /**
     * 开票时间
     */
    private Date invoiceTime;

    /**
     * 开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    private Integer invoiceStatus;

    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * ERP应收单状态：1-有效 2-无效
     */
    private Integer erpReceivableStatus;
    /**
     * 票折金额
     */
    private BigDecimal ticketDiscountAmount;
}
