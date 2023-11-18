package com.yiling.hmc.settlement.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/7/5
 */
@Data
public class SyncOrderResultBO implements Serializable {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;
}
