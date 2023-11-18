package com.yiling.marketing.couponactivity.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum CouponActivityErrorCode implements IErrorCode {

    BEAR_ERROR(10001, "费用承担方类型不能为空或0"),
    PLATFORM_RATIO_ERROR(10002, "费用承担方为平台，平台承担比例不能为空或0"),
    BUSINESS_RATIO_ERROR(10003, "费用承担方为商家，商家承担比例不能为空或0"),

    USE_DATE_TYPE_ERROR(10004, "有效期类型不能为空或0"),
    BEGIN_TIME_NULL_ERROR(10005, "开始时间不能为空"),
    BEGIN_TIME_ERROR(10005, "有效期类型为固定效期，开始时间不能为空，且不能小于当前时间"),
    END_TIME_NULL_ERROR(10006, "结束时间不能为空"),
    END_TIME_ERROR(10006, "有效期类型为固定效期，结束时间不能为空，且不能小于开始时间"),
    END_TIME_NOW_ERROR(10006, "有效期类型为固定效期，结束时间不能为空，且不能小于当前时间"),
    EXPIRY_DAYS_ERROR(10007, "有效期类型为按发放/领取时间设定，值不能为空，且不能小于等于0"),

    COEXIST_PROMOTION_ERROR(10008, "与哪些促销可共用，不能为空"),
    TENTERPRISE_LIMIT_ERROR(10009, "商家设置，不能为空或0"),
    TOTAL_COUNT_ERROR(10010, "优惠券生成总数量，不能为空或0"),

    UPDATE_ID_NULL_ERROR(10011, "优惠券活动保存或修改使用规则，ID不能为空"),
    UPDATE_ALLOW_CHECK_ERROR(10012, "优惠券活动不能修改"),
    EID_NULL_ERROR(10013, "企业ID不能为空"),
    STOP_ERROR(10014, "此优惠券活动状态不是“启用”，不能操作“停用”，请检查"),
    SCRAP_ERROR(10015, "此优惠券活动状态是“废弃”，不能再操作“作废”，请检查"),

    TYPE_ERROR(10016, "优惠券类型不能为空或0"),
    THRESHOLD_NULL_ERROR(10017, "活动类型不能为空或0"),
    SPONSOR_TYPE_NULL_ERROR(10017, "优惠券活动分类不能为空或0"),
    THRESHOLD_TYPE_ERROR(10018, "活动类型不存在，请检查"),
    THRESHOLD_VALUE_ERROR(10019, "订单实付金额满足金额不能为空或0"),
    DISCOUNT_VALUE_NULL_ERROR(10020, "优惠内容（减xx金额/打xx折扣）不能为空或0"),
    AMOUNT_REDUCE_VALUE_ERROR(10021, "优惠减去金额不能大于需满足金额"),
    DISCOUNT_MAX_VALUE_ERROR(10022, "最高优惠金额不能为0或负数"),
    DISCOUNT_RATIO_VALUE_ERROR(10023, "优惠内容（打xx折）折扣不能大于100%"),

    BUSINESS_COUPON_AUTHORIZATION_ERROR(10024, "不能修改他人创建的优惠券活动"),

    ENTERPRISE_LIMIT_SAVE_ERROR(10025, "优惠券活动添加供应商失败"),
    ENTERPRISE_LIMIT_DELETE_ERROR(10026, "删除优惠券活动已添加供应商失败"),
    GOODS_LIMIT_DELETE_ERROR(10027, "删除优惠券活动已添加商品失败"),
    GIVE_ENTERPRISE_QUERY_ENTERPRISE_ERROR(10028, "查询待发放企业信息异常"),
    GIVE_ENTERPRISE_QUERY_RECORD_ERROR(10029, "查询已发放企业信息列表异常"),
    GIVE_ENTERPRISE_ERROR(10030, "发放失败"),
    GIVE_DELETE_ENTERPRISE_PARAM_ERROR(10031, "发放供应商删除，优惠券活动id、企业id都不能为空"),
    GOODS_LIMIT_SAVE_ERROR(10032, "优惠券活动添加商品失败"),
    AUTO_GIVE_ENTERPRISE_LIMIT_SAVE_ERROR(10033, "自动发券活动添加会员企业失败"),
    AUTO_GIVE_GOODS_LIMIT_SAVE_ERROR(10034, "自动发券活动添加商品失败"),
    AUTO_GIVE_ENTERPRISE_LIMIT_DELETE_ERROR(10035, "删除自动发券活动已添加会员企业失败"),

    CAN_GET_SELECT_NULL_ERROR(10036, "用户领取设置，不能为空或0"),
    CAN_GET_BEGIN_TIME_ERROR(10037, "活动开始时间不能为空，且不能小于当前时间"),
    CAN_GET_END_TIME_ERROR(10038, "活动结束时间不能为空，且不能小于开始时间"),
    CAN_GET_GIVE_NUM_NULL(10039, "每企业可领取不能为空或0，请检查"),

    AUTO_GET_ENTERPRISE_LIMIT_SAVE_ERROR(10040, "自主领取活动添加会员企业失败"),
    NAME_NULL_ERROR(10041, "名称不能为空"),
    DISCOUNT_VALUE_ERROR(10020, "优惠金额必须小于实付金额，请检查"),
    INCREASE_TIME_ERROR(10021, "优惠券活动已结束，不能增券"),
    SPECIAL_ACTIVITY_EXITS(10022, "专场活动名称已经存在"),
    SPECIAL_ACTIVITY_ALREADY_APPOINTED(10023, "专场活动已经预约"),

    MEMBER_ACTIVITY_RELATIVE_MEMBER_EMPTY(10042, "当会员优惠券部分会员方案可用时,添加会员方案不能为空"),
    RATIO_NOT_MEET_100(10043, "费用承担方为共同承担，合计比例必须等于百分之百"),
    BEGINTIME_AFTER_BEGINTIME(10044, "支付尾款开始时间必须大于定金支付开始时间"),
    ENDTIME_AFTER_ENDTIME(10045, "支付尾款结束时间必须大于定金支付结束时间"),
    EXPANSION_MULTIPLIER_TOO_HIGH(10046, "定金膨胀比例已经超过原价"),
    FINAL_PAY_DISCOUNT_AMOUNT_TOO_HIGH(10047, "尾款立减金额超过尾款金额"),
    ;

    private final Integer code;
    private final String  message;
}
