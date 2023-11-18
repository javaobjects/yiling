package com.yiling.dataflow.order.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/5/26
 */
@Data
public class FlowPurchaseInventoryBO implements java.io.Serializable {

    private static final long serialVersionUID = -303901080772526085L;

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 大运河库存ID
     */
    private Long dyhId;

    /**
     * 大运河库存数量
     */
    private BigDecimal dyhQuantity;

    /**
     * 京东库存ID
     */
    private Long jdId;

    /**
     * 京东库存数量
     */
    private BigDecimal jdQuantity;

}
