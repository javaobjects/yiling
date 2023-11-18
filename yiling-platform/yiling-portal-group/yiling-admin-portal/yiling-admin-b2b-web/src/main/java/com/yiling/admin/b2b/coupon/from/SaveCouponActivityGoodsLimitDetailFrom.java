package com.yiling.admin.b2b.coupon.from;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author houjie.sun
 * @date 2021-11-01
 */
@Data
@Accessors(chain = true)
public class SaveCouponActivityGoodsLimitDetailFrom {

    /**
     * 优惠券活动ID
     */
    @ApiModelProperty("优惠券活动ID")
    @NotNull
    private Long couponActivityId;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    @NotNull
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    @NotEmpty
    private String ename;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    @NotNull
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    @NotEmpty
    private String goodsName;

    /**
     * 以岭品
     */
    @ApiModelProperty("以岭品 1以岭品 2非以岭品")
    @NotEmpty
    private Integer yilingGoodsFlag;

}
