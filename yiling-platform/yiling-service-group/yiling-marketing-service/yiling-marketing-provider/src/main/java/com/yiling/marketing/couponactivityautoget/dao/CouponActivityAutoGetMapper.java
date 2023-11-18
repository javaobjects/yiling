package com.yiling.marketing.couponactivityautoget.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRepeatNameRequest;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetCouponDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetDO;

/**
 * <p>
 * 自主领券活动表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityAutoGetMapper extends BaseMapper<CouponActivityAutoGetDO> {

    Long getIdByName(@Param("request") QueryCouponActivityRepeatNameRequest request);

    List<CouponActivityAutoGetCouponDO> getAvailableAutogetAcouponByActivityId(@Param("request")List<Long> activityIds);
}
