package com.yiling.marketing.couponactivity.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityListFiveByGoodsIdDTO extends BaseDTO {

    /**
     * 优惠劵名称
     */
    private String name;

    /**
     * 优惠规则
     */
    private String couponRules;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 创建人id
     */
    private Long createUser;

}
