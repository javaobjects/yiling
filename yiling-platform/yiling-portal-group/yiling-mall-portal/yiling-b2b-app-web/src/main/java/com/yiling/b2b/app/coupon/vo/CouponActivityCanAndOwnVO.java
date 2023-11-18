package com.yiling.b2b.app.coupon.vo;

import java.util.List;

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
public class CouponActivityCanAndOwnVO extends BaseVO {


    /**
     * 可领取优惠券活动列表
     */
    @ApiModelProperty(value = "可领取优惠券活动列表")
    private List<CouponActivityHasGetVO> canList;

    /**
     * 已领取优惠券活动列表
     */
    @ApiModelProperty(value = "已领取优惠券活动列表")
    private List<CouponActivityHasGetVO> ownList;


}
