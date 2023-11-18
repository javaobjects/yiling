package com.yiling.marketing.lotteryactivity.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.NotIn;
import com.yiling.user.common.util.bean.Order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询抽奖活动参与次数明细分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryJoinDetailPageRequest extends QueryPageListRequest {

    /**
     * 抽奖活动ID
     */
    @Eq
    private Long lotteryActivityId;

    /**
     * 不包含的奖品类型
     */
    @NotIn(name = "reward_type")
    private List<Integer> notInRewardTypeList;

    /**
     * 平台类型：1-B端 2-C端
     */
    @Eq
    private Integer platformType;

    /**
     * 用户ID
     */
    @Eq
    private Long uid;

    /**
     * 兑付状态：1-已兑付 2-未兑付
     */
    @Eq
    private Integer status;

    /**
     * 抽奖时间
     */
    @Order(asc = false)
    private Date lotteryTime;

}
