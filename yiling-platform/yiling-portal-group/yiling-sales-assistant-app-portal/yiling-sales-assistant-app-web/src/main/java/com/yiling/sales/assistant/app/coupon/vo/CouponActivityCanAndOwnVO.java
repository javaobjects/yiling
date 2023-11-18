package com.yiling.sales.assistant.app.coupon.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 店铺优惠券列表VO
 *
 * @author: houjie.sun
 * @date: 2022/04/06
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
