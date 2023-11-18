package com.yiling.user.member.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberDetailDTO;
import com.yiling.user.member.dto.MemberOrderCouponDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.CreateMemberRequest;
import com.yiling.user.member.dto.request.ImportOpenMemberRequest;
import com.yiling.user.member.dto.request.OpenMemberRequest;
import com.yiling.user.member.dto.request.QueryMemberCouponPageRequest;
import com.yiling.user.member.dto.request.QueryMemberOrderPageRequest;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberPromoterRequest;
import com.yiling.user.member.dto.request.UpdateMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;

/**
 * 会员订单 API
 *
 * @author: lun.yu
 * @date: 2022-10-09
 */
public interface MemberOrderApi {

    /**
     * 根据订单号查询会员订单信息
     *
     * @param orderNo
     * @return
     */
    MemberOrderDTO getMemberOrderByOrderNo(String orderNo);

    /**
     * 根据订单ID查询会员订单信息
     *
     * @param orderId
     * @return
     */
    MemberOrderDTO getMemberOrderByOrderId(Long orderId);

    /**
     * 查询会员订单分页列表
     *
     * @param request
     * @return
     */
    Page<MemberOrderDTO> queryMemberOrderPage(QueryMemberOrderPageRequest request);

    /**
     * 根据会员订单号查询当前会员订单所使用的优惠券ID
     *
     * @param orderNo 会员订单号
     * @return 返回0或空表示当前订单未使用优惠券
     */
    Long getMemberOrderCouponId(String orderNo);

    /**
     * 根据优惠券ID获取会员订单使用的该优惠券信息
     *
     * @param couponId
     * @return
     */
    List<MemberOrderCouponDTO> getByCouponId(Long couponId);

    /**
     * 根据优惠券活动ID获取会员订单使用的该优惠券分页列表
     *
     * @param request
     * @return
     */
    Page<MemberOrderCouponDTO> queryMemberOrderPageByCoupon(QueryMemberCouponPageRequest request);

    /**
     * 根据优惠券活动ID批量获取使用数量
     *
     * @param couponActivityIdList 优惠券活动ID集合
     * @return key为优惠券活动ID，value为使用数量
     */
    Map<Long, Long> getMemberCouponUseTimes(List<Long> couponActivityIdList);


}
