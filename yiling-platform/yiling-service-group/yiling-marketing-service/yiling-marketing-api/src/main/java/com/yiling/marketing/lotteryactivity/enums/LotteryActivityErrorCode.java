package com.yiling.marketing.lotteryactivity.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 抽奖活动错误状态码枚举类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Getter
@AllArgsConstructor
public enum LotteryActivityErrorCode implements IErrorCode {

    LOTTERY_ACTIVITY_NOT_EXIST(20001, "当前抽奖活动不存在"),
    LOTTERY_ACTIVITY_HAD_STOP(20002, "当前活动已经是停用状态"),
    ASSIGN_CUSTOMER_NOT_NULL(20003, "赠送范围为指定客户时，指定客户不能为空"),
    ASSIGN_ENTERPRISE_TYPE_NOT_NULL(20004, "赠送范围为指定企业类型时，企业类型不能为空"),
    ASSIGN_MEMBER_NOT_NULL(20005, "赠送范围为指定方案会员时，会员方案不能为空"),
    ASSIGN_PROMOTER_NOT_NULL(20006, "赠送范围为指定推广方会员时，推广方会员不能为空"),
    REWARD_NOT_MORE_THAN(20007, "奖品不能设置超过8个"),
    HIT_PROBABILITY_SUM_ERROR(20008, "中奖概率总和必须等于100"),
    LOTTERY_ACTIVITY_STATUS_ERROR(20009, "当前抽奖状态异常"),
    LOTTERY_ACTIVITY_JOIN_DETAIL_NOT_EXIST(20010, "当前参与抽奖明细不存在"),
    NOT_REAL_GOOD_NOT_UPDATE_CASH(20011, "非真实物品不支持修改兑付信息"),
    CURRENT_REWARD_HAD_CASH(20012, "当前奖品已经是已兑付状态"),
    RECEIPT_INFO_EXIST(20013, "已经存在收货地址"),
    REWARD_TYPE_NOT_SEE_DETAIL(20014, "当前奖品类型不支持查看详情"),
    LOTTERY_TIMES_USE_ZERO(20015, "抽奖次数用完了"),
    DRAW_FAIL(20016, "抽奖出现异常"),
    UN_SETTING_SIGN_GIVE(20017, "未设置签到赠送抽奖次数"),
    TODAY_HAD_SIGN(20018, "今日已签到"),
    NOT_SUBSCRIBE_HMC_GZH(20019, "您还未关注健康管理中心公众号"),
    REWARD_STATUS_NOT_SEE_DETAIL(20020, "当前奖品状态不支持查看详情"),
    DATA_HAD_ADD(20021, "当前数据已经添加过"),
    RECEIPT_INFO_NOT_EXIST(20022, "不存在收货地址"),
    HAD_CASH_NOT_UPDATE_RECEIPT(20023, "已兑付状态不允许修改收货信息"),
    HAD_CASH_COULD_UPDATE_CASH(20024, "只有已兑付状态才可修改兑付信息"),
    ACTIVITY_START_TIME_ERROR(20025, "活动生效时间必须大于当前时间"),
    LOTTERY_ACTIVITY_NOT_START(20026, "当前抽奖活动未开始"),
    LOTTERY_ACTIVITY_HAD_END(20027, "当前抽奖活动已结束"),
    LOTTERY_ACTIVITY_NAME_EXIST(20028, "抽奖活动名称重复"),
    EVERY_MAX_NUMBER_MUST_INTEGER(20029, "每天最大抽中数量必须是中奖数量的整数倍"),
    REWARD_SETTING_MUST_EMPTY(20030, "奖品必须包含空奖"),
    DELIVERY_ADDRESS_NUMBER_OVER(20031, "您的地址已超过50条，无法继续新增"),
    LOTTERY_INTEGRAL_NOT_ENOUGH(20032, "您的积分不足!"),
    LOTTERY_INTEGRAL_TODAY_TIMES_NOT_ENOUGH(20033, "你今天的积分抽奖已达到上限，明天再来参与吧"),
    LOTTERY_INTEGRAL_TIMES_NOT_ENOUGH(20033, "积分抽奖总次数已达到上限"),
    LOTTERY_INTEGRAL_CONFIG_NOT_NULL(20034, "积分参与抽奖活动兑换规则不存在!"),
    ;

    private final Integer code;
    private final String  message;
}
