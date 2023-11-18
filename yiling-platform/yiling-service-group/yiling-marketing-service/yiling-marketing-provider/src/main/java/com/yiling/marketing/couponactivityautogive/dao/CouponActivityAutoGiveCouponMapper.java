package com.yiling.marketing.couponactivityautogive.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveCouponDO;

/**
 * <p>
 * 自动发券活动关联优惠券活动表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityAutoGiveCouponMapper extends BaseMapper<CouponActivityAutoGiveCouponDO> {

    CouponActivityAutoGiveCouponDO getEffectiveAutoGiveCouponByByCouponActivityId(@Param("couponActivityId") Long couponActivityId);

}
