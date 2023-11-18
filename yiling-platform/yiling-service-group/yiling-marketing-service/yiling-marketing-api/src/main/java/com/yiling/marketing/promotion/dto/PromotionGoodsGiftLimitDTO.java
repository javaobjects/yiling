package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动赠品表
 * </p>
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class PromotionGoodsGiftLimitDTO extends BaseDTO {

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 活动名称
     */
    private String promotionName;

    /**
     * 活动类型（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 费用承担方（1-平台；2-商家）
     */
    private Integer bear;

    /**
     * 分摊-平台百分比
     */
    private BigDecimal platformPercent;

    /**
     * 满赠金额
     */
    private BigDecimal promotionAmount;

    /**
     * 赠送商品ID
     */
    private Long goodsGiftId;

    /**
     * 参与活动商品数量
     */
    private Integer promotionStock;

    /**
     * 已经参与活动商品数量
     */
    private Integer usedStock;

    /**
     * 赠品名称
     */
    private String giftName;

    /**
     * 赠品价格
     */
    private BigDecimal price;
}
