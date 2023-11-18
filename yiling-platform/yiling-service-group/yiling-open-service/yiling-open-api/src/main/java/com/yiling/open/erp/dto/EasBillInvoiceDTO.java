package com.yiling.open.erp.dto;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/7/23
 */
@Data
public class EasBillInvoiceDTO implements java.io.Serializable{

    @JSONField(name = "eas_bill_number",ordinal = 20)
    private String easBillNumber;

    @JSONField(name = "invoice_no",ordinal = 20)
    private String invoiceNo;

    @JSONField(name = "invoice_amount",ordinal = 20)
    private String invoiceAmount;
}
