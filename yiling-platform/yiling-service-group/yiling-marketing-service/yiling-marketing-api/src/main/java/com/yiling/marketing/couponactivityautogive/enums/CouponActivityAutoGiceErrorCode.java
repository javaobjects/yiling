package com.yiling.marketing.couponactivityautogive.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/4
 */
@Getter
@AllArgsConstructor
public enum CouponActivityAutoGiceErrorCode implements IErrorCode {

    UPDATE_ALLOW_CHECK_ERROR(10001, "优惠券活动不能修改"),
    AUTO_GIVE_RELATION_COUPON_ACTIVITY_NULL(10002, "自动发放优惠券活动关联的优惠券id不能为空或0，请检查"),
    AUTO_GIVE_RELATION_COUPON_ACTIVITY_NUM_NULL(10003, "自动发放优惠券活动关联的优惠券数量不能设置为空或0，请检查"),
    COUPON_ACTIVITY_REPEAT_ERROR (10004, "关联的优惠券活动id有重复值，请检查， 优惠券活动id:{0}"),
    COUPON_ACTIVITY_NULL (10005, "关联的优惠券活动id不存在， 优惠券活动id:{0}，请检查"),
    AUTO_GIVE_CUMULATIVE_NULL (10006, "累积金额不能为空或负数，请检查"),
    AUTO_GIVE_REPEAT_GIVE_NULL (10007, "是否重复发放不能为空或0，请检查"),
    AUTO_GIVE_REPEAT_GIVE_MAX_NUM_NULL (10008, "是否重复发放类型，已选择可重复发放，最多发放次数不能为空或0，请检查"),
    AUTO_GET_RELATION_COUPON_ACTIVITY_NULL(10009, "自主领券活动关联的优惠券id不能为空或0，请检查"),
    AUTO_GET_RELATION_COUPON_ACTIVITY_NUM_NULL(10010, "自主领券活动关联的优惠券数量不能设置为空或0，请检查"),
    AUTO_GET_RELATION_COUPON_ENTERPRISE_EMPTY(10011, "客户不能为空"),
    AUTO_GET_RELATION_COUPON_MEMBER_EMPTY(10012, "会员方案不能为空"),
    AUTO_GET_RELATION_COUPON_PROMOTION_USER_EMPTY(10013, "推广方会员不能为空"),
    ;

    private final Integer code;
    private final String  message;
}
