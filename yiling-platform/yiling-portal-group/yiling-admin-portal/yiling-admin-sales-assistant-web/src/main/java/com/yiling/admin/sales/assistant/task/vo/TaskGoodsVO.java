package com.yiling.admin.sales.assistant.task.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:gxl
 * @description: 任务关联商品
 * @date: Created in 19:55 2020/4/21
 * @modified By:
 */
@Data
public class TaskGoodsVO {

    @ApiModelProperty(value = "任务商品主键")
    private Long taskGoodsId;

    @ApiModelProperty(value = "商品主键")
    private Long goodsId;

    @ApiModelProperty(value = "任务商品基价")
    private BigDecimal price;
    @ApiModelProperty(value = "佣金 企业任务不设")
    private BigDecimal commission;

    //@ApiModelProperty(value = "按交易额算-需要佣金比例")
   // private String  commissionRate;

    // 商品其他属性调用远程接口查询
    @ApiModelProperty(value = "商品名称")
    private String goodsName;


    /**
     * 销售规格
     */
    @ApiModelProperty(value = "销售规格")
    private String sellSpecifications;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;
    @ApiModelProperty(value = "供应商")
    private String supplier;
    @ApiModelProperty(value = "是否拆零销售")
    private Boolean canSplit;



    @ApiModelProperty(value = "商品默认图片")
    private String goodsPic;

    @ApiModelProperty(value = "中包装")
    private Integer middlePackage;

    @ApiModelProperty(value = "大包装数量")
    private Integer bigPackage;



    @ApiModelProperty(value = "任务id")
    private List<Long> taskIds;


    @ApiModelProperty(value = "任务主体 0:平台任务1：企业任务")
    private Integer taskType;

    @ApiModelProperty(value = "出货价")
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    @ApiModelProperty(value = "商销价")
    private BigDecimal  sellPrice;

    @ApiModelProperty(value = "添加商品时间")
    private Date createTime;
}
