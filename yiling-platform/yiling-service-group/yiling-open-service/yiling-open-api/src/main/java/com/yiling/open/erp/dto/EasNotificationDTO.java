package com.yiling.open.erp.dto;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/8/4
 */
@Data
public class EasNotificationDTO implements java.io.Serializable{

    /**
     * 类型 1：生成销售订单 2：出库推应收 3.退货 4.重新调用通过出库单回写应收单单号
     */
    private String type;

    /**
     * 传递参数 type为1时传pop订单号，type为2时EAS出库单号，type为3时EAS出库单号
     */
    private String json;

    private String orderId;
}
