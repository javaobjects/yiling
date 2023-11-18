package com.yiling.marketing.strategy.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class AddStrategyMemberLimitRequest extends BaseRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 会员id-单独删除时使用
     */
    private Long memberId;

//    /**
//     * 会员id-批量删除时使用
//     */
//    private List<Long> idList;
}
