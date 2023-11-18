package com.yiling.mall.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.mall.member.dao.MemberOrderCouponMapper;
import com.yiling.mall.member.entity.MemberOrderCouponDO;
import com.yiling.mall.member.service.MemberOrderCouponService;

/**
 * <p>
 * B2B-会员订单使用优惠券表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-19
 */
@Service
public class MemberOrderCouponServiceImpl extends BaseServiceImpl<MemberOrderCouponMapper, MemberOrderCouponDO> implements MemberOrderCouponService {

    @Override
    public List<MemberOrderCouponDO> getByCouponId(Long couponId) {
        LambdaQueryWrapper<MemberOrderCouponDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberOrderCouponDO::getCouponId, couponId);
        wrapper.orderByDesc(MemberOrderCouponDO::getCreateTime);
        return this.list(wrapper);
    }

}
