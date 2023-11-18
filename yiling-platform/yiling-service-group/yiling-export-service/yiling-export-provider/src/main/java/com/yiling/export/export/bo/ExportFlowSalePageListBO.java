package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Data
public class ExportFlowSalePageListBO {

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
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 客户编码（客户内码）
     */
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 订单来源
     */
    private String soSource;

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

    /**
     * 商销价
     */
    private BigDecimal ylPrice;

    /**
     * 商销价总金额
     */
    private BigDecimal totalYlPrice;

    /**
     * 商业标签，多个用逗号隔开
     */
    private String tagNames;

    /**
     * 商务负责人
     */
    private String customerContact;

    /**
     * 以岭品ID
     */
    private Long ylGoodsId;
}
