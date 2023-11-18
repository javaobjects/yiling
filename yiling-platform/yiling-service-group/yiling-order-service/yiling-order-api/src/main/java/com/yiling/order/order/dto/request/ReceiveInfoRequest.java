package com.yiling.order.order.dto.request;

import lombok.Data;

/**
 * 收货批次信息
 * @author:wei.wang
 * @date:2021/6/25
 */
@Data
public class ReceiveInfoRequest implements java.io.Serializable {
    /**
     * 收货批次id
     */
    private Long id;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;
}
