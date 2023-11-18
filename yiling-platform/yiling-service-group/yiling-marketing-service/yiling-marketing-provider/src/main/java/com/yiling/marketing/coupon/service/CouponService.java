package com.yiling.marketing.coupon.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.coupon.bo.CouponUseOrderBO;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.CouponAutoGiveRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponPageRequest;
import com.yiling.marketing.coupon.dto.request.QueryHasGiveCouponAutoRequest;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.dto.request.UpdateCouponRequest;
import com.yiling.marketing.coupon.entity.CouponDO;

/**
 * <p>
 * 用户优惠券信息表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponService extends BaseService<CouponDO> {

    Boolean insertBatch(List<SaveCouponRequest> list);

    Boolean updateBatch(List<UpdateCouponRequest> list);

    /**
     * 保存发放优惠券
     * @param request
     * @return
     */
    Boolean autoGive(CouponAutoGiveRequest request);

    /**
     * 根据优惠券活动id、企业id 查询手动发放、正常状态的优惠券
     * @param couponActivityId
     * @param eidList
     * @return
     */
    List<CouponDTO> getByCouponActivityIdForGiveDelete(Long couponActivityId, List<Long> eidList);

    Boolean deleteByIds(List<Long> ids, Long userId);

    /**
     * 根据优惠券id列表统计已发放数量
     * @param couponActivityIdList
     * @return
     */
    List<Map<String, Long>> getGiveCountByCouponActivityId(List<Long> couponActivityIdList);

    /**
     * 根据优惠券id列表统计已发放数量
     * @param couponActivityIdList
     * @return
     */
    List<Map<String, Long>> getUseCountByCouponActivityId(List<Long> couponActivityIdList);

    /**
     * 根据自动发券活动id列表统计已发放数量
     * @param couponActivityAutoGiveIdList
     * @return
     */
    List<Map<Long, Integer>> getCountByCouponActivityAutoIdList(Integer getType, List<Long> couponActivityAutoGiveIdList);


    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<CouponDTO> queryListPage(QueryCouponPageRequest request);

    /**
     * 根据自动发放/领取活动id 查询所有优惠券
     * @param couponActivityAutoId
     * @return
     */
    List<CouponDTO> getByCouponActivityAutoId(Long couponActivityAutoId);

    /**
     * 根据企业id 查询已领取的优惠券活动
     * @param couponActivityAutoId
     * @return
     */
    Integer getEffectiveCountByCouponActivityId(Long couponActivityAutoId);

    /**
     * 移动端-我的
     * 根据企业id 查询未使用的优惠券数量
     * @param eid
     * @return
     */
    Integer getEffectiveCountByEid(Long eid);

    /**
     * 根据企业id、优惠券活动id列表 查询所有未过期、正常的优惠券
     * @param eid
     * @param couponActivityIdList
     * @return
     */
    List<CouponDO> getByEidAndCouponActivityId(Long eid, List<Long> couponActivityIdList);

    /**
     * 根据企业id 查询正常的优惠券
     * @param eid
     * @return
     */
    Integer getEffectiveCountWithoutScrapByEid(Long eid, Long couponActivityId, Long autoGetOrRulesId, Integer businessType);

    /**
     * 根据企业id 查询手动发放、正常状态的优惠券
     * @param currentEid
     * @param eids
     * @return
     */
    List<CouponDO> getEffectiveCanUseListByEid(Long currentEid, List<Long> eids);

    /**
     * 根据企业id、登录人id 查询优惠券表分页
     * @param request
     * @return
     */
//    Page<CouponDO> getCouponAndActivityListPageByEid(QueryCouponListPageRequest request);

    /**
     * 根据ids 查询可使用的优惠券列表
     * @param ids
     * @return
     */
    List<CouponDO> getEffectiveListByCouponActivityIdList(List<Long> ids,Long eid);

    /**
     * 根据优惠券id 查询可使用的优惠券列表
     * @param ids
     * @return
     */
    List<CouponDO> getEffectiveListByIdList(List<Long> ids);

    /**
     * 根据ids 使用优惠券
     * @param ids
     * @return
     */
    Boolean useCoupon(List<Long> ids, Long userId);

    /**
     * 根据ids 查询优惠券列表
     * @param ids
     * @return
     */
    List<CouponDO> getByIdList(List<Long> ids);

    /**
     * 根据id退还优惠券
     * @param ids
     * @return
     */
    Boolean returenCouponByIds(List<Long> ids);

    /**
     * 根据优惠券活动ids 查询已发放的优惠券列表
     * @param couponActivityIds
     * @return
     */
    List<CouponDTO> getHasGiveCountByCouponActivityList(List<Long> couponActivityIds);

    /**
     * 根据自动发放/领取活动ids 查询已发放的优惠券列表
     * @param request
     * @return
     */
    List<CouponDTO> getHasGiveListByCouponActivityIdList(QueryHasGiveCouponAutoRequest request);


    /**
     * 根据优惠券活动id查询优惠券已使用列表分页
     *
     * @param request
     * @return
     */
    Page<CouponUseOrderBO> getOrderCountUsePageByActivityId(QueryCouponPageRequest request);

    List<CouponDTO> getHasGiveCountByCouponActivityIdList(QueryHasGiveCouponAutoRequest getRequest);
}
