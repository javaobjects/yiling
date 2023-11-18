package com.yiling.admin.pop.recommend.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * banner商品 VO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecommendGoodsVO extends BaseVO {

    /**
     * RecommendID
     */
    @ApiModelProperty(value = "recommendId")
    private Long recommendId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * banner图片值
     */
    @ApiModelProperty(value = "banner图片值")
    private String pic;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specifications;

    /**
     * 规格单位
     */
    @ApiModelProperty(value = "规格单位")
    private String unit;

    /**
     * 注册证号（批准文号）
     */
    @ApiModelProperty(value = "注册证号（批准文号）")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 挂网价
     */
    @ApiModelProperty(value = "挂网价")
    private BigDecimal price;
}
