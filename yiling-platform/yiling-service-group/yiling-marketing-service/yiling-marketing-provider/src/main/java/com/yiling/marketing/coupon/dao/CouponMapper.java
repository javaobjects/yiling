package com.yiling.marketing.coupon.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.coupon.bo.CouponUseOrderBO;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.entity.CouponDO;

/**
 * <p>
 * 用户优惠券信息表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponMapper extends BaseMapper<CouponDO> {

    List<Map<String, Long>> getGiveCountByCouponActivityId(@Param("couponActivityIdList") List<Long> couponActivityIdList);

    List<Map<String, Long>> getUseCountByCouponActivityId(@Param("couponActivityIdList") List<Long> couponActivityIdList);

    List<Map<Long, Integer>> getCountByCouponActivityAutoIdList(@Param("getType") Integer getType, @Param("autoIdList") List<Long> couponActivityAutoIdList);

    Integer getEffectiveCountByCouponActivityId(@Param("couponActivityId") Long couponActivityId);

    Integer getEffectiveCountByEid(@Param("eid") Long eid);

    Integer getEffectiveCountWithoutScrapByEid(@Param("eid") Long eid, @Param("couponActivityId") Long couponActivityId,
                                               @Param("autoGetOrRulesId") Long autoGetOrRulesId, @Param("businessType") Integer businessType);

    List<CouponDO> getEffectiveCanUseListByEid(@Param("currentEid") Long currentEid, @Param("eids") List<Long> eids);

    Page<CouponUseOrderBO> getOrderCountUsePageByActivityId(Page<CouponUseOrderBO> page, @Param("couponActivityId") Long couponActivityId);

    List<CouponDTO> getHasGiveCountByCouponActivityList(@Param("ids")List<Long> couponActivityIds);

    List<CouponDTO> getHasGiveCountByEidList(@Param("eids")List<Long> eids,@Param("couponActivityId") Long couponActivityId);

    List<CouponDTO> getHasGiveCountByCouponActivityIdList(@Param("ids")List<Long> autoGetIds, @Param("type")Integer getType);
}
