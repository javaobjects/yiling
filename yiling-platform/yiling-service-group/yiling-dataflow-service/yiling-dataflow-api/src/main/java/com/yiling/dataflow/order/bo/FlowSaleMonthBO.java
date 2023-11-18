package com.yiling.dataflow.order.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/12/13
 */
@Data
public class FlowSaleMonthBO implements Serializable {

    private static final long serialVersionUID = 107835858607809590L;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 销售月份
     */
    private String soMonth;
}
