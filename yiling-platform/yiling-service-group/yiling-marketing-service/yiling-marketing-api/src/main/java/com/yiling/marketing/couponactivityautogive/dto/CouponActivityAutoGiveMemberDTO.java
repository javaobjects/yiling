package com.yiling.marketing.couponactivityautogive.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGiveMemberDTO extends BaseDTO {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

}
