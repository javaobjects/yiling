package com.yiling.dataflow.check.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/9/9
 */
@Data
public class FlowSaleSpecificationIdTotalQuantityBO implements Serializable {

    private static final long serialVersionUID = 7450944184946871072L;

    /**
     * 商家eid
     */
    private Long eid;

    /**
     * 商品规格id
     */
    private Long specificationId;

    /**
     * 采购数量小计
     */
    private BigDecimal totalSoQuantity;

}
