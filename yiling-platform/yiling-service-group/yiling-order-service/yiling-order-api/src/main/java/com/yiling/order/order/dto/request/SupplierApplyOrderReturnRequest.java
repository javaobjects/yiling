package com.yiling.order.order.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/8/2
 */
@Data
@Accessors(chain = true)
public class SupplierApplyOrderReturnRequest implements Serializable {
    /**
     * 交易订单号
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 退货类型
     */
    private Integer returnType;

    /**
     * 当前操作人
     */
    private Long currentUserId;

    /**
     * 商品明细组装集合
     */
    private List<SupplierApplyOrderReturnDetailRequest> applyOrderReturnDetailRequestList;
}
