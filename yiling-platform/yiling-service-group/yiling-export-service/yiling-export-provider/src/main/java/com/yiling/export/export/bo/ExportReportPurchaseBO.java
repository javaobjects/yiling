package com.yiling.export.export.bo;

import lombok.Data;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
public class ExportReportPurchaseBO  {

    /**
     * 库存id
     */
    private Long id;

    /**
     * eid
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String goodsSpecifications;

    /**
     * 以岭商品id
     */
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecifications;

    /**
     * 采购库存
     */
    private Long totalPoQuantity;

    /**
     * 采购渠道：1-大运河采购 2-京东采购
     */
    private Integer poSource;

    /**
     * 采购渠道：1-大运河采购 2-京东采购
     */
    private String poSourceStr;

    /**
     * 已返利库存
     */
    private Long rebateStock;

    /**
     * 采购剩余库存
     */
    private Long poQuantity;

    /**
     * 调整库存
     */
    private Long reviseStock;


}
