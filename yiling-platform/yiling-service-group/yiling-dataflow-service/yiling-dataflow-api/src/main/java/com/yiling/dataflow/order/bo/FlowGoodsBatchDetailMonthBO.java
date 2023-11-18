package com.yiling.dataflow.order.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/12/9
 */
@Data
public class FlowGoodsBatchDetailMonthBO implements Serializable {

    private static final long serialVersionUID = 4080205073130518990L;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 当前库存月份
     */
    private String gbDetailMonth;

}
