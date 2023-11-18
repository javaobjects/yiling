package com.yiling.marketing.strategy.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些策略满赠的常用错误操作码
 *
 * @author: yong.zhang
 * @date: 2022/9/18
 */
@Getter
@AllArgsConstructor
public enum StrategyErrorCode implements IErrorCode {

    /**
     * 策略满赠名称已经存在
     */
    STRATEGY_NAME_EXISTS(10111, "策略满赠名称已经存在"),
    /**
     * 平台商品数量太多，请分批操作
     */
    STRATEGY_PLATFORM_GOODS_TO_MANY(10112, "平台商品数量太多，请分批操作"),
    /**
     * 店铺商品数量太多，请分批操作
     */
    STRATEGY_ENTERPRISE_GOODS_TO_MANY(10113, "店铺商品数量太多，请分批操作"),
    /**
     * 客户数量太多，请分批操作
     */
    STRATEGY_BUYER_TO_MANY(10112, "客户数量太多，请分批操作"),
    /**
     * 添加的数据已存在
     */
    STRATEGY_DATA_IS_EXISTS(10113, "添加的数据已存在"),
    /**
     * 存在已经停用或过期的赠品
     */
    STRATEGY_GIFT_UNABLE(10114, "存在已经停用或过期的赠品"),
    /**
     * 策略满赠活动配置错误
     */
    STRATEGY_ERROR(10115, "策略满赠活动配置错误"),

    /**
     * 开始时间和结束时间不能为空
     */
    BEGINNING_EMPTY(10116, "开始时间和结束时间不能为空"),

    /**
     * 开始时间不能小于当前时间
     */
    BEGINNING_AFTER_NOE_ERROR(10117, "开始时间不能小于当前时间"),

    /**
     * 开始时间不能大于结束时间
     */
    BEGINNING_AFTER_ENDTIME_ERROR(10118, "开始时间不能小于当前时间"),

    /**
     * 选中的抽奖活动信息不完整
     */
    LOTTERY_INFO_INCOMPLETE(1119, "选中的抽奖活动信息不完整"),

    ;

    private final Integer code;
    private final String message;
}
