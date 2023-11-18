package com.yiling.admin.pop.category.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分类商品 VO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryGoodsVO extends BaseVO {

    /**
     * categoryId
     */
    @ApiModelProperty(value = "categoryId")
    private Long categoryId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * banner图片值
     */
    @ApiModelProperty(value = "banner图片值")
    private String pic;

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
