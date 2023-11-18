package com.yiling.marketing.lotteryactivity.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/1 0001
 */
@Data
@Accessors(chain = true)
public class QueryLotteryActivityGetCountRequest extends BaseRequest {

    /**
     * uid（B2B时为企业ID）
     */
    private Long uid;
    /**
     * 活动id
     */
    private Long lotteryActivityId;
    /**
     * 获取方式：1-签到、2-抽奖赠送、3-活动每天赠送、4-分享、5-活动开始赠送、6-购买会员、7-订单累计金额赠送、8-时间周期、9-积分兑换
     */
    private Integer getType;
    /**
     * 平台类型：1-B端 2-C端
     */
    private Integer platformType;
    /**
     * 是否只查询今天的
     */
    private Boolean isToday;
}
