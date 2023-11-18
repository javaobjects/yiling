package com.yiling.f2b.admin.flow.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 采购流向信息VO
 *
 * @author: houjie.sun
 * @date: 2022/2/15
 */
@Data
public class FlowPurchasePageVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "1-商品名称")
    private String goodsName;

    /**
     * op库主键
     */
    private String poId;

    /**
     * Erp采购订单号
     */
    private String poNo;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "1-商品规格")
    private String poSpecifications;

    /**
     * 商品批准文号
     */
    @ApiModelProperty(value = "1-商品批准文号")
    private String poLicense;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "1-生产厂家")
    private String poManufacturer;

    /**
     * 供应商编码（供应商内码）
     */
    @ApiModelProperty(value = "2-供应商编码（供应商内码）")
    private String enterpriseInnerCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "2-供应商名称")
    private String enterpriseName;

    /**
     * 商业代码（商家eid）
     */
    @ApiModelProperty(value = "3-商业代码（商家eid）")
    private Integer eid;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "3-商业名称（商家名称）")
    private String ename;

    /**
     * 订单来源
     */
    @ApiModelProperty(value = "4-订单来源")
    private String poSource;

    /**
     * 购进时间
     */
    @ApiModelProperty(value = "5-采购时间")
    private Date poTime;

    /**
     * 采购数量
     */
    @ApiModelProperty(value = "6-采购数量")
    private BigDecimal poQuantity;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "7-单位")
    private String poUnit;

    /**
     * 采购价格
     */
    @ApiModelProperty(value = "8-价格")
    private BigDecimal poPrice;

    /**
     * 金额
     */
    @ApiModelProperty(value = "9-金额")
    private BigDecimal poTotalAmount;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "10-批次号")
    private String poBatchNo;

    /**
     * 生产时间
     */
    @ApiModelProperty(value = "生产时间")
    private Date poProductTime;

    /**
     * 有效期时间
     */
    @ApiModelProperty(value = "11-有效日期")
    private Date poEffectiveTime;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "12-省")
    private String provinceName;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "13-市")
    private String cityName;

    /**
     * 所属区域名称
     */
    @ApiModelProperty(value = "14-区")
    private String regionName;

    /**
     * 商品内码
     */
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

    /**
     * 商品规格标准ID
     */
    @ApiModelProperty(value = "商品规格标准ID")
    private Long specificationId;

    /**
     * 标准商品名称
     */
    @ApiModelProperty(value = "标准商品名称")
    private String standardName;

    /**
     * 标准商品规格
     */
    @ApiModelProperty(value = "标准商品规格")
    private String standardSellSpecifications;


}
