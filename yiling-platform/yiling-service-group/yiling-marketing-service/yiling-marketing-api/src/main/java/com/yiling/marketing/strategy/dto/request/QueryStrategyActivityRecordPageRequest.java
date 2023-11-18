package com.yiling.marketing.strategy.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/9/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyActivityRecordPageRequest extends QueryPageListRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
     */
    private Integer strategyType;
}
