package com.yiling.user.member.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.dto.request.QueryMemberCouponPageRequest;
import com.yiling.user.member.entity.MemberOrderCouponDO;
import com.yiling.user.member.dao.MemberOrderCouponMapper;
import com.yiling.user.member.enums.MemberOrderCouponUseStatusEnum;
import com.yiling.user.member.service.MemberOrderCouponService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

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
        wrapper.eq(MemberOrderCouponDO::getUseStatus, MemberOrderCouponUseStatusEnum.USED.getCode());
        return this.list(wrapper);
    }

    @Override
    public Long getMemberOrderCouponId(String orderNo) {
        LambdaQueryWrapper<MemberOrderCouponDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberOrderCouponDO::getOrderNo, orderNo);
        wrapper.eq(MemberOrderCouponDO::getUseStatus, MemberOrderCouponUseStatusEnum.USED.getCode());
        wrapper.last("limit 1");

        return Optional.ofNullable(this.getOne(wrapper)).orElse(new MemberOrderCouponDO()).getCouponId();
    }

    @Override
    public Page<MemberOrderCouponDO> queryMemberOrderPageByCoupon(QueryMemberCouponPageRequest request) {
        LambdaQueryWrapper<MemberOrderCouponDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getCouponActivityId()) && request.getCouponActivityId() != 0) {
            wrapper.eq(MemberOrderCouponDO::getCouponActivityId, request.getCouponActivityId());
        }
        wrapper.eq(MemberOrderCouponDO::getUseStatus, MemberOrderCouponUseStatusEnum.USED.getCode());
        return this.page(request.getPage(), wrapper);
    }

    @Override
    public Map<Long, Long> getMemberCouponUseTimes(List<Long> couponActivityIdList) {
        if (CollUtil.isEmpty(couponActivityIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<MemberOrderCouponDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MemberOrderCouponDO::getCouponActivityId, couponActivityIdList);
        wrapper.eq(MemberOrderCouponDO::getUseStatus, MemberOrderCouponUseStatusEnum.USED.getCode());
        return this.list(wrapper).stream().collect(Collectors.groupingBy(MemberOrderCouponDO::getCouponActivityId, Collectors.counting()));
    }

}
