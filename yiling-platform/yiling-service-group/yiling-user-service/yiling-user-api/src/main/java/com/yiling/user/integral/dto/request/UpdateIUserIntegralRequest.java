package com.yiling.user.integral.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 变更用户积分 Request
 *
 * @author: lun.yu
 * @date: 2023-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateIUserIntegralRequest extends BaseRequest {

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    @NotNull
    private Integer platform;

    /**
     * 用户ID
     */
    @NotNull
    private Long uid;

    /**
     * 积分值
     */
    @NotNull
    private Integer integralValue;

    /**
     * 用户积分变更类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废（枚举：UserIntegralChangeTypeEnum）
     */
    @NotNull
    private Integer changeType;

}
