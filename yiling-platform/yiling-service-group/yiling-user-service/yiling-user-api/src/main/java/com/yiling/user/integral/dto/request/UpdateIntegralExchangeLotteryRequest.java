package com.yiling.user.integral.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换抽奖次数 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateIntegralExchangeLotteryRequest extends BaseRequest {

    /**
     * 抽奖活动ID
     */
    @NotNull
    private Long lotteryActivityId;

    /**
     * uid（B2B时为企业ID）
     */
    @NotNull
    private Long uid;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手（枚举：IntegralRulePlatformEnum）
     */
    @NotNull
    private Integer platform;

    /**
     * 兑换次数
     */
    @NotNull
    private Integer exchangeTimes;

}
