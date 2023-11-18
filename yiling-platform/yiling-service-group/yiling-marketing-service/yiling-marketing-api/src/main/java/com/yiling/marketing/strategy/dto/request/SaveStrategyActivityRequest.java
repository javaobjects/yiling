package com.yiling.marketing.strategy.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveStrategyActivityRequest extends BaseRequest {

    /**
     * 策略满赠活动id
     */
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
     */
    private Integer strategyType;

    /**
     * 选择平台（1-B2B；2-销售助手）逗号隔开
     */
    private String platformSelected;

    /**
     * 商家范围类型（1-全部商家；2-指定商家；）
     */
    private Integer conditionSellerType;

    /**
     * 商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）
     */
    private Integer conditionGoodsType;

    /**
     * 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
     */
    private Integer conditionBuyerType;

    /**
     * 指定企业类型(1:全部类型 2:指定类型)
     */
    private Integer conditionEnterpriseType;

    /**
     * 指定企业类型值，多个值用逗号隔开，字典enterprise_type（1-连锁总店；2-连锁直营；3-连锁加盟；4-单体药店；5-医疗机构；6-诊所）
     */
    private String conditionEnterpriseTypeValue;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分会员）
     */
    private Integer conditionUserType;

    /**
     * 指定部分会员用户类型（1-指定方案会员；2-指定推广方会员）逗号隔开
     */
    private String conditionUserMemberType;

    /**
     * 其他(1-新客适用,多个值用逗号隔开)
     */
    private String conditionOther;

    /**
     * 订单累计金额-限制订单状态(1-发货计算；2-下单计算)
     */
    private Integer orderAmountStatusType;

    /**
     * 订单累计金额-限制订单状态(1-发货计算；2-下单计算)
     */
    private String orderAmountStatus;

    /**
     * 订单累计金额-限制支付方式(1-全部支付方式;2-部分支付方式)
     */
    private Integer orderAmountPayType;

    /**
     * 订单累计金额-限制支付方式值(支付方式：1-线下支付 2-账期 3-预付款 4-在线支付和订单支付方式保持一致),逗号隔开
     */
    private String orderAmountPaymentType;

    /**
     * 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配)
     */
    private Integer orderAmountLadderType;

    /**
     * 时间周期-频率(1-按周;2-按月)
     */
    private Integer cycleRate;

    //    /**
    //     * 购买会员-会员id
    //     */
    //    private Long memberId;
    //
    //    /**
    //     * 购买会员-会员名称
    //     */
    //    private String memberName;
    //
    //    /**
    //     * 购买会员-购买会员天数
    //     */
    //    private Integer memberDays;

    /**
     * 购买会员-是否每月固定日期重复执行(1-是；2-否；)
     */
    private Integer memberRepeat;

    /**
     * 购买会员-每月固定日期重复执行(逗号隔开)
     */
    private String memberRepeatDay;

    /**
     * 购买会员-执行次数
     */
    private Integer memberTimes;

    /**
     * 订单累计金额-优惠阶梯
     */
    private List<SaveStrategyAmountLadderRequest> strategyAmountLadderList;

    /**
     * 时间周期-优惠阶梯
     */
    private List<SaveStrategyCycleLadderRequest> strategyCycleLadderList;

    /**
     * 购买会员-赠品
     */
    private List<SaveStrategyGiftRequest> strategyGiftList;
}
