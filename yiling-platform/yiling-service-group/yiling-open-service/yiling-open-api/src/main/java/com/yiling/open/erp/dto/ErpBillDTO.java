package com.yiling.open.erp.dto;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/7/28
 */
@Data
public class ErpBillDTO implements java.io.Serializable{

    /**
     * 申请单ID
     */
    @JSONField(name = "order_bill_id",ordinal = 20)
    private String orderBillId;

    /**
     * 订单ID
     */
    @JSONField(name = "order_id",ordinal = 20)
    private Long ordeId;

    /**
     * 订单ID
     */
    @JSONField(name = "eas_delivery_number",ordinal = 20)
    private String easDeliveryNumber;

    /**
     * 订单明细
     */
    @JSONField(name = "order_bill_list",ordinal = 25)
    private List<ErpOrderBillDTO> orderBillList;
}
