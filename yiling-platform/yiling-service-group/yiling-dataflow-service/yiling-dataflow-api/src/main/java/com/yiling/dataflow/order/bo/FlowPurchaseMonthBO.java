package com.yiling.dataflow.order.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/12/12
 */
@Data
public class FlowPurchaseMonthBO implements Serializable {


    private static final long serialVersionUID = 4607031443156586855L;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 购进月份
     */
    private String poMonth;

}

