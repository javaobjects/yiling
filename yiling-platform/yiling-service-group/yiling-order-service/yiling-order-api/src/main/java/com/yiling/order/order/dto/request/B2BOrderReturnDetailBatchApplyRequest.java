package com.yiling.order.order.dto.request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/19
 */
@Data
@Accessors(chain = true)
public class B2BOrderReturnDetailBatchApplyRequest implements Serializable {
    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 退货数量
     */
    private Integer returnQuantity;
}
