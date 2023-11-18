package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(callSuper = true)
public class PromotionAppListDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 活动ID
     */
    private Long promotionActivityId;

    /**
     * 活动类型（1-满赠；2-特价；3-秒杀）
     */
    private Integer type;

    /**
     * 活动名称
     */
    private String promotionName;

    /**
     * 满赠金额
     */
    private BigDecimal giftAmountLimit;

    /**
     * 赠品ID
     */
    private Long goodsGiftId;

    /**
     * 赠品名称
     */
    private String giftName;

    /**
     * 赠品价格
     */
    private BigDecimal price;

    /**
     * 赠品图片
     */
    private String pictureUrl;

    /**
     * 满赠活动商品
     */
    private List<Long> goodsIdList;

    /**
     * 差额
     */
    private BigDecimal diff;
}
