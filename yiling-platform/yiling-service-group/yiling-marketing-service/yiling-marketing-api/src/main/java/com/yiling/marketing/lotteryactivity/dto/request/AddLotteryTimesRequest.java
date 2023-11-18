package com.yiling.marketing.lotteryactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加抽奖次数 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddLotteryTimesRequest extends BaseRequest {

    /**
     * 抽奖活动ID
     */
    @NotNull
    private Long lotteryActivityId;

    /**
     * 活动名称
     */
    private String activityName;

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
     * 用户名称
     */
    private String uname;

    /**
     * 获取方式：1-签到、2-抽奖赠送、3-活动每天赠送、4-分享、5-活动开始赠送、6-购买会员、7-订单累计金额赠送、8-时间周期
     */
    @NotNull
    private Integer getType;

    /**
     * 增加次数
     */
    @NotNull
    private Integer times;

}
