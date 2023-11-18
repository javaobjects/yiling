package com.yiling.admin.b2b.coupon.from;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityAutoGiveGoodsLimitDetailFrom extends BaseForm {

    /**
     * 自动发券活动ID
     */
    @ApiModelProperty("自动发券活动ID")
    @NotNull
    private Long couponActivityAutoGiveId;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

}
