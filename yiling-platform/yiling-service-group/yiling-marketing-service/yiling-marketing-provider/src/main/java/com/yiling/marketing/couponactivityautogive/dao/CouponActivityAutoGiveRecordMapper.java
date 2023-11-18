package com.yiling.marketing.couponactivityautogive.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;

/**
 * <p>
 * 自动发券企业参与记录表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface CouponActivityAutoGiveRecordMapper extends BaseMapper<CouponActivityAutoGiveRecordDO> {

    /**
     * 根据企业ID 查询最近的优惠券自动发放活动记录
     * @param eid
     * @return
     */
    CouponActivityAutoGiveRecordDO getAutoGiveRecordLastOneByEid(@Param("eid") Long eid);

}
