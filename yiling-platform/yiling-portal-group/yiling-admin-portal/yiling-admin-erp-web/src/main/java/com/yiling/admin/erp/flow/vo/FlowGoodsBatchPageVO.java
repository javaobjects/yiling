package com.yiling.admin.erp.flow.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 库存流向信息VO
 *
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Data
public class FlowGoodsBatchPageVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "1-商品名称")
    private String gbName;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "1-商品规格")
    private String gbSpecifications;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "1-批准文号")
    private String gbLicense;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "1-生产厂家")
    private String gbManufacturer;

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
     * 批次号
     */
    @ApiModelProperty(value = "3-批次号")
    private String gbBatchNo;

    /**
     * 订单来源
     */
    @ApiModelProperty(value = "4-订单来源")
    private String gbSource;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "4-生产日期")
    private Date gbProduceTime;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "5-有效日期")
    private Date gbEndTime;

    /**
     * 入库日期
     */
    @ApiModelProperty(value = "6-入库日期")
    private Date gbTime;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "7-数量")
    private BigDecimal gbNumber;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "8-单位")
    private String gbUnit;

    /**
     * 总计数量（按规格维度数量求和）
     */
    @ApiModelProperty(value = "9-总计数量")
    private BigDecimal totalNumber;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "10-省")
    private String provinceName;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "11-市")
    private String cityName;

    /**
     * 所属区域名称
     */
    @ApiModelProperty(value = "12-区")
    private String regionName;

    /**
     * 商品内码
     */
    @ApiModelProperty(value = "商品内码")
    private String inSn;

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
