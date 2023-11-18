package com.yiling.marketing.couponactivityautoget.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.couponactivityautoget.dao.CouponActivityAutoGetRecordMapper;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetRecordDO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetRecordService;

/**
 * <p>
 * 自主领券企业参与记录表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Service
public class CouponActivityAutoGetRecordServiceImpl extends BaseServiceImpl<CouponActivityAutoGetRecordMapper, CouponActivityAutoGetRecordDO>
                                                    implements CouponActivityAutoGetRecordService {

    @Override
    public Boolean saveGetRecord(Long couponActivityId, Long eid, Long userId, int giveNum, int status, String faileReason) {
        CouponActivityAutoGetRecordDO autoGetRecord = new CouponActivityAutoGetRecordDO();
        autoGetRecord.setEid(eid);
        autoGetRecord.setCouponActivityId(couponActivityId);
        autoGetRecord.setGiveNum(giveNum);
        autoGetRecord.setStatus(status);
        autoGetRecord.setFaileReason(faileReason);
        autoGetRecord.setCreateUser(userId);
        autoGetRecord.setCreateTime(new Date());
        return this.save(autoGetRecord);
    }

}
