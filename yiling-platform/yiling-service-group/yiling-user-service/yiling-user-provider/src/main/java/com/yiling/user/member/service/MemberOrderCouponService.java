package com.yiling.user.member.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.dto.request.QueryMemberCouponPageRequest;
import com.yiling.user.member.entity.MemberOrderCouponDO;
import com.yiling.framework.common.base.BaseService;

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

    /**
     * 根据会员订单号查询当前会员订单所使用的优惠券ID
     *
     * @param orderNo
     * @return
     */
    Long getMemberOrderCouponId(String orderNo);

    /**
     * 获取会员优惠券使用订单分页列表
     *
     * @param request
     * @return
     */
    Page<MemberOrderCouponDO> queryMemberOrderPageByCoupon(QueryMemberCouponPageRequest request);

    /**
     * 根据优惠券活动ID批量获取使用数量
     *
     * @param couponActivityIdList 优惠券活动ID集合
     * @return key为优惠券活动ID，value为使用数量
     */
    Map<Long, Long> getMemberCouponUseTimes(List<Long> couponActivityIdList);

}
