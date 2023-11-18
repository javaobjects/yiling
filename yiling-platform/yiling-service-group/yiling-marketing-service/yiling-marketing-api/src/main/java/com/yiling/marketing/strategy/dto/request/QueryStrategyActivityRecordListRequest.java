package com.yiling.marketing.strategy.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/9/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyActivityRecordListRequest extends BaseRequest {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
     */
    private Integer strategyType;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 活动阶梯id
     */
    private Long ladderId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 截止时间
     */
    private Date stopTime;
}
