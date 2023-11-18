package com.yiling.order.order.dto.request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:tingwei.chen
 * @date:2021/6/22
 **/

@Data
@Accessors(chain = true)
public class OrderDeliveryRequest implements Serializable {
    
    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 退货数量
     */
    private Integer returnQuantity;

}


