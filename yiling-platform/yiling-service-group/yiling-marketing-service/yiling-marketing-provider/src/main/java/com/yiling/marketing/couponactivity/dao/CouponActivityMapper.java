package com.yiling.marketing.couponactivity.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.coupon.dto.request.QueryCouponListPageRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.couponactivity.bo.CouponBO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetChangeDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityCanUseShopDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponHasGetDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityIdAndEidDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivesRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRepeatNameRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponCanReceiveLimitRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetCouponDO;

/**
 * <p>
 * 优惠券活动表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityMapper extends BaseMapper<CouponActivityDO> {

    Long insertCouponActivity(CouponActivityDO couponActivityDO);

    Integer updateCouponActivityById(CouponActivityDO couponActivityDO);

//    Integer getCountByEid(@Param("eid") Long eid);

    List<CouponActivityDO> getListFiveByGoodsId(Long goodsId);

    List<CouponActivityDO> getCanReceiveListByEidAndGoodsId(@Param("request") QueryCouponCanReceiveLimitRequest request);

    List<CouponActivityDTO> getCanReceiveListByEid(@Param("request") QueryCouponActivityCanReceiveRequest request);

    List<CouponActivityDTO> getCanReceiveListPageByEid( @Param("request") QueryCouponActivityCanReceivePageRequest request);

    Page<CouponBO> getCouponAndActivityListPageByEid(Page<CouponBO> page, @Param("request") QueryCouponListPageRequest request);

    Long getIdByName(@Param("request") QueryCouponActivityRepeatNameRequest request);

    Page<CouponActivityDO> getPageForGoodsGift(Page<CouponActivityDO> page, @Param("request") QueryCouponActivityRequest request);

    List<CouponActivityCanUseShopDTO> getShopIdCanGiveCoupon(@Param("request") QueryCouponCanReceiveLimitRequest receiveLimitRequest);

    List<CouponActivityHasGetChangeDTO> getCanReceiveListPageByEids(@Param("request") QueryCouponActivityCanReceivesRequest request);

    List<CouponActivityDO> queryListForMarketing(Page<CouponActivityDTO> page, @Param("request")QueryCouponActivityRequest request);

    List<CouponActivityHasGetChangeDTO> getCanReceiveMemberCouponList(@Param("request") QueryCouponActivityCanReceivesRequest request);

    List<CouponActivityDO> getAvailableActivity(@Param("request")List<Long> request);

    List<CouponHasGetDTO> getGiveCountByActivityId(@Param("ids")List<Long> ids);

    List<Long> getCanUseIdByGoodsIdAndActivityIds(@Param("ids")List<Long> partCanuseActivityIds,@Param("goodsId") Long goodsId);

    List<CouponActivityCanUseShopDTO> getBusinessPlatFormEffecvtiveActivity(@Param("request")QueryCouponCanReceiveLimitRequest receiveLimitRequest);

    List<CouponActivityCanUseShopDTO> getMarketingPlatFormEffecvtiveActivity(@Param("request")QueryCouponCanReceiveLimitRequest receiveLimitRequest);

    List<Long> getActivityIdsByUserType(@Param("ids")List<Long> ids,@Param("currentEid") Long currentEid);

    List<CouponActivityIdAndEidDTO> getActivityIdsByEids(@Param("ids")List<Long> activityIds, @Param("eids")List<Long> purchasedEids);

    List<Long> getAutogetActivityIdsByUserType(@Param("ids")List<Long> activityIds,  @Param("currentEid")Long currentEid);

    List<Long> getActivityIdsByMemberLimit(@Param("ids")List<Long> activityIdList,@Param("memberId") List<Long> memberId);

    List<Long> getActivityIdsByPromoterLimit(@Param("ids")List<Long> activityIdList,@Param("promotionEid") List<Long> memberId);

    List<Long> getNumEnoughtActivity(@Param("ids")List<Long> ids);

    List<Long> scrapAndReturnCoupon(@Param("id")Long activityId);

    List<CouponDO>  getHasGetDailyNum(@Param("id")Long activityId, @Param("currentEid")Long currentEid, @Param("autoGetId")Long autoGetId);

    void updateHasGiveNum(@Param("id")Long couponActivityId,@Param("num") int size);
}
