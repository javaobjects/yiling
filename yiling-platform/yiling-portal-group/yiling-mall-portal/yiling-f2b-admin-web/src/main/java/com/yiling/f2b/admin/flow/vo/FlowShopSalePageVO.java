package com.yiling.f2b.admin.flow.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 连锁纯销流向信息VO
 *
 * @author: houjie.sun
 * @date: 2023/4/6
 */
@Data
public class FlowShopSalePageVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
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
    @ApiModelProperty(value = "商品规格")
    private String soSpecifications;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
    private String soLicense;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String soManufacturer;

    /**
     * 商业代码（商家eid）
     */
    @ApiModelProperty(value = "总店商家eid）")
    private Long eid;

    /**
     * 总店名称（商家名称）
     */
    @ApiModelProperty(value = "总店名称")
    private String ename;

    /**
     * 商业代码（商家eid）
     */
    @ApiModelProperty(value = "门店商家eid）")
    private Long shopEid;

    /**
     * 门店名称
     */
    @ApiModelProperty(value = "门店名称")
    private String shopEname;

    /**
     * 门店编码
     */
    @ApiModelProperty(value = "门店编码")
    private String shopNo;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "销售时间")
    private Date soTime;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "销售时间")
    private String soTimeStr;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "单位")
    private String soUnit;

    /**
     * 价格
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal soPrice;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal soTotalAmount;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批号")
    private String soBatchNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private Date soProductTime;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Date soEffectiveTime;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "省")
    private String provinceName;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "市")
    private String cityName;

    /**
     * 所属区域名称
     */
    @ApiModelProperty(value = "区")
    private String regionName;

    /**
     * 商品内码
     */
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

}
