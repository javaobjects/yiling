package com.yiling.b2b.app.coupon.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

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
public class CouponActivityCanUseVO extends BaseVO {

    /**
     * 可使用优惠券列表
     */
    private List<CouponActivityCanUseDetailVO> couponDetailList;

}
