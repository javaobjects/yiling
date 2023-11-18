package com.yiling.marketing.couponactivity.dto.request;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityIdAndEidDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;
}
