package com.yiling.admin.erp.flow.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售流向信息VO
 *
 * @author: houjie.sun
 * @date: 2022/2/15
 */
@Data
public class FlowSalePageVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "1-商品名称")
    private String goodsName;

    /**
     * op库主键
     */
    private String soId;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "1-商品规格")
    private String soSpecifications;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "1-批准文号")
    private String soLicense;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "1-生产厂家")
    private String soManufacturer;

    /**
     * 商业代码（商家eid）
     */
    @ApiModelProperty(value = "2-商业代码（商家eid）")
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "2-商业名称（商家名称）")
    private String ename;

    /**
     * 客户编码（客户内码）
     */
    @ApiModelProperty(value = "3-客户编码（客户内码）")
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "3-客户名称")
    private String enterpriseName;

    /**
     * 订单来源
     */
    @ApiModelProperty(value = "4-订单来源")
    private String soSource;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "5-销售时间")
    private Date soTime;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "5-销售时间")
    private String soTimeStr;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "6-销售数量")
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "7-商品单位")
    private String soUnit;

    /**
     * 价格
     */
    @ApiModelProperty(value = "8-价格")
    private BigDecimal soPrice;

    /**
     * 金额
     */
    @ApiModelProperty(value = "9-金额")
    private BigDecimal soTotalAmount;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "10-批次号")
    private String soBatchNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private Date soProductTime;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "11-有效期")
    private Date soEffectiveTime;

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

    /**
     * 商销价
     */
    @ApiModelProperty(value = "商销价")
    private BigDecimal ylPrice;

    /**
     * 商销价总金额
     */
    @ApiModelProperty(value = "商销价总金额")
    private BigDecimal totalYlPrice;

    /**
     * 商业标签，多个用逗号隔开
     */
    @ApiModelProperty(value = "商业标签")
    private String tagNames;

    /**
     * 商务负责人
     */
    @ApiModelProperty(value = "商务负责人")
    private String customerContact;

    /**
     * 以岭品ID
     */
    @ApiModelProperty(value = "以岭品ID")
    private Long ylGoodsId;

    /**
     * 以岭品ID（返回前端字段）
     */
    @ApiModelProperty(value = "以岭品ID")
    private String ylGoodsIdStr;
}
