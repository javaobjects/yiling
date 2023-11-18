package com.yiling.b2b.app.coupon.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/17
 */
@Data
@Accessors(chain = true)
public class GetCouponActivityResultVO extends BaseVO {

    /**
     * 是否领取成功，true
     */
    @ApiModelProperty(required = true, value = "是否领取成功")
    private Boolean getSussess;

    /**
     * 是否到达上限
     */
    @ApiModelProperty(required = true, value = "是否到达上限")
    private Boolean toLimit;

    /**
     * 是否到达上限
     */
    @ApiModelProperty(required = true, value = "是否到达上限(每日)")
    private Boolean toLimitDaily;
}
