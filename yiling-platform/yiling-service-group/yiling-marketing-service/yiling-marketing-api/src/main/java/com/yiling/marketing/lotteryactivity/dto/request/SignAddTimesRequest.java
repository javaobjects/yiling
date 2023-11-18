package com.yiling.marketing.lotteryactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 签到增加抽奖次数 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SignAddTimesRequest extends BaseRequest {

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

}
