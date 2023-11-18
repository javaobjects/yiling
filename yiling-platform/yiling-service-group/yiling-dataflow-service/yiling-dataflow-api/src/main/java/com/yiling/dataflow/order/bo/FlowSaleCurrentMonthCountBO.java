package com.yiling.dataflow.order.bo;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/10/18
 */
@Data
public class FlowSaleCurrentMonthCountBO implements java.io.Serializable{

    private static final long serialVersionUID = 9036187290923655748L;

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 当前月份销售数据总条数
     */
    private Long currentMonthCount;

}
