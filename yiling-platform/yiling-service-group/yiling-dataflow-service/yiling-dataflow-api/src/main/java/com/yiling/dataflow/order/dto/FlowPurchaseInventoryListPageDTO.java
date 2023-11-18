package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowPurchaseInventoryListPageDTO extends BaseDTO {

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
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 以岭品名称
     */
    private String ylGoodsName;

    /**
     * 以岭品规格
     */
    private String ylGoodsSpecifications;

    /**
     * 库存数量
     */
    private BigDecimal poQuantity;

    /**
     * 采购来源：1-大运河 2-京东
     */
    private Integer poSource;

    /**
     * 库存数量总计
     */
    private BigDecimal totalPoQuantity;

}
