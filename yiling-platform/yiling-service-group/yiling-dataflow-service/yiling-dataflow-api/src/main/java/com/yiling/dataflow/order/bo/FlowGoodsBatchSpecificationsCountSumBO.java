package com.yiling.dataflow.order.bo;

import lombok.Data;

/**
 * 库存按规格汇总数量类
 *
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Data
public class FlowGoodsBatchSpecificationsCountSumBO implements java.io.Serializable{

    private static final long serialVersionUID = 1163585498073994261L;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商品规格
     */
    private String gbSpecifications;

    /**
     * 数量汇总
     */
    private Long sum;

}
