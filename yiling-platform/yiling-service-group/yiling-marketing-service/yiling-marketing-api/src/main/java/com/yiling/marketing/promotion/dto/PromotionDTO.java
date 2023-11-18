package com.yiling.marketing.promotion.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionDTO extends BaseDTO {

    private PromotionActivityDTO promotionActivityDTO;

    private PromotionCombinationPackDTO promotionCombinationPackDTO;

    private PromotionSecKillSpecialDTO promotionSecKillSpecialDTO;

    private List<PromotionEnterpriseLimitDTO> enterpriseLimitDTOList;

    private List<PromotionGoodsGiftLimitDTO> goodsGiftLimitDTOList;

    private List<PromotionGoodsLimitDTO> goodsLimitDTOList;

    /**
     * 0 不可用，1可用
     */
    private Integer available;

    /**
     * 0 没有，1有货
     */
    private Integer inStock;
}
