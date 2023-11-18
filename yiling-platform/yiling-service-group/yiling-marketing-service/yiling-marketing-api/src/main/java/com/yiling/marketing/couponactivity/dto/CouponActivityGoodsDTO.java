package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author houjie.sun
 * @date 2021-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityGoodsDTO extends BaseDTO {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    private Integer goodsType;

    /**
     * 销售规格
     */
    private String sellSpecifications;

    /**
     * 商品状态 1上架 2下架 3待设置
     */
    private Integer goodsStatus;

    /**
     * 销售价（商品基价）
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Long goodsInventory;

    /**
     * 单位
     */
    private String sellUnit;

    /**
     * 是否以岭品 true是 false不是
     */
    private Integer yilingGoodsFlag;
}
