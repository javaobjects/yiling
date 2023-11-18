package com.yiling.marketing.couponactivity.event;


import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;


/**
 * @author shixing.sun
 * @version V1.0
 * @Package com.yiling.marketing.couponactivity.event
 * @date: 2023/1/3
 */
public class UpdateCouponGiveNumEvent extends ApplicationEvent {
    public UpdateCouponGiveNumEvent(Object source, List<CouponActivityAutoGiveRecordDO> couponActivityAutoGiveRecordDO) {
        super(source);
        this.couponActivityAutoGiveRecordDO = couponActivityAutoGiveRecordDO;
    }

    public List<CouponActivityAutoGiveRecordDO> getCouponActivityAutoGiveRecordDO() {
        return couponActivityAutoGiveRecordDO;
    }

    public void setCouponActivityAutoGiveRecordDO(List<CouponActivityAutoGiveRecordDO> couponActivityAutoGiveRecordDO) {
        this.couponActivityAutoGiveRecordDO = couponActivityAutoGiveRecordDO;
    }

    /**
     * 更i性能优惠券发放记录参数
     */
    private List<CouponActivityAutoGiveRecordDO> couponActivityAutoGiveRecordDO;

}
