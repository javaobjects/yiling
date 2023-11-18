package com.yiling.marketing.lotteryactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加或删除会员 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddOrDeleteMemberRequest extends BaseRequest {

    /**
     * 抽奖活动ID
     */
    @NotNull
    private Long lotteryActivityId;

    /**
     * 会员ID集合
     */
    @NotNull
    private Long memberId;

}
