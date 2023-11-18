package com.yiling.marketing.couponactivityautoget.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetCouponRequest;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetCouponDO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;

/**
 * <p>
 * 自主领券活动关联优惠券活动表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGetCouponService extends BaseService<CouponActivityAutoGetCouponDO> {

    /**
     * 根据优惠券活动ID查询 优惠券自动发放活动关联信息
     * @param couponActivityId
     * @return
     */
    CouponActivityAutoGetCouponDTO getByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券活动ID列表查询 优惠券自动发放活动关联信息列表
     * @param couponActivityIdList
     * @return
     */
    List<CouponActivityAutoGetCouponDTO> getByCouponActivityIdList(List<Long> couponActivityIdList);

    /**
     * 根据领券活动ID查询 优惠券自动发放活动关联信息
     * @param couponActivityGetId
     * @return
     */
    List<CouponActivityAutoGetCouponDO> getByCouponActivityGetId(Long couponActivityGetId);

    /**
     * 保存自动发放关联优惠券活动
     * @param autoGiveId
     * @param requestList
     * @return
     */
    Boolean saveGiveCoupon(Long opUserId, Long autoGiveId, List<SaveCouponActivityAutoGetCouponRequest> requestList);

    /**
     * 根据领券活动IDs查询 优惠券自动发放活动关联信息
     * @param couponActivityGetIds
     * @return
     */
    List<CouponActivityAutoGetCouponDO> getByCouponActivityGetIdList(List<Long> couponActivityGetIds);

    /**
     * 根据优惠券活动ID列表查询 优惠券自动发放活动关联信息
     * @param couponActivityIdList
     * @return
     */
//    CouponActivityAutoGetCouponDTO getByCouponActivityIdList(List<Long> couponActivityIdList);

}
