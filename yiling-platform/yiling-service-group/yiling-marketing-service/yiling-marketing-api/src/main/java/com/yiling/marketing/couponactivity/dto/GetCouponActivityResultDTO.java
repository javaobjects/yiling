package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author houjie.sun
 * @date 2021-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetCouponActivityResultDTO extends BaseDTO {

    private Boolean getSussess;

    /**
     * 是否到达上限
     */
    private Boolean toLimit;

    /**
     * 是否到达上限(每日)
     */
    private Boolean toLimitDaily;
}
