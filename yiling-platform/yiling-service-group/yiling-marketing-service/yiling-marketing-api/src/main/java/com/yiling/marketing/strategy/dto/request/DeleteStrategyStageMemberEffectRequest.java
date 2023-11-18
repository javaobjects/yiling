package com.yiling.marketing.strategy.dto.request;

import java.util.List;

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
public class DeleteStrategyStageMemberEffectRequest extends BaseRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 会员ID-删除
     */
    private Long memberId;

    /**
     * 会员id集合-删除当前页
     */
    private List<Long> memberIdList;
}
