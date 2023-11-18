package com.yiling.admin.b2b.lottery.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsGiftListVO extends BaseVO {
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 商品类别（1-真实物品；2-虚拟物品；3-优惠券；4-会员）
     */
    @ApiModelProperty("商品类别：1-真实物品；2-虚拟物品；3-优惠券；4-会员")
    private Integer goodsType;

    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private Long quantity;

    /**
     * 商品安全库存数量
     */
    @ApiModelProperty("商品安全库存数量")
    private Long safeQuantity;

    /**
     * 可用商品数量
     */
    @ApiModelProperty("可用商品数量")
    private Long availableQuantity;

    /**
     * 所属业务（1-全部；2-2b；3-2c
     */
    @ApiModelProperty("所属业务（1-全部；2-2b；3-2c")
    private Integer businessType;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
     * 商品品牌
     */
    @ApiModelProperty("商品品牌")
    private String brand;

    /**
     * 商品价值
     */
    @ApiModelProperty("商品价值")
    private BigDecimal price;

    /**
     * 是否参加活动：true-是，false-否
     */
    @ApiModelProperty("是否参加活动：true-是，false-否")
    private Boolean joinActivityFlag;
}
