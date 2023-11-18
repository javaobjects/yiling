package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Data
public class ExportFlowPurchasePageListBO {

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String poSpecifications;

    /**
     * 商品批准文号
     */
    private String poLicense;

    /**
     * 商品生产厂家
     */
    private String poManufacturer;

    /**
     * 供应商编码（供应商内码）
     */
    private String enterpriseInnerCode;

    /**
     * 供应商名称
     */
    private String enterpriseName;

    /**
     * 商业代码（商家eid）
     */
    private Integer eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * Erp采购订单号
     */
    private String poNo;

    /**
     * 订单来源
     */
    private String poSource;

    /**
     * 购进时间
     */
    private Date poTime;

    /**
     * 采购数量
     */
    private BigDecimal poQuantity;

    /**
     * 商品单位
     */
    private String poUnit;

    /**
     * 采购价格
     */
    private BigDecimal poPrice;

    /**
     * 金额
     */
    private BigDecimal poTotalAmount;

    /**
     * 批次号
     */
    private String poBatchNo;

    /**
     * 生产时间
     */
    private Date poProductTime;

    /**
     * 有效期时间
     */
    private Date poEffectiveTime;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 商品规格标准ID
     */
    private Long specificationId;

    /**
     * 标准商品名称
     */
    private String standardName;

    /**
     * 标准商品规格
     */
    private String standardSellSpecifications;

}
