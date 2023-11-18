package com.yiling.marketing.common.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 促销活动错误码枚举类
 * @author: fan.shen
 * @date: 2022/1/14
 */
@Getter
@AllArgsConstructor
public enum PromotionErrorCode implements IErrorCode {

    PLATFORM_SELECTED(10000, "促销渠道参数不完整"),

    ENTERPRISE_ONLY_ONE(10001, "满赠活动企业不能选择多个"),

    PERMITTED_ENTERPRISE_DETAIL(10002, "企业类型参数不完整"),

    PROMOTION_CODE_TOO_LONG(10003, "促销编码超出限制长度20"),

    BEAR_NOT_COMPLETE(10004, "平台或者商家分摊百分比不完整"),

    BEAR_NOT_100(10005, "平台或者商家分摊百分比合不为100"),

    GOODS_EMPTY(10006, "促销商品商品为空"),

    LAST_TIME_ERROR(10007, "持续时间不能小于0"),

    PROMOTION_STOCK_ERROR(10008, "参与活动商品数量不能小于0"),

    GOODS_HAVE_ACTIVITY(10009, "商品正在参与其他活动"),

    GIFT_AMOUNT_PASS(10010, "赠品总金额大于活动预算金额"),

    GOODS_NOT_ACTIVITY(10011, "促销活动没有选择商品"),

    ENTERPRISE_NOT_ACTIVITY(10012, "促销活动没有选择企业"),

    GIFT_NOT_ACTIVITY(10013, "促销活动没有设置赠品"),

    GIFT_NOT_ZERO(10014, "促销活动赠品数量不规范"),

    GIFT_NOT_AVAILABLE(10015, "促销活动赠品数量超过可用数量"),

    NAME_IS_REPEAT(10016, "促销活动名称出现重复"),

    ALLOW_BUY_COUNT_ERROR(10017, "允许购买数量非法"),

    PROMOTION_AMOUNT_REPEAT(10018, "满赠金额不能重复"),

    PROMOTION_AMOUNT_ERROR(10019, "满赠金额非法"),

    PLATFORM_ERROR(10020, "请选择促销渠道"),

    TYPE_EMPTY_ERROR(10021, "活动类型参数为空"),

    BUYER_EID_ERROR(10022, "买家企业id为空"),

    PROMOTION_ACTIVITY_ERROR(10023, "活动id为空"),

    PROMOTION_PRICE_ERROR(10024, "活动价不允许大于销售价"),

    PROMOTION_GOODS_TOO_MUCH_ERROR(10025, "组合包产品种类只能在2到4之间"),

    PROMOTION_GET_ENTERPRISE_INFO_ERROR(10026, "专场活动获取商户信息失败"),

    GET_ACTIVITY_INFO_ERROR(10027, "获取营销活动信息失败"),
    ;

    private final Integer code;

    private final String  message;
}
