package com.yiling.goods.medicine.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSkuFullDTO extends BaseDTO {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 包装数量
     */
    private Long packageNumber;

    /**
     * 商品产品线
     */
    private Integer goodsLine;

    /**
     * ERP内码
     */
    private String inSn;

    /**
     * ERP编码
     */
    private String sn;

    /**
     * sku属性扩展字段
     */
    private String extensionField;

    /**
     * 库存
     */
    private Long qty;

    /**
     * 实际库存
     */
    private Long realQty;

    /**
     * 冻结库存
     */
    private Long frozenQty;

    /**
     * 库存Id
     */
    private Long inventoryId;

    /**
     * 状态 0：正常 1：停用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商品信息
     */
    private GoodsFullDTO goodsInfo;
}
