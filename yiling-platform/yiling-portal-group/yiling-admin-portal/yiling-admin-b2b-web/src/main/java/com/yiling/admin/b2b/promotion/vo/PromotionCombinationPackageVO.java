package com.yiling.admin.b2b.promotion.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动-秒杀&特价
 *
 * @author: fan.shen
 * @date: 2022/1/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionCombinationPackageVO extends BaseVO {

    @ApiModelProperty(value = "促销活动ID")
    private Long promotionActivityId;

    @ApiModelProperty(value = "组合包名称")
    private String packageName;

    @ApiModelProperty(value = "组合包起购数量")
    private Integer initialNum;

    @ApiModelProperty(value = "退货要求")
    private String returnRequirement;

    @ApiModelProperty(value = "组合包商品简称")
    private String packageShortName;

    /**
     * 总数量
     */
    @ApiModelProperty(value = "总数量")
    private Integer totalNum;

    /**
     * 每人最大数量
     */
    @ApiModelProperty(value = "每人最大数量")
    private Integer perPersonNum;

    /**
     * 每人每天数量
     */
    @ApiModelProperty(value = "每人每天数量")
    private Integer perDayNum;

    @ApiModelProperty(value = "活动图片")
    private String pic;

    @ApiModelProperty(value = "组合包与其他营销活动说明")
    private String descriptionOfOtherActivity;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "实际销售价")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "组合包活动价")
    private BigDecimal combinationPackagePrice;

    @ApiModelProperty(value = "立省")
    private BigDecimal combinationDiscountPrice;
}
