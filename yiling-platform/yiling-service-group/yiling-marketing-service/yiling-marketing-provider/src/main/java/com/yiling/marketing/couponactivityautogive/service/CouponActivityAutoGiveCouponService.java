package com.yiling.marketing.couponactivityautogive.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveCouponRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveCouponDO;

/**
 * <p>
 * 自动发券活动关联优惠券活动表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGiveCouponService extends BaseService<CouponActivityAutoGiveCouponDO> {

    /**
     * 根据优惠券活动ID查询 优惠券自动发放活动关联信息
     * @param couponActivityId
     * @return
     */
    CouponActivityAutoGiveCouponDTO getByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券活动ID列表查询 优惠券自动发放活动关联信息列表
     * @param couponActivityIdList
     * @return
     */
    List<CouponActivityAutoGiveCouponDTO> getByCouponByCouponActivityIdList(List<Long> couponActivityIdList);

    /**
     * 根据自动发券活动ID查询 优惠券自动发放活动关联信息
     * @param couponActivityAutoGiveId
     * @return
     */
    List<CouponActivityAutoGiveCouponDO> getByCouponActivityAutoGiveId(Long couponActivityAutoGiveId);

    /**
     * 根据自动发券活动ID 查询优惠券自动发放活动关联信息列表
     * @param couponActivityAutoGiveIds
     * @return
     */
    List<CouponActivityAutoGiveCouponDO> getAutoGiveCouponByAutoGiveIdList(List<Long> couponActivityAutoGiveIds);

    /**
     * 保存自动发放关联优惠券活动
     * @param autoGiveId
     * @param requestList
     * @return
     */
    Boolean saveGiveCoupon(Long opUserId, Long autoGiveId, List<SaveCouponActivityAutoGiveCouponRequest> requestList);

}
