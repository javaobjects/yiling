package com.yiling.marketing.lotteryactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 兑付奖品 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCashRewardRequest extends QueryPageListRequest {

    /**
     * 抽奖活动ID
     */
    @NotNull
    private Long lotteryActivityId;

    /**
     * 兑付类型：1-单个兑付 2-兑付当前页 3-兑付全部
     */
    @NotNull
    private Integer cashType;

    /**
     * ID（单个兑付时必传）
     */
    private Long id;

    /**
     * 收货信息（单个兑付且收真实物品兑付时必须存在，不能为空）
     */
    private UpdateLotteryActivityReceiptInfoRequest activityReceiptInfo;

}
