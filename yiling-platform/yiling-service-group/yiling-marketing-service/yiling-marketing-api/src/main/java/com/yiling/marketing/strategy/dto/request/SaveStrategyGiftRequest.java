package com.yiling.marketing.strategy.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveStrategyGiftRequest extends BaseRequest {

    /**
     * 赠品表id
     */
    private Long id;

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 活动阶梯id
     */
    private Long ladderId;

    /**
     * 赠品类型(1-商品优惠券；2-会员优惠券；3-抽奖次数)
     */
    private Integer type;

    /**
     * 赠品id
     */
    private Long giftId;

    /**
     * 数量
     */
    private Long count;

}
