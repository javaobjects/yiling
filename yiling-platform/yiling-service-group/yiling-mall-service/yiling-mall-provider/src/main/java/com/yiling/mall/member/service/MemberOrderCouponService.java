package com.yiling.mall.member.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.member.entity.MemberOrderCouponDO;

/**
 * <p>
 * B2B-会员订单使用优惠券表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-19
 */
public interface MemberOrderCouponService extends BaseService<MemberOrderCouponDO> {

    /**
     * 根据优惠券ID获取会员订单使用的该优惠券信息
     *
     * @param couponId
     * @return
     */
    List<MemberOrderCouponDO> getByCouponId(Long couponId);

}
