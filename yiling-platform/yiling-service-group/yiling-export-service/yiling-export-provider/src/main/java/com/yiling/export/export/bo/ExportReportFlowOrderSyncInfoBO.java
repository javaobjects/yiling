package com.yiling.export.export.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@Data
@Accessors(chain = true)
public class ExportReportFlowOrderSyncInfoBO {

    /**
     * 同步信息表id
     */
    private Long id;

    /**
     * 订单编号
     */
    private String soNo;

    /**
     * 采购商ID
     */
    private String enterpriseInnerCode;

    /**
     * 采购商名称
     */
    private String enterpriseName;

    /**
     * 供应商ID
     */
    private Long eid;

    /**
     * 供应商名称
     */
    private String sellerEName;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 商品规格
     */
    private String soSpecifications;

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
