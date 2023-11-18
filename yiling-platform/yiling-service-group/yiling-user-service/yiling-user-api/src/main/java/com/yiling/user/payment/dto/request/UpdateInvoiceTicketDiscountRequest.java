package com.yiling.user.payment.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新开票后票折金额 Request
 *
 * @author: lun.yu
 * @date: 2021/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateInvoiceTicketDiscountRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 发票类型：1.开票 2.作废
     */
    private Integer invoiceType;

    /**
     * 票折额度
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 平台类型
     */
    private PlatformEnum platformEnum;



}
