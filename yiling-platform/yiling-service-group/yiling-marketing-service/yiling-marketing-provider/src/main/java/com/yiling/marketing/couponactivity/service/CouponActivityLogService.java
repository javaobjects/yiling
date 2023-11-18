package com.yiling.marketing.couponactivity.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.entity.CouponActivityLogDO;

/**
 * <p>
 * 优惠券活动操作日志表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityLogService extends BaseService<CouponActivityLogDO> {

    /**
     * 保存优惠券活动操作日志
     * @param CouponActivityId
     * @param type
     * @param content
     * @param status
     * @param faileReason
     * @return
     */
    Boolean insertCouponActivityLog(Long CouponActivityId, Integer type, String content, Integer status, String faileReason, Long createUser);


}
