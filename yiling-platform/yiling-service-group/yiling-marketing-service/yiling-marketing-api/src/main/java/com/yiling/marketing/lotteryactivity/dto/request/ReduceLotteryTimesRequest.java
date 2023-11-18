package com.yiling.marketing.lotteryactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 减少抽奖次数 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReduceLotteryTimesRequest extends BaseRequest {

    /**
     * 抽奖活动ID
     */
    @NotNull
    private Long lotteryActivityId;

    /**
     * 平台类型：1-B端 2-C端
     */
    @NotNull
    private Integer platformType;

    /**
     * 用户ID
     */
    @NotNull
    private Long uid;

    /**
     * 店铺企业ID（从店铺banner进入抽奖的会带入该店铺的企业ID）
     */
    private Long shopEid;

    /**
     * 抽奖类型:1-积分抽奖 0-默认积分抽奖
     */
    private Integer type;
}
