package com.yiling.dataflow.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveFlowPurchaseInventoryTotalPoQuantityRequest extends BaseRequest {

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
     * 库存总数量
     */
    private BigDecimal totalPoQuantity;

    /**
     * 采购来源：1-大运河 2-京东
     */
    private Integer poSource;

}
