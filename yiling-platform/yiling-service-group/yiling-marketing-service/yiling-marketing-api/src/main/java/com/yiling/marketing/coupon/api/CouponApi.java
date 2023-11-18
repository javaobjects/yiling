package com.yiling.marketing.coupon.api;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.coupon.bo.CouponUseOrderBO;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryCouponListPageRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponPageRequest;
import com.yiling.marketing.coupon.dto.request.QueryHasGiveCouponAutoRequest;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityForMemberResultDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityMyCouponDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;

/**
 * 优惠券卡包 API
 *
 * @author: houjie.sun
 * @date: 2021/10/23
 */
public interface CouponApi {

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<CouponDTO> queryListPage(QueryCouponPageRequest request);

    /**
     * 根据ids 查询优惠券列表
     * @param ids
     * @return
     */
    List<CouponDTO> getByIdList(List<Long> ids);

    /**
     * 根据id退还优惠券
     * @param ids
     * @return
     */
    Boolean returenCouponByIds(List<Long> ids);


    /**
     * 根据优惠券活动ids 查询已发放的优惠券数量
     * @param couponActivityIds
     * @return
     */
    List<CouponDTO> getHasGiveCountByCouponActivityList(List<Long> couponActivityIds);

    /**
     * 根据优惠券id列表统计已发放数量
     * @param couponActivityIdList
     * @return
     */
    List<Map<String, Long>> getGiveCountByCouponActivityId(List<Long> couponActivityIdList);

    /**
     * 根据自动发放/领取活动ids 查询已发放的优惠券列表
     * @param request
     * @return
     */
    List<CouponDTO> getHasGiveListByCouponActivityIdList(QueryHasGiveCouponAutoRequest request);

    /**
     * 生成优惠券
     * @param list
     * @return
     */
    Boolean insertBatch(List<SaveCouponRequest> list);

    /**
     * 根据优惠券活动id查询优惠券已使用列表分页
     *
     * @param request
     * @return
     */
    Page<CouponUseOrderBO> getOrderCountUsePageByActivityId(QueryCouponPageRequest request);

    // ********************************************** 移动端-我的 ****************************************
    /**
     * 根据企业id 查询未使用、未过期的优惠券数量
     * @param eid
     * @return
     */
    Integer getEffectiveCountByEid(Long eid);

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
    CouponActivityEidOrGoodsIdDTO geGoodsListPageByCouponId(Long couponId, Long eid, QueryCouponActivityGoodsRequest queryCouponActivityGoodsRequest);

    /**
     * 获取当前用户可使用的会员优惠券列表
     * @param request
     * @return
     */
    CouponActivityForMemberResultDTO myAvailableMemberCouponList(QueryCouponListPageRequest request);


    List<CouponDTO> getHasGiveCountByCouponActivityIdList(QueryHasGiveCouponAutoRequest getRequest);
}
