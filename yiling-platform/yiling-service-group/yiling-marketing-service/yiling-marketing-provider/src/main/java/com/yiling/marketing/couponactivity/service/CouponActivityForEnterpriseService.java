package com.yiling.marketing.couponactivity.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivesRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
public interface CouponActivityForEnterpriseService extends BaseService<CouponActivityDO> {

    // ********************************************** 移动端-店铺 ****************************************

    /**
     * 根据企业id 查询可领取优惠券活动列表
     * @param request
     * @return
     */
    List<CouponActivityHasGetDTO> getCouponActivityListByEid(QueryCouponActivityCanReceiveRequest request);

    /**
     * 根据企业id 查询优惠券活动列表分页
     * @param request
     * @return
     */
    List<CouponActivityHasGetDTO> getCouponActivityListPageByEid(QueryCouponActivityCanReceivePageRequest request);

    /**
     * 根据企业ids 查询优惠券活动列表分页
     * @param request
     * @return
     */
    List<CouponActivityHasGetDTO> getCouponActivityListPageByEids(QueryCouponActivityCanReceivesRequest request,List<Long>activityIds);

    /**
     * 根据企业id 查询优惠券活动列表分页
     * @param request
     * @return
     */
    Page<CouponActivityEnterpDTO> getCouponsCenter(QueryCouponActivityCanReceivePageRequest request);

    /**
     * 根据企业id 查询优惠券活动列表分页
     * @param request
     * @return
     */
    List<CouponActivityHasGetDTO> getMemberCouponsCenter(QueryCouponActivityCanReceivePageRequest request);
}
