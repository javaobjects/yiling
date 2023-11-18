package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/4/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderCouponCanUseVO extends BaseVO {

    @ApiModelProperty(value = "优惠劵信息")
    private CouponActivityCanUseDTO activityCanUseDTO;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalAmount;
}
