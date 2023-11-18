package com.yiling.marketing.common.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum CouponErrorCode implements IErrorCode {

    PLATFORM_LIMIT_ERROR(10001, "平台限制类型不能为空"),
    PLATFORM_LIMIT_VALUE_ERROR(10002, "平台限制类型为部分平台，选择平台不能为空"),

    PAY_METHOD_LIMIT_ERROR(10003, "支付方式限制不能为空"),
    PAY_METHOD_LIMIT_VALUE_ERROR(10004, "支付方式限制为部分支付，选择支付方式不能为空"),

    COEXIST_PROMOTION_ERROR(10005, "与哪些促销可共用，不能为空"),
    DATA_NOT_EXIST(10006, "查询信息不存在"),
    INCREASE_STATUS_ERROR(10007, "此优惠券活动不是“启用”状态，不能增券"),
    REDIS_LOCK_INCREASE_DOING_ERROR(10008, "新增生券数量，系统繁忙，请稍等重试"),
    REDIS_LOCK_INCREASE_ERROR(10009, "新增生券数量异常，请稍等重试"),

    GOODS_LIMIT_QUERY_PARAM_ERROR(10010, "查询商品信息参数不能为空"),
    REDIS_LOCK_SAVE_ENTERPRISE_LIMIT_DOING_ERROR(10011, "优惠券活动添加供应商，系统繁忙，请稍等重试"),
    REDIS_LOCK_SAVE_GOODS_LIMIT_DOING_ERROR(10012, "优惠券活动添加商品，系统繁忙，请稍等重试"),
    COUPON_ACTIVITY_NOT_EXIST_ERROR(10013, "优惠券活动不存在，请检查"),
    COUPON_ACTIVITY_STATUS_GIVE_ERROR(10014, "此优惠券活动不是“启用”状态，不能发放或删除已发放，请检查"),
    COUPON_ACTIVITY_SUM_GIVE_ERROR(10015, "发放数量不能超过优惠券总数量，不能发放，请检查"),
    COUPON_QUERY_GIVE_ERROR(10016, "查询已发放优惠券异常"),
    COUPON_QUERY_USED_ERROR(10016, "查询已使用优惠券异常"),

    COUPON_BEGIN_TIME_ERROR(10017, "开始时间不能小于当前时间"),
    COUPON_END_TIME_ERROR(10018, "结束时间不能小于等于开始时间"),

    ORDER_STATUS_LIMIT_ERROR(10019, "订单状态限制不能为空"),
    ORDER_STATUS_VALUE_ERROR(10020, "订单状态限制为部分订单状态，选择订单状态不能为空"),

    ENTERPRISE_TYPE_LIMIT_ERROR(10021, "企业类型限制不能为空"),
    ENTERPRISE_TYPE_VALUE_ERROR(10022, "企业类型限制为部分企业类型，选择企业类型不能为空"),

    USER_TYPE_LIMIT_ERROR(10023, "用户类型限制不能为空"),
    USER_TYPE_LIMIT_VALUE_NULL(10024, "添加的关联企业信息不能为空"),
    AUTO_GET_TYPE_NULL(10025, "查询自动发放或自动领取的已发放优惠券，发放方式不能为空"),
    AUTO_GIVE_RESULT_TYPE_ERROR(10026, "此优惠券已经发放成功，不用处理"),

    AUTO_GIVE_NOT_EXIST(10027, "自动发放活动信息不存在，自动发放活动id：{0}"),
    COUPON_ACTIVITY_NOT_EXIST(10028, "优惠券活动信息不存在，优惠券id：{0}"),
    AUTO_GIVE_RELATION_NOT_EXIST(10029, "自动发放活动未关联此优惠券活动，自动发放活动id：{0}，优惠券活动id：{1}"),
    COUPON_ACTIVITY_TOTAL_COUNT_ERROR(10030, "此优惠券活动发放数量已达到最大优惠券总数量，优惠券活动id：{0}，优惠券总数量：{1}"),
    AUTO_GIVE_ERROR(10031, "发放失败。{0}"),

    AUTO_GIVE_TYPE_ERROR(10032, "发券类型不能为空"),

    MOBILE_GOODS_QUERY_CAN_COUPON_ACTIVITY_ERROR(10033, "查询可参与优惠券活动异常"),
    MOBILE_GOODS_QUERY_GET_COUPON_ACTIVITY_ERROR(10034, "查询可领取、已领取优惠券活动异常"),
    MOBILE_GOODS_QUERY_GET_COUPON_ACTIVITY_GOODS_ERROR(10035, "获取优惠券活动相关的商品列出异常"),
    MOBILE_COUPON_ACTIVITY_STATUS_GET_ERROR(10036, "此优惠券活动不是“启用”状态，不能领取"),
    MOBILE_COUPON_ACTIVITY_TOTAL_COUNT_GET_ERROR(10037, "此优惠券已经全部领取完，不能领取"),
    MOBILE_GOODS_GET_COUPON_ERROR(10038, "领取优惠券异常"),
    GET_COUPON_ERROR(10038, "领取优惠券失败，{0}"),
    MOBILE_AUTO_GET_ERROR(10039, "此优惠券不能自主领取"),
    MOBILE_AUTO_GET_COUNT_ERROR(10040, "此优惠券领取数量已达到上限，不能再领取"),
    MOBILE_AUTO_GET_ENTERPRISE_TYPE_ERROR(10041, "此优惠券仅部分企业类型可以领取，您不能领取"),
    MOBILE_AUTO_GET_ENTERPRISE_LIMIT_ERROR(10042, "此优惠券仅部分会员可以领取，您不能领取"),
    MOBILE_AUTO_GET_CURRENT_MEMBER_ONE_ERROR(10043, "此优惠券仅普通用户可以领取，您不能领取"),
    MOBILE_AUTO_GET_CURRENT_MEMBER_ZERO_ERROR(10044, "此优惠券仅会员可以领取，您不能领取"),
    MOBILE_ENTERPRISE_GET_RULES_ERROR(10045, "此优惠券领取规则设置为空"),
    COUPON_ACTIVITY_GET_END_ERROR(10046, "此优惠券领取活动已结束，不能领取"),

    MOBILE_PURCHASE_ORDER_GET_CAN_USE_COUPON_ERROR(10047, "进货单获取可使用优惠券异常"),
    QUERY_COUPON_ACTIVITY_EXIST_ERROR(10048, "根据企业ID、商品ID列表 查询是否存在优惠券活动异常"),
    COUPON_USED_OR_EXPIRE_ERROR(10049, "此优惠券[{0}]未开始或已经被使用或已失效，请选择其他优惠券"),
    ORDER_USE_COUPON_SHARE_AMOUNT_BUDGET_ERROR(10050, "优惠券分摊异常，{0}"),
    ORDER_USE_COUPON_ERROR(10051, "提交订单使用优惠券异常"),
    COUPON_PAGE_QUERY_ERROR(10052, "查询优惠券信息异常"),
    COUPON_USED_BEGIN_TIME_ERROR(10053, "此优惠券活动未开始，不能使用[{0}]，请选择其他{1}"),
    COUPON_USED_END_TIME_ERROR(10054, "此优惠券[{0}]已过期，不能使用，请选择其他{1}"),
    COUPON_USED_OWN_ERROR(10055, "您不能使用此优惠券[{0}]，请选择其他{1}"),
    COUPON_USED_ENTERPRISE_LIMIT_ERROR(10056, "此店铺[{0}]商品不能参与使用此优惠券，请选择其他{1}"),
    COUPON_USED_LIMIT_ERROR(10057, "所有店铺商品都不能参与使用此平台优惠券[{0}]，请选择其他平台优惠券"),
    COUPON_USED_BUSINESS_ERROR(10058, "此商家优惠券不能参与使用[{0}]，请选择其他优惠券"),
    COUPON_USED_PAY_METHOD_ERROR(10059, "此优惠券[{0}]仅支持支付方式[{1}]能使用"),
    COUPON_USED_PLATFORM_ERROR(10060, "此优惠券[{0}]仅支持平台[{1}]能使用"),
    COUPON_STATUS_ERROR(10061, "此优惠券[{0}]已废弃，不能使用，请选择其他{1}"),
    ORDER_COUPON_USE_NULL_ERROR(10062, "此优惠券{0}未查询到订单优惠劵使用记录，请检查"),
    ORDER_COUPON_USE_RETURNED_ERROR(10063, "此优惠券{0}已退还"),

    COUPON_NULL_ERROR(10064, "优惠券信息不存在"),
    COUPON_NOT_USED_ERROR(10065, "此优惠券[{0}]未使用"),

    COUPON_TO_USE_ERROR(10066, "去使用优惠券[{0}]，查询可使用商品、企业异常"),

    ENTERPRISE_LIMIT_EXIST_ERROR(10067, "此供应商已添加[企业ID：{0}]，请检查"),

    COUPON_ACTIVITY_STATUS_AUTO_GIVE_ERROR(10068, "此优惠券活动不是“启用”状态，不能关联自动发放或自主领取活动，请检查"),

    COUPON_PARSE_DATE_ERROR(10069, "时间格式化异常"),

    GOODS_LIMIT_ERROR(10070, "指定商品类型不能为空"),
    TENTERPRISE_LIMIT_NULL_ERROR(10071, "商家设置为部分商家可用时，添加供应商不能为空"),
    MEMBER_LIMIT_NULL_ERROR(10072, "用户类型为部分会员时，添加会员不能为空"),
    GOODS_LIMIT_NULL_ERROR(10073, "商品设置为部分商品可用时，添加商品不能为空"),
    COUPON_RELEVANCE_ERROR(10074, "此优惠券不能关联"),

    AUTO_GIVE_CUMULATIVE_ERROR(10075, "累积金额不能为空或0或负数，请检查"),
    PAY_METHOD_ERROR(10076, "仅在线支付方式可使用优惠券"),
    AUTO_GIVE_MONTH_ERROR(10077, "共计发放月数大于活动起止时间范围的月数，请检查"),
    GIVE_ENTERPRISE_INFO_REPEAT_ERROR(10078, "添加发券采购商{0}不能重复，请检查"),

    GOODS_LIMIT_EXIST_ERROR(10079, "此商品已添加[商品ID：{0}]，请检查"),
    COUPON_SHOP_REPEAT_ERROR(10080, "多个店铺中都选择了优惠券[{1}]，此优惠券只能在一个店铺中使用, 请重新选择"),
    THRESHOLD_VALUE_NOT_FIT_ERROR(10081, "店铺订单商品金额不满足优惠券满减金额, 请重新选择"),
    COUPON_USED_STATUS_ERROR(10082, "此优惠券[{0}]已使用，不能再次使用，请选择其他{1}"),
    PAY_METHOD_LIMIT_TYPE_ERROR(10083, "不支持的支付方式，请检查"),
    PLATFORM_LIMIT_TYPE_ERROR(10084, "不支持的使用平台类型，请检查"),
    GOODS_LIMIT_ENTERPRISE_ERROR(10085, "此商品[商品ID:{0}]所属企业[企业ID:{1}]不在商家设置中，请在商家设置中添加企业、或在商品设置中删除该商品。"),
    UPDATE_RULES_BEGIN_TIME_NULL_ERROR(10086, "开始时间不能为空，请先选择有效期开始时间、并保存基本信息"),
    UPDATE_RULES_END_TIME_NULL_ERROR(10087, "结束时间不能为空，请先选择有效期结束时间、并保存基本信息"),
    COUPON_ACTIVITY_AUTO_GET_STATUS_ERROR(10088, "此优惠券自主领取活动不是“启用”状态，不能领取"),
    COUPON_ACTIVITY_CAN_GET_ERROR(10089, "此商家优惠券活动不可领取"),

    PROMOTION_CODE_ERROR(10090, "是否有推广码不能为空"),
    PROMOTION_REPEAT_CODE_ERROR(10091, "是否有推广码，选择【是或否】只能选择一次性发放"),
    ACTIVITY_RUNNING_DELETE_ENTERPRISE_LIMIT_ERROR(10091, "进行的中的活动禁止删除商家"),
    ACTIVITY_RUNNING_DELETE_GOODS_LIMIT_ERROR(10092, "进行的中的活动禁止删除商品"),
    GOODS_LIMIT_NOTCONTAIN_ERROR(10093, "商品所属企业不在商家设置中，请在商家设置中添加企业、或在商品设置中删除该商品。"),
    MEMBER_ACTIVITY_ADD_REPEAT(10094, "添加会员优惠券关联的会员id重复"),
    COUPON_NOT_EXIT(10095, "卡包里面不存在此优惠券"),
    COUPON_TIME_NOT_VALIABLE(10096, "优惠券使用时间不可用"),
    GET_ACTIVITY_MEMBER_LIMIT_ERROR(10097, "获取会员优惠券可用会员规格失败"),
    COUPON_NOT_VALIABLE_FOR_GUIGE(10097, "此会员优惠券不可购买此会员规格"),
    COUPON_GET_ENDTIME_AFTER_ACTIVITY_ENDTIME(10098, "领券活动的结束时间不能大于优惠券的结束时间"),
    ;

    private final Integer code;
    private final String  message;
}
