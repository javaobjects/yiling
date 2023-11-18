package com.yiling.user.integral.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加积分发放/扣减记录 Request
 *
 * @author: lun.yu
 * @date: 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIntegralRecordRequest extends BaseRequest {

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 积分值
     */
    private Integer integralValue;

    /**
     * 行为ID
     */
    private Long behaviorId;

    /**
     * 行为名称
     */
    private String behaviorName;

    /**
     * 类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废
     */
    private Integer changeType;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    private Integer platform;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 操作备注（订单送积分：订单号；签到送积分：天数；参与活动消耗：活动ID；退货扣减：订单号；兑换消耗：兑换单号）
     */
    private String opRemark;

    /**
     * 是否为连续签到奖励
     */
    private Boolean continueSignFlag;
}
