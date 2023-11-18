package com.yiling.export.export.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@Data
@Accessors(chain = true)
public class ExportReportB2bOrderSyncInfoBO {

    /**
     * 同步信息表id
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 采购商名称
     */
    private String buyerEName;

    /**
     * erp客户名称
     */
    private String erpCustomerName;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 供应商名称
     */
    private String sellerEName;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品内码
     */
    private String goodsErpCode;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylSpecifications;

    /**
     * 标识状态
     */
    private Integer identificationStatus;

    /**
     * 标识状态
     */
    private String identificationStatusStr;

}
