package com.yiling.marketing.couponactivity.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.coupon.dto.request.QueryCouponListPageRequest;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityForMemberResultDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityMyCouponDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
public interface CouponActivityForMyService extends BaseService<CouponActivityDO> {

    // ********************************************** 移动端-我的 ****************************************

    /**
     * 根据企业id、登录人id 查询优惠券表分页
     * @param request
     * @return
     */
    Page<CouponActivityMyCouponDTO> getCouponListPageByEid(QueryCouponListPageRequest request);

    /**
     * 根据优惠券id 查询可用商品列表表分页  GoodsController.activityGoodsSearch
     * @param couponId
     * @return
     */
    CouponActivityEidOrGoodsIdDTO geGoodsListPageByCouponId(Long couponId, Long eid, QueryCouponActivityGoodsRequest request);

    /**
     * 获取当前用户可使用的会员优惠券列表
     * @param request
     * @return
     */
    CouponActivityForMemberResultDTO myAvailableMemberCouponList(QueryCouponListPageRequest request);
}
