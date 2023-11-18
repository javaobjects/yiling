package com.yiling.marketing.couponactivityautogive.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRepeatNameRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveCountRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveDO;

/**
 * <p>
 * 自动发券活动表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityAutoGiveMapper extends BaseMapper<CouponActivityAutoGiveDO> {

    Boolean updateGiveCountByIdList(@Param("request") UpdateAutoGiveCountRequest request);

    Long getIdByName(@Param("request") QueryCouponActivityRepeatNameRequest request);

    /**
     * 查询自动发放分页
     * @param page
     * @param request
     * @return
     */
//    Page<CouponActivityAutoGiveBO> getEffectivePageList(Page<CouponActivityDO> page, @Param("request") QueryCouponActivityAutoGiveRequest request);

}
