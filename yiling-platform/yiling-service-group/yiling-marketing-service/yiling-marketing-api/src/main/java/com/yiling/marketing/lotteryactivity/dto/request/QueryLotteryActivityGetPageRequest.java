package com.yiling.marketing.lotteryactivity.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询获取抽奖机会明细分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLotteryActivityGetPageRequest extends QueryPageListRequest {

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

}
