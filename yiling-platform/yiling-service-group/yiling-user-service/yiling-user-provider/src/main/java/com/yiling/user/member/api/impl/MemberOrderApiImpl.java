package com.yiling.user.member.api.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.member.api.MemberOrderApi;
import com.yiling.user.member.dto.MemberOrderCouponDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.request.QueryMemberCouponPageRequest;
import com.yiling.user.member.dto.request.QueryMemberOrderPageRequest;
import com.yiling.user.member.entity.MemberBuyStageDO;
import com.yiling.user.member.entity.MemberOrderCouponDO;
import com.yiling.user.member.service.MemberBuyStageService;
import com.yiling.user.member.service.MemberOrderCouponService;
import com.yiling.user.member.service.MemberOrderService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员订单 API 实现
 *
 * @author: lun.yu
 * @date: 2022-10-09
 */
@Slf4j
@DubboService
public class MemberOrderApiImpl implements MemberOrderApi {

    @Autowired
    MemberOrderService memberOrderService;
    @Autowired
    MemberBuyStageService memberBuyStageService;
    @Autowired
    MemberOrderCouponService memberOrderCouponService;

    @Override
    public MemberOrderDTO getMemberOrderByOrderNo(String orderNo) {
        return memberOrderService.getMemberOrderByOrderNo(orderNo);
    }

    @Override
    public MemberOrderDTO getMemberOrderByOrderId(Long orderId) {
        return PojoUtils.map(memberOrderService.getById(orderId), MemberOrderDTO.class);
    }

    @Override
    public Page<MemberOrderDTO> queryMemberOrderPage(QueryMemberOrderPageRequest request) {
        return memberOrderService.queryMemberOrderPage(request);
    }

    @Override
    public Long getMemberOrderCouponId(String orderNo) {
        return memberOrderCouponService.getMemberOrderCouponId(orderNo);
    }

    @Override
    public List<MemberOrderCouponDTO> getByCouponId(Long couponId) {
        List<MemberOrderCouponDO> orderCouponDOList = memberOrderCouponService.getByCouponId(couponId);
        return getMemberOrderCouponDto(orderCouponDOList);
    }

    @Override
    public Page<MemberOrderCouponDTO> queryMemberOrderPageByCoupon(QueryMemberCouponPageRequest request) {
        Page<MemberOrderCouponDO> orderCouponDOPage = memberOrderCouponService.queryMemberOrderPageByCoupon(request);
        List<MemberOrderCouponDTO> orderCouponDTOList = getMemberOrderCouponDto(orderCouponDOPage.getRecords());

        Page<MemberOrderCouponDTO> couponDTOPage = PojoUtils.map(orderCouponDOPage, MemberOrderCouponDTO.class);
        couponDTOPage.setRecords(orderCouponDTOList);

        return couponDTOPage;
    }

    @Override
    public Map<Long, Long> getMemberCouponUseTimes(List<Long> couponActivityIdList) {
        return memberOrderCouponService.getMemberCouponUseTimes(couponActivityIdList);
    }

    private List<MemberOrderCouponDTO> getMemberOrderCouponDto(List<MemberOrderCouponDO> orderCouponDOList) {
        if (CollUtil.isEmpty(orderCouponDOList)) {
            return ListUtil.toList();
        }
        List<Long> stageIdList = orderCouponDOList.stream().map(MemberOrderCouponDO::getBuyStageId).distinct().collect(Collectors.toList());
        Map<Long, MemberBuyStageDO> stageMap = memberBuyStageService.listByIds(stageIdList).stream().collect(Collectors.toMap(BaseDO::getId, Function.identity()));

        List<String> memberOrderNoList = orderCouponDOList.stream().map(MemberOrderCouponDO::getOrderNo).collect(Collectors.toList());
        Map<String, MemberOrderDTO> orderMap = memberOrderService.getMemberOrderByOrderNoList(memberOrderNoList).stream().collect(Collectors.toMap(MemberOrderDTO::getOrderNo, Function.identity()));

        List<MemberOrderCouponDTO> orderCouponDTOList = PojoUtils.map(orderCouponDOList, MemberOrderCouponDTO.class);
        orderCouponDTOList.forEach(memberOrderCouponDTO -> {
            memberOrderCouponDTO.setValidTime(stageMap.getOrDefault(memberOrderCouponDTO.getBuyStageId(), new MemberBuyStageDO()).getValidTime());
            memberOrderCouponDTO.setOriginalPrice(orderMap.get(memberOrderCouponDTO.getOrderNo()).getOriginalPrice());
            memberOrderCouponDTO.setDiscountAmount(orderMap.get(memberOrderCouponDTO.getOrderNo()).getDiscountAmount());
            memberOrderCouponDTO.setPayAmount(orderMap.get(memberOrderCouponDTO.getOrderNo()).getPayAmount());
        });
        return orderCouponDTOList;
    }

}
