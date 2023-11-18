package com.yiling.open.erp.dto;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/8/5
 */
@Data
public class ErpReturnDTO implements java.io.Serializable{

    /**
     * 订单ID
     */
    @JSONField(name = "return_id",ordinal = 20)
    private Long returnId;


    /**
     * 订单ID
     */
    @JSONField(name = "eas_delivery_number",ordinal = 20)
    private String easDeliveryNumber;

    /**
     * 卖家企业ID
     */
    @JSONField(name = "seller_eid",ordinal = 20)
    private Long sellerEid;

    /**
     * 订单明细
     */
    @JSONField(name = "order_return_list",ordinal = 25)
    private List<ErpOrderReturnDTO> orderReturnList;
}
