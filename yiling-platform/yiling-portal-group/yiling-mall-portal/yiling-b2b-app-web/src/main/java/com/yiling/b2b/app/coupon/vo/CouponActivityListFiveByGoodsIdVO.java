package com.yiling.b2b.app.coupon.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityListFiveByGoodsIdVO extends BaseVO {

    /**
     * 优惠劵名称
     */
    @ApiModelProperty(value = "优惠劵名称")
    private String name;

    /**
     * 优惠规则
     */
    @ApiModelProperty(value = "优惠规则")
    private String couponRules;

}
