package com.yiling.order.order.dto;

import lombok.Data;

/**
 *
 * @author:tingwei.chen
 * @date:2021/6/18
 */
@Data
public class ReturnOrderNumberDTO implements java.io.Serializable {
    /**
     * 今天退货单数量
     */
    private Integer todayReturnOrderNum;

    /**
     * 昨天退货订单数量
     */
    private Integer yesterdayReturnOrderNum;

    /**
     * 所有退货单数量
     */
    private Integer AllReturnOrderNum;

}

