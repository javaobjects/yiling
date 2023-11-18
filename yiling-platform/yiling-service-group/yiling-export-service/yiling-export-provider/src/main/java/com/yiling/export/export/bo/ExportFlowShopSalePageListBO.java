package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/4/12
 */
@Data
public class ExportFlowShopSalePageListBO {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String soSpecifications;

    /**
     * 批准文号
     */
    private String soLicense;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 门店商业代码（商家eid）
     */
    private Long shopEid;

    /**
     * 门店商业名称（商家名称）
     */
    private String shopEname;

    /**
     * 门店编码
     */
    private String shopNo;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 销售日期
     */
    private String soTimeStr;

    /**
     * 销售数量
     */
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    private String soUnit;

    /**
     * 价格
     */
    private BigDecimal soPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;


    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 批次号
     */
    private String soBatchNo;

    /**
     * 生产日期
     */
    private Date soProductTime;

    /**
     * 有效期
     */
    private Date soEffectiveTime;

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

}
