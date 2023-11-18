package com.yiling.marketing.promotion.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionGoodsGiftReturnRequest extends BaseRequest {
    /**
     * 活动结束时间范围-开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间范围-截止时间
     */
    private Date endTime;
}
