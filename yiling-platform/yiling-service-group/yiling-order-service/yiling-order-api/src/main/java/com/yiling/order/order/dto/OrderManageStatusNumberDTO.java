package com.yiling.order.order.dto;

import lombok.Data;

/**
 * 审核订单数量统计
 * @author:wei.wang
 * @date:2021/7/12
 */
@Data
public class OrderManageStatusNumberDTO  implements java.io.Serializable {
    /**
     * 待审核数量
     */

    private Integer reviewingNumber;

    /**
     * 已审核数量
     */
    private Integer reviewNumber;

    /**
     * 驳回数量
     */
    private Integer rejectNumber;
}
