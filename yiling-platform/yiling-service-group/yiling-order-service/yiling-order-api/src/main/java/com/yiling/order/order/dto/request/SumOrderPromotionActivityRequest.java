package com.yiling.order.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import lombok.Data;
import lombok.experimental.Accessors;

/** 统计时间段内活动购买订单数量
 * @author zhigang.guo
 * @date: 2022/12/14
 */
@Data
@Accessors(chain = true)
public class SumOrderPromotionActivityRequest extends BaseRequest {
    /**
     * 活动Id
     */
    private Long activityId;
    /**
     * 企业Eid
     */
    private Long buyerEid;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 活动类型
     */
    private Integer activityType;

}
