package com.yiling.marketing.couponactivity.dto.request;


import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponHasGetDTO extends BaseDTO {

    /**
     * 优惠券对应的领取数量
     */
    private Integer num;
}
