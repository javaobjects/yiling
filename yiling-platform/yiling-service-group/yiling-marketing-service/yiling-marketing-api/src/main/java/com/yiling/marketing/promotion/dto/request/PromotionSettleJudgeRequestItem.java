package com.yiling.marketing.promotion.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 结算校验满赠参数
 *
 * @author: fan.shen
 * @date: 2022/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionSettleJudgeRequestItem extends BaseRequest {

    /**
     * 活动id
     */
    private Long promotionActivityId;

    /**
     * 满赠阶梯id
     */
    private Long goodsGiftLimitId;

}
