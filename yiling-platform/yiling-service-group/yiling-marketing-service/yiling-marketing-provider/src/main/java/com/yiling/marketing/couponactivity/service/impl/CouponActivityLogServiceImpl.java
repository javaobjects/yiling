package com.yiling.marketing.couponactivity.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.common.enums.CouponActivityLogTypeEnum;
import com.yiling.marketing.couponactivity.dao.CouponActivityLogMapper;
import com.yiling.marketing.couponactivity.entity.CouponActivityLogDO;
import com.yiling.marketing.couponactivity.service.CouponActivityLogService;

/**
 * <p>
 * 优惠券活动操作日志表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Service
public class CouponActivityLogServiceImpl extends BaseServiceImpl<CouponActivityLogMapper, CouponActivityLogDO> implements CouponActivityLogService {

    @Override
    public Boolean insertCouponActivityLog(Long CouponActivityId, Integer type, String content, Integer status, String faileReason, Long createUser) {
        CouponActivityLogDO log = new CouponActivityLogDO();
        log.setCouponActivityId(CouponActivityId);
        log.setType(CouponActivityLogTypeEnum.COPY.getCode());
        log.setJsonContent(content);
        log.setStatus(status);
        log.setFaileReason(faileReason);
        log.setCreateUser(createUser);
        log.setCreateTime(new Date());
        return this.save(log);
    }
}
