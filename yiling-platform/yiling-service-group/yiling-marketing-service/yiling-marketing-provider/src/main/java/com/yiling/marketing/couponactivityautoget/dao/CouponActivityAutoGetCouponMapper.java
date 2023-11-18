package com.yiling.marketing.couponactivityautoget.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetCouponDO;

/**
 * <p>
 * 自主领券活动关联优惠券活动表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityAutoGetCouponMapper extends BaseMapper<CouponActivityAutoGetCouponDO> {

    CouponActivityAutoGetCouponDO getOneByCouponActivityId(@Param("couponActivityId") Long couponActivityId);
}
