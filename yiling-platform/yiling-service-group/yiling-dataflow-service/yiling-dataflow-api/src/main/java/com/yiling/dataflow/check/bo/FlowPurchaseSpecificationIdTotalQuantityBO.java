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
public class FlowPurchaseSpecificationIdTotalQuantityBO implements Serializable {

    private static final long serialVersionUID = 3284278755136246087L;

    /**
     * 采购商eid
     */
    private Long eid;

    /**
     * 采购企业名称
     */
    private String ename;

    /**
     * 供应商eid
     */
    private Long supplierId;

    /**
     * 商品规格id
     */
    private Long specificationId;

    /**
     * 商品规格id
     */
    private String poBatchNo;

    /**
     * 采购时间
     */
    private Date poTime;

    /**
     * 采购数量小计
     */
    private BigDecimal totalPoQuantity;

}
