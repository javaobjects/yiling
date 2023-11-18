package com.yiling.dataflow.order.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/9/20
 */
@Data
public class FlowPurchaseInventoryTotalQuantityBO implements java.io.Serializable{

    private static final long serialVersionUID = 4158942165855926149L;

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 商业商品名称
     */
    private String goodsName;

    /**
     * 商业商品内码
     */
    private String goodsInSn;

    /**
     * 商业商品规格
     */
    private String goodsSpecifications;

    /**
     * 库存总数量
     */
    private BigDecimal totalPoQuantity;

    /**
     * 采购来源：1-大运河 2-京东
     */
    private Integer poSource;

}
