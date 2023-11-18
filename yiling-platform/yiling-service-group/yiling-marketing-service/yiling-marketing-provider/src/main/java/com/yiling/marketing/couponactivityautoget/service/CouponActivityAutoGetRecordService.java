package com.yiling.marketing.couponactivityautoget.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetRecordDO;

/**
 * <p>
 * 自主领券企业参与记录表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGetRecordService extends BaseService<CouponActivityAutoGetRecordDO> {

    /**
     * 手动领取记录
     * @param couponActivityId
     * @param eid
     * @param userId
     * @param giveNum
     * @param status
     * @param faileReason
     * @return
     */
    Boolean saveGetRecord(Long couponActivityId, Long eid, Long userId, int giveNum, int status, String faileReason);
}
