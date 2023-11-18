package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/9/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CompleteErpOrderInvoiceRequest extends BaseRequest {

    /**
     * 票折单号
     */
    private String invoiceNo;

    /***
     * 应收单号
     */
    private String erpReceivableNo;

    /**
     * 发票金额
     */
    private BigDecimal invoiceAmount;
}
