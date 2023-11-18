package com.yiling.admin.sales.assistant.task.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任务选择商品
 * @author ray
 * @date 2021-9-28
 */
@Data
@Accessors(chain=true)
public class TaskSelectGoodsVO {



    @ApiModelProperty(value = "商品主键")
    private Long goodsId;


    /**
     * 商品其他属性调用远程接口查询
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 销售规格
     */
    @ApiModelProperty(value = "销售规格")
    private String sellSpecifications;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "产地")
    private String manufacturerAddress;


    @ApiModelProperty(value = "是否拆零销售")
    private Boolean canSplit;

    @ApiModelProperty(value = "基价")
    private String price;

    @ApiModelProperty(value = "商品默认图片")
    private String goodsPic;

    @ApiModelProperty(value = "中包装")
    private Integer middlePackage;

    @ApiModelProperty(value = "大包装数量")
    private Integer bigPackage;



    @ApiModelProperty(value = "单位")
    private String sellUnit;
    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    @ApiModelProperty(value = "佣金 企业任务不设")
    private BigDecimal commission;

    @ApiModelProperty(value = "出货价")
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    @ApiModelProperty(value = "商销价")
    private BigDecimal  sellPrice;
}
