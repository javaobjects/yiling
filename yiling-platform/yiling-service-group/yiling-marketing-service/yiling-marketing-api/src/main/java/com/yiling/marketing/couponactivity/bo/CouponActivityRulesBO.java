package com.yiling.marketing.couponactivity.bo;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/4/20
 */
@Data
public class CouponActivityRulesBO implements java.io.Serializable{

    private static final long serialVersionUID = -4459224796026153267L;

    /**
     * 优惠规则
     */
    private String couponRules;

    public CouponActivityRulesBO(){
        this.couponRules = "";
    }

}
