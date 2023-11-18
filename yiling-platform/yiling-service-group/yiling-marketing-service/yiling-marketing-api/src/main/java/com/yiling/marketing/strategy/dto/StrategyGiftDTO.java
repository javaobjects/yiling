package com.yiling.marketing.strategy.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动赠品表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyGiftDTO extends BaseDTO {

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
    private Integer count;


    /**
     * 赠品名称
     */
    private String giftName;
}
