package com.yiling.marketing.strategy.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyMemberLimitPageRequest extends QueryPageListRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

//    /**
//     * 会员名称-模糊搜索
//     */
//    private Long name;
}
