package com.yiling.marketing.couponactivity.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityCanUseDTO extends BaseDTO {

    /**
     * 平台优惠券列表
     */
    private List<CouponActivityCanUseDetailDTO> platformList;

    /**
     * 商家优惠券列表
     */
    private List<CouponActivityCanUseDetailDTO> businessList;

}
