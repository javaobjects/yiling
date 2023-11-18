package com.yiling.marketing.lotteryactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询抽奖活动列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-15
 */
@Data
@Accessors(chain = true)
public class QueryJoinDetailRewardCountRequest extends BaseRequest {

    /**
     * 抽奖活动ID
     */
    @NotNull
    @Eq
    private Long lotteryActivityId;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会
     */
    @Eq
    private Integer rewardType;

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    @Eq
    private Integer level;

    /**
     * 关联奖品ID
     */
    @Eq
    @NotNull
    private Long rewardId;


}
