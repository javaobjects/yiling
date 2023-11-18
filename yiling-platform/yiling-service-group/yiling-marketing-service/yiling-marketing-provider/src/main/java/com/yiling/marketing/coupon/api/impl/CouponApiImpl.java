package com.yiling.marketing.coupon.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.bo.CouponUseOrderBO;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryCouponListPageRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponPageRequest;
import com.yiling.marketing.coupon.dto.request.QueryHasGiveCouponAutoRequest;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.dto.request.UseMemberCouponRequest;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityForMemberResultDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityMyCouponDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.service.CouponActivityForMyService;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@DubboService
public class CouponApiImpl implements CouponApi {

    @Autowired
    private CouponService              couponService;
    @Autowired
    private CouponActivityForMyService couponActivityForMyService;

    @Override
    public Page<CouponDTO> queryListPage(QueryCouponPageRequest request) {
        return couponService.queryListPage(request);
    }

    @Override
    public List<CouponDTO> getByIdList(List<Long> ids) {
        return PojoUtils.map(couponService.getByIdList(ids), CouponDTO.class);
    }

    @Override
    public Boolean returenCouponByIds(List<Long> ids) {
        return couponService.returenCouponByIds(ids);
    }

    @Override
    public List<CouponDTO> getHasGiveCountByCouponActivityList(List<Long> couponActivityIds) {
        return couponService.getHasGiveCountByCouponActivityList(couponActivityIds);
    }
    @Override
    public List<Map<String, Long>> getGiveCountByCouponActivityId(List<Long> couponActivityIdList) {
        return couponService.getGiveCountByCouponActivityId(couponActivityIdList);
    }

    @Override
    public List<CouponDTO> getHasGiveListByCouponActivityIdList(QueryHasGiveCouponAutoRequest request) {
        return couponService.getHasGiveListByCouponActivityIdList(request);
    }

    @Override
    public Boolean insertBatch(List<SaveCouponRequest> list) {
        return couponService.insertBatch(list);
    }


    @Override
    public Page<CouponUseOrderBO> getOrderCountUsePageByActivityId(QueryCouponPageRequest request) {
        return couponService.getOrderCountUsePageByActivityId(request);
    }

    @Override
    public Integer getEffectiveCountByEid(Long eid) {
        return couponService.getEffectiveCountByEid(eid);
    }

    @Override
    public Page<CouponActivityMyCouponDTO> getCouponListPageByEid(QueryCouponListPageRequest request) {
        return couponActivityForMyService.getCouponListPageByEid(request);
    }

    @Override
    public CouponActivityEidOrGoodsIdDTO geGoodsListPageByCouponId(Long couponId, Long eid, QueryCouponActivityGoodsRequest request) {
        return couponActivityForMyService.geGoodsListPageByCouponId(couponId,eid,request);
    }

    @Override
    public CouponActivityForMemberResultDTO myAvailableMemberCouponList(QueryCouponListPageRequest request) {
        return couponActivityForMyService.myAvailableMemberCouponList(request);
    }

    @Override
    public List<CouponDTO> getHasGiveCountByCouponActivityIdList(QueryHasGiveCouponAutoRequest getRequest) {
        return couponService.getHasGiveCountByCouponActivityIdList(getRequest);
    }
}
