package com.yiling.marketing.strategy.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/10/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLotteryStrategyPageRequest extends QueryPageListRequest {

    /**
     * 抽奖活动id
     */
    private Long lotteryActivityId;
}