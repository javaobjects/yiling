package com.yiling.open.erp.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/6/24
 */
@Data
public class ErpPurchaseFlowMonthBO implements Serializable {

    private static final long serialVersionUID = -3653122300699625231L;

    /**
     * 企业id
     */
    private Long suId;

    /**
     * 当前库存月份
     */
    private String poMonth;
}
